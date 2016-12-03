package com.whattabiz.legall.models;

/**
 * Created by User on 8/3/2016.
 */

public class AppointmentModel {

        private String name, time, address;
        private int imgid;


        public AppointmentModel() {
        }

        public AppointmentModel(String name, String time, String address, int imgid) {
            this.name = name;
            this.time = time;
            this.address = address;
            this.imgid = imgid;

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

        public int getImgid() {
            return imgid;
        }

        public void setImgid(int imgid) {
            this.imgid  = imgid;
        }
    }

