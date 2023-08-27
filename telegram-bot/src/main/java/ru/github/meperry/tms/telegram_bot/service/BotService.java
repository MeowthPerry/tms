package ru.github.meperry.tms.telegram_bot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.github.meperry.tms.telegram_bot.command_handler.CommandHandler;
import ru.github.meperry.tms.telegram_bot.domain.MessageExchange;

/**
 * @author Islam Khabibullin
 */
@Service
public class BotService extends TelegramLongPollingBot {

  @Value("${bot.username}")
  private String botUsername;

  private static final String BOT_TOKEN_VAR_NAME = "bot.token";

  private final List<CommandHandler> commandHandlers;

  private final Map<Long, BiFunction<Message, String, MessageExchange>> replyHandlers = new HashMap<>();

  public BotService(List<CommandHandler> commandHandlers) {
    super(System.getenv(BOT_TOKEN_VAR_NAME));
    this.commandHandlers = commandHandlers;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage()) {
      Message message = update.getMessage();

      if (isTextMessage(message)) {
        Long chatId = update.getMessage().getChatId();
        // TODO 27.08 Заменить на switch-case: команда из группового чата, команда из приватного чата, обычное текстовое сообщение
        // если это команда, то обрабатываем команду
        if (isCommandMessage(message)) {
          Optional<CommandHandler> commandHandlerOptional = commandHandlers.stream()
              .filter(commandHandler -> commandHandler.supports(message, getTextWithoutBotName(message.getText())))
              .findFirst();
          if (commandHandlerOptional.isPresent()) {
            MessageExchange messageExchange = commandHandlerOptional.get().handle(message, getTextWithoutBotName(message.getText()));
            sendMessage(messageExchange);
          }
          else {
            sendMessage(chatId, "Не поддерживаемая команда. Отправьте `/help` для помощи.");
          }
        }
        // если нет, ищем среди обработчиков ответов обработчик для данного chatId
        else if (replyHandlers.containsKey(chatId)) {
          BiFunction<Message, String, MessageExchange> replyHandler = replyHandlers.get(chatId);
          sendMessage(replyHandler.apply(message, getTextWithoutBotName(message.getText())));
        }
        // если нет обработчика пишем, что неправильное сообщение
        else {
          sendMessage(chatId, "Не поддерживаемая команда. Отправьте `/help` для помощи.");
        }
      }
    }
  }

  private String getTextWithoutBotName(String text) {
    if (text.startsWith("@" + botUsername + " ")) {
      return text.substring(("@" + botUsername + " ").length());
    }
    return text;
  }

  private boolean isTextMessage(Message message) {
    return message.hasText()
           && (!message.getChat().isGroupChat()
               || message.getText().startsWith("@" + botUsername + " "));
  }

  private boolean isCommandMessage(Message message) {
    String text = message.getText();
    return text.startsWith("/") || text.startsWith("@" + botUsername + " " + "/");
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }

  private void sendMessage(MessageExchange messageExchange) {
    if (messageExchange.hasReplyHandler()) {
      sendMessage(
          messageExchange.getChatId(),
          messageExchange.getMessage(),
          messageExchange.getReplyHandler()
      );
    }
    else {
      sendMessage(messageExchange.getChatId(), messageExchange.getMessage());
    }
  }

  private void sendMessage(long chatId, String message,
      BiFunction<Message, String, MessageExchange> replyHandler) {
    replyHandlers.put(chatId, replyHandler);
    sendMessage(chatId, message);
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
