package cuniwayfinderample.class_location_service.factory;

import org.springframework.stereotype.Component;
import cuniwayfinderample.class_location_service.entity.ClassLocation;

@Component
public class ClassLocationFactory {

    public ClassLocation create(String room, String building) {
        ClassLocation location = new ClassLocation();
        location.setRoom(room);
        location.setBuilding(building);
        return location;
    }
}
