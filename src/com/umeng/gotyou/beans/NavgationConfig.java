
package com.umeng.gotyou.beans;

import com.amap.api.maps.model.LatLng;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: NavgationConfig.java
 * @Package com.umeng.gotme.beans
 * @Description: 出行工具配置
 * @author Honghui He
 * @version V1.0
 */

public class NavgationConfig {

    private Vehicle mVehicle = Vehicle.BUS;
    private LatLng mStartLatLng = null;
    private String mStartStr = "";
    private LatLng mDestLatLng = null;
    private String mDestStr = "";

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
    public NavgationConfig(LatLng start, LatLng dest) {
        this(start, dest, Vehicle.BUS);
    }

    /**
     * @Title: NavgationConfig
     * @Description: NavgationConfig Constructor
     * @param start
     * @param dest
     * @param vehicle
     */
    public NavgationConfig(LatLng start, LatLng dest, Vehicle vehicle) {
        mStartLatLng = start;
        mDestLatLng = dest;
        mVehicle = vehicle;
    }

    /** (非 Javadoc)
     * @Title: toString
     * @Description: 
     * 
     * 
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NavgationConfig [mVehicle=" + mVehicle + ", mStartLatLng=" + mStartLatLng
                + ", mStartStr=" + mStartStr + ", mDestLatLng=" + mDestLatLng + ", mDestStr="
                + mDestStr + "]";
    }
    
    

}
