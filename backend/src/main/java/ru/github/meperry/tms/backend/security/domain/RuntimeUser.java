package ru.github.meperry.tms.backend.security.domain;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.github.meperry.tms.backend.model.Status;
import ru.github.meperry.tms.backend.security.model.Role;
import ru.github.meperry.tms.backend.security.model.User;

/**
 * @author Islam Khabibullin
 */
public class RuntimeUser implements UserDetails {

  private final Long id;
  private final String username;
  private final String firstname;
  private final String lastname;
  private final String password;
  private final String email;
  private final boolean enabled;
  private final Collection<? extends GrantedAuthority> authorities;

  public RuntimeUser(User user) {
    this(
        user.getUserId(),
        user.getUsername(),
        user.getFirstname(),
        user.getLastname(),
        user.getEmail(),
        user.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())),
        user.getStatus().equals(Status.ACTIVE)
    );
  }

  public RuntimeUser(
      Long id,
      String username,
      String firstname,
      String lastname,
      String email,
      String password,
      Collection<? extends GrantedAuthority> authorities,
      boolean enabled) {
    this.id = id;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
    this.enabled = enabled;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public User user() {
    User user = new User();
    user.setUserId(id);
    user.setUsername(username);
    user.setFirstname(firstname);
    user.setLastname(lastname);
    user.setEmail(email);
    user.setPassword(password);
    user.setRole(Role.valueOf(authorities.iterator().next().getAuthority()));
    return user;
  }
}
