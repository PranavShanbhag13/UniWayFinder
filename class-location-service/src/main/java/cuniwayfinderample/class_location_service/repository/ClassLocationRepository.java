package cuniwayfinderample.class_location_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cuniwayfinderample.class_location_service.entity.ClassLocation;

public interface ClassLocationRepository extends JpaRepository<ClassLocation, Long> {

    ClassLocation findByClassId(String classId);
}