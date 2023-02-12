package com.cc.conststar.wall.entity;

public class CarHomeBasicParam {

    private Long id;

    private String topName;

    /**
     * key_id
     */
    private Integer keyId;

    /**
     * name
     */
    private String name;

    /**
     * pnid
     */
    private String pnid;

    /**
     * specid
     */
    private Integer specid;

    /**
     * value
     */
    private String value;

    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getKeyId() {
        return keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPnid() {
        return pnid;
    }

    public void setPnid(String pnid) {
        this.pnid = pnid;
    }

    public Integer getSpecid() {
        return specid;
    }

    public void setSpecid(Integer specid) {
        this.specid = specid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
