package cuniwayfinderample.class_location_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import cuniwayfinderample.class_location_service.entity.ClassLocation;
import cuniwayfinderample.class_location_service.repository.ClassLocationRepository;
import cuniwayfinderample.class_location_service.dto.ClassLocationResponseDTO;
import cuniwayfinderample.class_location_service.exception.ResourceNotFoundException;
import cuniwayfinderample.class_location_service.observer.LocationObserver;
import cuniwayfinderample.class_location_service.observer.LocationLogger;

@Service
public class ClassLocationService {

    @Autowired
    private ClassLocationRepository repository;

    // ================= OBSERVER =================
    private List<LocationObserver> observers = new ArrayList<>();

    @Autowired
    private LocationLogger logger;

    @PostConstruct
    public void init() {
        observers.add(logger);
    }

    private void notifyObservers(ClassLocation location) {
        for (LocationObserver observer : observers) {
            observer.update(location);
        }
    }

    // ================= CRUD =================

    public ClassLocation save(ClassLocation location) {
        ClassLocation saved = repository.save(location);
        notifyObservers(saved);
        return saved;
    }

    public List<ClassLocation> getAll() {
        return repository.findAll();
    }

    public List<ClassLocation> searchByRoom(String room) {
        return repository.findByRoom(room);
    }

    public ClassLocation update(Long id, ClassLocation newData) {
        ClassLocation existing = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Class not found with id " + id));

        existing.setRoom(newData.getRoom());
        existing.setBuilding(newData.getBuilding());

        ClassLocation updated = repository.save(existing);
        notifyObservers(updated);

        return updated;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // ================= DTO =================

    public ClassLocationResponseDTO convertToDTO(ClassLocation location) {
        ClassLocationResponseDTO dto = new ClassLocationResponseDTO();
        dto.setId(location.getId());
        dto.setRoom(location.getRoom());
        dto.setBuilding(location.getBuilding());
        return dto;
    }

    public List<ClassLocationResponseDTO> getAllDTO() {
        List<ClassLocation> list = repository.findAll();
        List<ClassLocationResponseDTO> dtoList = new ArrayList<>();

        for (ClassLocation location : list) {
            dtoList.add(convertToDTO(location));
        }

        return dtoList;
    }
}