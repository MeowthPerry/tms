package ru.github.meperry.tms.backend.security.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.github.meperry.tms.backend.security.jwt.JwtTokenProvider;
import ru.github.meperry.tms.backend.security.model.User;
import ru.github.meperry.tms.backend.security.request.LoginRequest;
import ru.github.meperry.tms.backend.security.request.RegisterRequest;
import ru.github.meperry.tms.backend.security.service.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;

  private final JwtTokenProvider jwtTokenProvider;

  private final UserService userService;

  @Autowired
  public AuthenticationController(AuthenticationManager authenticationManager,
      JwtTokenProvider jwtTokenProvider, UserService userService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.userService = userService;
  }

  @PostMapping("/login") // TODO: 24.07.2023 реализовать возможность указать место, куда будет записан токен
  public ResponseEntity<User> login(@RequestBody LoginRequest requestDto,
      HttpServletResponse response) {
    try {
      String username = requestDto.getUsername();

      // пробуем аутентифицировать
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));

      User user = userService.findByUsername(username)
          .orElseThrow(() -> new UsernameNotFoundException(
              "User with username: " + username + " not found"));
      String token = jwtTokenProvider.createToken(username);

      addTokenToCookie(token, response);

      return ResponseEntity.ok(user);
    }
    catch (AuthenticationException e) {
      throw new BadCredentialsException("Invalid username or password");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody RegisterRequest requestDto,
      HttpServletResponse response) {
    String username = requestDto.getUsername();

    // TODO: 25.06.2023 add check for unique fields

    User user = User.builder()
        .email(requestDto.getEmail())
        .username(username)
        .firstname(requestDto.getFirstname())
        .lastname(requestDto.getLastname())
        .password(requestDto.getPassword())
        .build();

    userService.register(user);

    String token = jwtTokenProvider.createToken(username);
    addTokenToCookie(token, response);

    return ResponseEntity.ok(user);
  }

  private void addTokenToCookie(String token, HttpServletResponse response) {
    Cookie cookie = new Cookie(JwtTokenProvider.TOKEN, token);
    cookie.setPath("/api");
    response.addCookie(cookie);
  }
}