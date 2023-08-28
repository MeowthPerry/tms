package ru.github.meperry.tms.api.security.dto;

import lombok.Data;

/**
 * @author Islam Khabibullin
 */
@Data
public class UserToken {

  private final UserDto user;
  private final String accessToken;
}
