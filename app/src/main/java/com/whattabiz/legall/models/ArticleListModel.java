package com.whattabiz.legall.models;

/**
 * Created by User on 8/3/2016.
 */

public class ArticleListModel {

        private String title, caption,img,pdf,date;
        private int imgid;


        public ArticleListModel() {
        }

        public ArticleListModel(String title, String caption, int imgid) {
            this.title = title;
            this.caption = caption;
            this.imgid = imgid;

        }



    public ArticleListModel(String title, String caption, String img, String pdf, String date) {
        this.title = title;
        this.caption = caption;
        this.img = img;
        this.pdf=pdf;
        this.date=date;

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

        public void setCaption(String type) {
            this.caption = caption;
        }

        public int getImgid() {
            return imgid;
        }

        public void setLink(int imgid) {
            this.imgid  = imgid;
        }

    public String getImg(){return img;}
    public void setImg(String img){this.img=img;}

    public String getPdf(){return pdf;}
    public void setPdf(){this.pdf=pdf;}

    public String getDate(){return date;}
    public void setDate(String date){this.date=date;}
    }

