package lk.amila.cars.controller.dto;

import java.util.List;

public class Listing {
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

    // Additional fields found in JSON
    public Boolean hasVideo;
    public Boolean isOfferNew;
    public Ratings ratings;

    // Default constructor
    public Listing() {}
}
