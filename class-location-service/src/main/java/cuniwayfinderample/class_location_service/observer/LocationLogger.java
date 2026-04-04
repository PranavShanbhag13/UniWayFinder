package cuniwayfinderample.class_location_service.observer;

import org.springframework.stereotype.Component;
import cuniwayfinderample.class_location_service.entity.ClassLocation;

@Component
public class LocationLogger implements LocationObserver {

    @Override
    public void update(ClassLocation location) {
        System.out.println("🔔 Location updated: Room "
                + location.getRoom() + " in " + location.getBuilding());
    }
}