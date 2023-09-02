package ru.github.meperry.tms.telegram_bot.security.exception;

/**
 * @author Islam Khabibullin
 */
public class MissingTokenForUserException extends IllegalStateException {

  public MissingTokenForUserException(Long userId) {
    super("There is no token for a user with an ID: " + userId);
  }
}
