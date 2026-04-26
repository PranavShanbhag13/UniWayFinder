package com.example.engagementserviceapplication.repository;

import com.example.engagementserviceapplication.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    // This naming tells Spring Boot to find events after a certain time
    List<Event> findByStartTimeAfter(LocalDateTime startTime);
}