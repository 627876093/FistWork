package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */
public class GetRecomList {
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
         * "headURL":"wa"
         * "phone":"13526562536"
         * "nickname":"ds"
         * "sex":"男"
         */

        private String headURL;
        private String phone;
        private String nickname;
        private String sex;
        private int type;

        public DataEntity(int type) {
            this.type = type;
        }

        public String getHeadURL() {
            return headURL;
        }

        public void setHeadURL(String headURL) {
            this.headURL = headURL;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
