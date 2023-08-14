package ru.github.meperry.tms.backend.security.request;

import lombok.Data;

@Data
public class LoginRequest {
  private String username;
  private String password;
}
