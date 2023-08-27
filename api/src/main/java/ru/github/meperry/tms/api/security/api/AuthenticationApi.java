package ru.github.meperry.tms.api.security.api;

import org.springframework.stereotype.Component;
import ru.github.meperry.tms.api.api.BaseApi;
import ru.github.meperry.tms.api.security.dto.RegisterRequest;
import ru.github.meperry.tms.api.security.dto.UserToken;

/**
 * @author Islam Khabibullin
 */
@Component
public class AuthenticationApi extends BaseApi {

  public UserToken register(RegisterRequest registerRequest) {
    throw new UnsupportedOperationException();
  }
}
