package lk.amila.cars.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "brands")
public class BrandDocument {
    @Id
    public String id;

    @Indexed(unique = true)
    public Integer brandId;
    public String name;

    public BrandDocument() {}

    public BrandDocument(Integer brandId, String name) {
        this.brandId = brandId;
        this.name = name;
    }
}
