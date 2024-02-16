package fr.uge.chargepointconfiguration;

import fr.uge.chargepointconfiguration.chargepoint.ChargePointManager;
import fr.uge.chargepointconfiguration.chargepoint.WebSocketRequestMessage;
import fr.uge.chargepointconfiguration.chargepoint.WebSocketResponseMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageBuilder;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppVersion;
import fr.uge.chargepointconfiguration.repository.ChargepointRepository;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for the ChargePointManager.java.
 */
@ExtendWith(MockitoExtension.class)
public class ChargePointManagerTest {

  private final OcppVersion ocppVersion16 = OcppVersion.V1_6;
  private final OcppVersion ocppVersion2 = OcppVersion.V2;
  private final OcppMessageBuilder ocppMessageBuilder16 = OcppMessageBuilder.instantiateFromVersion(ocppVersion16);
  private final OcppMessageBuilder ocppMessageBuilder2 = OcppMessageBuilder.instantiateFromVersion(ocppVersion2);
  @Mock
  private ChargepointRepository chargepointRepository;
  @Mock
  private WebSocket webSocket;

  @Test
  public void chargePointManager16CorrectConstructor() {
    new ChargePointManager(ocppVersion16,
            message -> webSocket.send(message.toString()),
            chargepointRepository);
  }

  @Test
  public void chargePointManager2CorrectConstructor() {
    new ChargePointManager(ocppVersion2,
            message -> webSocket.send(message.toString()),
            chargepointRepository);
  }

  @Test
  public void chargePointManagerNullConstructor() {
    assertThrows(NullPointerException.class, () -> {
      new ChargePointManager(null, null, null);
    });
  }

  @Test
  public void chargePointManager16ReceivesBootNotificationRequest() {
    var manager = new ChargePointManager(ocppVersion16,
            message -> webSocket.send(message.toString()),
            chargepointRepository);
    var receivedMessage = new WebSocketRequestMessage(2,
            "1",
            WebSocketRequestMessage.WebSocketMessageName.BOOT_NOTIFICATION_REQUEST,
            "{}");

    var actualResponse = manager.processMessage(receivedMessage);
    var resp = ocppMessageBuilder16.buildMessage(receivedMessage);
    var expectedWebSocketResponseMessage = new WebSocketResponseMessage(3,
            receivedMessage.messageId(),
            JsonParser.objectToJsonString(resp));
    assertEquals(expectedWebSocketResponseMessage.messageId(), actualResponse.orElseThrow().messageId());
    assertEquals(expectedWebSocketResponseMessage.callType(), actualResponse.orElseThrow().callType());
    // WE DON'T COMPARE THE DATE BECAUSE WE CANNOT HAVE THE SAME DATE, MS DIFFERENCE.
  }

  @Test
  public void chargePointManager2ReceivesBootNotificationRequest() {
    var manager = new ChargePointManager(ocppVersion16,
            message -> webSocket.send(message.toString()),
            chargepointRepository);
    var receivedMessage = new WebSocketRequestMessage(2,
            "2",
            WebSocketRequestMessage.WebSocketMessageName.BOOT_NOTIFICATION_REQUEST,
            "{}");

    var actualResponse = manager.processMessage(receivedMessage);
    var resp = ocppMessageBuilder2.buildMessage(receivedMessage);
    var expectedWebSocketResponseMessage = new WebSocketResponseMessage(3,
            receivedMessage.messageId(),
            JsonParser.objectToJsonString(resp));
    assertEquals(expectedWebSocketResponseMessage.messageId(), actualResponse.orElseThrow().messageId());
    assertEquals(expectedWebSocketResponseMessage.callType(), actualResponse.orElseThrow().callType());
    // WE DON'T COMPARE THE DATE BECAUSE WE CANNOT HAVE THE SAME DATE, MS DIFFERENCE.
  }

}
