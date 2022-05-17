package com.example.trhealth.Patient;

public class BookDoctorModel {
    String day;
    String slots;
    String timings;

    public BookDoctorModel(String day, String slots, String timings) {
        this.day = day;
        this.slots = slots;
        this.timings = timings;
    }

    public BookDoctorModel() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }
}
