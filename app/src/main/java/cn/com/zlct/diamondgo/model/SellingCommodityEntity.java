package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18 0018.
 */
public class SellingCommodityEntity {
    //{"list":[
    // {"id":56,"page":0,"brand_name":null,"pageSize":0,"rebate":0.0,"twoRebate":0.0,"shelves":0,
    // "sort_name":null,"sort_id":0,"modelCode":null,"brand_id":0,"goodsCode":null,"normsType":null,
    // "color":null,"netWeight":null,"barCode":null,"quality":null,"salesVolume":0,
    // "picture_address":"/shoppingManagementSystem/CommodityUpload/首页.jpg","commodity_name":"测试排序",
    // "cdy_description":null,"commodity_price":55.0,"commodity_integral":0,"commodity_parameter":null,
    // "produceDate":null,"generateCode":null,"approvalCode":null,"manufacturerName":null,
    // "manufacturerAddress":null,"listCount":0,"pageCount":0}

    //"id":66,"page":0,"salesVolume":0,"brand_name":null,"pageSize":0,"shelves":0,
    // "twoRebate":0.0,"brand_id":0,"sort_name":null,"sort_id":0,"rebate":0.0,"modelCode":null,
    // "goodsCode":null,"normsType":null,"color":null,"netWeight":null,"barCode":null,
    // "quality":null,"picture_address":null,"commodity_name":"维他柠檬茶",
    // "cdy_description":null,"commodity_price":1000.0,"commodity_integral":0,"commodity_parameter":null,
    // "produceDate":null,"generateCode":null,"approvalCode":null,"manufacturerName":null,"manufacturerAddress":null,

    //"picture_address":null,//商品轮播图
    // "desc_address":null,//详情
    // "home_address":"/shoppingManagementSystem/CommodityUpload/14967156930160.jpg",//列表展示图

    // "listCount":0,"pageCount":0

List<DataEntity> list;

    public List<DataEntity> getList() {
        return list;
    }

    public void setList(List<DataEntity> list) {
        this.list = list;
    }

    public static class DataEntity {
        String id;
        String commodity_name;
        String picture_address;
        String desc_address;
        String home_address;
        String commodity_price;
        int type;

        public String getDesc_address() {
            return desc_address;
        }

        public void setDesc_address(String desc_address) {
            this.desc_address = desc_address;
        }

        public String getHome_address() {
            return home_address;
        }

        public void setHome_address(String home_address) {
            this.home_address = home_address;
        }

        public DataEntity(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCommodity_name() {
            return commodity_name;
        }

        public void setCommodity_name(String commodity_name) {
            this.commodity_name = commodity_name;
        }

        public String getPicture_address() {
            return picture_address;
        }

        public void setPicture_address(String picture_address) {
            this.picture_address = picture_address;
        }

        public String getCommodity_price() {
            return commodity_price;
        }

        public void setCommodity_price(String commodity_price) {
            this.commodity_price = commodity_price;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }


}
