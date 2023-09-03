package ru.github.meperry.tms.telegram_bot.command_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.github.meperry.tms.telegram_bot.domain.MessageForBot;
import ru.github.meperry.tms.telegram_bot.security.service.AuthenticationService;

/**
 * @author Islam Khabibullin
 */
@Component
@RequiredArgsConstructor
public class StartCommandHandler extends CommandHandler {

  private final AuthenticationService authenticationService;

  @Override
  String getCommand() {
    return "/start";
  }

  @Override
  public void handle(MessageForBot messageForBot) {
    User forwardFrom = messageForBot.getMessage().getFrom();

    authenticationService.loginOrRegister(forwardFrom).subscribe();
  }
}
