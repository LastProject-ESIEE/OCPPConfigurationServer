package fr.uge.chargepointconfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures the web application to activate the backend and the frontend.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // Map all requests for the root path ("/") to the index.html file
    registry.addResourceHandler("/")
            .addResourceLocations("classpath:/web/index.html")
            .setCachePeriod(0);
    // Set base path
    registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/web/")
            .setCachePeriod(0);
  }
}