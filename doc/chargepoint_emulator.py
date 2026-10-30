#!/usr/bin/env python3
"""
Charge point emulator for the OCPP configuration server.

This program emulates an OCPP charge point connecting to the application's
websocket endpoint and speaking the OCPP-J framing used by the `websocket`
module (fr.uge.ocpp.websocket):

  - CALL        (request)  : [2, "<messageId>", "<action>", <json payload>]
  - CALLRESULT  (response) : [3, "<messageId>", <json payload>]

The OCPP version is negotiated through the `Sec-WebSocket-Protocol` handshake
header ("ocpp1.6" or "ocpp2.0.1"), see OcppVersion.parse(...).

Supported flows (see doc/serverChargepointCommunicationDoc.md):
  - BootNotification            (charge point -> server, answered by the server)
  - UpdateFirmware              (server -> charge point, ack + FirmwareStatusNotification)
  - FirmwareStatusNotification  (charge point -> server, answered with an empty ack)
  - ChangeConfiguration         (server -> charge point, OCPP 1.6)
  - SetVariables                (server -> charge point, OCPP 2.0.1)
  - Reset                       (server -> charge point, OCPP 1.6)
  - Heartbeat                   (optional, charge point -> server)

Usage:
    pip install websockets
    python3 chargepoint_emulator.py --serial ACE0272306 --vendor Alfen
    python3 chargepoint_emulator.py --url ws://192.168.0.5:8080 --version ocpp1.6

If --url is not given, the WEBSOCKET_URL / WEBSOCKET_PORT environment
variables are used (as in the README), defaulting to ws://localhost:8080.
"""

import argparse
import asyncio
import json
import os
import sys
from datetime import datetime, timezone

try:
    import websockets
except ImportError:
    sys.exit("Missing dependency: pip install websockets")

OCPP_1_6 = "ocpp1.6"
OCPP_2_0_1 = "ocpp2.0.1"

# Firmware installation steps reported by a real charge point after an
# UpdateFirmware request (see FirmwareStatus in ocpp16/data).
FIRMWARE_STATUS_SEQUENCE = ["Downloading", "Downloaded", "Installing", "Installed"]


def log(direction: str, raw: str) -> None:
    stamp = datetime.now(timezone.utc).strftime("%H:%M:%S")
    print(f"[{stamp}] {direction} {raw}", flush=True)


def format_call(message_id: int, action: str, payload: dict) -> str:
    """Builds an OCPP-J CALL frame: [2,"<id>","<action>",{...}]."""
    return json.dumps([2, str(message_id), action, payload], separators=(",", ":"))


def format_call_result(message_id, payload: dict) -> str:
    """Builds an OCPP-J CALLRESULT frame: [3,"<id>",{...}]."""
    return json.dumps([3, str(message_id), payload], separators=(",", ":"))


def utc_now() -> str:
    return datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%S.000Z")


