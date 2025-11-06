package lk.amila.cars.repository;

import lk.amila.cars.model.ListingDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ListingRepository extends MongoRepository<ListingDocument, String> {
    // no custom methods required for now
}

