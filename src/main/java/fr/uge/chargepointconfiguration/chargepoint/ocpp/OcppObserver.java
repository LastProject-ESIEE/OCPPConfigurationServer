package fr.uge.chargepointconfiguration.chargepoint.ocpp;

import fr.uge.chargepointconfiguration.chargepoint.ChargePointManager;
import fr.uge.chargepointconfiguration.chargepoint.OcppMessageSender;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.Ocpp16ConfigurationObserver;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.OcppConfigurationObserver2;
import fr.uge.chargepointconfiguration.repository.ChargepointRepository;
import fr.uge.chargepointconfiguration.repository.FirmwareRepository;
import fr.uge.chargepointconfiguration.repository.StatusRepository;

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
      case V1_6 -> new Ocpp16ConfigurationObserver(
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

  void onMessage(OcppMessage ocppMessage, ChargePointManager chargePointManager, long messageId);

  void onConnection(ChargePointManager chargePointManager);

  void onDisconnection(ChargePointManager chargePointManager);
}
