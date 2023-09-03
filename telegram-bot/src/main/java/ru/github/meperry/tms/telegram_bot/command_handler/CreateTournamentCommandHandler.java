package ru.github.meperry.tms.telegram_bot.command_handler;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.github.meperry.tms.api.api.TournamentApi;
import ru.github.meperry.tms.api.dto.CreatingTournamentType;
import ru.github.meperry.tms.api.dto.TournamentCreationRequest;
import ru.github.meperry.tms.telegram_bot.command_handler.state.State;
import ru.github.meperry.tms.telegram_bot.domain.MessageExchange;
import ru.github.meperry.tms.telegram_bot.domain.MessageForBot;
import ru.github.meperry.tms.telegram_bot.security.service.AuthenticationService;

/**
 * @author Islam Khabibullin
 */
@Component
@RequiredArgsConstructor
public class CreateTournamentCommandHandler extends StatefulCommandHandler {

  private final TournamentApi tournamentApi;
  private final AuthenticationService authenticationService;

  @Override
  String getCommand() {
    return "/create_tournament";
  }

  @Override
  public void handle(MessageForBot messageForBot) {
    stateRepository.save(new State(messageForBot.getChatId(), new TournamentCreationRequest()));
    MessageExchange messageExchange = new MessageExchange(messageForBot.getChatId(), "Название?",
        this::handleName
    );
    botService.sendMessage(messageExchange);
  }

  private void handleName(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    ((TournamentCreationRequest) state.getData()).setName(textWithoutBotName);
    stateRepository.save(state);

    botService.sendMessage(
        new MessageExchange(message.getChatId(), "Описание?", this::handleDescription));
  }

  private void handleDescription(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    ((TournamentCreationRequest) state.getData()).setDescription(textWithoutBotName);
    stateRepository.save(state);

    botService.sendMessage(
        new MessageExchange(message.getChatId(), "Дата начала?", this::handleStartDate));
  }

  private void handleStartDate(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    ((TournamentCreationRequest) state.getData()).setStartDate(LocalDate.parse(textWithoutBotName));
    stateRepository.save(state);

    botService.sendMessage(new MessageExchange(message.getChatId(), "Тип?", this::handleType));
  }

  private void handleType(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    CreatingTournamentType type = CreatingTournamentType.valueOf(textWithoutBotName);
    TournamentCreationRequest creationRequest = (TournamentCreationRequest) state.getData();
    creationRequest.setType(type);
    stateRepository.save(state);

    MessageExchange reply;
    switch (type) {
      case SINGLE_ELIMINATION:
        reply = new MessageExchange(message.getChatId(),
            tournamentCreationRequestConfirmationText(creationRequest), this::handleConfirmation
        );
        break;
      case PLAY_OFFS:
        reply = new MessageExchange(message.getChatId(), "Количество групп?",
            this::handleGroupCount
        );
        break;
      default:
        throw new RuntimeException();
    }
    botService.sendMessage(reply);
  }

  private void handleGroupCount(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    ((TournamentCreationRequest) state.getData()).setGroupCount(
        Integer.valueOf(textWithoutBotName));
    stateRepository.save(state);

    botService.sendMessage(
        new MessageExchange(message.getChatId(), "Количество выходящих из группы?",
            this::handlePassingCount
        ));
  }

  private void handlePassingCount(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    TournamentCreationRequest creationRequest = (TournamentCreationRequest) state.getData();
    creationRequest.setPassingCount(Integer.valueOf(textWithoutBotName));
    stateRepository.save(state);

    botService.sendMessage(new MessageExchange(message.getChatId(),
        tournamentCreationRequestConfirmationText(creationRequest), this::handleConfirmation
    ));
  }

  private void handleConfirmation(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    TournamentCreationRequest creationRequest = (TournamentCreationRequest) state.getData();

    switch (textWithoutBotName.toUpperCase()) {
      case "ДА":
        User from = message.getFrom();
        tournamentApi
            .create(authenticationService.getToken(from), creationRequest)
            .subscribe(tournament -> botService.sendMessage(
                new MessageExchange(message.getChatId(), "Турнир успешно создан, идентификатор - " + tournament.getTournamentId()
            )));
        break;
      case "НЕТ":
        clearState(message.getChatId());
        botService.sendMessage(new MessageExchange(message.getChatId(), "Отменено"));
        break;
      default:
        botService.sendMessage(
            new MessageExchange(message.getChatId(), "Возможные ответы: Да или Нет",
                this::handleConfirmation
            ));
    }
  }

  private static final String BASE_TOURNAMENT_CREATION_REQUEST_TEXT_TEMPLATE =
      "Создаваемы турнир: \n\n"
      + "Название: %s\n"
      + "Описание: %s\n"
      + "Дата начала: %s\n"
      + "Тип: %s\n";

  private static final String EXTENDED_TOURNAMENT_CREATION_REQUEST_TEXT_TEMPLATE =
      BASE_TOURNAMENT_CREATION_REQUEST_TEXT_TEMPLATE
      + "Количество групп: %d\n"
      + "Количество выходящих из группы: %d\n";

  private static final String CREATION_CONFIRMATION_TEXT = "Подтверждаете создание?";

  private String tournamentCreationRequestConfirmationText(TournamentCreationRequest request) {
    switch (request.getType()) {
      case SINGLE_ELIMINATION:
        return String.format(
            BASE_TOURNAMENT_CREATION_REQUEST_TEXT_TEMPLATE + "\n\n" + CREATION_CONFIRMATION_TEXT,
            request.getName(),
            request.getDescription(),
            request.getStartDate().toString(),
            request.getType()
        );
      case PLAY_OFFS:
        return String.format(
            EXTENDED_TOURNAMENT_CREATION_REQUEST_TEXT_TEMPLATE + "\n\n"
            + CREATION_CONFIRMATION_TEXT,
            request.getName(),
            request.getDescription(),
            request.getStartDate().toString(),
            request.getType(),
            request.getGroupCount(),
            request.getPassingCount()
        );
      default:
        throw new RuntimeException();
    }
  }
}
