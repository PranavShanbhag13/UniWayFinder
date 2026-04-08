package com.uniwayfinder.notification.service.delivery;

import com.uniwayfinder.notification.entity.Reminder;

public interface NotificationSender {
    void send(Reminder reminder);
}