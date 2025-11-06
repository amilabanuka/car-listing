package lk.amila.cars.controller.dto;

/**
 * DTO for a seal entry from the listings JSON.
 * Example JSON element:
 * { "id": 146, "image": "https://.../146.gif", "culture": "nl-NL" }
 */
public class Seal {
    public Integer id;
    public String image;
    public String culture;

    public Seal() {}
}

