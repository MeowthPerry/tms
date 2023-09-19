package ru.github.meperry.tms.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.github.meperry.tms.backend.security.model.User;
import ru.github.meperry.tms.backend.security.service.UserService;
import ru.github.meperry.tms.backend.service.MatchService;

/**
 * @author Islam Khabibullin
 */
@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

  private final MatchService matchService;
  private final UserService userService;

  @PutMapping("/report")
  public void report(
      @RequestParam(name = "participant_one") String participantOneUsername,
      @RequestParam(name = "participant_one_score") Integer participantOneScore,
      @RequestParam(name = "participant_two") String participantTwoUsername,
      @RequestParam(name = "participant_two_score") Integer participantTwoScore) {

    User participantOne = userService.findByUsername(participantOneUsername).orElseThrow(
        () -> new UsernameNotFoundException(
            "User with username: " + participantOneUsername + " not found"));

    User participantTwo = userService.findByUsername(participantTwoUsername).orElseThrow(
        () -> new UsernameNotFoundException(
            "User with username: " + participantTwoUsername + " not found"));

    matchService.report(participantOne, participantOneScore, participantTwo, participantTwoScore);
  }
}
