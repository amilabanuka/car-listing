package lk.amila.cars.controller.dto;

public class Price {
    public String priceFormatted;
    public String vatLabel;
    public String priceSuperscriptString;
    public String oldSuperDealPrice; // New: capture the old super-deal price when it's provided under the `price` object

    public Price() {}
}
