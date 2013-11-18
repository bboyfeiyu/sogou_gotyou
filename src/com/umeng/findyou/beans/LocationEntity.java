
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
//    GeoPoint ptLB = new GeoPoint( (int)(39.901375 * 1E6),(int)(116.329099 * 1E6)); 


    private GeoPoint mLatLng = null;
    private String mAddress = "";

    /**
     * @Title: LocationEntity
     * @Description: LocationEntity Constructor
     * @param latLng
     * @param addr
     */
    public LocationEntity(GeoPoint latLng, String addr) {
        mLatLng = latLng;
        mAddress = addr;
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
        return "LocationEntity [mLatLng=" + mLatLng + ", mAddress=" + mAddress + "]";
    }

}
