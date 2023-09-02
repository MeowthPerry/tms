package ru.github.meperry.tms.backend.model;

import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.github.meperry.tms.backend.security.model.User;

/**
 * @author Islam Khabibullin
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "matches")
@Data
public class Match extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "match_id")
  private Long matchId;

  @ManyToOne
  @JoinColumn(name = "group_id", nullable = false)
  @JsonBackReference
  @EqualsAndHashCode.Exclude
  private Group group;

  @Column(nullable = false)
  private Integer round;

  @Column(nullable = false, name = "order_number")
  private Integer orderNumber;

  @Column(name = "match_date")
  private Date matchDate;

  @ManyToOne
  @JoinColumn(name = "user_participant_one_id", nullable = false)
  private User participantOne;

  @Column(name = "user_participant_one_score")
  private Integer participantOneScore;

  @ManyToOne
  @JoinColumn(name = "user_participant_two_id")
  private User participantTwo;

  @Column(name = "user_participant_two_score")
  private Integer participantTwoScore;

  public User winner() {
    return participantTwo == null || participantOneScore > participantTwoScore
           ? participantOne
           : participantTwo;
  }
}
