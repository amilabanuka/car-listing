package lk.amila.cars.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.amila.cars.controller.dto.Listing;
import lk.amila.cars.service.ListingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = "http://localhost:3000") // allow frontend dev server to call this API
public class ListingController {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    // Accepts a raw JSON string (in the request body) and extracts the `pageProps.listings` array
    @PostMapping("/parse")
    public ResponseEntity<List<Listing>> parseListings(@RequestBody String jsonString) {
        try {
            JsonNode root = mapper.readTree(jsonString);
            JsonNode listingsNode = root.path("pageProps").path("listings");

            // Fallback: if someone posts the `pageProps` object directly
            if (listingsNode.isMissingNode() || listingsNode.isNull()) {
                listingsNode = root.path("listings");
            }

            if (listingsNode.isMissingNode() || !listingsNode.isArray()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
            }

            // Use diamond operator to avoid explicit generic warning
            List<Listing> listings = mapper.convertValue(listingsNode, new TypeReference<>() {});
            // Persist all parsed listings to MongoDB
            try {
                listingService.saveAll(listings);
            } catch (Exception e) {
                // Log and continue; return listings even if DB save fails
                // In production you'd use a logger; keeping minimal to avoid adding logger dependency here
                System.err.println("Warning: failed to persist listings: " + e.getMessage());
            }
            return ResponseEntity.ok(listings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }
    }
}
