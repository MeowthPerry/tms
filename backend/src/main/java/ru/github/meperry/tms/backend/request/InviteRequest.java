package ru.github.meperry.tms.backend.request;

import lombok.Data;

/**
 * @author Islam Khabibullin
 */
@Data
public class InviteRequest {
  private Long tournamentId;
  private String username;
}
