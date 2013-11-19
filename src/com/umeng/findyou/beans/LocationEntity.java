
package com.umeng.findyou.beans;

import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: LocationEntity.java
 * @Package com.umeng.gotme.beans
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class LocationEntity {

    // 北京西站
    // GeoPoint ptLB = new GeoPoint( (int)(39.901375 * 1E6),(int)(116.329099 *
    // 1E6));

    private GeoPoint mGeoPoint = null;
    private String mAddress = "";

    /**
     * @Title: LocationEntity
     * @Description: LocationEntity Constructor
     * @param latLng
     * @param addr
     */
    public LocationEntity() {
    }

    /**
     * @Title: LocationEntity
     * @Description: LocationEntity Constructor
     * @param latLng
     * @param addr
     */
    public LocationEntity(GeoPoint geoPoint, String addr) {
        mGeoPoint = geoPoint;
        mAddress = addr;
    }

    /**
     * 获取 mGeoPoint
     * 
     * @return 返回 mGeoPoint
     */
    public GeoPoint getGeoPoint() {
        return mGeoPoint;
    }

    /**
     * 设置 mGeoPoint
     * 
     * @param 对mGeoPoint进行赋值
     */
    public void setGeoPoint(GeoPoint geoPoint) {
        this.mGeoPoint = geoPoint;
    }

    /**
     * 获取 mAddress
     * 
     * @return 返回 mAddress
     */
    public String getAddress() {
        return mAddress;
    }

    /**
     * 设置 mAddress
     * 
     * @param 对mAddress进行赋值
     */
    public void setAddress(String addr) {
        this.mAddress = addr;
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
        return "LocationEntity [mGeoPoint=" + mGeoPoint + ", mAddress=" + mAddress + "]";
    }

}
