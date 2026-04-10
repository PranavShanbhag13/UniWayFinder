package com.uniwayfinder.timetable.controller;

import com.uniwayfinder.timetable.dto.*;
import com.uniwayfinder.timetable.service.TimetableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timetable")
public class TimetableController {

    private final TimetableService service;

    public TimetableController(TimetableService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public TimetableResponseDTO add(@RequestBody TimetableRequestDTO request) {
        return service.addClass(request);
    }

    @GetMapping("/{userId}")
    public List<TimetableResponseDTO> get(@PathVariable String userId) {
        return service.getTimetable(userId);
    }
}