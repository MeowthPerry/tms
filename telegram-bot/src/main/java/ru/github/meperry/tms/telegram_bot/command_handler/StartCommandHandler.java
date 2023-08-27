package ru.github.meperry.tms.telegram_bot.command_handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.github.meperry.tms.telegram_bot.domain.MessageExchange;

/**
 * @author Islam Khabibullin
 */
@Component
public class StartCommandHandler implements CommandHandler {

  @Override
  public boolean supports(Message message, String textWithoutBotName) {
    return textWithoutBotName.startsWith("/start");
  }

  @Override
  public MessageExchange handle(Message message, String textWithoutBotName) {
    return null;
  }
}
