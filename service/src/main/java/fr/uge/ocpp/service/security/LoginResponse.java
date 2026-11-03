package fr.uge.ocpp.service.security;

/** Response of the login endpoint: the JWT to send as {@code Authorization: Bearer}. */
public record LoginResponse(String token) {}
