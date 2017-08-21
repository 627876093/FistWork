package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * 中国省、市、区县信息 实体类
 */
public class ChinaCitiesEntity {
    /**
     * ErrNum : 0
     * ErrMsg : 获取成功！
     * data :
     */

    private String ErrNum;
    private String ErrMsg;
    /**
     * "name": "北京市",
     * "id": 110000,
     * "list":
     */

    private List<DataEntity> list;

    public String getErrNum() {
        return ErrNum;
    }

    public void setErrNum(String ErrNum) {
        this.ErrNum = ErrNum;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String ErrMsg) {
        this.ErrMsg = ErrMsg;
    }

    public List<DataEntity> getList() {
        return list;
    }

    public void setList(List<DataEntity> list) {
        this.list = list;
    }

    public static class DataEntity {
        private String id;
        private String name;


        /**
         * "name": "北京市",
         * "id": 110000,
         * "list":
         */

        private List<DataEntity> list;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<DataEntity> getList() {
            return list;
        }

        public void setList(List<DataEntity> list) {
            this.list = list;
        }
    }
}
