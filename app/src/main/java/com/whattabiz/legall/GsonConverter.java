package com.whattabiz.legall;

import com.google.gson.Gson;
import com.whattabiz.legall.models.CaseModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Rumaan on 12-11-2016.
 */

public class GsonConverter {
    /**
     * @param caseModelArrayList Convert List of POJO's to JSON string
     */
    public static String fromPOJOtoJson(ArrayList<CaseModel> caseModelArrayList) {
        return new Gson().toJson(caseModelArrayList);
    }

    /**
     * @param type Type of Class to which the Json deserializer should convert to
     * @param json JSON string that is Stored from SharedPrefs
     *             <p>
     *             Convert JSON to a List of POJO's
     */
    public static ArrayList<CaseModel> fromJsonToPOJO(Type type, String json) {
        return new Gson().fromJson(json, type);
    }
}
