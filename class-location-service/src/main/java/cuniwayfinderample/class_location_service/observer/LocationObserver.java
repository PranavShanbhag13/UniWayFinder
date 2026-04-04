package cuniwayfinderample.class_location_service.observer;

import cuniwayfinderample.class_location_service.entity.ClassLocation;

public interface LocationObserver {
    void update(ClassLocation location);
}