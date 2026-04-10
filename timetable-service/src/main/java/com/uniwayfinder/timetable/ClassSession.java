package com.uniwayfinder.timetable.entity;

import jakarta.persistence.*;

@Entity
public class ClassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String courseId;
    private String professor;

    private String building;
    private String floor;
    private String room;

    private String time;

    public ClassSession() {}

    public ClassSession(String userId, String courseId, String professor,
                        String building, String floor, String room, String time) {
        this.userId = userId;
        this.courseId = courseId;
        this.professor = professor;
        this.building = building;
        this.floor = floor;
        this.room = room;
        this.time = time;
    }

    // basic getters
    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public String getCourseId() { return courseId; }
    public String getProfessor() { return professor; }
    public String getBuilding() { return building; }
    public String getFloor() { return floor; }
    public String getRoom() { return room; }
    public String getTime() { return time; }

    // used internally during parsing
    public void setBuilding(String building) { this.building = building; }
    public void setFloor(String floor) { this.floor = floor; }
    public void setRoom(String room) { this.room = room; }
}