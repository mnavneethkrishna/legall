package com.whattabiz.legall.models;

/**
 * Created by User on 8/3/2016.
 */

public class AdvocateModel {

        private String id, name, exp, city;
        private String  rating;


        public AdvocateModel() {
        }

        public AdvocateModel(String id, String name, String exp, String city, String rating) {
            this.id = id;
            this.name = name;
            this.exp = exp;
            this.city = city;
            this.rating = rating;

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

        public String getExp() {
            return exp;
        }

        public void setExp(String exp) {
            this.exp = exp;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating  = rating;
        }


        }

