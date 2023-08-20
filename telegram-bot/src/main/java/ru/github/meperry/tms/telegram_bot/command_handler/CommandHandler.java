package ru.github.meperry.tms.telegram_bot.command_handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Islam Khabibullin
 */
public interface CommandHandler {

  boolean supports(Message message);

  SendMessage handle(Message message);
}
