package com.example.trhealth.Patient;

import java.io.Serializable;

public class MyAppointmentModel implements Serializable {
    String patientName;
    String patientId;
    String patientFriendlyId = "";
    String appointmentId;
    String appointmentDate;
    String appointmentTime;
    String appointmentStatus;

    public MyAppointmentModel(String name, String date, String id) {
        this.patientName = name;
        this.patientId = id;
    }

    public MyAppointmentModel() {
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getPatientFriendlyId() {
        return patientFriendlyId;
    }

    public void setPatientFriendlyId(String patientFriendlyId) {
        this.patientFriendlyId = patientFriendlyId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }
}
