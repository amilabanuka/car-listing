package lk.amila.cars.controller.dto;

public class SuperDeal {
    public String oldPriceFormatted;
    public Boolean isEligible;
    // sometimes there may be a numeric old price field; keep a flexible string field to capture it
    public String oldSuperDealPrice;

    public SuperDeal() {}
}
