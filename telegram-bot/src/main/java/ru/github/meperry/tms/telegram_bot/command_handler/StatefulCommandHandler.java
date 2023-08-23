package ru.github.meperry.tms.telegram_bot.command_handler;

import lombok.RequiredArgsConstructor;
import ru.github.meperry.tms.telegram_bot.command_handler.state.StateRepository;

/**
 * @author Islam Khabibullin
 */
@RequiredArgsConstructor
public abstract class StatefulCommandHandler implements CommandHandler {

  protected final StateRepository stateRepository;
}
