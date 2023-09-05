package ru.github.meperry.tms.api.api;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.github.meperry.tms.api.dto.TournamentCreationRequest;
import ru.github.meperry.tms.api.dto.TournamentDto;
import ru.github.meperry.tms.api.dto.TournamentDtoList;

/**
 * @author Islam Khabibullin
 */
@Component
public class TournamentApi extends BaseApi {

  public Mono<TournamentDto> create(String token, TournamentCreationRequest request) {
    return request(HttpMethod.POST, "/api/tournament", token, request, TournamentDto.class);
  }

  public Mono<ResponseEntity<Void>> registerToTournament(String token, Long tournamentId) {
    return request(HttpMethod.PUT, String.format("/api/tournament/%d/register", tournamentId), token, null);
  }

  public Mono<TournamentDto> startTournament(String token, Long tournamentId) {
    return request(HttpMethod.PUT, String.format("/api/tournament/%d/start", tournamentId), token, null, TournamentDto.class);
  }

  public Mono<TournamentDtoList> myTournaments(String token) {
    return request(HttpMethod.GET, "/api/tournament/my_tournaments", token, null, TournamentDtoList.class);
  }
}
