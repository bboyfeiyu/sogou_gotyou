
package com.umeng.gotyou.beans;

import com.amap.api.maps.model.LatLng;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: LocationEntity.java
 * @Package com.umeng.gotme.beans
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class LocationEntity {

    private LatLng mLatLng = null;
    private String mAddress = "";

    /**
     * @Title: LocationEntity
     * @Description: LocationEntity Constructor
     * @param latLng
     * @param addr
     */
    public LocationEntity(LatLng latLng, String addr) {
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
