package ru.github.meperry.tms.backend.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

  private final static String MESSAGE = "JWT token is expired or invalid";

  public JwtAuthenticationException() {
    super(MESSAGE);
  }

  public JwtAuthenticationException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
