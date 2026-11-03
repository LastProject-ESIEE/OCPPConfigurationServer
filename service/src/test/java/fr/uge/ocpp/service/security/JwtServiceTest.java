package fr.uge.ocpp.service.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

  private static final String SECRET = "test-secret-key-that-is-long-enough-for-hs256";

  private final JwtService service = new JwtService(SECRET, 60_000);

  @Test
  void should_roundtrip_email_through_token() {
    var token = service.generateToken("dummy@local");

    assertEquals("dummy@local", service.extractEmail(token));
  }

  @Test
  void should_reject_token_signed_with_another_secret() {
    var token = service.generateToken("dummy@local");
    var otherService = new JwtService("another-secret-key-that-is-long-enough-32bytes", 60_000);

    assertThrows(JwtException.class, () -> otherService.extractEmail(token));
  }

  @Test
  void should_reject_expired_token() {
    var expiredService = new JwtService(SECRET, -1_000);
    var token = expiredService.generateToken("dummy@local");

    assertThrows(ExpiredJwtException.class, () -> service.extractEmail(token));
  }

  @Test
  void should_reject_malformed_token() {
    assertThrows(JwtException.class, () -> service.extractEmail("not-a-jwt"));
  }
}
