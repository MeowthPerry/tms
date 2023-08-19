package ru.github.meperry.tms.backend;

import javax.persistence.EntityListeners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SpringBootApplication
@EntityListeners(AuditingEntityListener.class)
public class TmsBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(TmsBackendApplication.class, args);
  }
}
