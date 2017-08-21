package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/7/4 0004.
 */
public class GetWithdrawalList {


    //0提现中 1提现成功 2提现失败

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
         * "id":0,
         * "zfbId":"123456456465",
         * "struts":0,
         * "zfbName":"12341534",
         * "page":null,
         * "pageSize":null,
         * "phone":"gs0002",
         * "listCount":null,
         * "pageCount":null,
         * "dateTime":"2017-07-04 14:44:43",
         * "money":"100"
         */

        int id;
        int struts;
        String zfbId;
        String zfbName;
        String phone;
        String dateTime;
        String money;
        int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public DataEntity(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStruts() {
            return struts;
        }

        public void setStruts(int struts) {
            this.struts = struts;
        }

        public String getZfbId() {
            return zfbId;
        }

        public void setZfbId(String zfbId) {
            this.zfbId = zfbId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getZfbName() {
            return zfbName;
        }

        public void setZfbName(String zfbName) {
            this.zfbName = zfbName;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }
    }
    }
