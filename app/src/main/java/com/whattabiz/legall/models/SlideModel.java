package com.whattabiz.legall.models;

import android.graphics.Bitmap;

/**
 * Created by User on 8/3/2016.
 */

public class SlideModel {

        private String title,link,pdf,caption;
    private Bitmap image;


        public SlideModel() {
        }


    public SlideModel(String title, String caption, String link,String pdf) {
        this.title = title;
        this.caption = caption;
        this.link = link;
        this.image=null;
        this.pdf = pdf;


    }

    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }



    public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getLink(){return link;}

    public void setLink(String Link){this.link=link;}

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}

