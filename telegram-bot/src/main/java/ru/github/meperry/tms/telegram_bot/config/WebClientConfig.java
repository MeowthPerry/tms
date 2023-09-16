package ru.github.meperry.tms.telegram_bot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Islam Khabibullin
 */
@Configuration
@Slf4j
public class WebClientConfig {

  @Bean
  public WebClient webClient(@Value("${tms.backend.url:localhost:8080}") String backendUrl) {
    return WebClient.builder()
        .baseUrl(backendUrl)
        .build();
  }
}
