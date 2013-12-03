
package com.umeng.findyou.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.umeng.findyou.beans.LocationEntity;
import com.umeng.findyou.beans.NavConfig;
import com.umeng.findyou.beans.NavConfig.Vehicle;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: LocationUtil.java
 * @Package com.umeng.gotme.utils
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class SearchUtil {

    static MKSearch mMKSearch = new MKSearch();

    /**
     * @Title: init
     * @Description: 初始化
     * @param bmp
     * @param listener
     * @throws
     */
    public static void init(BMapManager manager, MKSearchListener listener) {
        mMKSearch.init(manager, listener);
    }

    /**
     * @Title: locationToString
     * @Description: 将坐标转换成地址
     * @return
     * @throws
     */
    public static void locationToAddress(GeoPoint location) {
        // 逆地址解析
        mMKSearch.reverseGeocode(location);
    }

    /**
     * @Title: drivingSearch
     * @Description:
     * @throws
     */
    public static void routeSearch(NavConfig config, LocationEntity startEntity,
            LocationEntity destEntity) {
        if (config == null || startEntity == null || destEntity == null) {
            return;
        }

        MKPlanNode start = new MKPlanNode();
        GeoPoint myGeoPoint = startEntity.getGeoPoint();
        if (myGeoPoint != null) {
            start.pt = new GeoPoint(myGeoPoint.getLatitudeE6(), myGeoPoint.getLongitudeE6());
        }
        MKPlanNode end = new MKPlanNode();
        // end.pt = new GeoPoint(destEntity.getGeoPoint().getLatitudeE6(),
        // destEntity
        // .getGeoPoint().getLongitudeE6());
        end.pt = new GeoPoint(40057031, 116307852);
        // 设置驾车路线搜索策略，时间优先、费用最少或距离最短
        mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
        if (config.getVehicle() == Vehicle.CAR) {
            // 搜索
            mMKSearch.drivingSearch(startEntity.getCity(), start, destEntity.getCity(), end);
            Log.d("", "### 驾车搜索");
        } else if (config.getVehicle() == Vehicle.BUS) {
            Log.d("", "### 公交搜索, city = " + startEntity.getCity() + ", start = " + start
                    + ", end = " + end);
            mMKSearch.transitSearch(startEntity.getCity(), start, end);
        } else if (config.getVehicle() == Vehicle.WALK) {
            mMKSearch.walkingSearch(startEntity.getCity(), start, destEntity.getCity(), end);
        }

    }

    /**
     * @Title: drivingSearch
     * @Description:
     * @throws
     */
    public static void drivingSearch(LocationEntity startEntity, LocationEntity destEntity) {
        MKPlanNode start = new MKPlanNode();
        start.pt = new GeoPoint(startEntity.getGeoPoint().getLatitudeE6(), startEntity
                .getGeoPoint().getLongitudeE6());
        MKPlanNode end = new MKPlanNode();
        end.pt = new GeoPoint(destEntity.getGeoPoint().getLatitudeE6(), destEntity
                .getGeoPoint().getLongitudeE6());
        // 设置驾车路线搜索策略，时间优先、费用最少或距离最短
        mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
        // 搜索
        mMKSearch.drivingSearch(startEntity.getCity(), start, destEntity.getCity(), end);

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
            String geoPointStr = addr.split(Constants.ADDR_FLAG)[1]
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
