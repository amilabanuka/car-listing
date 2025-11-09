package lk.amila.cars.service;

import lk.amila.cars.model.ModelEntity;
import lk.amila.cars.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ModelService {
    private final ModelRepository repository;

    public ModelService(ModelRepository repository) {
        this.repository = repository;
    }

    /**
     * Save models that do not already exist in the database (by id).
     * Returns the number of entities inserted.
     */
    public int saveNewModels(List<ModelEntity> entities) {
        if (entities == null || entities.isEmpty()) return 0;

        List<Long> ids = entities.stream()
                .map(ModelEntity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (ids.isEmpty()) return 0;

        List<ModelEntity> existing = repository.findAllById(ids);
        Set<Long> existingIds = existing.stream()
                .map(ModelEntity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<ModelEntity> toSave = entities.stream()
                .filter(e -> e.getId() != null && !existingIds.contains(e.getId()))
                .collect(Collectors.toList());

        if (!toSave.isEmpty()) {
            repository.saveAll(toSave);
        }

        return toSave.size();
    }
}

