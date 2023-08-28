package ru.github.meperry.tms.telegram_bot.command_handler;

import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import ru.github.meperry.tms.telegram_bot.domain.MessageExchange;

/**
 * @author Islam Khabibullin
 */
public interface AsyncCommandHandler {

  boolean supports(Message message, String textWithoutBotName);

  Mono<MessageExchange> handle(Message message, String textWithoutBotName);
}
