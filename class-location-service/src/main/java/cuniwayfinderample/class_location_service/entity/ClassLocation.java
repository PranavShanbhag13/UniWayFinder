package cuniwayfinderample.class_location_service.entity;

import jakarta.persistence.*;

@Entity
public class ClassLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String classId;
    private String building;
    private String floor;

    public ClassLocation() {}

    public ClassLocation(String classId, String building, String floor) {
        this.classId = classId;
        this.building = building;
        this.floor = floor;
    }

    public Long getId() {
        return id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}