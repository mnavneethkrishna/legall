package com.whattabiz.legall.models;

/**
 * Created by User on 8/3/2016.
 */

public class ActsDocModel {

        private String title, caption, link;


        public ActsDocModel() {
        }

        public ActsDocModel(String title, String caption, String link) {
            this.title = title;
            this.caption = caption;
            this.link = link;

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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link  = link;
        }
    }