class ChargePointEmulator:
    """Emulates a single charge point connected over a websocket."""

    def __init__(self, args: argparse.Namespace) -> None:
        self.version = args.version
        self.serial = args.serial
        self.vendor = args.vendor
        self.model = args.model
        self.chargebox_serial = args.chargebox_serial or args.serial
        self.firmware_version = args.firmware_version
        self.reason = args.reason
        self.reject_keys = set(args.reject_keys or [])
        self.simulate_firmware = not args.no_firmware_simulation
        self.firmware_delay = args.firmware_delay
        self.heartbeat_interval = args.heartbeat
        self.ws: websockets.WebSocketClientProtocol | None = None
        self._message_id = 0
        self._pending: dict[str, str] = {}  # messageId -> action of our own calls

    # ------------------------------------------------------------------ ws --
    async def send_call(self, action: str, payload: dict) -> str:
        self._message_id += 1
        message_id = str(self._message_id)
        frame = format_call(self._message_id, action, payload)
        self._pending[message_id] = action
        log("SEND", frame)
        await self.ws.send(frame)
        return message_id

    async def send_result(self, message_id, payload: dict) -> None:
        frame = format_call_result(message_id, payload)
        log("SEND", frame)
        await self.ws.send(frame)

    async def receive(self):
        """Returns (message_type, message_id, action_or_None, payload)."""
        raw = await self.ws.recv()
        log("RECV", raw)
        frame = json.loads(raw)
        message_type, message_id = frame[0], str(frame[1])
        if message_type == 2:  # CALL
            return message_type, message_id, frame[2], frame[3]
        if message_type == 3:  # CALLRESULT
            return message_type, message_id, None, frame[2]
        raise ValueError(f"Unknown OCPP frame type: {frame!r}")

    # --------------------------------------------------------------- boot --
    def boot_notification_payload(self) -> dict:
        if self.version == "2.0.1":
            return {
                "chargingStation": {
                    "model": self.model,
                    "vendorName": self.vendor,
                    "serialNumber": self.serial,
                    "firmwareVersion": self.firmware_version,
                },
                "reason": self.reason,
            }
        return {
            "chargePointVendor": self.vendor,
            "chargePointModel": self.model,
            "chargePointSerialNumber": self.serial,
            "chargeBoxSerialNumber": self.chargebox_serial,
            "firmwareVersion": self.firmware_version,
        }

    async def boot(self) -> dict:
        """Sends BootNotification and waits for the BootNotification response."""
        message_id = await self.send_call("BootNotification", self.boot_notification_payload())
        while True:
            message_type, resp_id, action, payload = await self.receive()
            if message_type == 3 and resp_id == message_id:
                self._pending.pop(resp_id, None)
                status = payload.get("status")
                print(f"BootNotification response: status={status} "
                      f"interval={payload.get('interval')} currentTime={payload.get('currentTime')}",
                      flush=True)
                if status != "Accepted":
                    raise ConnectionError(f"BootNotification rejected by the server: {status}")
                return payload
            # The server may send requests before answering; handle them.
            await self.handle_server_call(resp_id, action, payload)

    # ------------------------------------------------------- server calls --
    async def handle_server_call(self, message_id, action: str, payload: dict) -> None:
        if action == "ChangeConfiguration":
            key, value = payload.get("key"), payload.get("value")
            status = "Rejected" if key in self.reject_keys else "Accepted"
            print(f"ChangeConfiguration key={key!r} value={value!r} -> {status}", flush=True)
            await self.send_result(message_id, {"status": status})
        elif action == "SetVariables":
            results = []
            for data in payload.get("setVariableData", []):
                attribute = data.get("attributeValue")
                variable = data.get("variable", {}).get("name")
                status = "Rejected" if variable in self.reject_keys else "Accepted"
                print(f"SetVariables variable={variable!r} value={attribute!r} -> {status}", flush=True)
                results.append({
                    "attributeStatus": status,
                    "component": data.get("component"),
                    "variable": data.get("variable"),
                })
            await self.send_result(message_id, {"setVariableResult": results})
        elif action == "UpdateFirmware":
            print(f"UpdateFirmware payload={payload}", flush=True)
            if self.version == "2.0.1":
                await self.send_result(message_id, {"status": "Accepted"})
            else:
                await self.send_result(message_id, {})  # UpdateFirmwareResponse16 is empty
            if self.simulate_firmware:
                await self.simulate_firmware_update()
        elif action == "Reset":
            print(f"Reset type={payload.get('type')}", flush=True)
            await self.send_result(message_id, {"status": "Accepted"})
        elif action == "Heartbeat":
            await self.send_result(message_id, {"currentTime": utc_now()})
        else:
            print(f"Unhandled server request '{action}', ignoring.", flush=True)

    async def simulate_firmware_update(self) -> None:
        """Sends FirmwareStatusNotification frames like a real charge point."""
        for status in FIRMWARE_STATUS_SEQUENCE:
            await asyncio.sleep(self.firmware_delay)
            message_id = await self.send_call("FirmwareStatusNotification", {"status": status})
            # Wait for the server's empty acknowledgement.
            while True:
                message_type, resp_id, action, payload = await self.receive()
                if message_type == 3 and resp_id == message_id:
                    self._pending.pop(resp_id, None)
                    break
                await self.handle_server_call(resp_id, action, payload)

    # ------------------------------------------------------------ main loop --
    async def heartbeat_loop(self, interval: float) -> None:
        while True:
            await asyncio.sleep(interval)
            await self.send_call("Heartbeat", {"currentTime": utc_now()})

    async def run(self, url: str) -> None:
        subprotocol = OCPP_2_0_1 if self.version == "2.0.1" else OCPP_1_6
        print(f"Connecting to {url} with subprotocol '{subprotocol}' ...", flush=True)
        # The OCPP version is offered via Sec-WebSocket-Protocol, which the
        # server reads on connection (OcppVersion.parse); the connection is
        # accepted even if the server does not select it back.
        async with websockets.connect(url, subprotocols=[subprotocol]) as ws:
            self.ws = ws
            print("Connected.", flush=True)
            await self.boot()
            heartbeat_task = None
            if self.heartbeat_interval and self.heartbeat_interval > 0:
                heartbeat_task = asyncio.create_task(self.heartbeat_loop(self.heartbeat_interval))
            print("Emulation running, waiting for server requests (Ctrl+C to stop).", flush=True)
            try:
                while True:
                    message_type, message_id, action, payload = await self.receive()
                    if message_type == 2:
                        await self.handle_server_call(message_id, action, payload)
                    else:
                        pending = self._pending.pop(message_id, None)
                        print(f"Response for '{pending}': {payload}", flush=True)
            except websockets.exceptions.ConnectionClosed:
                print("Disconnected by the server.", flush=True)
            finally:
                if heartbeat_task is not None:
                    heartbeat_task.cancel()


