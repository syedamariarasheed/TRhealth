package com.example.trhealth.Patient;

public class BookAppointmentModel {
    Long id;
    String name;
    String speciality;
    String address;
    String ratings;

    public BookAppointmentModel(Long id, String name, String speciality, String address, String ratings) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.address = address;
        this.ratings = ratings;
    }

    public BookAppointmentModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }
}
