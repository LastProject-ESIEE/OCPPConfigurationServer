package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.chargepoint.ChargepointController;
import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.configuration.Configuration;
import fr.uge.chargepointconfiguration.firmware.Firmware;
import fr.uge.chargepointconfiguration.status.Status;
import fr.uge.chargepointconfiguration.typeallowed.TypeAllowed;
import fr.uge.chargepointconfiguration.chargepoint.ChargepointRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for ChargepointController
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
class ChargepointControllerTest {

  @Autowired
  private ChargepointController chargepointController;

  @Autowired
  private ChargepointRepository chargepointRepository;

  /**
   * Compare values stored in h2 database with values showed by api call
   * /chargepoint/all
   */
  @Test
  void getAllChargepoints() {
    var status = new Status();
    var configuration = new Configuration("MyConfig", "{}");
    var firmware = new Firmware("www.alfen5-4-3.com", "5.4.3-4073)", "Alfen", Set.of(new TypeAllowed("Alfen", "mega-type")));
    var chargepoint = new Chargepoint("alfen_serial_number", "mega-type", "alfen", "COMMUNIAU_1", "localhost", configuration, status, firmware);
    chargepointRepository.save(chargepoint);

    var status2 = new Status();
    var configuration2 = new Configuration("NewConfig", "{}");
    var firmware2 = new Firmware("www.alfen6-1-3.com", "6.1.3-4073)", "Alfen", Set.of(new TypeAllowed("Alfen", "S-type")));
    var chargepoint2 = new Chargepoint("alfen_serial_number2", "S-type", "alfen", "COSTANDINI_1", "localhost", configuration2, status2, firmware2);
    chargepointRepository.save(chargepoint2);

    assertEquals(List.of(chargepoint.toDto(), chargepoint2.toDto()), chargepointController.getAllChargepoints());
  }

  /**
   * Empty the values stored in h2 database and check that api call return is an empty list
   * /chargepoint/all
   */
  @Test
  void getEmptyAllChargepoints() {
    assertEquals(List.of(), chargepointController.getAllChargepoints());
  }

  /**
   * Compare a value stored in h2 database with value showed by api call
   * /chargepoint/{id}
   */
  @Test
  void getChargepointById(){
    var status = new Status();
    var configuration = new Configuration("MyConfig", "{}");
    var firmware = new Firmware("www.alfen5-4-3.com", "5.4.3-4073)", "Alfen", Set.of(new TypeAllowed("Alfen", "mega-type")));
    var chargepoint = new Chargepoint("alfen_serial_number", "mega-type", "alfen", "COMMUNIAU_1", "localhost", configuration, status, firmware);
    chargepointRepository.save(chargepoint);

    assertEquals(chargepoint.toDto(), chargepointController.getChargepointById(chargepoint.getId()).orElseThrow());
  }

  /**
   * Check that api call return an empty object when asking for a wrong id
   * /chargepoint/{id}
   */
//  @Test
//  void getWrongChargepointById() {
//  // TODO : exception BAD REQUEST si id est pas un nombre ou existe pas
//    assertEquals(chargepointController.getChargepointById(-1), Optional.empty());
//  }
}