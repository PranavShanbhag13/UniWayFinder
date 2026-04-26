package com.uniwayfinder.timetable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TimetableServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimetableServiceApplication.class, args);
    }

}
