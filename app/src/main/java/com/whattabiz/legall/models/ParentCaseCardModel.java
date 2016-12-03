package com.whattabiz.legall.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rumaan on 11-11-2016.
 */

public class ParentCaseCardModel {
    private List<ChildCaseDetailsModel> casesDetailsList = new ArrayList<>();
    private String day;

    public ParentCaseCardModel(List<ChildCaseDetailsModel> casesDetailsList, String day) {
        this.casesDetailsList = casesDetailsList;
        this.day = day;
    }

    public ParentCaseCardModel() {
    }

    public List<ChildCaseDetailsModel> getCasesDetailsList() {
        return casesDetailsList;
    }

    public void setCasesDetailsList(List<ChildCaseDetailsModel> casesDetailsList) {
        this.casesDetailsList = casesDetailsList;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
