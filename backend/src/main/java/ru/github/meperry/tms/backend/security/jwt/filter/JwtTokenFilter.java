package ru.github.meperry.tms.backend.security.jwt.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.github.meperry.tms.backend.security.jwt.JwtTokenProvider;
import ru.github.meperry.tms.backend.security.jwt.exception.JwtAuthenticationException;

@Component
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {

  private final JwtTokenProvider jwtTokenProvider;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
      throws IOException, ServletException {
    String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        if (auth != null) {
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }
    } catch (JwtAuthenticationException e) {
      log.error("Jwt exception", e);
      ((HttpServletResponse) res).setStatus(HttpStatus.FORBIDDEN.value());
    }

    filterChain.doFilter(req, res);
  }
}
