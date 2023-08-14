package ru.github.meperry.tms.backend.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.github.meperry.tms.backend.model.Stage;
import ru.github.meperry.tms.backend.model.Tournament;
import ru.github.meperry.tms.backend.model.TournamentStatus;
import ru.github.meperry.tms.backend.repository.TournamentRepository;
import ru.github.meperry.tms.backend.security.service.RuntimeUserService;

/**
 * @author Islam Khabibullin
 */
@Service
@RequiredArgsConstructor
public class TournamentService {

  private final RuntimeUserService runtimeUserService;

  private final TournamentRepository tournamentRepository;

  public Tournament save(Tournament tournament) {
    if (tournament.getCreator() == null) {
      tournament.setCreator(runtimeUserService.currentUser().user());
    }
    if (tournament.getStatus() == null) {
      tournament.setTournamentStatus(TournamentStatus.NOT_STARTED);
    }
    return tournamentRepository.save(tournament);
  }

  public Optional<Tournament> findById(Long id) {
    return tournamentRepository.findById(id);
  }

  public Tournament start(Tournament tournament) {
    Stage firstStage = tournament.getStages().get(0);

    switch (firstStage.getStageType()) {
      case SINGLE_ELIMINATION:
        break;
      case ROUND_ROBIN:
        break;
      default:
        throw new IllegalStateException(
            "Unknown stage type: should be SINGLE_ELIMINATION or ROUND_ROBIN");
    }

    tournament.setTournamentStatus(TournamentStatus.STARTED);

    return tournament;
  }
}
