package com.whattabiz.legall.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by User on 8/3/2016.
 */

public class FileModel {

        private String filename, path, UploadID;
        private Bitmap image;


        public FileModel() {
        }

        public FileModel(String filename, String path, Bitmap image) {
            this.filename = filename;
            this.path = path;
            this.image = image;

        }

    public String getUploadID() {
        return UploadID;
    }

    public void setUploadID(String uploadID) {
        UploadID = uploadID;
    }

    public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Bitmap getImage() {
            return image;
        }

        public void setImage(Bitmap image) {
            this.image  = image;
        }
    }

