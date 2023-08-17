package ru.github.meperry.tms.backend.model;

import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.github.meperry.tms.backend.security.model.User;

/**
 * @author Islam Khabibullin
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "groups")
@Data
public class Group extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_id")
  private Long groupId;

  @ManyToOne
  @JoinColumn(name = "stage_id", nullable = false)
  @JsonBackReference
  @EqualsAndHashCode.Exclude
  private Stage stage;

  @OneToMany(mappedBy = "group")
  @JsonManagedReference
  private List<Match> matches;

  @ManyToMany
  @JoinTable(
      name = "link_group_user",
      joinColumns = @JoinColumn(name = "group_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> participants;
}
