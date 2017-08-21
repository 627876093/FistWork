package cn.com.zlct.diamondgo.model;

/**
 * 订单列表
 * Created by Administrator on 2017/5/22 0022.
 */
public class GetUserOrder {

    private String OrderMark;//全部，待付款，待发货，待评价，售后
    private String phone;

    public GetUserOrder( String phone,String orderMark) {
        OrderMark = orderMark;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getOrderMark() {
        return OrderMark;
    }
    public void setOrderMark(String orderMark) {
        OrderMark = orderMark;
    }
}
