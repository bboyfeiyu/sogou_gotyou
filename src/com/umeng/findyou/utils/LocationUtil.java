
package com.umeng.findyou.utils;

import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
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
     * @Description: 将坐标转换成地址
     * @return
     * @throws
     */
    public static void locationToAddress(GeoPoint location,
            BMapManager manager, MKSearchListener listener) {
        mMKSearch.init(manager, listener);
        // 逆地址解析
        mMKSearch.reverseGeocode(location);
    }

    /**
     * @Title: stringToGeoPoint
     * @Description: 字符串转换成GeoPoint
     * @param context
     * @param addr 剪切板中的地址
     * @return
     * @throws
     */
    public static GeoPoint stringToGeoPoint(Context context, String addr) {
        GeoPoint friendPoint = null;
        if (addr.contains("#")) {
            String geoPointStr = addr.split( Constants.ADDR_FLAG )[1]
                    .replace(" ", "")
                    .replace("(", "")
                    .replace(")", "");
            String[] points = geoPointStr.split(",");
            int latitude = (int) (Float.valueOf(points[0]) * 1e6);
            int longtitude = (int) (Float.valueOf(points[1]) * 1e6);
            friendPoint = new GeoPoint(latitude, longtitude);
        } else {
            Toast.makeText(context, "抱歉,地址格式不对...", Toast.LENGTH_SHORT).show();
        }
        return friendPoint;
    }
}
