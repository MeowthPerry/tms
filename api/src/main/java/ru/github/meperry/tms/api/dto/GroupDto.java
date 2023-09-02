package ru.github.meperry.tms.api.dto;

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
public class GroupDto {

  private Long groupId;
  private List<MatchDto> matches;
  private List<UserDto> participants;
}
