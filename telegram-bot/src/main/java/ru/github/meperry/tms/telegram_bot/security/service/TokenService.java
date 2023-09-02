package ru.github.meperry.tms.telegram_bot.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.github.meperry.tms.telegram_bot.security.exception.MissingTokenForUserException;
import ru.github.meperry.tms.telegram_bot.security.repository.TokenRepository;

/**
 * @author Islam Khabibullin
 */
@Service
@RequiredArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;

  public String tokenFor(Long userId) {
    return tokenRepository.findById(userId)
        .orElseThrow(() -> new MissingTokenForUserException(userId))
        .getToken();
  }
}
