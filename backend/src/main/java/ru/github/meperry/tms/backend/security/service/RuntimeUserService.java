package ru.github.meperry.tms.backend.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.github.meperry.tms.backend.security.model.RuntimeUser;
import ru.github.meperry.tms.backend.security.model.User;

@Service
public class RuntimeUserService implements UserDetailsService {

  private final UserService userService;

  @Autowired
  public RuntimeUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userService.findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("User with username: " + username + " not found"));
    return new RuntimeUser(user);
  }

  public RuntimeUser currentUser() {
    return (RuntimeUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
