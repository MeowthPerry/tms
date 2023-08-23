package ru.github.meperry.tms.telegram_bot.command_handler.state;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author Islam Khabibullin
 */
@Data
@RedisHash("state")
@AllArgsConstructor
public class State {
  @Id
  private Long chatId;
  private Object data;
}
