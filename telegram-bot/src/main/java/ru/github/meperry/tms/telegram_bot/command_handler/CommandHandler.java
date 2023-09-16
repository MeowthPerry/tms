package ru.github.meperry.tms.telegram_bot.command_handler;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.User;
import reactor.core.publisher.Mono;
import ru.github.meperry.tms.telegram_bot.domain.MessageForBot;
import ru.github.meperry.tms.telegram_bot.security.service.AuthenticationService;
import ru.github.meperry.tms.telegram_bot.service.BotService;

/**
 * @author Islam Khabibullin
 */
public abstract class CommandHandler {

  protected BotService botService;
  private AuthenticationService authenticationService;

  @PostConstruct
  public void subscribeToMessagesForBot() {
    botService.subscribeToMessagesForBotByCommand(this::handle, getCommand(), supportsGroupChat());
  }

  protected boolean supportsGroupChat() {
    return false;
  }

  @Autowired
  public void setBotService(BotService botService) {
    this.botService = botService;
  }

  @Autowired
  public void setAuthenticationService(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  abstract String getCommand();

  abstract void handle(MessageForBot message);

  protected Mono<String> tokenSupplier(User user) {
    return authenticationService.loginOrRegister(user);
  }
}
