package ru.github.meperry.tms.api.api;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.github.meperry.tms.api.dto.TournamentCreationRequest;
import ru.github.meperry.tms.api.dto.TournamentDto;

/**
 * @author Islam Khabibullin
 */
@Component
public class TournamentApi extends BaseApi {

  public Mono<TournamentDto> create(String token, TournamentCreationRequest request) {
    return request(HttpMethod.POST, "/api/tournament", token, request, TournamentDto.class);
  }

  public Mono<ResponseEntity<Void>> registerToTournament(String token, Long tournamentId) {
    return request(HttpMethod.PUT, String.format("/api/tournament/%d/register", tournamentId), token);
  }

  public Mono<List<TournamentDto>> myTournaments(String token) {
    return request(HttpMethod.GET, "/api/tournament/my_tournaments", token, new ParameterizedTypeReference<>() {});
  }
}
