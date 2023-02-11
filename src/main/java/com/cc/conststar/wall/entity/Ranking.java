package com.cc.conststar.wall.entity;

import com.cc.conststar.wall.bean.BaseEntity;

import java.util.Date;

public class Ranking extends BaseEntity {

    private String url;

    private Date creteTime;

    private Date updateTime;

    private int paiseNum;

    public int getPaiseNum() {
        return paiseNum;
    }

    public void setPaiseNum(int paiseNum) {
        this.paiseNum = paiseNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreteTime() {
        return creteTime;
    }

    public void setCreteTime(Date creteTime) {
        this.creteTime = creteTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
