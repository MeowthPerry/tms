package ru.github.meperry.tms.telegram_bot.command_handler;

import org.springframework.beans.factory.annotation.Autowired;
import ru.github.meperry.tms.telegram_bot.command_handler.state.State;
import ru.github.meperry.tms.telegram_bot.command_handler.state.StateRepository;

/**
 * @author Islam Khabibullin
 */
public abstract class StatefulCommandHandler extends CommandHandler {

  protected StateRepository stateRepository;

  @Autowired
  public void setStateRepository(StateRepository stateRepository) {
    this.stateRepository = stateRepository;
  }

  protected State getState(Long chatId) {
    return stateRepository.findById(chatId).get();
  }

  protected void clearState(Long chatId) {
    stateRepository.deleteById(chatId);
  }
}
