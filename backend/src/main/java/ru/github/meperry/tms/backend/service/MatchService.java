package ru.github.meperry.tms.backend.service;

import java.util.ArrayList;
import java.util.List;

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
    List<Match> matches = new ArrayList<>();

    if (participants == null || participants.size() < 2) {
      throw new IllegalArgumentException();
    }

    int numParticipants = participants.size();

    for (int round = 0; round < numParticipants - 1; round++) {
      for (int i = 0; i < numParticipants / 2; i++) {
        User participantOne = participants.get(i);
        User participantTwo = participants.get(numParticipants - 1 - i);

        Match match = new Match();
        match.setRound(round);
        match.setOrder(i);
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

  }

  public List<Match> saveAll(List<Match> matches) {
    return matchRepository.saveAll(matches);
  }
}
