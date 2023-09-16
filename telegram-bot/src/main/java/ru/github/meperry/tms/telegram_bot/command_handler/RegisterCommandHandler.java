package ru.github.meperry.tms.telegram_bot.command_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.github.meperry.tms.api.api.TournamentApi;
import ru.github.meperry.tms.telegram_bot.domain.MessageForBot;
import ru.github.meperry.tms.telegram_bot.security.service.AuthenticationService;

/**
 * @author Islam Khabibullin
 */
@Component
@RequiredArgsConstructor
public class RegisterCommandHandler extends CommandHandler {

  private final TournamentApi tournamentApi;
  private final AuthenticationService authenticationService;

  @Override
  String getCommand() {
    return "/register";
  }

  @Override
  void handle(MessageForBot message) {
    User from = message.getMessage().getFrom();

    Long tournamentId = Long.valueOf(
        message.getTextWithoutBotName().substring((getCommand() + " ").length()));

    tournamentApi.registerToTournament(authenticationService.getToken(from), tokenSupplier(from), tournamentId)
        .subscribe(
            response -> botService.sendMessage(message.getChatId(), "Вы успешно зарегистрировались на турнир")
        );
  }
}
