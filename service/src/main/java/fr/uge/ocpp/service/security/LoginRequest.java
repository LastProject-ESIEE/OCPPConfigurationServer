package fr.uge.ocpp.service.security;

/** Credentials sent to the login endpoint to obtain a JWT. */
public record LoginRequest(String email, String password) {}
