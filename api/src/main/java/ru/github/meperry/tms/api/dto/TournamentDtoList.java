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
public class TournamentDtoList {

  private List<TournamentDto> result;
}
