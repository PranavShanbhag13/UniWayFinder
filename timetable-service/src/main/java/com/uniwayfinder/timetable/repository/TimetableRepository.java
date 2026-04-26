package com.uniwayfinder.timetable.repository;

import com.uniwayfinder.timetable.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByUserIdOrderByDayOfWeekAscStartTimeAsc(String userId);

    List<Timetable> findByDayOfWeekAndStartTimeBetween(Integer dayOfWeek, java.time.LocalTime start, java.time.LocalTime end);
}