package fr.uge.chargepointconfiguration.chargepoint;

public record WebSocketResponseMessage(int callType, String messageId, String data) {

    @Override
    public String toString(){
        return "[" + callType + ",\"" + messageId + "\"," + data + "]";
    }
}
