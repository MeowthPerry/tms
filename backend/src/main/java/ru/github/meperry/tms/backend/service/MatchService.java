package ru.github.meperry.tms.backend.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.github.meperry.tms.backend.model.Match;
import ru.github.meperry.tms.backend.repository.MatchRepository;
import ru.github.meperry.tms.backend.security.model.User;

/**
 * @author Islam Khabibullin
 */
@Repository
@RequiredArgsConstructor
public class MatchService {

  private final MatchRepository matchRepository;

  public List<Match> generateRoundRobinMatches(List<User> participants) {
    if (participants == null || participants.size() < 2) {
      throw new IllegalArgumentException();
    }

    List<Match> matches = new ArrayList<>();

    int numParticipants = participants.size();

    for (int round = 0; round < numParticipants - 1; round++) {
      for (int i = 0; i < numParticipants / 2; i++) {
        User participantOne = participants.get(i);
        User participantTwo = participants.get(numParticipants - 1 - i);

        Match match = new Match();
        match.setRound(round);
        match.setOrderNumber(i);
        match.setParticipantOne(participantOne);
        match.setParticipantTwo(participantTwo);
        matches.add(match);
      }

      // поворачиваем
      participants.add(1, participants.remove(numParticipants - 1));
    }

    return matches;
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
      match.setParticipantTwo(participantIterator.next());
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
}
