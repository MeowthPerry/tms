package ru.github.meperry.tms.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.github.meperry.tms.api.dto.CreatingTournamentType;
import ru.github.meperry.tms.api.dto.TournamentCreationRequest;
import ru.github.meperry.tms.backend.model.RoundRobinStageMetadata;
import ru.github.meperry.tms.backend.model.Stage;
import ru.github.meperry.tms.api.dto.StageType;
import ru.github.meperry.tms.backend.model.Tournament;
import ru.github.meperry.tms.backend.security.domain.RuntimeUser;
import ru.github.meperry.tms.backend.security.model.User;
import ru.github.meperry.tms.backend.security.service.RuntimeUserService;
import ru.github.meperry.tms.backend.service.RoundRobinStageMetadataService;
import ru.github.meperry.tms.backend.service.StageService;
import ru.github.meperry.tms.backend.service.TournamentService;

/**
 * @author Islam Khabibullin
 */
@RestController
@RequestMapping("/tournament")
@RequiredArgsConstructor
public class TournamentController {

  private final TournamentService tournamentService;
  private final StageService stageService;
  private final RoundRobinStageMetadataService roundRobinStageMetadataService;
  private final RuntimeUserService runtimeUserService;

  @GetMapping("/{tournamentId}")
  public ResponseEntity<Tournament> get(@PathVariable Long tournamentId) {
    Tournament tournament = tournamentService.findById(tournamentId)
        // TODO 12.08.23 заменить на понятные исключения
        .orElseThrow(RuntimeException::new);
    return ResponseEntity.ok(tournament);
  }
  
  @PutMapping("/{tournamentId}/register")
  public void registerToTournament(@PathVariable Long tournamentId) {
    RuntimeUser runtimeUser = runtimeUserService.currentUser();

    Tournament tournament = tournamentService.findById(tournamentId)
        .orElseThrow(RuntimeException::new);

    List<User> participants = tournament.getParticipants();
    if (participants == null) {
      participants = new ArrayList<>();
      tournament.setParticipants(participants);
    }
    participants.add(runtimeUser.user());
    tournamentService.save(tournament);
  }

  @PostMapping
  public ResponseEntity<Tournament> create(@RequestBody TournamentCreationRequest request) {
    validateCreationRequest(request);

    Tournament tournament = new Tournament();
    tournament.setName(request.getName());
    tournament.setDescription(request.getDescription());
    tournament.setStartDate(request.getStartDate());

    Tournament savedTournament = tournamentService.save(tournament);

    List<Stage> stages = createStagesByType(request.getType()).stream().map(stage -> {
      stage.setTournament(savedTournament);
      Stage savedStage = stageService.save(stage);

      if (StageType.ROUND_ROBIN.equals(savedStage.getStageType())) {
        RoundRobinStageMetadata roundRobinStageMetadata = new RoundRobinStageMetadata();
        roundRobinStageMetadata.setGroupCount(request.getGroupCount());
        roundRobinStageMetadata.setPassingCount(request.getPassingCount());
        roundRobinStageMetadata.setRoundRobinStage(savedStage);
        roundRobinStageMetadata = roundRobinStageMetadataService.save(roundRobinStageMetadata);
        savedStage.setRoundRobinStageMetadata(roundRobinStageMetadata);
      }

      return savedStage;
    }).collect(Collectors.toList());
    savedTournament.setStages(stages);

    return new ResponseEntity<>(savedTournament, HttpStatus.CREATED);
  }

  private void validateCreationRequest(TournamentCreationRequest request) {
    if (CreatingTournamentType.SINGLE_ELIMINATION.equals(request.getType())) {
      if (request.getGroupCount() != null || request.getPassingCount() != null) {
        throw new IllegalArgumentException();
      }
    }
    else {
      if (request.getGroupCount() == null || request.getPassingCount() == null) {
        throw new IllegalArgumentException();
      }
    }
  }

  /**
   * Распределяет участников по группам и генерирует матчи для первой стадии (возможно единственной) турнира
   */
  @PutMapping("/{tournamentId}/start")
  public ResponseEntity<Tournament> start(@PathVariable Long tournamentId) {
    // TODO 19.08 Сделать так, чтобы только создатель мог начать турнир

    Tournament tournament = tournamentService.findById(tournamentId)
        .orElseThrow(RuntimeException::new);
    return ResponseEntity.ok(tournamentService.start(tournament));
  }

  private List<Stage> createStagesByType(CreatingTournamentType type) {
    List<Stage> stages = new ArrayList<>();
    switch (type) {
      case SINGLE_ELIMINATION:
        stages.add(createSingleEliminationStage());
        break;
      case PLAY_OFFS:
        Stage roundRobinStage = createRoundRobinStage();
        roundRobinStage.setOrderNumber(1);
        stages.add(roundRobinStage);
        Stage singleEliminationStage = createSingleEliminationStage();
        singleEliminationStage.setOrderNumber(2);
        stages.add(singleEliminationStage);
        break;
    }
    return stages;
  }

  private Stage createRoundRobinStage() {
    Stage roundRobinStage = new Stage();
    roundRobinStage.setStageType(StageType.ROUND_ROBIN);
    return roundRobinStage;
  }

  private Stage createSingleEliminationStage() {
    Stage roundRobinStage = new Stage();
    roundRobinStage.setStageType(StageType.SINGLE_ELIMINATION);
    return roundRobinStage;
  }
}
