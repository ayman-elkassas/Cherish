package com.example.ayman.cherish.models;

import java.util.ArrayList;

public class TimelineParentCardData {

    int day;
    String month;
    
    ArrayList<TimelineChildCardData> timelineChildCardData;
    
    public TimelineParentCardData() {
    }
    
    public TimelineParentCardData(int day, String month, ArrayList<TimelineChildCardData> timelineChildCardData) {
        this.day = day;
        this.month = month;
        this.timelineChildCardData = timelineChildCardData;
    }
    
    public int getDay() {
        return day;
    }
    
    public void setDay(int day) {
        this.day = day;
    }
    
    public String getMonth() {
        return month;
    }
    
    public void setMonth(String month) {
        this.month = month;
    }
    
    public ArrayList<TimelineChildCardData> getTimelineChildCardData() {
        return timelineChildCardData;
    }
    
    public void setTimelineChildCardData(ArrayList<TimelineChildCardData> timelineChildCardData) {
        this.timelineChildCardData = timelineChildCardData;
    }
}
