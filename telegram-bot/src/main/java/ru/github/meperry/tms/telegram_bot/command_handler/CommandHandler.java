package ru.github.meperry.tms.telegram_bot.command_handler;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.github.meperry.tms.telegram_bot.domain.MessageExchange;

/**
 * @author Islam Khabibullin
 */
public interface CommandHandler {

  boolean supports(Message message, String textWithoutBotName);

  MessageExchange handle(Message message, String textWithoutBotName);
}
