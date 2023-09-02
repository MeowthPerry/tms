package ru.github.meperry.tms.api.dto;

import java.time.LocalDate;

import lombok.Data;

/**
 * @author Islam Khabibullin
 */
@Data
public class TournamentCreationRequest {
  private String name;
  private String description;
  private LocalDate startDate;
  private CreatingTournamentType type;

  // if type == CreatingTournamentType.PLAY_OFFS
  // Round-robin stage metadata
  private Integer groupCount;
  private Integer passingCount;
}
