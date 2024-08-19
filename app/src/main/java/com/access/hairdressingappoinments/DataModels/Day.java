package com.access.hairdressingappoinments.DataModels;

public class Day {

    private String isDayOff;
    private String startHour;
    String dayOfWeek;
    private String endHour;

    // Default constructor required for calls to DataSnapshot.getValue(Day.class)
    public Day() {
    }

    // Constructor with parameters


    public Day(String isDayOff, String startHour, String dayOfWeek, String endHour) {
        this.isDayOff = isDayOff;
        this.startHour = startHour;
        this.dayOfWeek = dayOfWeek;
        this.endHour = endHour;
    }

    public String isDayOff() {
        return isDayOff;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    // Getter and Setter methods
    public String getDayOff() {
        return isDayOff;
    }

    public void setDayOff(String isDayOff) {
        this.isDayOff = isDayOff;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }
}