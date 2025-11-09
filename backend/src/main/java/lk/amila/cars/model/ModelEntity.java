package lk.amila.cars.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "models")
public class ModelEntity {
    @Id
    private Long id;
    private String name;
    private Long makeId;
    private String vehicleTypeId;

    public ModelEntity() {
    }

    public ModelEntity(Long id, String name, Long makeId, String vehicleTypeId) {
        this.id = id;
        this.name = name;
        this.makeId = makeId;
        this.vehicleTypeId = vehicleTypeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMakeId() {
        return makeId;
    }

    public void setMakeId(Long makeId) {
        this.makeId = makeId;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    @Override
    public String toString() {
        return "ModelEntity{" +
                "id=" + id +
                ", name=\"" + name + "\"" +
                ", makeId=" + makeId +
                ", vehicleTypeId=\"" + vehicleTypeId + "\"" +
                '}';
    }
}
