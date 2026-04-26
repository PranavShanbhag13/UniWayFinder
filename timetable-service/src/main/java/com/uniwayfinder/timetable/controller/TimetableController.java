package com.uniwayfinder.timetable.controller;

import com.uniwayfinder.timetable.entity.Timetable;
import com.uniwayfinder.timetable.service.TimetableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    /**
     * Add a new class to the user's timetable.
     * The userId is securely extracted from the JWT token, preventing users from adding classes for others.
     */
    @PostMapping
    public ResponseEntity<Timetable> addTimetable(@RequestBody Timetable timetable, Authentication authentication) {
        // Extract the user ID from the Spring Security context (populated by your JwtAuthenticationFilter)
        String userId = authentication.getName();

        Timetable savedTimetable = timetableService.addTimetable(timetable, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTimetable);
    }

    /**
     * Get all classes for the currently logged-in user.
     */
    @GetMapping
    public ResponseEntity<List<Timetable>> getMyTimetables(Authentication authentication) {
        String userId = authentication.getName();

        List<Timetable> myTimetables = timetableService.getMyTimetables(userId);
        return ResponseEntity.ok(myTimetables);
    }
}