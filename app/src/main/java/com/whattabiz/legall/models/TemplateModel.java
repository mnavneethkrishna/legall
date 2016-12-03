package com.whattabiz.legall.models;

/**
 * Created by User on 8/3/2016.
 */

public class TemplateModel {

        private String id, title, cost;


        public TemplateModel() {
        }

        public TemplateModel(String id, String title, String cost) {
            this.title = title;
            this.id = id;
            this.cost = cost;

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost  = cost;
        }
    }

