package com.whattabiz.legall.models;

/**
 * Created by User on 10/21/2016.
 */

public class LawyerModel {

    public String id, name, city, exp;

    public LawyerModel(String id, String name, String city, String exp) {

        this.id = id;
        this.name = name;
        this.city = city;
        this.exp = exp;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }


}
