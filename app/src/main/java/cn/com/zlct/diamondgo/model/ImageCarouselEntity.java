package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
public class ImageCarouselEntity {

    /**
     * List : []
     */
    List<DataEntity> list;

    public List<DataEntity> getList() {
        return list;
    }

    public void setList(List<DataEntity> list) {
        this.list = list;
    }

    public static class DataEntity {

        /**
         *"id":3,
         *"picture_url":"/shoppingManagementSystem/pushUpload/经销存功能图.jpg",
         *"pushSize":0,
         *"push_color":4,
         *"push_url":"http://localhost:8888/shoppingManagementSystem/goPushAdd.do?",
         *"push_title":"123412341234345"}
         */
        private String id;
        private String picture_url;
        private String pushSize;
        private String push_color;
        private String push_url;
        private String push_title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPicture_url() {
            return picture_url;
        }

        public void setPicture_url(String picture_url) {
            this.picture_url = picture_url;
        }

        public String getPushSize() {
            return pushSize;
        }

        public void setPushSize(String pushSize) {
            this.pushSize = pushSize;
        }

        public String getPush_color() {
            return push_color;
        }

        public void setPush_color(String push_color) {
            this.push_color = push_color;
        }

        public String getPush_url() {
            return push_url;
        }

        public void setPush_url(String push_url) {
            this.push_url = push_url;
        }

        public String getPush_title() {
            return push_title;
        }

        public void setPush_title(String push_title) {
            this.push_title = push_title;
        }
    }
}