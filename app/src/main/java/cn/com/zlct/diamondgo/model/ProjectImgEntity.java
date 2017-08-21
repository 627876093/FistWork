package cn.com.zlct.diamondgo.model;

/**
 * Created by Administrator on 2017/6/2 0002.
 */
public class ProjectImgEntity {
    /**
     * "manufacturerAddress":"",
     * "produceDate":"",
     * "modelCode":"",
     * "commodity_parameter":"盒",
     * "desc_address":"/shoppingManagementSystem/CommodityUpload/14968167854370.jpg;/shoppingManagementSystem/CommodityUpload/14968167854381.jpg",
     * "normsType":"",
     * "twoRebate":"30.0",
     * "approvalCode":"",
     * "goodsCode":"",
     * "msg":"success",
     * "brand_name":"云端黑茶",
     * "commodity_name":"渠江薄片",
     * "commodity_integral":"100",
     * "sort_name":"云端黑茶",
     * "netWeight":"",
     * "color":"",
     * "commodity_price":"2100.0",
     * "quality":"",
     * "cdy_description":"黑茶",
     * "barCode":"",
     * "generateCode":"",
     * "rebate":"100.0",
     * "manufacturerName":"",
     * "sort_id":"15",
     * "picture_address":"/shoppingManagementSystem/CommodityUpload/14968167854370.jpg"
     */
    private String id;
    private String sort_id;// 类别ID
    private String brand_id;// 品牌ID
    private String home_address;//首页图片地址
    private String shelves;// 1：上架 0：下架
    private String page;//当前页
    private String pageSize;//每页条目
    private String pageCount;//总页数
    private String ListCount;//总数据
    private String  salesVolume;//销量
    private String manufacturerAddress;//厂商地址
    private String produceDate;//生产日期
    private String modelCode;//商品型号
    private String commodity_parameter;//商品参数
    private String normsType;//规格类型
    private String approvalCode;//批准文号
    private String goodsCode;//货号
    private String msg;
    private String brand_name;//品牌名称
    private String commodity_name;//商品名称
    private String commodity_integral;//商品积分
    private String sort_name;//类别名称
    private String netWeight;//净含量
    private String color;//商品颜色
    private String commodity_price;//商品价格
    private String quality;//保质期
    private String cdy_description;//商品描述
    private String barCode;//条码编号
    private String generateCode;//生成许可证编号
    private String manufacturerName;//厂商名称
    private String desc_address;//参数地址
    private String picture_address;//商品图片地址
    private String rebate;//一级返利
    private String twoRebate;//二级返利
    private int type;

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

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getListCount() {
        return ListCount;
    }

    public void setListCount(String listCount) {
        ListCount = listCount;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getShelves() {
        return shelves;
    }

    public void setShelves(String shelves) {
        this.shelves = shelves;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getSort_id() {
        return sort_id;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getRebate() {
        return rebate;
    }

    public void setRebate(String rebate) {
        this.rebate = rebate;
    }

    public String getTwoRebate() {
        return twoRebate;
    }

    public void setTwoRebate(String twoRebate) {
        this.twoRebate = twoRebate;
    }

    public String getManufacturerAddress() {
        return manufacturerAddress;
    }

    public void setManufacturerAddress(String manufacturerAddress) {
        this.manufacturerAddress = manufacturerAddress;
    }

    public String getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(String produceDate) {
        this.produceDate = produceDate;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getCommodity_parameter() {
        return commodity_parameter;
    }

    public void setCommodity_parameter(String commodity_parameter) {
        this.commodity_parameter = commodity_parameter;
    }

    public String getNormsType() {
        return normsType;
    }

    public void setNormsType(String normsType) {
        this.normsType = normsType;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getCommodity_integral() {
        return commodity_integral;
    }

    public void setCommodity_integral(String commodity_integral) {
        this.commodity_integral = commodity_integral;
    }

    public String getSort_name() {
        return sort_name;
    }

    public void setSort_name(String sort_name) {
        this.sort_name = sort_name;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCommodity_price() {
        return commodity_price;
    }

    public void setCommodity_price(String commodity_price) {
        this.commodity_price = commodity_price;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getCdy_description() {
        return cdy_description;
    }

    public void setCdy_description(String cdy_description) {
        this.cdy_description = cdy_description;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getGenerateCode() {
        return generateCode;
    }

    public void setGenerateCode(String generateCode) {
        this.generateCode = generateCode;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getPicture_address() {
        return picture_address;
    }

    public void setPicture_address(String picture_address) {
        this.picture_address = picture_address;
    }

    public String getDesc_address() {
        return desc_address;
    }

    public void setDesc_address(String desc_address) {
        this.desc_address = desc_address;
    }
}
