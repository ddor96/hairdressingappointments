package com.access.hairdressingappoinments.DataModels;

import java.util.HashMap;
import java.util.Map;

public class AppointmentsData {
    String Date, Time,Uid;
    long timeStamp;

    public AppointmentsData() {
    }

    public AppointmentsData(long TimeStamp,String date, String time, String uid) {
        Date = date;
        Time = time;
        Uid = uid;
        timeStamp =TimeStamp;

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}