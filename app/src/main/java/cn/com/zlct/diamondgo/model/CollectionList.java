package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */
public class CollectionList {
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
         * "sort_name":"手机数码",
         * "phone":null,
         * "commodity_name":"测试排序",
         * "commodity_price":55.0,
         * "appUserId":0,
         * "commodityId":56,
         * "commodityUrl":"/shoppingManagementSystem/CommodityUpload/首页.jpg"}
         */

        private String id;
        private String sort_name;
        private String phone;
        private String commodity_name;
        private String commodity_price;
        private String appUserId;
        private String commodityId;
        private String commodityUrl;
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

        public String getCommodityUrl() {
            return commodityUrl;
        }

        public void setCommodityUrl(String commodityUrl) {
            this.commodityUrl = commodityUrl;
        }

        public String getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(String commodityId) {
            this.commodityId = commodityId;
        }

        public String getAppUserId() {
            return appUserId;
        }

        public void setAppUserId(String appUserId) {
            this.appUserId = appUserId;
        }

        public String getCommodity_price() {
            return commodity_price;
        }

        public void setCommodity_price(String commodity_price) {
            this.commodity_price = commodity_price;
        }

        public String getCommodity_name() {
            return commodity_name;
        }

        public void setCommodity_name(String commodity_name) {
            this.commodity_name = commodity_name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSort_name() {
            return sort_name;
        }

        public void setSort_name(String sort_name) {
            this.sort_name = sort_name;
        }
    }
}
