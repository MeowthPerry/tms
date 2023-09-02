package ru.github.meperry.tms.telegram_bot.command_handler;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.github.meperry.tms.api.dto.CreatingTournamentType;
import ru.github.meperry.tms.api.dto.TournamentCreationRequest;
import ru.github.meperry.tms.telegram_bot.command_handler.state.State;
import ru.github.meperry.tms.telegram_bot.domain.MessageExchange;
import ru.github.meperry.tms.telegram_bot.domain.MessageForBot;

/**
 * @author Islam Khabibullin
 */
@Component
public class CreateTournamentCommandHandler extends StatefulCommandHandler {

  @Override
  String getCommand() {
    return "/create_tournament";
  }

  @Override
  public void handle(MessageForBot messageForBot) {
    stateRepository.save(new State(messageForBot.getChatId(), new TournamentCreationRequest()));
    MessageExchange messageExchange = new MessageExchange(messageForBot.getChatId(), "Название?", this::handleName);
    botService.sendMessage(messageExchange);
  }

  private MessageExchange handleName(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    ((TournamentCreationRequest) state.getData()).setName(textWithoutBotName);
    stateRepository.save(state);

    return new MessageExchange(message.getChatId(), "Описание?", this::handleDescription);
  }

  private MessageExchange handleDescription(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    ((TournamentCreationRequest) state.getData()).setDescription(textWithoutBotName);
    stateRepository.save(state);

    return new MessageExchange(message.getChatId(), "Дата начала?", this::handleStartDate);
  }

  private MessageExchange handleStartDate(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    ((TournamentCreationRequest) state.getData()).setStartDate(LocalDate.parse(textWithoutBotName));
    stateRepository.save(state);

    return new MessageExchange(message.getChatId(), "Тип?", this::handleType);
  }

  private MessageExchange handleType(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    CreatingTournamentType type = CreatingTournamentType.valueOf(textWithoutBotName);
    TournamentCreationRequest creationRequest = (TournamentCreationRequest) state.getData();
    creationRequest.setType(type);
    stateRepository.save(state);

    switch (type) {
      case SINGLE_ELIMINATION:
        return new MessageExchange(message.getChatId(), tournamentCreationRequestConfirmationText(creationRequest), this::handleConfirmation);
      case PLAY_OFFS:
        return new MessageExchange(message.getChatId(), "Количество групп?", this::handleGroupCount);
      default:
        throw new RuntimeException();
    }
  }

  private MessageExchange handleGroupCount(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    ((TournamentCreationRequest) state.getData()).setGroupCount(Integer.valueOf(textWithoutBotName));
    stateRepository.save(state);

    return new MessageExchange(message.getChatId(), "Количество выходящих из группы?", this::handlePassingCount);
  }

  private MessageExchange handlePassingCount(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());
    TournamentCreationRequest creationRequest = (TournamentCreationRequest) state.getData();
    creationRequest.setPassingCount(Integer.valueOf(textWithoutBotName));
    stateRepository.save(state);

    return new MessageExchange(message.getChatId(), tournamentCreationRequestConfirmationText(creationRequest), this::handleConfirmation);
  }

  private MessageExchange handleConfirmation(Message message, String textWithoutBotName) {
    State state = getState(message.getChatId());

    switch (textWithoutBotName.toUpperCase()) {
      case "ДА":
        return new MessageExchange(message.getChatId(), "Пшпшпш... Создаем турнир...");
      case "НЕТ":
        return new MessageExchange(message.getChatId(), "Отменено");
      default:
        return new MessageExchange(message.getChatId(), "Возможные ответы: Да или Нет", this::handleConfirmation);
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
            EXTENDED_TOURNAMENT_CREATION_REQUEST_TEXT_TEMPLATE + "\n\n" + CREATION_CONFIRMATION_TEXT,
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
