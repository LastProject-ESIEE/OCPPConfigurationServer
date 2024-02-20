package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.repository.ChargepointRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for ChargepointController
 */
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
    assertEquals(chargepointRepository.findAll(), chargepointController.getAllChargepoints());
  }

  /**
   * Empty the values stored in h2 database and check that api call is empty
   * /chargepoint/all
   */
  @Test
  void getEmptyAllChargepoints() {
    chargepointRepository.deleteAll();
    assertEquals(List.of(), chargepointController.getAllChargepoints());
  }

  /**
   * Compare a value stored in h2 database with value showed by api call
   * /chargepoint/{id}
   */
  @Test
  void getChargepointById(){
    assertEquals(chargepointRepository.findById(1), chargepointController.getChargepointById(1));
  }

  /**
   * Check that api call return an empty object when asking for a wrong id
   * /chargepoint/{id}
   */
  @Test
  void getWrongChargepointById() {
    assertEquals(Optional.empty(), chargepointController.getChargepointById(-1));
  }
}