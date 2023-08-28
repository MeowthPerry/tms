package ru.github.meperry.tms.api.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private Long userId;
  private String username;
  private String firstname;
  private String lastname;
  private String email;
}
