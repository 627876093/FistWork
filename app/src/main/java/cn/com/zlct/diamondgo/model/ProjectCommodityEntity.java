package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2 0002.
 */
public class ProjectCommodityEntity {
//    private String manufacturerAddress;
//    private String produceDate;
//    private String modelCode;
//    private String commodity_parameter;
//    private String normsType;
//    private String approvalCode;
//    private String goodsCode;
//    private String msg;
//    private String brand_name;
//    private String commodity_name;
//    private String commodity_integral;
//    private String sort_name;
//    private String netWeight;
//    private String color;
//    private String commodity_price;
//    private String quality;
//    private String cdy_description;
//    private String barCode;
//    private String generateCode;
//    private String manufacturerName;
//    private String desc_address;
//    private String picture_address;
//    private int type;
//    private List<DataEntity> list;
//
//    public String getManufacturerAddress() {
//        return manufacturerAddress;
//    }
//
//    public void setManufacturerAddress(String manufacturerAddress) {
//        this.manufacturerAddress = manufacturerAddress;
//    }
//
//    public String getProduceDate() {
//        return produceDate;
//    }
//
//    public void setProduceDate(String produceDate) {
//        this.produceDate = produceDate;
//    }
//
//    public String getModelCode() {
//        return modelCode;
//    }
//
//    public void setModelCode(String modelCode) {
//        this.modelCode = modelCode;
//    }
//
//    public String getCommodity_parameter() {
//        return commodity_parameter;
//    }
//
//    public void setCommodity_parameter(String commodity_parameter) {
//        this.commodity_parameter = commodity_parameter;
//    }
//
//    public String getNormsType() {
//        return normsType;
//    }
//
//    public void setNormsType(String normsType) {
//        this.normsType = normsType;
//    }
//
//    public String getApprovalCode() {
//        return approvalCode;
//    }
//
//    public void setApprovalCode(String approvalCode) {
//        this.approvalCode = approvalCode;
//    }
//
//    public String getGoodsCode() {
//        return goodsCode;
//    }
//
//    public void setGoodsCode(String goodsCode) {
//        this.goodsCode = goodsCode;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public String getBrand_name() {
//        return brand_name;
//    }
//
//    public void setBrand_name(String brand_name) {
//        this.brand_name = brand_name;
//    }
//
//    public String getCommodity_name() {
//        return commodity_name;
//    }
//
//    public void setCommodity_name(String commodity_name) {
//        this.commodity_name = commodity_name;
//    }
//
//    public String getCommodity_integral() {
//        return commodity_integral;
//    }
//
//    public void setCommodity_integral(String commodity_integral) {
//        this.commodity_integral = commodity_integral;
//    }
//
//    public String getSort_name() {
//        return sort_name;
//    }
//
//    public void setSort_name(String sort_name) {
//        this.sort_name = sort_name;
//    }
//
//    public String getNetWeight() {
//        return netWeight;
//    }
//
//    public void setNetWeight(String netWeight) {
//        this.netWeight = netWeight;
//    }
//
//    public String getColor() {
//        return color;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }
//
//    public String getQuality() {
//        return quality;
//    }
//
//    public void setQuality(String quality) {
//        this.quality = quality;
//    }
//
//    public String getCommodity_price() {
//        return commodity_price;
//    }
//
//    public void setCommodity_price(String commodity_price) {
//        this.commodity_price = commodity_price;
//    }
//
//    public String getCdy_description() {
//        return cdy_description;
//    }
//
//    public void setCdy_description(String cdy_description) {
//        this.cdy_description = cdy_description;
//    }
//
//    public String getBarCode() {
//        return barCode;
//    }
//
//    public void setBarCode(String barCode) {
//        this.barCode = barCode;
//    }
//
//    public String getGenerateCode() {
//        return generateCode;
//    }
//
//    public void setGenerateCode(String generateCode) {
//        this.generateCode = generateCode;
//    }
//
//    public String getManufacturerName() {
//        return manufacturerName;
//    }
//
//    public void setManufacturerName(String manufacturerName) {
//        this.manufacturerName = manufacturerName;
//    }
//
//    public String getDesc_address() {
//        return desc_address;
//    }
//
//    public void setDesc_address(String desc_address) {
//        this.desc_address = desc_address;
//    }
//
//    public String getPicture_address() {
//        return picture_address;
//    }
//
//    public void setPicture_address(String picture_address) {
//        this.picture_address = picture_address;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public List<DataEntity> getList() {
//        return list;
//    }
//
//    public void setList(List<DataEntity> list) {
//        this.list = list;
//    }
//
//    public class DataEntity{
//        /**
//         * "id":1,
//         * "head_portrait":null,
//         * "comment_time":"2017-05-15 00:00:00",
//         * "comment_cell_phone":"13526562536",
//         * "comment_content":"宝贝很好 我很喜欢！！",
//         * "picture_path":"/shoppingManagementSystem/querySystemJournal.do",
//         * "headURL":null,
//         * "nickname":"",
//         * "userVIP":"",1-5
//         * "pId":0,
//         * "commodity_id":57
//         */
//        private String id;
//        private String head_portrait;
//        private String comment_time;
//        private String comment_cell_phone;
//        private String comment_content;
//        private String picture_path;
//        private String headURL;
//        private String nickname;
//        private String userVIP;
//        private String pId;
//        private String commodity_id;
//        private int type;
//
//        public DataEntity(int type) {
//            this.type = type;
//        }
//
//        public int getType() {
//            return type;
//        }
//
//        public void setType(int type) {
//            this.type = type;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getHead_portrait() {
//            return head_portrait;
//        }
//
//        public void setHead_portrait(String head_portrait) {
//            this.head_portrait = head_portrait;
//        }
//
//        public String getComment_time() {
//            return comment_time;
//        }
//
//        public void setComment_time(String comment_time) {
//            this.comment_time = comment_time;
//        }
//
//        public String getComment_cell_phone() {
//            return comment_cell_phone;
//        }
//
//        public void setComment_cell_phone(String comment_cell_phone) {
//            this.comment_cell_phone = comment_cell_phone;
//        }
//
//        public String getComment_content() {
//            return comment_content;
//        }
//
//        public void setComment_content(String comment_content) {
//            this.comment_content = comment_content;
//        }
//
//        public String getPicture_path() {
//            return picture_path;
//        }
//
//        public void setPicture_path(String picture_path) {
//            this.picture_path = picture_path;
//        }
//
//        public String getHeadURL() {
//            return headURL;
//        }
//
//        public void setHeadURL(String headURL) {
//            this.headURL = headURL;
//        }
//
//        public String getNickname() {
//            return nickname;
//        }
//
//        public void setNickname(String nickname) {
//            this.nickname = nickname;
//        }
//
//        public String getUserVIP() {
//            return userVIP;
//        }
//
//        public void setUserVIP(String userVIP) {
//            this.userVIP = userVIP;
//        }
//
//        public String getCommodity_id() {
//            return commodity_id;
//        }
//
//        public void setCommodity_id(String commodity_id) {
//            this.commodity_id = commodity_id;
//        }
//
//        public String getpId() {
//            return pId;
//        }
//
//        public void setpId(String pId) {
//            this.pId = pId;
//        }
//    }


    private ProjectImgEntity imgList;
    private ProjectDataEntity dataList;
    private int type;

    public ProjectCommodityEntity(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ProjectCommodityEntity() {

    }

    public ProjectImgEntity getImgList() {
        return imgList;
    }

    public void setImgList(ProjectImgEntity imgList) {
        this.imgList = imgList;
    }

    public ProjectDataEntity getDataList() {
        return dataList;
    }

    public void setDataList(ProjectDataEntity dataList) {
        this.dataList = dataList;
    }
}
