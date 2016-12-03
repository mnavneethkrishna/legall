package com.whattabiz.legall.models;

import android.widget.Switch;

/**
 * Created by User on 8/3/2016.
 */

public class CartItemModel {

    private String title, cost, id,cat;


    public CartItemModel() {
    }

    public CartItemModel(String id, String title, String cost, String cat) {
        this.title = title;

        this.id = id;

        this.cost = cost;

        this.cat = cat;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id  = id;
    }

    public String getCat() {

      return cat;


    }

    public void setCat(String cat) {
        this.cat = cat;
    }
}

