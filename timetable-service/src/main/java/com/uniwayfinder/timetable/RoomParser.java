package com.uniwayfinder.timetable.util;

public class RoomParser {

    public static String getBuilding(String code) {
        return code.substring(0, 3);
    }

    public static String getFloor(String code) {
        return String.valueOf(code.charAt(3));
    }

    public static String getRoom(String code) {
        return code.substring(4);
    }
}