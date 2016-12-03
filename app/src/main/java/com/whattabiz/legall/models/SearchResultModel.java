package com.whattabiz.legall.models;

/**
 * Created by User on 8/3/2016.
 */

public class SearchResultModel {

        private String title, id, link;
        private int type;


        public SearchResultModel() {
        }

        public SearchResultModel(int type,String title, String id, String link) {
            this.type = type;
            this.title = title;
            this.id = id;
            this.link = link;

        }

    public SearchResultModel(int type,String title,String link){
        this.type = type;
        this.title = title;
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link  = link;
        }
    }

