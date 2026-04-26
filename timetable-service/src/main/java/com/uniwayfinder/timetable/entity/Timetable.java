package com.uniwayfinder.timetable.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;

@Entity
@Table(name = "timetables")
@Data
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseId;
    private String className;
    private String teacherName;
    private String location;

    private Integer dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;

    private String userId;
}