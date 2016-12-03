package com.whattabiz.legall.models;

/**
 * Created by User on 8/3/2016.
 */

public class BulletText {

        private String title, description, link;


        public BulletText() {
        }

        public BulletText(String title,String description,String link) {
            this.title = title;
            this.description = description;
            this.link = link;

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return description;
        }

        public void setType(String description) {
            this.description = description;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link  = link;
        }
    }

