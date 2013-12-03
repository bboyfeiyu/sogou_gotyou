
package com.umeng.findyou.beans;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: NavgationConfig.java
 * @Package com.umeng.gotme.beans
 * @Description: 出行工具配置
 * @author Honghui He
 * @version V1.0
 */

public class NavConfig {

    private Vehicle mVehicle = Vehicle.BUS;
    private LocationEntity mStartEntity = null;
    private LocationEntity mDestEntity = null;

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
    public NavConfig() {

    }

    /**
     * @Title: NavgationConfig
     * @Description: NavgationConfig Constructor
     * @param start
     * @param dest
     */
    public NavConfig(LocationEntity start, LocationEntity dest) {
        this(start, dest, Vehicle.BUS);
    }

    /**
     * @Title: NavgationConfig
     * @Description: NavgationConfig Constructor
     * @param start
     * @param dest
     * @param vehicle
     */
    public NavConfig(LocationEntity start, LocationEntity dest, Vehicle vehicle) {
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
