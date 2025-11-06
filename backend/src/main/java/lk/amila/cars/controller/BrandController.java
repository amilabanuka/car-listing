package lk.amila.cars.controller;

import lk.amila.cars.model.BrandDocument;
import lk.amila.cars.repository.BrandRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandRepository repository;

    public BrandController(BrandRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<BrandDocument> listAll() {
        return repository.findAll();
    }

    public static class BrandRequest {
        public Integer brandId;
        public String name;
    }

    @PostMapping
    public ResponseEntity<?> createBrand(@RequestBody BrandRequest req) {
        if (req == null || req.brandId == null || req.name == null || req.name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("brandId and name are required");
        }

        if (repository.existsByBrandId(req.brandId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Brand with this id already exists");
        }

        BrandDocument doc = new BrandDocument(req.brandId, req.name.trim());
        try {
            BrandDocument saved = repository.save(doc);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (DuplicateKeyException ex) {
            // Rare race condition where another process inserted the same brandId concurrently
            Optional<BrandDocument> existing = repository.findByBrandId(req.brandId);
            return existing
                    .map(e -> ResponseEntity.status(HttpStatus.CONFLICT).body("Brand with this id already exists"))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create brand"));
        }
    }
}
