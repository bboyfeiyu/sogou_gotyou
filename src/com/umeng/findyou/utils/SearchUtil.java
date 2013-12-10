
package com.umeng.findyou.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.umeng.findyou.beans.LocationEntity;
import com.umeng.findyou.beans.SearchConfig;
import com.umeng.findyou.beans.SearchConfig.Vehicle;
import com.umeng.findyou.beans.SearchEntity;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: LocationUtil.java
 * @Package com.umeng.gotme.utils
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class SearchUtil {

    private static MKSearch mMKSearch = new MKSearch();
    private static SearchConfig mSearchConfig = null;

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
    public static void routeSearch(Context context, SearchConfig config) {
        if (config == null) {
            return;
        }

        // 起点
        LocationEntity startEntity = config.getStartEntity();
        GeoPoint myGeoPoint = startEntity.getGeoPoint();
        MKPlanNode startNode = new MKPlanNode();
        if (myGeoPoint != null) {
            startNode.pt = new GeoPoint(myGeoPoint.getLatitudeE6(), myGeoPoint.getLongitudeE6());
        } else {
            startNode.name = startEntity.getAddress();
        }

        // 模拟一个点
        // end.pt = new GeoPoint(40057031, 116307852);
        // 目的地
        LocationEntity destEntity = config.getDestEntity();
        MKPlanNode endNode = new MKPlanNode();
        GeoPoint destGeoPoint = destEntity.getGeoPoint();
        if (destGeoPoint != null) {
            endNode.pt = new GeoPoint(destGeoPoint.getLatitudeE6(), destGeoPoint.getLongitudeE6());
        } else {
            endNode.name = destEntity.getAddress();
        }
        // 交换起点和终点
        if (config.isChangeAddr()) {
            MKPlanNode tempNode = startNode;
            startNode = endNode;
            endNode = tempNode;
        }
        // 设置驾车路线搜索策略，时间优先、费用最少或距离最短
        mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
        if (config.getVehicle() == Vehicle.CAR) {
            // 搜索
            mMKSearch
                    .drivingSearch(startEntity.getCity(), startNode, destEntity.getCity(), endNode);
            Log.d("", "### 驾车搜索");
        } else if (config.getVehicle() == Vehicle.BUS) {
            Log.d("", "### 公交搜索, city = " + startEntity.getCity() + ", start = " + startNode
                    + ", end = " + endNode);
            mMKSearch.transitSearch(startEntity.getCity(), startNode, endNode);
        } else if (config.getVehicle() == Vehicle.WALK) {
            Log.d("", "### 步行搜索");
            mMKSearch
                    .walkingSearch(startEntity.getCity(), startNode, destEntity.getCity(), endNode);
        }

    }

    /**
     * @Title: poiSearch
     * @Description: POI搜索
     * @param myGeoPoint
     * @param keyword
     * @throws
     */
    public static void poiSearch(Context context, SearchConfig config) {
        SearchEntity searchEntity = config.getSearchEntity();
        String keyword = searchEntity.getKeyWord();
        GeoPoint myGeoPoint = searchEntity.getGeoPoint();

        if (myGeoPoint == null) {
            if (context != null) {
                Toast.makeText(context, "抱歉,还没有获取到您的位置,请稍侯...", Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(context, "请输入关键字...", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(context, keyword + " 搜索中...", Toast.LENGTH_SHORT).show();
        // 获取中心点位置
        GeoPoint geoPoint = new GeoPoint(myGeoPoint.getLatitudeE6(), myGeoPoint.getLongitudeE6());
        mSearchConfig = config;
        mMKSearch.poiSearchNearBy(keyword, geoPoint, 5000);
    }

    /**
     * @Title: busSearch
     * @Description: 公交搜索, 需要通过POI搜索获取到uid
     * @throws
     */
    public static void busSearch(Context context, SearchConfig config) {
        mSearchConfig = config;
        SearchEntity searchEntity = config.getSearchEntity();
        Log.d("", "#### city = " + searchEntity.getCity() + ", bus = " + searchEntity.getKeyWord());
        mMKSearch.poiSearchInCity(searchEntity.getCity(), searchEntity.getKeyWord());
    }

    /**
     * @Title: busSearch
     * @Description: 公交搜索, 获取UID后再搜索
     * @throws
     */
    public static void busLineSearch(String uid) {
        SearchEntity searchEntity = mSearchConfig.getSearchEntity();
        Log.d("", "####busLineSearch city = " + searchEntity.getCity() + ", bus = " + uid);
        mMKSearch.busLineSearch(searchEntity.getCity(), uid);
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
