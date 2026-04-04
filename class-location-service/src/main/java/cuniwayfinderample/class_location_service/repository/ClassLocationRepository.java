package cuniwayfinderample.class_location_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import cuniwayfinderample.class_location_service.entity.ClassLocation;

public interface ClassLocationRepository extends JpaRepository<ClassLocation, Long> {
    List<ClassLocation> findByRoom(String room);
}