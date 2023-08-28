package ru.github.meperry.tms.api.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Islam Khabibullin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {
  private UserDto user;
  private String accessToken;
}
