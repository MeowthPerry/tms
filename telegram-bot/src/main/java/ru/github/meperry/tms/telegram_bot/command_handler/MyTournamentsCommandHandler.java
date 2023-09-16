package ru.github.meperry.tms.telegram_bot.command_handler;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.github.meperry.tms.api.api.TournamentApi;
import ru.github.meperry.tms.api.dto.TournamentDto;
import ru.github.meperry.tms.telegram_bot.domain.MessageForBot;
import ru.github.meperry.tms.telegram_bot.security.service.AuthenticationService;

/**
 * @author Islam Khabibullin
 */
@Component
@RequiredArgsConstructor
public class MyTournamentsCommandHandler extends CommandHandler {

  private final TournamentApi tournamentApi;
  private final AuthenticationService authenticationService;

  @Override
  String getCommand() {
    return "/my_tournaments";
  }

  @Override
  void handle(MessageForBot message) {
    User from = message.getMessage().getFrom();

    tournamentApi.myTournaments(authenticationService.getToken(from), tokenSupplier(from))
        .subscribe(
            tournaments -> botService.sendMessage(
                message.getChatId(),
                myTournamentsMessage(tournaments.getResult()))
        );
  }

  private String myTournamentsMessage(List<TournamentDto> tournaments) {
    StringBuilder messageBuilder = new StringBuilder("Ваши турниры:\n\n");

    for (int i = 0; i < tournaments.size(); i++) {
      TournamentDto tournament = tournaments.get(i);

      messageBuilder
          .append(i + 1).append(")\n")
          .append("Идентификатор: ").append(tournament.getTournamentId()).append("\n")
          .append("Название: ").append(tournament.getName()).append("\n")
          .append("Описание: ").append(tournament.getDescription()).append("\n")
          .append("Дата начала: ").append(tournament.getStartDate()).append("\n")
          .append("Автор: ").append(tournament.getCreator().getUsername()).append("\n")
          .append("Участники: \n").append(
              tournament.getParticipants()
                  .stream()
                  .map(participant -> "- " + participant.getUsername())
                  .collect(Collectors.joining("\n"))
          ).append("\n")
          .append("Статус: ").append(tournament.getTournamentStatus()).append("\n")
          .append("\n");
    }

    return messageBuilder.toString();
  }
}
