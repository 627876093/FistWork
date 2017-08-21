package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19 0019.
 */
public class GetUserBankCard {
//{"list":[{
// "id":20,"phone":"13686422747","bankMine":"213","card":"441622199212131234","cardholder":"1231","phoneId":"13686422747","bankCardType":"null","bankCardNumber":"1223"},{"id":21,"phone":"13686422747","bankMine":"123","card":"441922199212134652","cardholder":"123","phoneId":"13686422747","bankCardType":"null","bankCardNumber":"123"}]}
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
         * "id":20
         * "phone":"13686422747"
         * "bankMine":"213"//银行名称
         * "card":"441622199212131234"//身份证
         * "cardholder":"1231"//持卡人
         * "phoneId":"13686422747"
         * "bankCardType":"null"
         * "bankCardNumber":"1223"卡号
         */
        private String id;
        private String phone;
        private String bankMine;
        private String card;
        private String cardholder;
        private String phoneId;
        private String bankCardType;
        private String bankCardNumber;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBankMine() {
            return bankMine;
        }

        public void setBankMine(String bankMine) {
            this.bankMine = bankMine;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getCardholder() {
            return cardholder;
        }

        public void setCardholder(String cardholder) {
            this.cardholder = cardholder;
        }

        public String getBankCardType() {
            return bankCardType;
        }

        public void setBankCardType(String bankCardType) {
            this.bankCardType = bankCardType;
        }

        public String getBankCardNumber() {
            return bankCardNumber;
        }

        public void setBankCardNumber(String bankCardNumber) {
            this.bankCardNumber = bankCardNumber;
        }

        public String getPhoneId() {
            return phoneId;
        }

        public void setPhoneId(String phoneId) {
            this.phoneId = phoneId;
        }
    }
}
