package ru.github.meperry.tms.backend.request;

import java.sql.Date;

import lombok.Data;
import ru.github.meperry.tms.backend.model.RoundRobinStageMetadata;

/**
 * @author Islam Khabibullin
 */
@Data
public class TournamentCreationRequest {
  private String name;
  private String description;
  private Date startDate;
  private CreatingTournamentType type;

  // if type == CreatingTournamentType.ROUND_ROBIN || type == CreatingTournamentType.ROUND_ROBIN.PLAY_OFFS
  // Round-robin stage metadata
  private int groupCount;
  private int winnerCount;
}
