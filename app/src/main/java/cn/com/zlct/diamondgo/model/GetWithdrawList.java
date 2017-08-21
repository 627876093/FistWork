package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23 0023.
 */
public class GetWithdrawList {
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
         * "id":0
         * "date":"2017-05-10 16:54:58"
         * "page":0
         * "status":null
         * "phone":null
         * "txvalue":2132.0
         * "userid":0
         * "pageSize":0
         * "listCount":0
         * "pageCount":0
         */

        private String id;
        private String date;
        private String page;
        private String status;
        private String phone;
        private String txvalue;
        private String userid;
        private String pageSize;
        private String listCount;
        private String pageCount;
        private int type;

        public DataEntity(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getTxvalue() {
            return txvalue;
        }

        public void setTxvalue(String txvalue) {
            this.txvalue = txvalue;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getListCount() {
            return listCount;
        }

        public void setListCount(String listCount) {
            this.listCount = listCount;
        }

        public String getPageCount() {
            return pageCount;
        }

        public void setPageCount(String pageCount) {
            this.pageCount = pageCount;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
