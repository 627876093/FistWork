package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/27 0027.
 */
public class ClassInfoEntity {


    List<DataEntity> list;

    public List<DataEntity> getList() {
        return list;
    }

    public void setList(List<DataEntity> list) {
        this.list = list;
    }

    public static class DataEntity {
        /**
         * "id":18,
         * "className":"手机",
         * "page":0,
         * "pageSize":0,
         * "classURL":null,
         * "classDesc":null,
         * "listCount":0,
         * "pageCount":0}
         */

        private String id;
        private String className;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }
}
