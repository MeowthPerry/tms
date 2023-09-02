package ru.github.meperry.tms.telegram_bot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Islam Khabibullin
 */
@Data
@AllArgsConstructor
public class MessageForBot {

  private Message message;
  private String textWithoutBotName;

  public Long getChatId() {
    return message.getChatId();
  }
}
