package com.uniwayfinder.timetable.service.impl;

import com.uniwayfinder.timetable.entity.Timetable;
import com.uniwayfinder.timetable.repository.TimetableRepository;
import com.uniwayfinder.timetable.service.TimetableService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimetableServiceImpl implements TimetableService {

    private final TimetableRepository timetableRepository;

    public TimetableServiceImpl(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    @Override
    public Timetable addTimetable(Timetable timetable, String userId) {
        // Enforce Zero-Trust data isolation: Bind the entity to the extracted JWT user ID
        timetable.setUserId(userId);
        return timetableRepository.save(timetable);
    }

    @Override
    public List<Timetable> getMyTimetables(String userId) {
        // Fetch sorted timetable data for the authenticated user
        return timetableRepository.findByUserIdOrderByDayOfWeekAscStartTimeAsc(userId);
    }
}