
package com.umeng.findyou.beans;


/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: NavgationConfig.java
 * @Package com.umeng.gotme.beans
 * @Description: 出行工具配置
 * @author Honghui He
 * @version V1.0
 */

public class SearchConfig {

    /**
     * 路线搜索
     */
    private SearchType mSearchType = SearchType.ROUTE;
    private Vehicle mVehicle = Vehicle.BUS;
    private LocationEntity mStartEntity = null;
    private LocationEntity mDestEntity = null;
    private boolean isChangeAddr = false ;

    /**
     * 搜索的配置类
     */
    private SearchEntity mSearchEntity = new SearchEntity();

    /**
     * @ClassName: SearchType
     * @Description: 搜索类型
     * @author Honghui He
     */
    public enum SearchType {
        ROUTE,
        BUS,
        POI
    }

    /**
     * @ClassName: Vehicle
     * @Description: 交通工具， 公交车、自驾、步行
     * @author Honghui He
     */
    public enum Vehicle {
        BUS,
        CAR,
        WALK
    }

    /**
     * @Title: NavgationConfig
     * @Description: NavgationConfig Constructor
     * @param start
     * @param dest
     */
    public SearchConfig() {
    }

    /**
     * @Title: NavgationConfig
     * @Description: NavgationConfig Constructor
     * @param start
     * @param dest
     */
    public SearchConfig(LocationEntity start, LocationEntity dest) {
        this(start, dest, Vehicle.BUS);
    }

    /**
     * @Title: NavgationConfig
     * @Description: NavgationConfig Constructor
     * @param start
     * @param dest
     * @param vehicle
     */
    public SearchConfig(LocationEntity start, LocationEntity dest, Vehicle vehicle) {
        mStartEntity = start;
        mDestEntity = dest;
        mVehicle = vehicle;
    }

    /**
     * 获取 mStartEntity
     * 
     * @return 返回 mStartEntity
     */
    public LocationEntity getStartEntity() {
        return mStartEntity;
    }

    /**
     * 设置 mStartEntity
     * 
     * @param 对mStartEntity进行赋值
     */
    public void setStartEntity(LocationEntity startEntity) {
        this.mStartEntity = startEntity;
        mSearchEntity.setCity(mStartEntity.getCity());
        mSearchEntity.setGeoPoint(mStartEntity.getGeoPoint());
    }

    /**
     * 获取 mDestEntity
     * 
     * @return 返回 mDestEntity
     */
    public LocationEntity getDestEntity() {
        return mDestEntity;
    }

    /**
     * 设置 mDestEntity
     * 
     * @param 对mDestEntity进行赋值
     */
    public void setDestEntity(LocationEntity destEntity) {
        this.mDestEntity = destEntity;
    }

    
    
    /**获取 isChangeAddr
     * @return 返回 isChangeAddr
     */
    public boolean isChangeAddr() {
        return isChangeAddr;
    }

    /**设置 isChangeAddr
     * @param 对isChangeAddr进行赋值
     */
    public void setChangeAddr(boolean change) {
        this.isChangeAddr = change;
    }

    /**
     * 获取 mSearchType
     * 
     * @return 返回 mSearchType
     */
    public SearchType getSearchType() {
        return mSearchType;
    }

    /**
     * 设置 mSearchType
     * 
     * @param 对mSearchType进行赋值
     */
    public void setSearchType(SearchType type) {
        this.mSearchType = type;
    }

    /**
     * 获取 mSearchEntity
     * 
     * @return 返回 mSearchEntity
     */
    public SearchEntity getSearchEntity() {
        return mSearchEntity;
    }

    /**
     * 设置 mSearchEntity
     * 
     * @param 对mSearchEntity进行赋值
     */
    public void setSearchEntity(SearchEntity entity) {
        this.mSearchEntity = entity;
    }

    /**
     * @Title: setVehicle
     * @Description: 设置交通方式
     * @param vehicle
     * @throws
     */
    public void setVehicle(Vehicle vehicle) {
        mVehicle = vehicle;
    }

    /**
     * @Title: getVehicle
     * @Description:
     * @return
     * @throws
     */
    public Vehicle getVehicle() {
        return mVehicle;
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: toString
     * @Description:
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NavgationConfig [mVehicle=" + mVehicle + ", mStartEntity=" + mStartEntity
                + ", mDestEntity=" + mDestEntity + "]";
    }

}
