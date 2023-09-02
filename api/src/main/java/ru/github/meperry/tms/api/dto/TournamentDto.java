package ru.github.meperry.tms.api.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.github.meperry.tms.api.security.dto.UserDto;

/**
 * @author Islam Khabibullin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentDto {

  private Long tournamentId;
  private String name;
  private String description;
  private LocalDate startDate;
  private UserDto creator;
  private List<UserDto> participants;
  private TournamentStatus tournamentStatus;
  private List<StageDto> stages;
}
