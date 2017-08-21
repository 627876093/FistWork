package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */
public class OrderList {
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
             *  "address":"天津天津市和平区有"
             *  "id":0
             *  "time":"2017-05-18 14:19:53"
             *  "desc":null
             *  "page":0
             *  "order_state":3//订单状态
             *  "update_time":"2017-05-18 14:19:53"
             *  "totalAmount":0.0
             *  "paymentMethod":0
             *  "order_Account":"384286621028"//订单编号
             *  "pageCount":0
             *  "listCount":0
             *  "accList":[{"id":0,"count":12,"order_Account":null,"rebate":0.0,"twoRebate":0.0,"cmdbrand":"红富士","cmdName":"苹果","cmdId":57,"cmdClass":"手机数码","pictureURL":"/shoppingManagementSystem/CommodityUpload/登录.jpg","commodity_price":65.0}]
             *  "pageSize":0
             *  "rebate":0.0
             *  "twoRebate":0.0
             *  "integral":0
             *  "user_name":null
             *  "user_id":0
             *  "recipientName":"曹奕程"
             *  "recipientPhone":"13705038428"
             *  "commodity_id":0
             *  "commodity_name":null
             *  "paymentMethod":2
             */
//{"address":"河北省邢台市内丘县寄情山水","id":0,"time":"2017-05-31 10:14:06","listCount":0,"pageCount":0,
// "page":0,"pageSize":0,"twoRebate":0.0,"rebate":0.0,"integral":0,"user_name":null,"user_id":0,"desc":null,
// "recipientName":"曹奕程","recipientPhone":"13705038428","order_Account":"0671096","order_state":1,
// "update_time":"2017-06-01 14:53:07","totalAmount":0.0,"paymentMethod":2,"accList":[]
        private String time;
        private String order_Account;
        private int order_state;//(-1:全部 0：未支付；1：支付待发货；2：发货待收货；3：收货待评价;4：已评价;5:待退款;)
        private List<ChildDataEntity> accList;
//        private double Account_price;//合计价格
        private int paymentMethod;//0:支付宝;1：微信;2:余额
        private int integral;
        private int type;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public DataEntity(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public int getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(int paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getOrder_Account() {
            return order_Account;
        }

        public void setOrder_Account(String order_Account) {
            this.order_Account = order_Account;
        }

        public int getOrder_state() {
            return order_state;
        }

        public void setOrder_state(int order_state) {
            this.order_state = order_state;
        }

        public List<ChildDataEntity> getAccList() {
            return accList;
        }

        public void setAccList(List<ChildDataEntity> accList) {
            this.accList = accList;
        }

//        public double getAccount_price() {
//            if (accList!=null){
//                for (int i=0;i<accList.size();i++){
//                    Account_price+=accList.get(i).getCommodity_price();
//                }
//                return Account_price;
//            }else {
//                return Account_price;
//            }
//        }

//        public void setAccount_price(double account_price) {
//            Account_price = account_price;
//        }

        public static class ChildDataEntity {
            /**
             * "id":0
             * "count":1,
             * "twoRebate":0.0,
             * "rebate":0.0,
             * "cmdbrand":"雅客",
             * "cmdName":"华为G11",
             * "cmdId":58,
             * "cmdClass":"手机数码",
             * "pictureURL":"/shoppingManagementSystem/CommodityUpload/设置.jpg",
             * "commodity_price":1000.0,
             * "order_Account":null,
             * "sum_price":0.0
             */
            private String cmdId;
            private String cmdClass;
            private String cmdName;
            private int count;//数量
            private double commodity_price;//单价
            private String pictureURL;//图片地址URL

            public String getCmdClass() {
                return cmdClass;
            }

            public void setCmdClass(String cmdClass) {
                this.cmdClass = cmdClass;
            }

            public String getCmdName() {
                return cmdName;
            }

            public void setCmdName(String cmdName) {
                this.cmdName = cmdName;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public double getCommodity_price() {
                return commodity_price;
            }

            public String getCmdId() {
                return cmdId;
            }

            public void setCmdId(String cmdId) {
                this.cmdId = cmdId;
            }

            public void setCommodity_price(double commodity_price) {
                this.commodity_price = commodity_price;
            }

            public String getPictureURL() {
                return pictureURL;
            }

            public void setPictureURL(String pictureURL) {
                this.pictureURL = pictureURL;
            }
        }
    }
}
