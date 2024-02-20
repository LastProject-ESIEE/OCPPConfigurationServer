package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.repository.FirmwareRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for FirmwareController
 */
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
  void getAllFirmwares() {
    assertEquals(firmwareRepository.findAll(), firmwareController.getAllFirmwares());
  }

  /**
   * Empty the values stored in h2 database and check that api call is empty
   * /firmware/all
   */
  @Test
  void getEmptyAllFirmwares() {
    firmwareRepository.deleteAll();
    assertEquals(List.of(), firmwareController.getAllFirmwares());
  }

  /**
   * Compare a value stored in h2 database with value showed by api call
   * /firmware/{id}
   */
  @Test
  void getFirmwareById(){
    assertEquals(firmwareRepository.findById(1), firmwareController.getFirmwareById(1));
  }

  /**
   * Check that api call return an empty object when asking for a wrong id
   * /firmware/{id}
   */
  @Test
  void getWrongFirmwareById() {
    assertEquals(Optional.empty(), firmwareController.getFirmwareById(-1));
  }

}