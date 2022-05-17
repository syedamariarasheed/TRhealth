package com.example.trhealth.Doctor.Screens;

public class VPModel {

    String uid;
    String phone;
    String name;
    Long id;
    String email;
    String cnic;

    public VPModel() {
    }

    public VPModel(String uid, String phone, String name, Long id, String email, String cnic) {
        this.uid = uid;
        this.phone = phone;
        this.name = name;
        this.id = id;
        this.email = email;
        this.cnic = cnic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }
}
