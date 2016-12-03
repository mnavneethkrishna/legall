package com.whattabiz.legall.models;

/**
 * Created by Rumaan on 11-11-2016.
 */

public class ChildCaseDetailsModel {
    private String time;
    private String caseTitle;
    private String caseDetails;
    private String assosUserName;
    private String clientPhone;

    public ChildCaseDetailsModel(String caseTitle, String time, String caseDetails, String assosUserName) {
        this.caseTitle = caseTitle;
        this.time = time;
        this.caseDetails = caseDetails;
        this.assosUserName = assosUserName;
    }

    public ChildCaseDetailsModel() {
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
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

    public String getAssosUserName() {
        return assosUserName;
    }

    public void setAssosUserName(String assosUserName) {
        this.assosUserName = assosUserName;
    }
}
