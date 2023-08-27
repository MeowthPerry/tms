package ru.github.meperry.tms.telegram_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.github.meperry.tms.api.config.ApiConfig;

/**
 * @author Islam Khabibullin
 */
@SpringBootApplication
@Import({ApiConfig.class})
public class TmsTelegramBotApplication {

  public static void main(String[] args) {
    SpringApplication.run(TmsTelegramBotApplication.class, args);
  }
}
