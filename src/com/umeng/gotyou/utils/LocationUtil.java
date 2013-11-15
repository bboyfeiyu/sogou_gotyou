
package com.umeng.gotyou.utils;

import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: LocationUtil.java
 * @Package com.umeng.gotme.utils
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class LocationUtil {

    /**
     * @Title: locationToString
     * @Description:
     * @return
     * @throws
     */
    public static String locationToAddress(AMapLocation location) {
        String addr = "";
        Bundle extras = location.getExtras();
        if (extras != null) {
            addr = extras.getString("desc") + " -- (" + location.getLatitude() + ","
                    + location.getLongitude() + ")";
            Log.d("", "### my location addr : " + addr);
        }
        return addr;
    }

}
