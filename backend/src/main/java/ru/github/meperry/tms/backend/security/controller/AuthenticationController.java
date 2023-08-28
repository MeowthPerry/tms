package ru.github.meperry.tms.backend.security.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.github.meperry.tms.api.security.dto.LoginRequest;
import ru.github.meperry.tms.api.security.dto.RegisterRequest;
import ru.github.meperry.tms.api.security.dto.UserDto;
import ru.github.meperry.tms.api.security.dto.UserToken;
import ru.github.meperry.tms.backend.security.jwt.JwtTokenProvider;
import ru.github.meperry.tms.backend.security.model.User;
import ru.github.meperry.tms.backend.security.service.UserService;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserService userService;
  private final ModelMapper modelMapper;

  @PostMapping("/login")
  public ResponseEntity<UserToken> login(@RequestBody LoginRequest requestDto,
      HttpServletResponse response,
      @RequestParam(name = "token_to_cookie", required = false, defaultValue = "false") Boolean tokenToCookie) {
    try {
      String username = requestDto.getUsername();

      // пробуем аутентифицировать
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));

      User user = userService.findByUsername(username)
          .orElseThrow(() -> new UsernameNotFoundException(
              "User with username: " + username + " not found"));
      String token = jwtTokenProvider.createToken(username);

      if (tokenToCookie) {
        addTokenToCookie(token, response);
      }

      return ResponseEntity.ok(new UserToken(modelMapper.map(user, UserDto.class), token));
    }
    catch (AuthenticationException e) {
      throw new BadCredentialsException("Invalid username or password");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<UserToken> register(@RequestBody RegisterRequest requestDto,
      HttpServletResponse response,
      @RequestParam(name = "token_to_cookie", required = false, defaultValue = "false") Boolean tokenToCookie) {
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

    if (tokenToCookie) {
      addTokenToCookie(token, response);
    }

    return ResponseEntity.ok(new UserToken(modelMapper.map(user, UserDto.class), token));
  }

  private void addTokenToCookie(String token, HttpServletResponse response) {
    Cookie cookie = new Cookie(JwtTokenProvider.TOKEN, token);
    cookie.setPath("/api");
    response.addCookie(cookie);
  }
}
