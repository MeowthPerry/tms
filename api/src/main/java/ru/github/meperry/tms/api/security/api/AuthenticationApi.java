package ru.github.meperry.tms.api.security.api;

import org.springframework.http.HttpMethod;
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
    return request(HttpMethod.POST, registerRequest, UserToken.class);
  }
}
