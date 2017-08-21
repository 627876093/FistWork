package cn.com.zlct.diamondgo.model;

/**
 * 手机验证码 实体类
 */
public class MobileCodeEntity {


    /**
     * phone : 13632840502
     * code : 677823
     * recommPhone : null
     * msg : success
     */

    private String phone;
    private String code;
    private Object recommPhone;
    private String msg;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getRecommPhone() {
        return recommPhone;
    }

    public void setRecommPhone(Object recommPhone) {
        this.recommPhone = recommPhone;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
