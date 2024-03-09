package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.firmware.Firmware;
import fr.uge.chargepointconfiguration.firmware.FirmwareController;
import fr.uge.chargepointconfiguration.firmware.FirmwareRepository;
import fr.uge.chargepointconfiguration.typeallowed.TypeAllowed;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for FirmwareController
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
class FirmwareControllerTest {

  @Autowired
  FirmwareRepository firmwareRepository;

  @Autowired
  FirmwareController firmwareController;

  /**
   * Compare values stored in h2 database with values showed by api call
   * /firmware/all
   */
  @Test
  @WithMockUser(roles = "EDITOR")
  void getAllFirmwares() {
    var firmware = new Firmware("www.alfen5-4-3.com", "5.4.3-4073)", "Alfen", Set.of(new TypeAllowed("Alfen", "mega-type")));
    firmwareRepository.save(firmware);

    var firmware2 = new Firmware("www.alfen6-1-3.com", "6.1.3-4073)", "Alfen", Set.of(new TypeAllowed("Alfen", "S-type")));
    firmwareRepository.save(firmware2);

    assertEquals(List.of(firmware2.toDto(), firmware.toDto()), firmwareController.getAllFirmwares());
  }

  /**
   * Empty the values stored in h2 database and check that api call is empty
   * /firmware/all
   */
  @Test
  @WithMockUser(roles = "EDITOR")
  void getEmptyAllFirmwares() {
    System.out.println(firmwareController.getAllFirmwares());
    System.out.println(firmwareRepository.findAllByOrderByIdDesc());
    assertEquals(List.of(), firmwareController.getAllFirmwares());
  }

  /**
   * Compare a value stored in h2 database with value showed by api call
   * /firmware/{id}
   */
  @Test
  @WithMockUser(roles = "EDITOR")
  void getFirmwareById(){
//    firmwareRepository.deleteAll();

    var firmware = new Firmware("www.alfen5-4-3.com", "5.4.3-4073)", "Alfen", Set.of(new TypeAllowed("Alfen", "mega-type")));
    firmwareRepository.save(firmware);

    assertEquals(firmware.toDto(), firmwareController.getFirmwareById(firmware.getId()).orElseThrow());
  }

  /**
   * Check that api call return an empty object when asking for a wrong id
   * /firmware/{id}
   */
//  @Test
//  void getWrongFirmwareById() {
//  // TODO : exception BAD REQUEST si id est pas un nombre ou existe pas
//    assertEquals(firmwareController.getFirmwareById(-1), Optional.empty());
//  }

}