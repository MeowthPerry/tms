package ru.github.meperry.tms.backend.security.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.github.meperry.tms.backend.model.BaseEntity;
import ru.github.meperry.tms.backend.model.Status;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "username", unique = true)
  private String username;

  @Column(name = "firstname")
  private String firstname;

  @Column(name = "lastname")
  private String lastname;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "password")
  @JsonIgnore
  private String password;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  @JsonIgnore
  private Role role;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  @JsonIgnore
  private Status status;
}
