
package cuniwayfinderample.class_location_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import cuniwayfinderample.class_location_service.entity.ClassLocation;
import cuniwayfinderample.class_location_service.service.ClassLocationService;
import cuniwayfinderample.class_location_service.factory.ClassLocationFactory;

@RestController
@RequestMapping("/class")
public class ClassLocationController {

    @Autowired
    private ClassLocationService service;

    @Autowired
    private ClassLocationFactory factory;

    // ✅ CREATE (Factory Pattern used here)
    @PostMapping("/add")
    public ClassLocation addClass(@RequestBody ClassLocation dto) {

        ClassLocation location = factory.create(
                dto.getRoom(),
                dto.getBuilding()
        );

        return service.save(location);
    }

    // ✅ GET ALL
    @GetMapping("/all")
    public List<ClassLocation> getAll() {
        return service.getAll();
    }

    // ✅ SEARCH BY ROOM
    @GetMapping("/search/{room}")
    public List<ClassLocation> search(@PathVariable String room) {
        return service.searchByRoom(room);
    }

    // ✅ UPDATE
    @PutMapping("/update/{id}")
    public ClassLocation update(@PathVariable Long id,
                                @RequestBody ClassLocation location) {
        return service.update(id, location);
    }

    // ✅ DELETE
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Deleted successfully";
    }
}