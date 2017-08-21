package cn.com.zlct.diamondgo.model;

/**
 * Created by Administrator on 2017/6/6 0006.
 */
public class UserPaymentEntity {

    /**
     * "SerialNumber":"6791168",
     * "NotifyUrl":"/userLogin/zfbNotify.do",
     * "SettlementMoney":"1000.0",
     * "msg":"success",
     * "PrivateKey":"MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMqax66KY0Inob9idzhd8Elcu8OfaSNecTIpGgheGn21tZDKNI18Arb+no2r3c/x4tc+guP641oy32URL1jTqP++K8VLGUkMZzrcNJgnRHgdTh+AMcmmcxm/hrtazmLPa//3C8DoastDdG6XxeK3d2RmLitH0cvXnABc5mmGWhzXAgMBAAECgYBWLWfqEesOZJUkNtnHHA3s5ojnOJMb/DvhviHYlU5nUjcckyWvWKQ++iau0//RR23ZaDl8h2bVIvZqotiky8MB4z3POjlY3zkz5Q17MUnwc/YdlJOMdjjWZqru08W2fq2JW2AB+LrhvQICcyHtQ3w5SwMiL3QaZWxO1d0VE1lxoQJBAOXP9QH7Rsd7hOekZPlF2vNY2ugPl5XFsrRAVlOo0i2LtRQ1KjU15cKpe0kfmij9TN2t8U158XL71KNRI03reIUCQQDhsR/QjPRErmjdz+5sud5QEIKv2nsmVqrlgARIPA/plLGDR8EMNJ3fYqrSAdugf+ztvHWcPwD8keD84Xh3/uyrAkBb97wSHe/2Vt0aInTyON1lc1KvecXs/yAD+JdThYUPCxDdVGVexAH9w/t9iPMVokDHmhuuKLXSkStIbGkXfrtNAkBF1Byo1QO6wE+32V7GixeKpCEbMbkKmqQTj/FDPDocJiJqIOhM03bJJ+j8QxDl7s6qm7Wz2xZ+DtCSzVwNnHUzAkB6BUWX8Vf1GqTf5i1/OpTPbPX+fxwdQAFG7YhMk/9lzpWlh6o97OR40iLjWFNDPMBRi3TkZ1oIQsGYGE3FpVZC"
     */

//{"SerialNumber":"7381442",
// "NotifyUrl":"/userLogin/zfbNotify.do",
// "SettlementMoney":"1.0",
// "msg":"success",
// "PrivateKey":"MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMqax66KY0Inob9idzhd8Elcu8OfaSNecTIpGgheGn21tZDKNI18Arb+no2r3c/x4tc+guP641oy32URL1jTqP++K8VLGUkMZzrcNJgnRHgdTh+AMcmmcxm/hrtazmLPa//3C8DoastDdG6XxeK3d2RmLitH0cvXnABc5mmGWhzXAgMBAAECgYBWLWfqEesOZJUkNtnHHA3s5ojnOJMb/DvhviHYlU5nUjcckyWvWKQ++iau0//RR23ZaDl8h2bVIvZqotiky8MB4z3POjlY3zkz5Q17MUnwc/YdlJOMdjjWZqru08W2fq2JW2AB+LrhvQICcyHtQ3w5SwMiL3QaZWxO1d0VE1lxoQJBAOXP9QH7Rsd7hOekZPlF2vNY2ugPl5XFsrRAVlOo0i2LtRQ1KjU15cKpe0kfmij9TN2t8U158XL71KNRI03reIUCQQDhsR/QjPRErmjdz+5sud5QEIKv2nsmVqrlgARIPA/plLGDR8EMNJ3fYqrSAdugf+ztvHWcPwD8keD84Xh3/uyrAkBb97wSHe/2Vt0aInTyON1lc1KvecXs/yAD+JdThYUPCxDdVGVexAH9w/t9iPMVokDHmhuuKLXSkStIbGkXfrtNAkBF1Byo1QO6wE+32V7GixeKpCEbMbkKmqQTj/FDPDocJiJqIOhM03bJJ+j8QxDl7s6qm7Wz2xZ+DtCSzVwNnHUzAkB6BUWX8Vf1GqTf5i1/OpTPbPX+fxwdQAFG7YhMk/9lzpWlh6o97OR40iLjWFNDPMBRi3TkZ1oIQsGYGE3FpVZC"}

    private String SerialNumber;
    private String NotifyUrl;
    private String SettlementMoney;
    private String msg;
    private String PrivateKey;

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getNotifyUrl() {
        return NotifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        NotifyUrl = notifyUrl;
    }

    public String getSettlementMoney() {
        return SettlementMoney;
    }

    public void setSettlementMoney(String settlementMoney) {
        SettlementMoney = settlementMoney;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPrivateKey() {
        return PrivateKey;
    }

    public void setPrivateKey(String privateKey) {
        PrivateKey = privateKey;
    }
}
