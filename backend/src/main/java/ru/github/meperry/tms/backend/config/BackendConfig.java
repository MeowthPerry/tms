package ru.github.meperry.tms.backend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Islam Khabibullin
 */
@Configuration
public class BackendConfig {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
