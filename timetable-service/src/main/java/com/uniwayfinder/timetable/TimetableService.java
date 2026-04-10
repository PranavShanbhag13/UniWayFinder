package com.uniwayfinder.timetable.service;

import com.uniwayfinder.timetable.dto.*;
import com.uniwayfinder.timetable.entity.ClassSession;
import com.uniwayfinder.timetable.repository.ClassSessionRepository;
import com.uniwayfinder.timetable.util.RoomParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimetableService {

    private final ClassSessionRepository repository;

    public TimetableService(ClassSessionRepository repository) {
        this.repository = repository;
    }

    public TimetableResponseDTO addClass(TimetableRequestDTO request) {

        if (request.getRoomCode() == null || request.getRoomCode().length() < 5) {
            throw new RuntimeException("Invalid room code");
        }

        String building = RoomParser.getBuilding(request.getRoomCode());
        String floor = RoomParser.getFloor(request.getRoomCode());
        String room = RoomParser.getRoom(request.getRoomCode());

        ClassSession session = new ClassSession(
                request.getUserId(),
                request.getCourseId(),
                request.getProfessor(),
                building,
                floor,
                room,
                request.getTime()
        );

        ClassSession saved = repository.save(session);

        return new TimetableResponseDTO(
                saved.getId(),
                saved.getCourseId(),
                saved.getProfessor(),
                saved.getBuilding(),
                saved.getFloor(),
                saved.getRoom(),
                saved.getTime()
        );
    }

    public List<TimetableResponseDTO> getTimetable(String userId) {

        return repository.findByUserId(userId)
                .stream()
                .map(s -> new TimetableResponseDTO(
                        s.getId(),
                        s.getCourseId(),
                        s.getProfessor(),
                        s.getBuilding(),
                        s.getFloor(),
                        s.getRoom(),
                        s.getTime()
                ))
                .collect(Collectors.toList());
    }
}