def build_url(args: argparse.Namespace) -> str:
    if args.url:
        return args.url
    host = os.environ.get("WEBSOCKET_URL", "localhost")
    port = os.environ.get("WEBSOCKET_PORT", "8080")
    return f"ws://{host}:{port}"


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Emulates an OCPP charge point connecting to the configuration server.")
    parser.add_argument("--url", help="Websocket URL of the server "
                                      "(default: ws://$WEBSOCKET_URL:$WEBSOCKET_PORT or ws://localhost:8080)")
    parser.add_argument("--version", choices=["1.6", "2.0.1"], default="1.6",
                        help="OCPP protocol version (default: 1.6)")
    parser.add_argument("--serial", default="ACE0272306", help="Charge point serial number")
    parser.add_argument("--vendor", default="Alfen", help="Charge point vendor (max 20 chars)")
    parser.add_argument("--model", default="EVE Single S-Line", help="Charge point model (max 20 chars)")
    parser.add_argument("--chargebox-serial", help="Charge box serial number (default: same as --serial)")
    parser.add_argument("--firmware-version", default="6.1.0", help="Current firmware version")
    parser.add_argument("--reason", default="PowerUp", help="BootNotification reason (OCPP 2.0.1 only)")
    parser.add_argument("--reject-keys", nargs="*", metavar="KEY",
                        help="Configuration keys the emulator answers 'Rejected' to")
    parser.add_argument("--no-firmware-simulation", action="store_true",
                        help="Only ack UpdateFirmware without sending FirmwareStatusNotification")
    parser.add_argument("--firmware-delay", type=float, default=1.0,
                        help="Delay in seconds between firmware status notifications (default: 1.0)")
    parser.add_argument("--heartbeat", type=int, default=0,
                        help="Send a Heartbeat every N seconds (0 = disabled, default: 0)")
    return parser.parse_args()


async def amain() -> None:
    args = parse_args()
    emulator = ChargePointEmulator(args)
    await emulator.run(build_url(args))


def main() -> None:
    try:
        asyncio.run(amain())
    except KeyboardInterrupt:
        print("Stopped by user.", flush=True)


if __name__ == "__main__":
    main()
