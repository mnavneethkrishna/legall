package com.whattabiz.legall;

import com.whattabiz.legall.models.AdvocateModel;
import com.whattabiz.legall.models.CaseModel;
import com.whattabiz.legall.models.LawyerModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 10/21/2016.
 */
public class Lawyers {

    public static ArrayList<CaseModel> caseModelArrayList = new ArrayList<>();

    public static ArrayList<LawyerModel> allLawyers = new ArrayList<>();

    public static HashMap<String, ArrayList<AdvocateModel>> sortedLawyer = new HashMap<>();

   public static ArrayList<String> cityList = new ArrayList<>();

    private static Lawyers ourInstance = new Lawyers();

    public static Integer state;

    public static Lawyers getInstance() {

        return ourInstance;
    }

    private Lawyers() {
    }




}
