package fr.uge.ocpp.websocket.ocpp.ocpp16;

import fr.uge.ocpp.websocket.ocpp.OcppMessageResponse;

/**
 * Empty packet sent by the server to acknowledge the {@link FirmwareStatusNotificationRequest16}.
 */
public record FirmwareStatusNotificationResponse16() implements OcppMessageResponse {}
