package cuniwayfinderample.class_location_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cuniwayfinderample.class_location_service.entity.ClassLocation;
import cuniwayfinderample.class_location_service.repository.ClassLocationRepository;

@Service
public class ClassLocationService {

    @Autowired
    private ClassLocationRepository repository;

    // ✅ GET CLASS LOCATION BY classId (MAIN API)
    public ClassLocation getByClassId(String classId) {
        return repository.findByClassId(classId);
    }

    // ✅ SAVE CLASS LOCATION (FOR ADDING DATA)
    public ClassLocation save(ClassLocation location) {
        return repository.save(location);
    }
}