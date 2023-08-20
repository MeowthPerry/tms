package ru.github.meperry.tms.telegram_bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Islam Khabibullin
 */
@Service
public class BotService extends TelegramLongPollingBot {

  @Value("${bot.username}")
  private String botUsername;

  private static final String BOT_TOKEN_VAR_NAME = "bot.token";

  public BotService() {
    super(System.getenv(BOT_TOKEN_VAR_NAME));
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (isUpdateValid(update)) {
      sendMessage(update.getMessage().getChatId(), "Привет!");
    }
  }

  private boolean isUpdateValid(Update update) {
    return update.hasMessage()
        && update.getMessage().hasText()
        && (!update.getMessage().getChat().isGroupChat()
            || update.getMessage().getText().startsWith("@" + botUsername));
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }

  private void sendMessage(long chatId, String message) {
    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);

    try {
      execute(sendMessage);
    }
    catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }
}
