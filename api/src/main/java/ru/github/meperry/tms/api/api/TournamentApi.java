package ru.github.meperry.tms.api.api;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.github.meperry.tms.api.dto.TournamentCreationRequest;
import ru.github.meperry.tms.api.dto.TournamentDto;

/**
 * @author Islam Khabibullin
 */
@Component
public class TournamentApi extends BaseApi {

  public Mono<TournamentDto> create(TournamentCreationRequest request) {
    return request(HttpMethod.POST, request, TournamentDto.class);
  }
}
