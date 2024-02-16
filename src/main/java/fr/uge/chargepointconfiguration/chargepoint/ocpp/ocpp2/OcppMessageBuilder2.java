package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2;

import fr.uge.chargepointconfiguration.chargepoint.WebSocketRequestMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageBuilder;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.RegistrationStatus;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.BootNotificationResponse;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Builds an OCPP 2.0.1 message.
 */
public class OcppMessageBuilder2 implements OcppMessageBuilder {

  @Override
  public Optional<OcppMessage> buildMessage(WebSocketRequestMessage webSocketRequestMessage) {
    return switch (webSocketRequestMessage.messageName()) {
      case BOOT_NOTIFICATION_REQUEST ->
              Optional.of(new BootNotificationResponse(LocalDateTime.now().toString(),
                      5,
                      RegistrationStatus.Accepted));
      case UPDATE_FIRMWARE_RESPONSE ->
              Optional.empty(); // TODO : Listen until we receive a status firmware request
      case STATUS_FIRMWARE_REQUEST ->
              Optional.empty(); // TODO : Send a status firmware response
      case CHANGE_CONFIGURATION_RESPONSE ->
              Optional.empty(); // TODO : Send a change configuration request to confirm the change
      case OTHER -> Optional.empty(); // TODO : WE JUST LISTEN;
    };
  }

  @Override
  public String transform(OcppMessage message) {
    return JsonParser.objectToJsonString(message);
  }
}
