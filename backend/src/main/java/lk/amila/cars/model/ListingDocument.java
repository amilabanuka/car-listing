package lk.amila.cars.model;

import lk.amila.cars.controller.dto.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "listings")
public class ListingDocument {
    @Id
    public String id;
    public EvBanner evBanner;
    public String crossReferenceId;
    public List<String> images;
    public List<Seal> seals;
    public List<String> ocsImagesA;
    public Price price;
    public Boolean availableNow;
    public SuperDeal superDeal;
    public String url;
    public Vehicle vehicle;
    public Location location;
    public Seller seller;
    public String appliedAdTier;
    public String adTier;
    public Boolean isOcs;
    public List<Object> specialConditions;
    public Statistics statistics;
    public String searchResultType;
    public String searchResultSection;
    public Tracking tracking;
    public Double coverImageAttractiveness;
    public List<VehicleDetail> vehicleDetails;
    public List<TrackingParameter> trackingParameters;

    public Boolean hasVideo;
    public Boolean isOfferNew;
    public lk.amila.cars.controller.dto.Ratings ratings;

    public ListingDocument() {}
}

