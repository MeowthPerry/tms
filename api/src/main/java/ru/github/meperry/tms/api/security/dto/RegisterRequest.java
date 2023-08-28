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
public class RegisterRequest {
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String password;
}
