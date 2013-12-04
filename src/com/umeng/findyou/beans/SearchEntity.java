
package com.umeng.findyou.beans;

import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: SearchEntity.java
 * @Package com.umeng.findyou.beans
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class SearchEntity {

    private String mCity = "";
    private GeoPoint mGeoPoint = null;
    private String mKeyWord = "";

    public SearchEntity() {
    }

    /**
     * 获取 mCity
     * 
     * @return 返回 mCity
     */
    public String getCity() {
        return mCity;
    }

    /**
     * 设置 mCity
     * 
     * @param 对mCity进行赋值
     */
    public void setCity(String city) {
        this.mCity = city;
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
     * 获取 mKeyWord
     * 
     * @return 返回 mKeyWord
     */
    public String getKeyWord() {
        return mKeyWord;
    }

    /**
     * 设置 mKeyWord
     * 
     * @param 对mKeyWord进行赋值
     */
    public void setKeyWord(String keyword) {
        this.mKeyWord = keyword;
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
        return "SearchEntity [mCity=" + mCity + ", mGeoPoint=" + mGeoPoint + ", mKeyWord="
                + mKeyWord + "]";
    }

}
