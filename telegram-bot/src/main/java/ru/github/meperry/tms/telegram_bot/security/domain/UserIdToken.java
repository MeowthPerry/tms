package ru.github.meperry.tms.telegram_bot.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author Islam Khabibullin
 */
@Data
@RedisHash("token")
@AllArgsConstructor
public class UserIdToken {

  @Id
  private Long userId;
  private String token;
}
