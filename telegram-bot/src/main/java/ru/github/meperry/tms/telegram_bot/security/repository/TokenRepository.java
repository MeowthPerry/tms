package ru.github.meperry.tms.telegram_bot.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.github.meperry.tms.telegram_bot.security.domain.UserIdToken;

/**
 * @author Islam Khabibullin
 */
@Repository
public interface TokenRepository extends CrudRepository<UserIdToken, Long> {
}
