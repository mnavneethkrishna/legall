package com.whattabiz.legall.models;

/**
 * Created by thecoolguy on 13/11/16.
 */

public class CaseModel {
    private String day;
    private String time;
    private String caseTitle;
    private String caseDetails;
    private String clientNAme;
    private String clientPhone;
    private String nextHearing;
    private String prevHearing;
    private String caseStatus;

    public CaseModel(String day, String time, String caseTitle, String caseDetails, String clientName, String clientPhone) {
        this.day = day;
        this.time = time;
        this.caseTitle = caseTitle;
        this.caseDetails = caseDetails;
        this.clientNAme = clientName;
        this.clientPhone = clientPhone;
    }

    public CaseModel() {
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getNextHearing() {
        return nextHearing;
    }

    public void setNextHearing(String nextHearing) {
        this.nextHearing = nextHearing;
    }

    public String getPrevHearing() {
        return prevHearing;
    }

    public void setPrevHearing(String prevHearing) {
        this.prevHearing = prevHearing;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(String caseDetails) {
        this.caseDetails = caseDetails;
    }

    public String getClientNAme() {
        return clientNAme;
    }

    public void setClientNAme(String clientNAme) {
        this.clientNAme = clientNAme;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }


}
