package ru.github.meperry.tms.backend.security.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.github.meperry.tms.backend.model.Status;
import ru.github.meperry.tms.backend.security.model.Role;
import ru.github.meperry.tms.backend.security.model.User;
import ru.github.meperry.tms.backend.security.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User register(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setStatus(Status.ACTIVE);
    user.setRole(Role.ROLE_USER);
    return userRepository.save(user);
  }

  public List<User> getAll() {
    return userRepository.findAll();
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
  }

  public void delete(Long id) {
    User user = findById(id).orElseThrow(() -> {
      // TODO: 24.06.2023 error explanation
      throw new RuntimeException();
    });
    user.setStatus(Status.DELETED);
    userRepository.save(user);
  }
}
