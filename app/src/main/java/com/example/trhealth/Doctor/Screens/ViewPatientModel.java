package com.example.trhealth.Doctor.Screens;

public class ViewPatientModel {
    Long id;
    String patientName;
    String patientAge;


    public ViewPatientModel(Long id, String patientName, String patientAge) {
        this.id = id;
        this.patientName = patientName;
        this.patientAge = patientAge;
    }

    public ViewPatientModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }
}
