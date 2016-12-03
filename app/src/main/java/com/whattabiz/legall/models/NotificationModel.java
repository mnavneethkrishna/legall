package com.whattabiz.legall.models;

/**
 * Created by User on 8/3/2016.
 */

public class NotificationModel {

        private String title, caption, message, type,date;
    private int status;


        public NotificationModel() {
        }

        public NotificationModel(String title, String caption, String message, String type, String date, int status) {
            this.title = title;
            this.caption = caption;
            this.message = message;
            this.type = type;
            this.date = date;
            this.status = status;

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

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type  = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        }

