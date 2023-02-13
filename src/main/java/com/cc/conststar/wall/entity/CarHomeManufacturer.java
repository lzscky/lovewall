package com.cc.conststar.wall.entity;

public class CarHomeManufacturer {

    private int brandId;


    /**
     * fctid
     */
    private Integer fctid;

    /**
     * fctname
     */
    private String fctname;

    /**
     * fctpy
     */
    private String fctpy;

    /**
     * series_place
     */
    private String seriesPlace;

    /**
     * series_place_num
     */
    private Integer seriesPlaceNum;


    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public Integer getFctid() {
        return fctid;
    }

    public void setFctid(Integer fctid) {
        this.fctid = fctid;
    }

    public String getFctname() {
        return fctname;
    }

    public void setFctname(String fctname) {
        this.fctname = fctname;
    }

    public String getFctpy() {
        return fctpy;
    }

    public void setFctpy(String fctpy) {
        this.fctpy = fctpy;
    }

    public String getSeriesPlace() {
        return seriesPlace;
    }

    public void setSeriesPlace(String seriesPlace) {
        this.seriesPlace = seriesPlace;
    }

    public Integer getSeriesPlaceNum() {
        return seriesPlaceNum;
    }

    public void setSeriesPlaceNum(Integer seriesPlaceNum) {
        this.seriesPlaceNum = seriesPlaceNum;
    }
}
