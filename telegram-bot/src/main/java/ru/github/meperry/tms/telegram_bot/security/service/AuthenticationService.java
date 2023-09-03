package ru.github.meperry.tms.telegram_bot.security.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import reactor.core.publisher.Mono;
import ru.github.meperry.tms.api.security.api.AuthenticationApi;
import ru.github.meperry.tms.api.security.dto.LoginRequest;
import ru.github.meperry.tms.api.security.dto.RegisterRequest;
import ru.github.meperry.tms.telegram_bot.security.domain.UserIdToken;
import ru.github.meperry.tms.telegram_bot.security.repository.TokenRepository;

/**
 * @author Islam Khabibullin
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  @Value("${tms.telegram.bot.secret}")
  private String tmsTelegramBotSecret;

  private final TokenRepository tokenRepository;
  private final AuthenticationApi authenticationApi;

  public void saveToken(Long userId, String token) {
    tokenRepository.save(new UserIdToken(userId, token));
  }

  public String getToken(User user) {
    Optional<UserIdToken> userIdToken = tokenRepository.findById(user.getId());

    if (userIdToken.isPresent()) {
      return userIdToken.get().getToken();
    }

    return loginOrRegister(user).block();
  }

  public Mono<String> loginOrRegister(User user) {
    return authenticationApi.login(new LoginRequest(user.getUserName(), getPassword(user.getId())))
        .map(
            userToken -> {
              saveToken(user.getId(), userToken.getAccessToken());
              return userToken.getAccessToken();
            })
        .onErrorResume(
            error -> {
              // регистрируемся если не получилось аутентифицировать
              RegisterRequest registerRequest = new RegisterRequest(
                  user.getUserName(),
                  user.getFirstName(),
                  user.getLastName(),
                  null,
                  getPassword(user.getId())
              );

              return authenticationApi.register(registerRequest)
                  .map(
                      userToken -> {
                        saveToken(user.getId(), userToken.getAccessToken());
                        return userToken.getAccessToken();
                      });
            }
        );
  }

  private String getPassword(Long userId) {
    return DigestUtils.sha256Hex(tmsTelegramBotSecret + userId);
  }
}
