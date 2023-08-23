package ru.github.meperry.tms.telegram_bot.domain;

import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Islam Khabibullin
 */
@Data
@AllArgsConstructor
public class MessageExchange {

  private Long chatId;
  private String message;
  private Function<Message, MessageExchange> replyHandler;

  public MessageExchange(Long chatId, String message) {
    this.chatId = chatId;
    this.message = message;
  }

  public boolean hasReplyHandler() {
    return replyHandler != null;
  }
}
