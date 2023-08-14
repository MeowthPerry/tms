package ru.github.meperry.tms.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.github.meperry.tms.backend.model.Stage;
import ru.github.meperry.tms.backend.repository.StageRepository;

/**
 * @author Islam Khabibullin
 */
@Service
@RequiredArgsConstructor
public class StageService {

  private final StageRepository stageRepository;

  public Stage save(Stage stage) {
    return stageRepository.save(stage);
  }
}
