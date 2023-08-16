package ru.github.meperry.tms.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.github.meperry.tms.backend.model.RoundRobinStageMetadata;
import ru.github.meperry.tms.backend.model.Stage;
import ru.github.meperry.tms.backend.model.StageType;
import ru.github.meperry.tms.backend.model.Tournament;
import ru.github.meperry.tms.backend.request.CreatingTournamentType;
import ru.github.meperry.tms.backend.request.TournamentCreationRequest;
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

  @GetMapping("/{tournamentId}")
  public ResponseEntity<Tournament> get(@PathVariable Long tournamentId) {
    Tournament tournament = tournamentService.findById(tournamentId)
        // TODO 12.08.23 заменить на понятные исключения
        .orElseThrow(RuntimeException::new);
    return ResponseEntity.ok(tournament);
  }

  @PostMapping
  public ResponseEntity<Tournament> create(@RequestBody TournamentCreationRequest request) {
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
        roundRobinStageMetadata.setWinnerCount(request.getWinnerCount());
        roundRobinStageMetadata.setRoundRobinStage(savedStage);
        roundRobinStageMetadataService.save(roundRobinStageMetadata);
      }

      return savedStage;
    }).collect(Collectors.toList());
    savedTournament.setStages(stages);

    return new ResponseEntity<>(savedTournament, HttpStatus.CREATED);
  }

  /**
   * Распределяет участников по группам и генерирует матчи для первой стадии (возможно единственной) турнира
   */
  @PutMapping("/{tournamentId}/start")
  public ResponseEntity<Tournament> start(@PathVariable Long tournamentId) {
    Tournament tournament = tournamentService.findById(tournamentId)
        .orElseThrow(RuntimeException::new);
    return ResponseEntity.ok(tournamentService.start(tournament));
  }

  private List<Stage> createStagesByType(CreatingTournamentType type) {
    List<Stage> stages = new ArrayList<>();
    switch (type) {
      case ROUND_ROBIN:
        stages.add(createRoundRobinStage());
        break;
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
