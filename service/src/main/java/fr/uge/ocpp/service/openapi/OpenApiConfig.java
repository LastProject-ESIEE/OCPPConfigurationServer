package fr.uge.ocpp.service.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** OpenAPI documentation configuration of the REST API. */
@Configuration
public class OpenApiConfig {

  /** Name of the HTTP Bearer security scheme declared in the OpenAPI document. */
  public static final String BEARER_AUTH = "bearerAuth";

  /**
   * Provides the OpenAPI definition of the API.
   *
   * @return the OpenAPI definition.
   */
  @Bean
  OpenAPI ocppConfigurationServerApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("OCPP Configuration Server API")
                .description("REST API used to configure charge points over the OCPP protocol.")
                .version("0.0.1-SNAPSHOT"))
        .components(
            new Components()
                .addSecuritySchemes(
                    BEARER_AUTH,
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
  }
}
