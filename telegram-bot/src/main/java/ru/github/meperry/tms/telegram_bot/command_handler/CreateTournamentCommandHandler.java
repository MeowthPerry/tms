package ru.github.meperry.tms.telegram_bot.command_handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
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
    String name = message.getText();
    ((TournamentCreationRequest) state.getData()).setName(name);
    stateRepository.save(state);

    return new MessageExchange(message.getChatId(), "Текущее состояние: " + stateRepository.findById(message.getChatId()).get().getData());
  }
}
