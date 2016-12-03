package com.whattabiz.legall;

import android.content.Context;

import com.whattabiz.legall.models.CaseModel;

import java.util.ArrayList;



class Singleton {
    // this will work
    private static Singleton ourInstance = new Singleton();
   public static ArrayList<CaseModel> caseModelArrayList = new ArrayList<>();

    public static Singleton getInstance(Context context) {
        //User.context = context;
        return ourInstance;
    }

    public Singleton() {
    }
}
