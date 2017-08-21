package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23 0023.
 */
public class RebateList {
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
         * "date":"2017-05-22 14:41:42"
         * "phone":null
         * "reuserid":0
         * "reheadURL":"ds"
         * "rephone":"13526562536"
         * "kind":"2"
         * "renickname":"搜索"
         * "userid":0
         * "rebatevalue":12.0
         *
         * "id":1,
         * "rebate":"500",
         * "phone":"phone",
         * "rebate_phone":"gs0002"
         */
        private String id;
        private String rebate;
        private String phone;
        private String rebate_phone;

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

        public String getRebate() {
            return rebate;
        }

        public void setRebate(String rebate) {
            this.rebate = rebate;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRebate_phone() {
            return rebate_phone;
        }

        public void setRebate_phone(String rebate_phone) {
            this.rebate_phone = rebate_phone;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
