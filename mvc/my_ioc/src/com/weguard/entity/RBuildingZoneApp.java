package com.weguard.entity;


/**
 * (RBuildingZoneApp)表实体类
 *
 * @author makejava
 * @since 2018-09-03 10:56:59
 */
public class RBuildingZoneApp {
    
    private Integer id;
    
    private Integer buildingZoneId;
    
    private String appid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBuildingZoneId() {
        return buildingZoneId;
    }

    public void setBuildingZoneId(Integer buildingZoneId) {
        this.buildingZoneId = buildingZoneId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

}