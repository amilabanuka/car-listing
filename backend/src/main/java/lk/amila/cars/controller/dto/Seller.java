package lk.amila.cars.controller.dto;

import java.util.List;

public class Seller {
    public Object dealer; // sometimes an empty object
    public String id;
    public String type;
    public String companyName;
    public String contactName;
    public lk.amila.cars.controller.dto.Links links;
    public List<Phone> phones;

    public Seller() {}
}
