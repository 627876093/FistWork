package cn.com.zlct.diamondgo.model;

/**
 * 本地自行创建的实体类
 */
public class LocalEntity {

    private int imgId;
    private String name;
    private String text;
    private int type;

    public LocalEntity(int imgId, String name, String text) {
        this.imgId = imgId;
        this.name = name;
        this.text = text;
        this.type = type;
    }

    public LocalEntity(int imgId, String name, int type) {
        this.imgId = imgId;
        this.name = name;
        this.type = type;
    }

    public LocalEntity(int imgId, String name) {
        this.imgId = imgId;
        this.name = name;
    }


    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
