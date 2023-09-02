package ru.github.meperry.tms.telegram_bot.command_handler;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import ru.github.meperry.tms.telegram_bot.domain.MessageForBot;
import ru.github.meperry.tms.telegram_bot.service.BotService;

/**
 * @author Islam Khabibullin
 */
public abstract class CommandHandler {

  protected BotService botService;

  @PostConstruct
  public void subscribeToMessagesForBot() {
    botService.subscribeToMessagesForBotByCommand(this::handle, getCommand());
  }

  @Autowired
  public void setBotService(BotService botService) {
    this.botService = botService;
  }

  abstract String getCommand();

  abstract void handle(MessageForBot message);
}
