package ru.github.meperry.tms.api.security.request;

import lombok.Data;

/**
 * @author Islam Khabibullin
 */
@Data
public class RegisterRequest {
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String password;
}
