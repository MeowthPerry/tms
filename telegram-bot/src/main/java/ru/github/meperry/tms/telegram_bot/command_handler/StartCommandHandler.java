package ru.github.meperry.tms.telegram_bot.command_handler;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.github.meperry.tms.api.security.api.AuthenticationApi;
import ru.github.meperry.tms.api.security.dto.RegisterRequest;
import ru.github.meperry.tms.telegram_bot.domain.MessageExchange;
import ru.github.meperry.tms.telegram_bot.domain.MessageForBot;

/**
 * @author Islam Khabibullin
 */
@Component
@RequiredArgsConstructor
public class StartCommandHandler extends CommandHandler {

  @Value("${tms.telegram.bot.secret}")
  private String tmsTelegramBotSecret;

  private final AuthenticationApi authenticationApi;

  @Override
  String getCommand() {
    return "/start";
  }

  @Override
  public void handle(MessageForBot messageForBot) {
    User forwardFrom = messageForBot.getMessage().getFrom();
    RegisterRequest registerRequest = new RegisterRequest(
        forwardFrom.getUserName(),
        forwardFrom.getFirstName(),
        forwardFrom.getLastName(),
        null,
        DigestUtils.sha256Hex(tmsTelegramBotSecret + forwardFrom.getId())
    );

    authenticationApi.register(registerRequest)
        .subscribe(
            userToken -> {
              MessageExchange messageExchange = new MessageExchange(messageForBot.getChatId(), userToken.toString());
              botService.sendMessage(messageExchange);
            });
  }
}
