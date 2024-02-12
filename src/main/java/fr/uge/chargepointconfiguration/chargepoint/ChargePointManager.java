package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.UserRepository;
import fr.uge.chargepointconfiguration.Users;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OCPPMessageParser;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OCPPVersion;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.BootNotificationResponse;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.OCPPMessageParser16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.RegistrationStatus;
import fr.uge.chargepointconfiguration.tools.JsonParser;

import java.time.LocalDateTime;

public class ChargePointManager {
    private final OCPPVersion ocppVersion;
    private final OCPPMessageParser ocppMessageParser;
    private final MessageSender messageSender;
    private final UserRepository userRepository;

    public ChargePointManager(OCPPVersion ocppVersion, MessageSender messageSender, UserRepository userRepository){
        this.ocppVersion = ocppVersion;
        this.ocppMessageParser = new OCPPMessageParser16(); // TODO add a static method in the interface
        this.messageSender = messageSender;
        this.userRepository = userRepository;
    }

    public void processMessage(WebSocketRequestMessage webSocketRequestMessage){
        try{
            var message = ocppMessageParser.parseMessage(webSocketRequestMessage);
            System.out.println("Received message: " + message);
            userRepository.save(new Users(webSocketRequestMessage.messageName(),"Borne"));
            var resp = new BootNotificationResponse(LocalDateTime.now().toString(), 60, RegistrationStatus.Accepted);
            messageSender.sendMessage(new WebSocketResponseMessage(3, webSocketRequestMessage.messageId(), JsonParser.objectToJsonString(resp)));
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public void onDisconnection(){
        // TODO change borne status
    }

    public void onError(){
        // TODO change borne status
    }
}
