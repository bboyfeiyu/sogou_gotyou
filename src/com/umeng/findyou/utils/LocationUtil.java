
package com.umeng.findyou.utils;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: LocationUtil.java
 * @Package com.umeng.gotme.utils
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class LocationUtil {

    static MKSearch mMKSearch = new MKSearch();

    /**
     * @Title: locationToString
     * @Description:
     * @return
     * @throws
     */
    public static void locationToAddress(GeoPoint location,
            BMapManager manager, MKSearchListener listener) {
        // Bundle extras = location.getExtras();
        // if (extras != null) {
        // addr = extras.getString("desc") + " -- (" + location.getLatitude() +
        // ","
        // + location.getLongitude() + ")";
        // Log.d("", "### my location addr : " + addr);
        // }
        mMKSearch.init(manager, listener);
        mMKSearch.reverseGeocode(location); // 逆地址解析
    }
    
    public static String locationDataToString(LocationData locData) {
        // Bundle extras = location.getExtras();
        // if (extras != null) {
        // addr = extras.getString("desc") + " -- (" + location.getLatitude() +
        // ","
        // + location.getLongitude() + ")";
        // Log.d("", "### my location addr : " + addr);
        // }
        return "";
    }
}
