package ru.github.meperry.tms.backend.model;

import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Islam Khabibullin
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stages")
@Data
public class Stage extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "stage_id")
  private Long stageId;

  @Enumerated(EnumType.STRING)
  @Column(name = "stage_type", nullable = false)
  private StageType stageType;

  @ManyToOne
  @JoinColumn(name = "tournament_id", nullable = false)
  @JsonBackReference
  private Tournament tournament;

  @OneToMany(mappedBy = "stage")
  @JsonManagedReference
  private Set<Group> groups;

  @Column(name = "order_number")
  private int orderNumber;
}
