package cuniwayfinderample.class_location_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cuniwayfinderample.class_location_service.entity.ClassLocation;
import cuniwayfinderample.class_location_service.service.ClassLocationService;

@RestController
@RequestMapping("/api/location")
public class ClassLocationController {

    @Autowired
    private ClassLocationService service;

    // ✅ REQUIRED API (TEAM LEADER TASK)
    @GetMapping("/{classId}")
    public ClassLocation getLocation(@PathVariable String classId) {
        return service.getByClassId(classId);
    }

    // ✅ OPTIONAL (FOR ADDING DATA)
    @PostMapping("/add")
    public ClassLocation add(@RequestBody ClassLocation location) {
        return service.save(location);
    }
}