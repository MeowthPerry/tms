package ru.github.meperry.tms.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Islam Khabibullin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StageDto {

  private Long stageId;
  private StageType stageType;
  private List<GroupDto> groups;
  private Integer orderNumber;
  private RoundRobinStageMetadataDto roundRobinStageMetadata;
}
