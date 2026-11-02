package fr.uge.ocpp.service.websocket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.ChargePoints;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.Firmwares;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.models.StatusProcess;
import fr.uge.ocpp.domain.models.Step;
import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.models.TypesAllowed;
import fr.uge.ocpp.domain.ports.spi.ChargePointRepository;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration test of the {@link ConfigurationServer}: a web socket client emulates an OCPP 1.6
 * charge point (BootNotification -> UpdateFirmware -> FirmwareStatusNotification -> Reset) against
 * in-memory repository stubs.
 */
class ConfigurationServerTest {

  private static final int PORT = 18741;
  private static final String SERIAL = "TEST123";
  private static final String VENDOR = "Alfen";

  private ConfigurationServer server;
  private InMemoryChargePointRepository chargePointRepository;
  private CountDownLatch serverStarted;

  @BeforeEach
  void startServer() throws Exception {
    var typeAllowed = new TypeAllowed(1L, VENDOR, "EVE Single S-Line");
    var firmware =
        new Firmware(
            1L, "ftp://fs/6.2.0.fwi", "6.2.0", VENDOR, new TypesAllowed(Set.of(typeAllowed)));
    var configuration =
        new Configuration(1L, "conf", "desc", LocalDateTime.now(), "{\"1\":\"80\"}", firmware);
    var chargePoint =
        ChargePoint.of(SERIAL, "EVE Single S-Line", VENDOR, "client-1", configuration)
            .withId(1L)
            .withStatus(Step.FIRMWARE, StatusProcess.PENDING);
    chargePointRepository = new InMemoryChargePointRepository(Map.of(SERIAL + VENDOR, chargePoint));
    var firmwareRepository = new InMemoryFirmwareRepository(List.of(firmware));
    serverStarted = new CountDownLatch(1);
    server =
        new ConfigurationServer(
            new InetSocketAddress("127.0.0.1", PORT), chargePointRepository, firmwareRepository) {
          @Override
          public void onStart() {
            super.onStart();
            serverStarted.countDown();
          }
        };
    server.setReuseAddr(true);
    Thread.ofPlatform().daemon().start(server::start);
    assertTrue(serverStarted.await(10, TimeUnit.SECONDS), "server did not start");
  }

