package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * 清单 实体类
 */
public class CartListEntity {
    /**
     * list :[]
     */
    private List<DataEntity> list;

    public List<DataEntity> getList() {
        return list;
    }

    public void setList(List<DataEntity> list) {
        this.list = list;
    }

    public static class DataEntity {
        /**
         * "id":0,
         * "count":1,
         * "commodityId":65,
         * "commodityUrl":"/shoppingManagementSystem/CommodityUpload/123123.jpg;/shoppingManagementSystem/CommodityUpload/12312333.jpg;/shoppingManagementSystem/CommodityUpload/u=646471818,4223079843&fm=72.jpg",
         * "commodity_name":"话梅",
         * "commodity_price":1000.0,
         * "appUserId":26,
         * "sort_name":"零食",
         * "phone":"13705038428"
         */

        private String id;
        private String count;
        private String commodityId;
        private String commodityUrl;
        private String commodity_name;
        private String commodity_price;
        private String appUserId;
        private String sort_name;
        private String phone;
        private boolean checked;
        private boolean isbj;

        public DataEntity() {
        }

        public boolean isbj() {
            return isbj;
        }

        public void setIsbj(boolean isbj) {
            this.isbj = isbj;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(String commodityId) {
            this.commodityId = commodityId;
        }

        public String getCommodityUrl() {
            return commodityUrl;
        }

        public void setCommodityUrl(String commodityUrl) {
            this.commodityUrl = commodityUrl;
        }

        public String getCommodity_name() {
            return commodity_name;
        }

        public void setCommodity_name(String commodity_name) {
            this.commodity_name = commodity_name;
        }

        public String getCommodity_price() {
            return commodity_price;
        }

        public void setCommodity_price(String commodity_price) {
            this.commodity_price = commodity_price;
        }

        public String getAppUserId() {
            return appUserId;
        }

        public void setAppUserId(String appUserId) {
            this.appUserId = appUserId;
        }

        public String getSort_name() {
            return sort_name;
        }

        public void setSort_name(String sort_name) {
            this.sort_name = sort_name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
