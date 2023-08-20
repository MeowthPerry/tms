package ru.github.meperry.tms.telegram_bot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.github.meperry.tms.telegram_bot.service.BotService;

/**
 * @author Islam Khabibullin
 */
@Configuration
@RequiredArgsConstructor
public class BotConfig {

  private final BotService botService;

  @EventListener(ContextRefreshedEvent.class)
  public void init() {
    try {
      TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
      telegramBotsApi.registerBot(botService);
    }
    catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }
}
