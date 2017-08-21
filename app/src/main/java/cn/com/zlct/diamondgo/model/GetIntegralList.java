package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23 0023.
 */
public class GetIntegralList {
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
         * "date":"2017-05-09 11:18:35"
         * "userid":0
         * "casename":"sp1"
         * "casevalue":213
         */

        private String id;
        private String date;
        private String userid;
        private String casename;
        private String casevalue;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getCasename() {
            return casename;
        }

        public void setCasename(String casename) {
            this.casename = casename;
        }

        public String getCasevalue() {
            return casevalue;
        }

        public void setCasevalue(String casevalue) {
            this.casevalue = casevalue;
        }
    }

}
