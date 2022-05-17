package com.example.trhealth.Doctor.Screens;

public class DoctorModel {

    public Long id;
    public String uid;
    public String name;
    public String email;
    public String phone;
    public String cnic;
    public String university;
    public String specialization;
    public String hospital;
    public TimingsModel timingsModel;

    public DoctorModel(Long id, String uid, String name, String email, String phone, String cnic, String university, String specialization, String hospital, TimingsModel timingsModel) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cnic = cnic;
        this.university = university;
        this.specialization = specialization;
        this.hospital = hospital;
        this.timingsModel = timingsModel;
    }

    public DoctorModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public TimingsModel getTimingsModel() {
        return timingsModel;
    }

    public void setTimingsModel(TimingsModel timingsModel) {
        this.timingsModel = timingsModel;
    }
}
