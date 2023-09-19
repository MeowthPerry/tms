package ru.github.meperry.tms.backend.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.github.meperry.tms.backend.model.Match;

/**
 * @author Islam Khabibullin
 */
@Getter
public class MatchResultReportedEvent extends ApplicationEvent {

  public MatchResultReportedEvent(Match match) {
    super(match);
  }

  public Match match() {
    return (Match) getSource();
  }
}
