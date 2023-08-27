package ru.github.meperry.tms.telegram_bot.command_handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.github.meperry.tms.api.dto.TournamentCreationRequest;
import ru.github.meperry.tms.telegram_bot.command_handler.state.State;
import ru.github.meperry.tms.telegram_bot.command_handler.state.StateRepository;
import ru.github.meperry.tms.telegram_bot.domain.MessageExchange;

/**
 * @author Islam Khabibullin
 */
@Component
public class CreateTournamentCommandHandler extends StatefulCommandHandler {

  public CreateTournamentCommandHandler(StateRepository stateRepository) {
    super(stateRepository);
  }

  @Override
  public boolean supports(Message message, String textWithoutBotName) {
    return textWithoutBotName.startsWith("/create_tournament");
  }

  @Override
  public MessageExchange handle(Message message, String textWithoutBotName) {
    stateRepository.save(new State(message.getChatId(), new TournamentCreationRequest()));
    return new MessageExchange(message.getChatId(), "Отлично! Давайте обсудим детали вашего турнира. Какое будет название?", this::handleName);
  }

  private MessageExchange handleName(Message message, String textWithoutBotName) {
    State state = stateRepository.findById(message.getChatId()).get();
    String name = message.getText();
    ((TournamentCreationRequest) state.getData()).setName(name);
    stateRepository.save(state);

    return new MessageExchange(message.getChatId(), "Текущее состояние: " + stateRepository.findById(message.getChatId()).get().getData());
  }
}
