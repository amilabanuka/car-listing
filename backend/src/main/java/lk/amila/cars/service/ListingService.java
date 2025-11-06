package lk.amila.cars.service;

import lk.amila.cars.controller.dto.Listing;
import lk.amila.cars.model.ListingDocument;
import lk.amila.cars.repository.ListingRepository;
import org.springframework.stereotype.Service;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ListingService {

    private final ListingRepository repository;

    public ListingService(ListingRepository repository) {
        this.repository = repository;
    }

    // Save a collection of Listing DTOs by mapping to ListingDocument
    // New behavior: do NOT reject the call if some ids already exist. Instead, ignore listings whose id already exists
    // and insert only the new ones. Listings with null id will be inserted and Mongo will generate an id.
    public List<ListingDocument> saveAll(List<Listing> listings) {
        if (listings == null || listings.isEmpty()) return new ArrayList<>();

        // Collect non-null incoming ids to check which already exist
        List<String> incomingIds = listings.stream()
                .filter(Objects::nonNull)
                .map(l -> l.id)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Set<String> existingIds = new HashSet<>();
        if (!incomingIds.isEmpty()) {
            Iterable<ListingDocument> existing = repository.findAllById(incomingIds);
            existing.forEach(d -> {
                if (d != null && d.id != null) existingIds.add(d.id);
            });
        }

        // Build documents to save: include listings that have null id (allow Mongo to generate) or ids not present in DB
        List<ListingDocument> docsToSave = new ArrayList<>();
        for (Listing l : listings) {
            if (l == null) continue;
            if (l.id != null && existingIds.contains(l.id)) {
                // skip existing id
                continue;
            }
            ListingDocument d = mapToDocument(l);
            docsToSave.add(d);
        }

        if (docsToSave.isEmpty()) {
            // Nothing new to save; return empty list
            return new ArrayList<>();
        }

        // Attempt to save all new docs at once; if there's a race and a duplicate key occurs, fallback to per-doc save
        try {
            return repository.saveAll(docsToSave);
        } catch (DuplicateKeyException ex) {
            List<ListingDocument> saved = new ArrayList<>();
            for (ListingDocument d : docsToSave) {
                try {
                    ListingDocument s = repository.save(d);
                    if (s != null) saved.add(s);
                } catch (DuplicateKeyException ex2) {
                    // another concurrent process inserted this id -- skip it
                }
            }
            return saved;
        }
    }

    private ListingDocument mapToDocument(Listing l) {
        ListingDocument d = new ListingDocument();
        if (l == null) return d;
        d.id = l.id;
        d.evBanner = l.evBanner;
        d.crossReferenceId = l.crossReferenceId;
        d.images = l.images;
        d.seals = l.seals;
        d.ocsImagesA = l.ocsImagesA;
        d.price = l.price;
        d.availableNow = l.availableNow;
        d.superDeal = l.superDeal;
        d.url = l.url;
        d.vehicle = l.vehicle;
        d.location = l.location;
        d.seller = l.seller;
        d.appliedAdTier = l.appliedAdTier;
        d.adTier = l.adTier;
        d.isOcs = l.isOcs;
        d.specialConditions = l.specialConditions;
        d.statistics = l.statistics;
        d.searchResultType = l.searchResultType;
        d.searchResultSection = l.searchResultSection;
        d.tracking = l.tracking;
        d.coverImageAttractiveness = l.coverImageAttractiveness;
        d.vehicleDetails = l.vehicleDetails;
        d.trackingParameters = l.trackingParameters;
        d.hasVideo = l.hasVideo;
        d.isOfferNew = l.isOfferNew;
        d.ratings = l.ratings;
        return d;
    }
}
