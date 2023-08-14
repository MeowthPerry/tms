package ru.github.meperry.tms.backend.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.github.meperry.tms.backend.model.Invitation;
import ru.github.meperry.tms.backend.repository.InvitationRepository;

/**
 * @author Islam Khabibullin
 */
@Service
@RequiredArgsConstructor
public class InvitationService {

  private final InvitationRepository invitationRepository;

  public Invitation save(Invitation invitation) {
    return invitationRepository.save(invitation);
  }

  public Optional<Invitation> findById(Long id) {
    return invitationRepository.findById(id);
  }

  public void delete(Invitation invitation) {
    invitationRepository.delete(invitation);
  }
}
