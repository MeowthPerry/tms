package ru.github.meperry.tms.api.dto;

import java.sql.Date;

import lombok.Data;

/**
 * @author Islam Khabibullin
 */
@Data
public class TournamentCreationRequest {
  private String name;
  private String description;
  private Date startDate;
  private CreatingTournamentType type;

  // if type == CreatingTournamentType.PLAY_OFFS
  // Round-robin stage metadata
  private Integer groupCount;
  private Integer passingCount;
}
