package fr.uge.ocpp.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;

/** Issues and validates the JWT access tokens of the API (HS256). */
public class JwtService {

  private final SecretKey key;
  private final long expirationMs;

  /**
   * Creates the service.
   *
   * @param secret the HMAC secret (at least 32 bytes for HS256).
   * @param expirationMs the lifetime of a token in milliseconds.
   */
  public JwtService(String secret, long expirationMs) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
  }

  /**
   * Generates a signed token for the given user.
   *
   * @param email the email of the user, used as token subject.
   * @return the compact JWT.
   */
  public String generateToken(String email) {
    var now = Instant.now();
    return Jwts.builder()
        .subject(email)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusMillis(expirationMs)))
        .signWith(key)
        .compact();
  }

  /**
   * Extracts the email of the user from a token.
   *
   * @param token the compact JWT.
   * @return the subject of the token.
   * @throws io.jsonwebtoken.JwtException if the token is invalid or expired.
   */
  public String extractEmail(String token) {
    return parse(token).getSubject();
  }

  private Claims parse(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }
}
