package com.uniwayfinder.notification.repository;

import com.uniwayfinder.notification.entity.Reminder;
import com.uniwayfinder.notification.entity.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    /**
     * Used by the SchedulingService to find reminders that need to be sent.
     * It looks for a specific status (e.g., SCHEDULED) where the reminder time has already passed or is exactly now.
     * The database index we added on 'reminder_time' makes this query highly efficient.
     */
    List<Reminder> findByStatusAndReminderTimeLessThanEqual(ReminderStatus status, LocalDateTime currentTime);

    /**
     * Used by the REST API: GET /api/notify/user/{id}
     * Fetches all reminders for a specific student.
     */
    List<Reminder> findByUserId(String userId);

    /**
     * Used by the REST API: GET /api/notify/unread (Assuming PENDING/SCHEDULED are considered 'unread' or upcoming)
     * Fetches reminders for a user based on their status.
     */
    List<Reminder> findByUserIdAndStatus(String userId, ReminderStatus status);
}