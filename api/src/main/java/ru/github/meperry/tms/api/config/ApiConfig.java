package ru.github.meperry.tms.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Islam Khabibullin
 */
@Configuration
@ComponentScan({"ru.github.meperry.tms.api.security.api"})
public class ApiConfig {

  @Bean
  public WebClient webClient(@Value("${tms.backend.url:localhost:8080}") String backendUrl) {
    return WebClient.create(backendUrl);
  }
}
