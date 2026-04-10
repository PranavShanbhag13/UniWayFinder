package com.uniwayfinder.timetable.dto;

public class TimetableRequestDTO {

    private String userId;
    private String courseId;
    private String professor;
    private String roomCode;
    private String time;

    public String getUserId() { return userId; }
    public String getCourseId() { return courseId; }
    public String getProfessor() { return professor; }
    public String getRoomCode() { return roomCode; }
    public String getTime() { return time; }
}