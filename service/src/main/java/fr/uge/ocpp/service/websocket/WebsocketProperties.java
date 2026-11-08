package fr.uge.ocpp.service.websocket;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration of the OCPP web socket server, bound from the {@code websocket.*} properties (see
 * {@code application.yml} and {@code application-local.yml}).<br>
 * Each value can be overridden through an environment variable: {@code WEBSOCKET_URL}, {@code
 * WEBSOCKET_PORT} and {@code FINAL_WS_SERVER_ADDRESS}.
 *
 * @param url the IP address the web socket server binds to.
 * @param port the port the web socket server binds to, as a string (parsed at startup).
 * @param finalServerAddress the address of the next supervisor server forwarded to the charge
 *     points, empty if none.
 */
@ConfigurationProperties(prefix = "websocket")
public record WebsocketProperties(String url, String port, String finalServerAddress) {}
