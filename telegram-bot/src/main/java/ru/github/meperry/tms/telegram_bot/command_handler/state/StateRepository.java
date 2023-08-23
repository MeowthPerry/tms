package ru.github.meperry.tms.telegram_bot.command_handler.state;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Islam Khabibullin
 */
@Repository
public interface StateRepository extends CrudRepository<State, Long> {
}