  @AfterEach
  void stopServer() {
    if (server != null) {
      try {
        server.stop(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  @Test
  void should_process_full_firmware_flow() throws Exception {
    BlockingQueue<String> received = new LinkedBlockingQueue<>();
    CountDownLatch closed = new CountDownLatch(1);
    var client =
        new WebSocketClient(new URI("ws://127.0.0.1:" + PORT)) {
          @Override
          public void onOpen(ServerHandshake handshake) {
            send(
                "[2,\"1\",\"BootNotification\",{\"chargePointVendor\":\""
                    + VENDOR
                    + "\",\"chargePointModel\":\"EVE Single S-Line\",\"chargePointSerialNumber\":\""
                    + SERIAL
                    + "\",\"chargeBoxSerialNumber\":\""
                    + SERIAL
                    + "\",\"firmwareVersion\":\"6.1.0\"}]");
          }

          @Override
          public void onMessage(String message) {
            received.add(message);
            if (message.contains("\"BootNotification\"") || message.startsWith("[3,\"1\"")) {
              // after the boot response, wait for the server's UpdateFirmware request
            }
            if (message.startsWith("[2,") && message.contains("\"UpdateFirmware\"")) {
              var id = message.split(",")[1];
              send("[3," + id + ",{}]");
              send("[2,\"2\",\"FirmwareStatusNotification\",{\"status\":\"Installed\"}]");
            }
            if (message.startsWith("[2,") && message.contains("\"Reset\"")) {
              var id = message.split(",")[1];
              send("[3," + id + ",{\"status\":\"Accepted\"}]");
            }
          }

          @Override
          public void onClose(int code, String reason, boolean remote) {
            closed.countDown();
          }

          @Override
          public void onError(Exception ex) {}
        };
    client.addHeader("Sec-WebSocket-Protocol", "ocpp1.6");
    client.connectBlocking(10, TimeUnit.SECONDS);

    var bootResponse = received.poll(10, TimeUnit.SECONDS);
    assertTrue(bootResponse != null && bootResponse.contains("\"Accepted\""), bootResponse);
    var updateFirmware = received.poll(10, TimeUnit.SECONDS);
    assertTrue(updateFirmware != null && updateFirmware.contains("UpdateFirmware"), updateFirmware);
    assertTrue(updateFirmware.contains("ftp://fs/6.2.0.fwi"), updateFirmware);
    var statusAck = received.poll(10, TimeUnit.SECONDS);
    assertTrue(statusAck != null && statusAck.startsWith("[3,\"2\",{}]"), statusAck);
    var reset = received.poll(10, TimeUnit.SECONDS);
    assertTrue(reset != null && reset.contains("\"Reset\"") && reset.contains("Hard"), reset);

    // Wait for the server to persist the FINISHED status after the Reset response.
    var deadline = System.currentTimeMillis() + 10_000;
    ChargePoint updated =
        chargePointRepository
            .findBySerialNumberChargepointAndConstructor(SERIAL, VENDOR)
            .orElseThrow();
    while (updated.statusProcess() != StatusProcess.FINISHED
        && System.currentTimeMillis() < deadline) {
      Thread.sleep(50);
      updated =
          chargePointRepository
              .findBySerialNumberChargepointAndConstructor(SERIAL, VENDOR)
              .orElseThrow();
    }
    assertEquals(StatusProcess.FINISHED, updated.statusProcess());
    assertEquals(false, updated.state());
    client.closeBlocking();
  }

  @Test
  void should_reject_unknown_charge_point() throws Exception {
    BlockingQueue<String> received = new LinkedBlockingQueue<>();
    var client =
        new WebSocketClient(new URI("ws://127.0.0.1:" + PORT)) {
          @Override
          public void onOpen(ServerHandshake handshake) {
            send(
                "[2,\"1\",\"BootNotification\",{\"chargePointVendor\":\"Unknown\","
                    + "\"chargePointModel\":\"X\",\"chargePointSerialNumber\":\"NOPE\","
                    + "\"chargeBoxSerialNumber\":\"NOPE\",\"firmwareVersion\":\"6.1.0\"}]");
          }

          @Override
          public void onMessage(String message) {
            received.add(message);
          }

          @Override
          public void onClose(int code, String reason, boolean remote) {}

          @Override
          public void onError(Exception ex) {}
        };
    client.addHeader("Sec-WebSocket-Protocol", "ocpp1.6");
    client.connectBlocking(10, TimeUnit.SECONDS);
    var response = received.poll(10, TimeUnit.SECONDS);
    assertTrue(response != null && response.contains("\"Rejected\""), response);
    client.closeBlocking();
  }

  /** In-memory {@link ChargePointRepository} stub. */
  private static final class InMemoryChargePointRepository implements ChargePointRepository {
    private final Map<String, ChargePoint> store = new ConcurrentHashMap<>();

    InMemoryChargePointRepository(Map<String, ChargePoint> initial) {
      store.putAll(initial);
    }

    @Override
    public Optional<ChargePoint> findBySerialNumberChargepointAndConstructor(
        String serialNumber, String constructor) {
      return Optional.ofNullable(store.get(serialNumber + constructor));
    }

    @Override
    public ChargePoints findAllByOrderByIdDesc() {
      return new ChargePoints(List.copyOf(store.values()));
    }

    @Override
    public PageResult<ChargePoint> findAllByClientIdContainingIgnoreCaseOrderByIdDesc(
        PageQuery pageQuery, String clientId) {
      var content = store.values().stream().filter(cp -> cp.clientId().contains(clientId)).toList();
      return new PageResult<>(content, pageQuery.page(), pageQuery.size(), content.size());
    }

    @Override
    public Optional<ChargePoint> findById(long id) {
      return store.values().stream().filter(cp -> cp.id() == id).findFirst();
    }

    @Override
    public ChargePoint save(ChargePoint chargePoint) {
      store.put(chargePoint.serialNumber() + chargePoint.constructor(), chargePoint);
      return chargePoint;
    }
  }

  /** In-memory {@link FirmwareRepository} stub. */
  private static final class InMemoryFirmwareRepository implements FirmwareRepository {
    private final List<Firmware> firmwares;

    InMemoryFirmwareRepository(List<Firmware> firmwares) {
      this.firmwares = firmwares;
    }

    @Override
    public Optional<Firmware> findByVersion(String version) {
      return firmwares.stream().filter(f -> f.version().equals(version)).findFirst();
    }

    @Override
    public Firmwares findAllByTypeAllowedAsc(TypeAllowed typeAllowed) {
      return new Firmwares(
          firmwares.stream()
              .filter(f -> f.typesAllowed().typesAllowedSet().contains(typeAllowed))
              .sorted((a, b) -> a.version().compareTo(b.version()))
              .toList());
    }

    @Override
    public Firmwares findAllByTypeAllowedDesc(TypeAllowed typeAllowed) {
      return new Firmwares(
          firmwares.stream()
              .filter(f -> f.typesAllowed().typesAllowedSet().contains(typeAllowed))
              .sorted((a, b) -> b.version().compareTo(a.version()))
              .toList());
    }

    @Override
    public Firmwares findAllByOrderByIdDesc() {
      return new Firmwares(firmwares);
    }

    @Override
    public PageResult<Firmware> findAllByOrderByIdDesc(PageQuery pageQuery) {
      return new PageResult<>(firmwares, pageQuery.page(), pageQuery.size(), firmwares.size());
    }

    @Override
    public Optional<Firmware> findByUrl(String url) {
      return firmwares.stream().filter(f -> f.url().equals(url)).findFirst();
    }

    @Override
    public Optional<Firmware> findById(long id) {
      return firmwares.stream().filter(f -> f.id() == id).findFirst();
    }

    @Override
    public Firmware save(Firmware firmware) {
      return firmware;
    }
  }
}
