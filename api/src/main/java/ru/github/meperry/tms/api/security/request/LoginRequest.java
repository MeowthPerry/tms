package ru.github.meperry.tms.api.security.request;

import lombok.Data;

@Data
public class LoginRequest {
  private String username;
  private String password;
}
