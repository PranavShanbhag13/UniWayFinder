package com.uniwayfinder.timetable.service;

import com.uniwayfinder.timetable.entity.Timetable;
import java.util.List;

public interface TimetableService {

    /**
     * Adds a new timetable entry and securely binds it to the current user.
     */
    Timetable addTimetable(Timetable timetable, String userId);

    /**
     * Retrieves all timetable entries for a specific user, sorted by day and time.
     */
    List<Timetable> getMyTimetables(String userId);
}