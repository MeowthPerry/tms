package ru.github.meperry.tms.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.github.meperry.tms.backend.model.Match;

/**
 * @author Islam Khabibullin
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

  Optional<Match> findByParticipantOneUserIdAndParticipantTwoUserId(Long participantOneId, Long participantTwoId);
}
