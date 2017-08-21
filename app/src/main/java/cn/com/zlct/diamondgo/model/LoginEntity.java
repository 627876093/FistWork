package cn.com.zlct.diamondgo.model;

/**
 * Created by Administrator on 2017/5/10 0010.
 */
public class LoginEntity {

    /**
     * phone : 13632840502
     * nickname : null
     * success : 成功
     */

    private String phone;
    private Object nickname;
    private String msg;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getNickname() {
        return nickname;
    }

    public void setNickname(Object nickname) {
        this.nickname = nickname;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
