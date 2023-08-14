package ru.github.meperry.tms.backend.model;

import java.sql.Date;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.github.meperry.tms.backend.security.model.User;

/**
 * @author Islam Khabibullin
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tournaments")
@Data
public class Tournament extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tournament_id")
  private Long tournamentId;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(length = 1000)
  private String description;

  @Column(nullable = false)
  private Date startDate;

  @ManyToOne
  @JoinColumn(name = "user_creator_id", nullable = false)
  private User creator;

  // TODO: 25.06.2023 implement participants: individual and teams
  @ManyToMany
  @JoinTable(
      name = "link_tournament_user",
      joinColumns = @JoinColumn(name = "tournament_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> participants;

  @Enumerated(EnumType.STRING)
  @Column(name = "tournament_status", nullable = false)
  private TournamentStatus tournamentStatus;

  @OneToMany(mappedBy = "tournament")
  @JsonManagedReference
  private List<Stage> stages;
}
