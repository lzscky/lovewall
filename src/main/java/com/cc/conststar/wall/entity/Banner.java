package com.cc.conststar.wall.entity;

import com.cc.conststar.wall.bean.Pagenation;

import java.util.Date;
import java.io.Serializable;

/**
 * (Banner)实体类
 *
 * @author cky
 * @since 2022-12-05 14:31:40
 */
public class Banner  implements Serializable {
    private static final long serialVersionUID = -68297396201406017L;

    private Integer id;

    private String name;

    private String url;

    private String imgUrl;

    private Integer creatBy;

    private Integer updateBy;

    private Date creteTime;

    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getCreatBy() {
        return creatBy;
    }

    public void setCreatBy(Integer creatBy) {
        this.creatBy = creatBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
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

