package ru.github.meperry.tms.telegram_bot.util;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Islam Khabibullin
 */
@UtilityClass
public class MessageUtil {

  /**
   * Текстовым сообщением для бота будет считаться любое сообщение из приватного чата с ботом или
   * сообщение начинающееся с @botUsername (с пробелом после этого) из группового чата
   */
  public static boolean isTextMessageForBor(Message message, String botUsername) {
    if (!message.hasText()) {
      return false;
    }
    return !message.getChat().isGroupChat() || message.getText()
        .startsWith("@" + botUsername + " ");
  }

  /**
   * Удаляет упоминание имени бота из текста, если оно есть
   */
  public static String getTextWithoutBotName(String text, String botUsername) {
    if (text.startsWith("@" + botUsername + " ")) {
      return text.substring(("@" + botUsername + " ").length());
    }
    return text;
  }
}
