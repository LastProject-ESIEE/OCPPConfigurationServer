package fr.uge.chargepointconfiguration.chargepoint.ocpp;

import java.util.Optional;

public enum OCPPVersion {
    V1_6;

    public static Optional<OCPPVersion> parse(String version){
        return switch (version){
            case "ocpp1.6" -> Optional.of(V1_6);
            default -> Optional.empty();
        };
    }
}
