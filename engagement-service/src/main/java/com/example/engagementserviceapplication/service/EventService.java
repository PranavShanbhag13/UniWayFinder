package com.example.engagementserviceapplication.service;

import com.example.engagementserviceapplication.entity.Event;
import com.example.engagementserviceapplication.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    /**
     * This method searches the database for events
     * that happen AFTER a specific time (like when a class ends).
     */
    public List<Event> getEventsAfterClass(LocalDateTime classEndTime) {
        return eventRepository.findByStartTimeAfter(classEndTime);
    }
}