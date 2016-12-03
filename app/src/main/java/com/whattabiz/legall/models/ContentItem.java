package com.whattabiz.legall.models;

/**
 * Created by User on 8/3/2016.
 */

public class ContentItem {

        private String title, caption,link,price,cat;
        private int imageId;

        public ContentItem() {
        }

        public ContentItem(String title, String caption, String price,String link,String cat) {
            this.title = title;
            this.caption = caption;
            this.price = price;
            this.link=link;
            this.cat=cat;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    public String getLink(){return link;}
    public void setLink(String link){this.link=link;}

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }
}

