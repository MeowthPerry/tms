package ru.github.meperry.tms.backend.request;

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
}
