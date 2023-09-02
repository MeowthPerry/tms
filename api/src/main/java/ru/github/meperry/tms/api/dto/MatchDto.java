package ru.github.meperry.tms.api.dto;

import java.util.Date;

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
public class MatchDto {

  private Long matchId;
  private Integer round;
  private Integer orderNumber;
  private Date matchDate;
  private UserDto participantOne;
  private Integer participantOneScore;
  private UserDto participantTwo;
  private Integer participantTwoScore;
}
