package com.example.trhealth.model;

public class PatientNotification {
    String appointmentStatus;
    String doctorId;
    String doctorName;
    String prescription;

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public PatientNotification() {
    }

    public PatientNotification(String appointmentStatus, String doctorId, String doctorName, String prescription) {
        this.appointmentStatus = appointmentStatus;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.prescription = prescription;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getPrescription() {
        return prescription;
    }
}
