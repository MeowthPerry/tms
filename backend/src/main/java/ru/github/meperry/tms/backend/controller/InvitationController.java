package ru.github.meperry.tms.backend.controller;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.github.meperry.tms.backend.model.Invitation;
import ru.github.meperry.tms.backend.model.Tournament;
import ru.github.meperry.tms.backend.request.InviteRequest;
import ru.github.meperry.tms.backend.security.model.User;
import ru.github.meperry.tms.backend.security.service.UserService;
import ru.github.meperry.tms.backend.service.InvitationService;
import ru.github.meperry.tms.backend.service.TournamentService;

/**
 * @author Islam Khabibullin
 */
@RestController
@RequestMapping("/invitation")
@RequiredArgsConstructor
public class InvitationController {

  private final InvitationService invitationService;
  private final TournamentService tournamentService;
  private final UserService userService;

  @PostMapping
  public ResponseEntity<Invitation> invite(@RequestBody InviteRequest request) {
    Tournament tournament = tournamentService.findById(request.getTournamentId())
        .orElseThrow(RuntimeException::new);
    User user = userService.findByUsername(request.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException(
            "User with username: " + request.getUsername() + " not found"));

    Invitation invitation = new Invitation();
    invitation.setUser(user);
    invitation.setTournament(tournament);
    return ResponseEntity.ok(invitationService.save(invitation));
  }

  @PutMapping("/{invitationId}/accept")
  public ResponseEntity<Tournament> accept(@PathVariable Long invitationId) {
    Invitation invitation = invitationService.findById(invitationId)
        .orElseThrow(RuntimeException::new);

    // TODO 24.07.2023 сделать так, чтобы можно было принимать только свои приглашения

    Tournament tournament = invitation.getTournament();
    List<User> participants = tournament.getParticipants();
    if (participants == null) {
      participants = new ArrayList<>();
      tournament.setParticipants(participants);
    }
    participants.add(invitation.getUser());
    tournamentService.save(tournament);

    invitationService.delete(invitation);

    return ResponseEntity.ok(tournament);
  }
}
