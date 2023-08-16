package ru.github.meperry.tms.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.github.meperry.tms.backend.model.RoundRobinStageMetadata;
import ru.github.meperry.tms.backend.repository.RoundRobinStageMetadataRepository;

/**
 * @author Islam Khabibullin
 */
@Service
@RequiredArgsConstructor
public class RoundRobinStageMetadataService {

  private final RoundRobinStageMetadataRepository repository;

  public RoundRobinStageMetadata save(RoundRobinStageMetadata metadata) {
    return repository.save(metadata);
  }
}
