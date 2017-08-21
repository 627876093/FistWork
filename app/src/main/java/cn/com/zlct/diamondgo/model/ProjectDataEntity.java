package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2 0002.
 */
public class ProjectDataEntity {
//{"list":[
    private List<DataEntity> list;

    public List<DataEntity> getList() {
        return list;
    }

    public void setList(List<DataEntity> list) {
        this.list = list;
    }

    public class DataEntity{
        /**
         * "id":1,
         * "head_portrait":null,
         * "comment_time":"2017-05-15 00:00:00",
         * "comment_cell_phone":"13526562536",
         * "comment_content":"宝贝很好 我很喜欢！！",
         * "picture_path":"/shoppingManagementSystem/querySystemJournal.do",
         * "headURL":null,
         * "nickname":"",
         * "userVIP":"",1-5
         * "pId":0,
         * "commodity_id":57
         */
        private String id;
        private String head_portrait;
        private String comment_time;
        private String comment_cell_phone;
        private String comment_content;
        private String picture_path;
        private String headURL;
        private String nickname;
        private String userVIP;
        private String pId;
        private String commodity_id;
        private int type;

        public DataEntity(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHead_portrait() {
            return head_portrait;
        }

        public void setHead_portrait(String head_portrait) {
            this.head_portrait = head_portrait;
        }

        public String getComment_time() {
            return comment_time;
        }

        public void setComment_time(String comment_time) {
            this.comment_time = comment_time;
        }

        public String getComment_cell_phone() {
            return comment_cell_phone;
        }

        public void setComment_cell_phone(String comment_cell_phone) {
            this.comment_cell_phone = comment_cell_phone;
        }

        public String getComment_content() {
            return comment_content;
        }

        public void setComment_content(String comment_content) {
            this.comment_content = comment_content;
        }

        public String getPicture_path() {
            return picture_path;
        }

        public void setPicture_path(String picture_path) {
            this.picture_path = picture_path;
        }

        public String getHeadURL() {
            return headURL;
        }

        public void setHeadURL(String headURL) {
            this.headURL = headURL;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUserVIP() {
            return userVIP;
        }

        public void setUserVIP(String userVIP) {
            this.userVIP = userVIP;
        }

        public String getCommodity_id() {
            return commodity_id;
        }

        public void setCommodity_id(String commodity_id) {
            this.commodity_id = commodity_id;
        }

        public String getpId() {
            return pId;
        }

        public void setpId(String pId) {
            this.pId = pId;
        }
    }
}
