package ru.github.meperry.tms.backend.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Islam Khabibullin
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "round_robin_stage_metadata")
@Data
public class RoundRobinStageMetadata extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "round_robin_stage_metadata_id")
  @JsonIgnore
  private Long roundRobinStageMetadataId;

  @OneToOne
  @JoinColumn(name = "round_robin_stage_id", nullable = false)
  @JsonBackReference
  private Stage roundRobinStage;

  @Column(nullable = false, name = "group_count")
  private int groupCount;

  @Column(nullable = false, name = "passing_count")
  private int passingCount;
}
