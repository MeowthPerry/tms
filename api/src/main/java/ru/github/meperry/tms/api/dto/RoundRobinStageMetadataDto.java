package ru.github.meperry.tms.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Islam Khabibullin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundRobinStageMetadataDto {

  private Integer groupCount;
  private Integer passingCount;
}
