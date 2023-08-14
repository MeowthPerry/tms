package ru.github.meperry.tms.backend.model;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.github.meperry.tms.backend.security.model.User;

/**
 * @author Islam Khabibullin
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "invitations")
@Data
public class Invitation extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "invitation_id")
  private Long invitationId;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "tournament_id", nullable = false)
  private Tournament tournament;
}
