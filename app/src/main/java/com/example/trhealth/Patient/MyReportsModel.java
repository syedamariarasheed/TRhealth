package com.example.trhealth.Patient;

public class MyReportsModel {
    String name;
    String date;
    Long id;
    String url;

    public MyReportsModel(String name, String date, Long id) {
        this.name = name;
        this.date = date;
        this.id = id;
    }

    public MyReportsModel(String name, String date, Long id, String url) {
        this.name = name;
        this.date = date;
        this.id = id;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MyReportsModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
