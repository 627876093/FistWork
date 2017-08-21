package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/27 0027.
 */
public class PushInfoEntity {
//{"list":[

    List<DataEntity> list;

    public List<DataEntity> getList() {
        return list;
    }

    public void setList(List<DataEntity> list) {
        this.list = list;
    }

    public static class DataEntity {
        /**
         * "id":3,
         * "push_url":"www.baidu.com",
         * "push_title":"推广",
         * "push_color":0,
         * "picture_url":"/shoppingManagementSystem/pushUpload/3.png",
         * "pushSize":0
         */

        private String id;
        private String push_url;
        private String push_title;
        private int push_color;
        private String picture_url;
        private String pushSize;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPush_url() {
            return push_url;
        }

        public void setPush_url(String push_url) {
            this.push_url = push_url;
        }

        public int getPush_color() {
            return push_color;
        }

        public void setPush_color(int push_color) {
            this.push_color = push_color;
        }

        public String getPush_title() {
            return push_title;
        }

        public void setPush_title(String push_title) {
            this.push_title = push_title;
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
    }
}
