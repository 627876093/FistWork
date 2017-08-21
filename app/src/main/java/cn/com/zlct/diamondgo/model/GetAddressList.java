package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19 0019.
 */
public class GetAddressList {

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
         * "address":"天津天津市和平区有"
         * "id":18
         * "recipientName":"曹奕程"
         * "recipientPhone":"13705038428"
         * "userid":0
         * "isdefault":1//1：默认
         */
        private String id;
        private String address;
        private String recipientName;
        private String recipientPhone;
        private String userid;
        private String isdefault;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRecipientName() {
            return recipientName;
        }

        public void setRecipientName(String recipientName) {
            this.recipientName = recipientName;
        }

        public String getRecipientPhone() {
            return recipientPhone;
        }

        public void setRecipientPhone(String recipientPhone) {
            this.recipientPhone = recipientPhone;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getIsdefault() {
            return isdefault;
        }

        public void setIsdefault(String isdefault) {
            this.isdefault = isdefault;
        }
    }
}
