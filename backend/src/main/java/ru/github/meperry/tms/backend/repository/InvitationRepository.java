package ru.github.meperry.tms.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.github.meperry.tms.backend.model.Invitation;

/**
 * @author Islam Khabibullin
 */
@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
}
