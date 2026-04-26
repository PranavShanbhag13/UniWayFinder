package com.example.engagementserviceapplication.controller;

import com.example.engagementserviceapplication.entity.Event;
import com.example.engagementserviceapplication.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/engagement")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/after-class")
    public ResponseEntity<List<Event>> getEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime classEndTime) {
        return ResponseEntity.ok(eventService.getEventsAfterClass(classEndTime));
    }
}