package lk.amila.cars.repository;

import lk.amila.cars.model.BrandDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface BrandRepository extends MongoRepository<BrandDocument, String> {
    Optional<BrandDocument> findByBrandId(Integer brandId);
    boolean existsByBrandId(Integer brandId);
}

