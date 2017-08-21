package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * 版本更新json解析类
 * Created by Administrator on 2017/6/20 0020.
 */
public class GetVersionQueryEntity {
    /**
     * list :[]
     */
    private List<DataEntity> list;

    public List<DataEntity> getList() {
        return list;
    }

    public void setList(List<DataEntity> list) {
        this.list = list;
    }

    public static class DataEntity {
        /**
         * "id":1,
         * "page":0,
         * "versionNumber":"v1.0.0",
         * "versionDescribe":"QQ更新",
         * "versionType":0,
         * "versionURL":"/shoppingManagementSystem/version/getVersionUpdate.do",
         * "pageSize":0,
         * "pageCount":0,
         * "listCount":0
         */

        private String versionNumber;
        private String versionURL;;
        private String versionDescribe;

        public String getVersionDescribe() {
            return versionDescribe;
        }

        public void setVersionDescribe(String versionDescribe) {
            this.versionDescribe = versionDescribe;
        }

        public String getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(String versionNumber) {
            this.versionNumber = versionNumber;
        }

        public String getVersionURL() {
            return versionURL;
        }

        public void setVersionURL(String versionURL) {
            this.versionURL = versionURL;
        }
    }
    }
