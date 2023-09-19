package ru.github.meperry.tms.backend.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;
import ru.github.meperry.tms.backend.event.MatchResultReportedEvent;
import ru.github.meperry.tms.backend.model.Match;
import ru.github.meperry.tms.backend.model.Stage;
import ru.github.meperry.tms.backend.repository.MatchRepository;
import ru.github.meperry.tms.backend.security.model.User;

/**
 * @author Islam Khabibullin
 */
@Repository
@RequiredArgsConstructor
public class MatchService {

  private final MatchRepository matchRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final StageService stageService;

  private static final User DUMMY_USER = new User();

  public List<Match> generateRoundRobinMatches(List<User> participants) {
    // не манипулируем изначальным списком
    participants = new ArrayList<>(participants);

    if (participants.size() < 2) {
      throw new IllegalArgumentException();
    }

    List<Match> matches = new ArrayList<>();

    if (participants.size() % 2 != 0) {
      participants.add(DUMMY_USER);
    }

    int numParticipants = participants.size();

    // TODO 17.08 неправильно считается если количество участников нечетное
    for (int round = 0; round < numParticipants - 1; round++) {
      for (int i = 0; i < numParticipants / 2; i++) {
        User participantOne = participants.get(i);
        User participantTwo = participants.get(numParticipants - 1 - i);

        if (participantOne.equals(DUMMY_USER) || participantTwo.equals(DUMMY_USER)) {
          continue;
        }

        Match match = new Match();
        match.setRound(round);
        match.setParticipantOne(participantOne);
        match.setParticipantTwo(participantTwo);
        matches.add(match);
      }

      // поворачиваем
      participants.add(1, participants.remove(numParticipants - 1));
    }

    // проставляем порядковые номера, не могли в предыдущем цикле из-за не существующего участника
    putDownSerialNumbers(matches);

    return matches;
  }

  private static void putDownSerialNumbers(List<Match> matches) {
    int currentOrder = 0;
    int currentRound = 0;
    for (Match match : matches) {
      if (match.getRound() != currentRound) {
        currentOrder = 0;
        currentRound = match.getRound();
      }
      match.setOrderNumber(currentOrder++);
    }
  }

  public List<Match> generateSingleEliminationMatches(List<User> participants) {
    if (participants == null || participants.size() < 2) {
      throw new IllegalArgumentException();
    }

    int pairsCount = (int) Math.ceil(participants.size() / 2.0);
    int matchCount = 1;
    while (matchCount < pairsCount) {
      matchCount *= 2;
    }

    int round = (int) (Math.log(matchCount) / Math.log(2));

    List<Match> matches = Stream.generate(Match::new)
        .limit(matchCount)
        .collect(Collectors.toList());

    Collections.shuffle(participants);

    Iterator<User> participantIterator = participants.iterator();

    // задаем первых игроков, а также раунд и порядковый номер матча
    for (int i = 0; i < matchCount; i++) {
      Match match = matches.get(i);
      match.setRound(round);
      match.setOrderNumber(i);
      match.setParticipantOne(participantIterator.next());
    }

    // задаем вторых игроков
    for (Match match : matches) {
      // в какой-то момент участники могут закончится
      if (participantIterator.hasNext()) {
        match.setParticipantTwo(participantIterator.next());
      }
      else {
        break;
      }
    }

    return matches;
  }

  public List<Match> saveAll(List<Match> matches) {
    return matchRepository.saveAll(matches);
  }

  /**
   * Задать результат матча
   *
   * @param participantOne      первый участник
   * @param participantOneScore счет первого участника
   * @param participantTwo      второй участник
   * @param participantTwoScore счет второго участника
   */
  public void report(User participantOne, Integer participantOneScore, User participantTwo,
      Integer participantTwoScore) {
    Match match = matchRepository.findByParticipantOneUserIdAndParticipantTwoUserId(
            participantOne.getUserId(),
            participantTwo.getUserId()
        )
        .orElseThrow(() -> new RuntimeException(
            String.format("Match for participants (%s, %s) not found", participantOne.getUsername(),
                participantTwo.getUsername()
            )));

    match.setParticipantOneScore(participantOneScore);
    match.setParticipantTwoScore(participantTwoScore);

    matchRepository.save(match);

    eventPublisher.publishEvent(new MatchResultReportedEvent(match));
  }

  @EventListener(MatchResultReportedEvent.class)
  public void matchResultReportedEventListener(MatchResultReportedEvent event) {
    // в зависимости от типа стадии генерируем следующие матчи или стартуем следующий этап
    Stage stage = event.match().getGroup().getStage();
    switch (stage.getStageType()) {
      case SINGLE_ELIMINATION:
        break;
      case ROUND_ROBIN:
        // проверяем, что все матчи во всех группах имеют победителя
        if (allMatchesHasWinner(stage)) {
          // если да, то стартуем плей-офф
          stageService.generateGroupsWithMatchesAndSave(
            stage.getTournament().getStages().get(1),
            stageService.getRoundRobinStageWinners(stage)
          );
        }
        break;
    }
  }

  private boolean allMatchesHasWinner(Stage stage) {
    return stage.getGroups()
        .stream()
        .flatMap(group -> group.getMatches().stream())
        .allMatch(Match::hasWinner);
  }
}
