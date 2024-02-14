package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppVersion;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.BootNotificationResponse;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.OcppMessageParser16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.RegistrationStatus;
import fr.uge.chargepointconfiguration.entities.User;
import fr.uge.chargepointconfiguration.repository.UserRepository;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import java.time.LocalDateTime;

/**
 * Manages the charge point by listening and sending messages to the charge point.
 */
public class ChargePointManager {
  private final OcppVersion ocppVersion;
  private final OcppMessageParser ocppMessageParser;
  private final MessageSender messageSender;
  private final UserRepository userRepository;

  /**
   * ChargePointManager's constructor.
   *
   * @param ocppVersion OcppVersion.
   * @param messageSender MessageSender.
   * @param userRepository UserRepository.
   */
  public ChargePointManager(OcppVersion ocppVersion,
                            MessageSender messageSender,
                            UserRepository userRepository) {
    this.ocppVersion = ocppVersion;
    this.ocppMessageParser = new OcppMessageParser16(); // TODO add a static method in the interface
    this.messageSender = messageSender;
    this.userRepository = userRepository;
  }

  /**
   * Processes the message from the sender in OCPP.
   *
   * @param webSocketRequestMessage WebSocketRequestMessage.
   */
  public void processMessage(WebSocketRequestMessage webSocketRequestMessage) {
    try {
      var message = ocppMessageParser.parseMessage(webSocketRequestMessage);
      System.out.println("Received message: " + message);
      userRepository.save(new User("Borne",
              "toBeALive",
              webSocketRequestMessage.messageName(),
              "g00d p4ssw0rd",
              User.Role.Visualizer));
      var resp = new BootNotificationResponse(LocalDateTime.now().toString(),
              60,
              RegistrationStatus.Accepted);
      messageSender.sendMessage(new WebSocketResponseMessage(3,
              webSocketRequestMessage.messageId(),
              JsonParser.objectToJsonString(resp)));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Does something when the sender has been disconnected.
   */
  public void onDisconnection() {
    // TODO change borne status
  }

  /**
   * Does something if there is an error.
   */
  public void onError() {
    // TODO change borne status
  }
}
