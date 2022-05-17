package com.example.trhealth.model;

public class Reports {
    String reportTitle;
    String reportUrl;
    String reportId;

    public String getReportTitle() {
        return reportTitle;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public String getReportId() {
        return reportId;
    }


    public Reports(String reportTitle, String reportUrl, String reportId) {
        this.reportTitle = reportTitle;
        this.reportUrl = reportUrl;
        this.reportId = reportId;
    }
}
