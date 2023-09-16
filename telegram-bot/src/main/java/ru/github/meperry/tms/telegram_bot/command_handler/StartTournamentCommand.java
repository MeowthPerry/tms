package ru.github.meperry.tms.telegram_bot.command_handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.github.meperry.tms.api.api.TournamentApi;
import ru.github.meperry.tms.api.dto.GroupDto;
import ru.github.meperry.tms.api.dto.MatchDto;
import ru.github.meperry.tms.api.dto.TournamentDto;
import ru.github.meperry.tms.telegram_bot.domain.MessageForBot;
import ru.github.meperry.tms.telegram_bot.security.service.AuthenticationService;

/**
 * @author Islam Khabibullin
 */
@Component
@RequiredArgsConstructor
public class StartTournamentCommand extends CommandHandler {

  private final TournamentApi tournamentApi;
  private final AuthenticationService authenticationService;

  @Override
  String getCommand() {
    return "/start_tournament";
  }

  @Override
  void handle(MessageForBot message) {
    User from = message.getMessage().getFrom();

    Long tournamentId = Long.valueOf(
        message.getTextWithoutBotName().substring((getCommand() + " ").length()));

    botService.sendMessage(message.getChatId(), "Матчи генерируются...");

    tournamentApi.startTournament(authenticationService.getToken(from), tokenSupplier(from), tournamentId)
        .subscribe(
            response -> botService.sendMessage(message.getChatId(), getGeneratedMatches(response))
        );
  }

  private String getGeneratedMatches(TournamentDto tournamentDto) {
    StringBuilder stringBuilder = new StringBuilder("Матчи:\n\n");

    List<GroupDto> groups = tournamentDto.getStages().get(0).getGroups();
    for (int i = 0; i < groups.size(); i++) {
      stringBuilder.append("Группа ").append(i + 1).append(":\n");

      Map<Integer, List<MatchDto>> roundMatches = groups.get(i)
          .getMatches()
          .stream()
          .collect(Collectors.groupingBy(MatchDto::getRound));

      roundMatches.forEach((key, value) -> {
        stringBuilder.append("Раунд ").append(key + 1).append(":\n");

        value.forEach(match -> stringBuilder.append(match.getOrderNumber() + 1).append(") ")
            .append(match.getParticipantOne().getUsername())
            .append(" vs ")
            .append(match.getParticipantTwo().getUsername())
            .append("\n"));
      });
    }

    return stringBuilder.toString();
  }
}
