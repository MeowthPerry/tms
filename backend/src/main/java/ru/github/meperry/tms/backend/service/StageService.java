package ru.github.meperry.tms.backend.service;

import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.github.meperry.tms.backend.model.Group;
import ru.github.meperry.tms.backend.model.Match;
import ru.github.meperry.tms.backend.model.Stage;
import ru.github.meperry.tms.backend.repository.StageRepository;
import ru.github.meperry.tms.backend.security.model.User;

/**
 * @author Islam Khabibullin
 */
@Service
@RequiredArgsConstructor
public class StageService {

  private final StageRepository stageRepository;
  private final MatchService matchService;
  private final GroupService groupService;

  public Stage save(Stage stage) {
    return stageRepository.save(stage);
  }

  public void generateGroupsWithMatchesAndSave(Stage stage, List<User> participants) {

    List<Group> groups;
    switch (stage.getStageType()) {
      case SINGLE_ELIMINATION:
        Group oneGroup = new Group();
        oneGroup.setStage(stage);
        oneGroup.setParticipants(participants);
        oneGroup = groupService.save(oneGroup);

        //

        groups = Collections.singletonList(oneGroup);
        break;
      case ROUND_ROBIN:
        groups = groupService.distributeByGroups(participants,
            stage.getRoundRobinStageMetadata().getGroupCount()
        );

        // сохраняем группы
        groups.forEach(group -> {
          group.setStage(stage);
        });
        groups = groupService.saveAll(groups);

        // генерируем матчи для каждой группы
        groups.forEach(group -> {
          List<Match> matches = matchService.generateRoundRobinMatches(group.getParticipants());

          // сохраняем матчи
          matches.forEach(match -> {
            match.setGroup(group);
          });
          matches = matchService.saveAll(matches);

          group.setMatches(matches);
        });
        break;
      default:
        throw new IllegalStateException(
            "Unknown stage type: should be SINGLE_ELIMINATION or ROUND_ROBIN");
    }

    stage.setGroups(groups);
  }
}
