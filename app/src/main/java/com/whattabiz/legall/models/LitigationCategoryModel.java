package com.whattabiz.legall.models;

/**
 * Created by User on 8/3/2016.
 */

public class LitigationCategoryModel {

    private String title, caption, url, catId;


    public LitigationCategoryModel() {
    }

    public LitigationCategoryModel(String title, String caption, String url) {
        this.title = title;
        this.caption = caption;
        this.url = url;

    }


    public LitigationCategoryModel(String title, String caption, String url, String catId) {
        this.title = title;
        this.caption = caption;
        this.url = url;
        this.catId = catId;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }
}

