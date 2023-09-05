package ru.github.meperry.tms.backend.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

  @Transactional
  public void generateGroupsWithMatchesAndSave(Stage stage, List<User> participants) {

    List<Group> groups;
    switch (stage.getStageType()) {
      case SINGLE_ELIMINATION:
        Group singleGroup = new Group();
        singleGroup.setStage(stage);
        // если не создать новый лист, то будет ошибка
        // Found shared references to a collection org.hibernate.HibernateException
        singleGroup.setParticipants(new ArrayList<>(participants));
        Group savedGroup = groupService.save(singleGroup);

        List<Match> seMatches = matchService.generateSingleEliminationMatches(savedGroup.getParticipants());

        // сохраняем матчи
        seMatches.forEach(match -> match.setGroup(savedGroup));
        seMatches = matchService.saveAll(seMatches);

        savedGroup.setMatches(seMatches);

        groups = Collections.singletonList(savedGroup);
        break;
      case ROUND_ROBIN:
        groups = groupService.distributeByGroups(participants,
            stage.getRoundRobinStageMetadata().getGroupCount()
        );

        // сохраняем группы
        groups.forEach(group -> group.setStage(stage));
        groups = groupService.saveAll(groups);

        // генерируем матчи для каждой группы
        groups.forEach(group -> {
          List<Match> rrMatches = matchService.generateRoundRobinMatches(group.getParticipants());

          // сохраняем матчи
          rrMatches.forEach(match -> match.setGroup(group));
          rrMatches = matchService.saveAll(rrMatches);

          group.setMatches(rrMatches);
        });
        break;
      default:
        throw new IllegalStateException("Unknown stage type: should be SINGLE_ELIMINATION or ROUND_ROBIN");
    }

    stage.setGroups(groups);
  }
}
