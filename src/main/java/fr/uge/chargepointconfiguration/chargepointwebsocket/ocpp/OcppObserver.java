package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp;

import fr.uge.chargepointconfiguration.chargepoint.ChargepointRepository;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ChargePointManager;
import fr.uge.chargepointconfiguration.chargepointwebsocket.OcppMessageSender;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16.OcppConfigurationObserver16;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp2.OcppConfigurationObserver2;
import fr.uge.chargepointconfiguration.firmware.FirmwareRepository;
import fr.uge.chargepointconfiguration.status.StatusRepository;

/**
 * Interface used to define an OCPP message from a visitor.
 */
public interface OcppObserver {

  /**
   * Instantiate an OcppObserver according to the used OCPP version.
   *
   * @param ocppVersion           used ocpp version
   * @param ocppMessageSender     websocket chanel to send message
   * @param chargepointRepository charge point repository
   * @param firmwareRepository    firmware repository
   * @param statusRepository      status repository
   * @return OcppObserver
   */
  static OcppObserver instantiateFromVersion(OcppVersion ocppVersion,
                                             OcppMessageSender ocppMessageSender,
                                             ChargepointRepository chargepointRepository,
                                             FirmwareRepository firmwareRepository,
                                             StatusRepository statusRepository) {
    return switch (ocppVersion) {
      case V1_6 -> new OcppConfigurationObserver16(
              ocppMessageSender,
              chargepointRepository,
              firmwareRepository,
              statusRepository
      );
      case V2 -> new OcppConfigurationObserver2(
              ocppMessageSender,
              chargepointRepository,
              firmwareRepository,
              statusRepository
      );
    };
  }

  void onMessage(OcppMessage ocppMessage, ChargePointManager chargePointManager);

  void onConnection(ChargePointManager chargePointManager);

  void onDisconnection(ChargePointManager chargePointManager);
}