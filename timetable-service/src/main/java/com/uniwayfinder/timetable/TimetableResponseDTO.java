package com.uniwayfinder.timetable.dto;

public class TimetableResponseDTO {

    private Long id;
    private String courseId;
    private String professor;
    private String building;
    private String floor;
    private String room;
    private String time;

    public TimetableResponseDTO(Long id, String courseId, String professor,
                                String building, String floor, String room, String time) {
        this.id = id;
        this.courseId = courseId;
        this.professor = professor;
        this.building = building;
        this.floor = floor;
        this.room = room;
        this.time = time;
    }

    public Long getId() { return id; }
    public String getCourseId() { return courseId; }
    public String getProfessor() { return professor; }
    public String getBuilding() { return building; }
    public String getFloor() { return floor; }
    public String getRoom() { return room; }
    public String getTime() { return time; }
}