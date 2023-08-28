package ru.github.meperry.tms.telegram_bot.command_handler;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import reactor.core.publisher.Mono;
import ru.github.meperry.tms.api.security.api.AuthenticationApi;
import ru.github.meperry.tms.api.security.dto.RegisterRequest;
import ru.github.meperry.tms.telegram_bot.domain.MessageExchange;

/**
 * @author Islam Khabibullin
 */
@Component
@RequiredArgsConstructor
public class StartCommandHandler implements AsyncCommandHandler {

  @Value("${tms.telegram.bot.secret}")
  private String tmsTelegramBotSecret;

  private final AuthenticationApi authenticationApi;

  @Override
  public boolean supports(Message message, String textWithoutBotName) {
    return textWithoutBotName.startsWith("/start");
  }

  @Override
  public Mono<MessageExchange> handle(Message message, String textWithoutBotName) {
    User forwardFrom = message.getFrom();
    RegisterRequest registerRequest = new RegisterRequest(
        forwardFrom.getUserName(),
        forwardFrom.getFirstName(),
        forwardFrom.getLastName(),
        null,
        DigestUtils.sha256Hex(tmsTelegramBotSecret + forwardFrom.getId())
    );

    return authenticationApi.register(registerRequest)
        .flatMap(
            userToken -> Mono.just(new MessageExchange(message.getChatId(), userToken.toString())));
  }
}
