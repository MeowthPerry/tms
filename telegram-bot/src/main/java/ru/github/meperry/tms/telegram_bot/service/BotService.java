package ru.github.meperry.tms.telegram_bot.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import io.reactivex.rxjava3.subjects.PublishSubject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.github.meperry.tms.telegram_bot.domain.MessageExchange;
import ru.github.meperry.tms.telegram_bot.domain.MessageForBot;
import ru.github.meperry.tms.telegram_bot.util.MessageUtil;

/**
 * @author Islam Khabibullin
 */
@Service
public class BotService extends TelegramLongPollingBot {

  @Value("${bot.username}")
  private String botUsername;

  private static final String BOT_TOKEN_VAR_NAME = "bot.token";

  private final PublishSubject<MessageForBot> messageForBotSubject = PublishSubject.create();

  private final Map<Long, BiConsumer<Message, String>> replyHandlers = new HashMap<>();

  public BotService() {
    super(System.getenv(BOT_TOKEN_VAR_NAME));
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage()) {
      Message message = update.getMessage();

      if (MessageUtil.isTextMessageForBor(message, botUsername)) {
        String textWithoutBotName = MessageUtil.getTextWithoutBotName(message.getText(), botUsername);

        // есть обработчик ответа, то не публикуем сообщение обработчикам
        if (replyHandlers.containsKey(message.getChatId())) {
          processReply(new MessageForBot(message, textWithoutBotName));
          return;
        }

        messageForBotSubject.onNext(new MessageForBot(message, textWithoutBotName));
      }
    }
  }

  /**
   * Подписка на сообщения начинающиеся на /command
   *
   * @param consumer обработчик сообщения
   * @param command команда
   */
  public void subscribeToMessagesForBotByCommand(Consumer<MessageForBot> consumer, String command,
      boolean supportsGroupChat) {
    subscribeToMessagesForBot(consumer,
        messageForBot -> messageForBot.getTextWithoutBotName().startsWith(command)
                         && (supportsGroupChat || !messageForBot.getMessage().isGroupMessage()));
  }

  private void subscribeToMessagesForBot(Consumer<MessageForBot> consumer,
      Predicate<MessageForBot> predicate) {
    messageForBotSubject.filter(predicate::test).subscribe(consumer::accept);
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }

  public void sendMessage(MessageExchange messageExchange) {
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

  public void sendMessage(long chatId, String message,
      BiConsumer<Message, String> replyHandler) {
    replyHandlers.put(chatId, replyHandler);
    sendMessage(chatId, message);
  }

  /**
   * Отправка текстового сообщения в чат
   *
   * @param chatId идентификатор чата
   * @param message текст сообщения
   */
  public void sendMessage(long chatId, String message) {
    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);

    try {
      execute(sendMessage);
    }
    catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  // обработчик ответов
  private void processReply(MessageForBot messageForBot) {
    Message message = messageForBot.getMessage();
    Long chatId = message.getChatId();

    BiConsumer<Message, String> replyHandler = replyHandlers.remove(chatId);
    replyHandler.accept(message, messageForBot.getTextWithoutBotName());
  }
}
