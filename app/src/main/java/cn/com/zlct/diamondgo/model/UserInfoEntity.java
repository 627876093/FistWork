package cn.com.zlct.diamondgo.model;

/**
 * Created by Administrator on 2017/5/18 0018.
 */
public class UserInfoEntity {
    /**
     *"sex":null// 性别
     * "headURL":null// 头像路径
     * "phone":"13686422747"//用户登录的手机号码
     * "userIntegral":null//用户积分（累计）
     * "memberid"://会员号
     * "userVIP":null//会员等级
     * "msg":"success"
     * "recommphone":null //推荐人手机号
     * "name":null//用户姓名
     * "currency":null//现金分
     * "userrebate":"0.0"//用户返利
     * "carat":null//克拉
     * "accountbalance":null//账户余额
     */

//{"sex":null,"headURL":"/shoppingManagementSystem/headUpload/20170524154757.jpg",
// "phone":"gs0002","userIntegral":"48510.0","memberid":null,"userVIP":null,"msg":"success","currency":"0","recommphone":null,"accountbalance":"98991.0","name":null,"userrebate":"0.0","carat":"0"}
// {"sex":"-","headURL":null,"phone":"13686422747","userIntegral":null,"memberid":null,"userVIP":null,"msg":"success","currency":"0","recommphone":null,"accountbalance":"0.0","name":"Mr`lin","userrebate":"0.0","carat":"0"}

    String sex;
    String headURL;
    String phone;
    String userIntegral;
    String memberid;
    String userVIP;
    String msg;
    String currency;
    String recommphone;
    String accountbalance;
    String name;
    String userrebate;
    String carat;
    String provinceName;
    String cityName;
    String countyName;

    public String getSex() {
        if (sex==null){
            return "-";
        }else if (sex.equals("")){
            return "-";
        }else {
            return sex;
        }
    }


    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadURL() {
        if (headURL==null){
            return "";
        }else if (headURL.equals("")){
            return "";
        }else {
            return headURL;
        }
    }

    public void setHeadURL(String headURL) {
        this.headURL = headURL;
    }

    public String getPhone() {
        if (phone==null){
            return "-";
        }else if (phone.equals("")){
            return "-";
        }else {
            return phone;
        }
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserIntegral() {
        if (userIntegral==null){
            return "-";
        }else if (userIntegral.equals("")){
            return "-";
        }else {
            return userIntegral;
        }
    }

    public void setUserIntegral(String userIntegral) {
        this.userIntegral = userIntegral;
    }

    public String getMemberid() {

        if (memberid==null){
            return "-";
        }else if (memberid.equals("")){
            return "-";
        }else {
            return memberid;
        }
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getUserVIP() {
        if (userVIP==null){
            return "-";
        }else if (userVIP.equals("")){
            return "-";
        }else {
            return userVIP;
        }
    }

    public void setUserVIP(String userVIP) {
        this.userVIP = userVIP;
    }

    public String getMsg() {
        if (msg==null){
            return "-";
        }else if (msg.equals("")){
            return "-";
        }else {
            return msg;
        }
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCurrency() {
        if (currency==null){
            return "0";
        }else if (currency.equals("")){
            return "0";
        }else {
            return currency;
        }
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRecommphone() {
        if (recommphone==null){
            return "-";
        }else if (recommphone.equals("")){
            return "-";
        }else {
            return recommphone;
        }
    }

    public void setRecommphone(String recommphone) {
        this.recommphone = recommphone;
    }

    public String getAccountbalance() {
        if (accountbalance==null){
            return "0";
        }else if (accountbalance.equals("")){
            return "0";
        }else {
            return accountbalance;
        }
    }

    public void setAccountbalance(String accountbalance) {
        this.accountbalance = accountbalance;
    }

    public String getName() {
        if (name==null){
            return "-";
        }else if (name.equals("")){
            return "-";
        }else {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserrebate() {
        if (userrebate==null){
            return "-";
        }else if (userrebate.equals("")){
            return "-";
        }else {
            return userrebate;
        }
    }

    public void setUserrebate(String userrebate) {
        this.userrebate = userrebate;
    }

    public String getCarat() {
        if (carat==null){
            return "-";
        }else if (carat.equals("")){
            return "-";
        }else {
            return carat;
        }
    }

    public void setCarat(String carat) {
        this.carat = carat;
    }
}
