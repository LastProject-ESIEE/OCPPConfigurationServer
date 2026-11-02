package fr.uge.ocpp.websocket.ocpp.ocpp16;

import fr.uge.ocpp.websocket.ocpp.OcppMessageResponse;

/** An empty packet sent to the server to acknowledge the {@link UpdateFirmwareRequest16}. */
public record UpdateFirmwareResponse16() implements OcppMessageResponse {}
