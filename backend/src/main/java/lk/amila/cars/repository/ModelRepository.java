package lk.amila.cars.repository;

import lk.amila.cars.model.ModelEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends MongoRepository<ModelEntity, Long> {
}

