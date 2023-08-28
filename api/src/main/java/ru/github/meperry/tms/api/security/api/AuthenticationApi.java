package ru.github.meperry.tms.api.security.api;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.github.meperry.tms.api.api.BaseApi;
import ru.github.meperry.tms.api.security.dto.RegisterRequest;
import ru.github.meperry.tms.api.security.dto.UserToken;

/**
 * @author Islam Khabibullin
 */
@Component
public class AuthenticationApi extends BaseApi {

  public Mono<UserToken> register(RegisterRequest registerRequest) {
    return webClient.post()
        .uri("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(registerRequest), RegisterRequest.class)
        .retrieve()
        .bodyToMono(UserToken.class);
  }
}
