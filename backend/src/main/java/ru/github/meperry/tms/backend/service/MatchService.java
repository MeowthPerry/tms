package ru.github.meperry.tms.backend.service;

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

  public List<Match> createMatchesForSingleElimination(List<User> participants) {
    // TODO 14.08 реализовать
    throw new UnsupportedOperationException();
  }
}
