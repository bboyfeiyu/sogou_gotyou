
package com.umeng.gotyou.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.umeng.gotyou.R;
import com.umeng.gotyou.beans.LocationEntity;
import com.umeng.gotyou.beans.NavgationConfig;
import com.umeng.gotyou.sogouapi.SogouEntryActivity;
import com.umeng.gotyou.utils.ClipboardUtil;
import com.umeng.gotyou.utils.GpsUtil;
import com.umeng.gotyou.utils.LocationUtil;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: NavigationActivity.java
 * @Package com.umeng.gotme.activities
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class MainActivity extends Activity implements OnClickListener, LocationSource,
        AMapLocationListener {

    /**
     * AMapV2地图中介绍如何使用mapview显示地图
     */
    private MapView mMapView;

    /**
     * 
     */
    private Button mNavButton = null;
    /**
     * 
     */
    private Button mLocationButton = null;

    /**
     * 高德地图
     */
    private AMap mGaoDeMap;
    /**
     * 
     */
    private OnLocationChangedListener mListener;
    /**
     * 
     */
    private LocationManagerProxy mAMapLocationManager;
    /**
     * 返回的文字内容
     */
    private String mContent = "";
    /**
     * 
     */
    private AMapLocation mLocation = null;
    /**
     * 好友位置
     */
    private LocationEntity mFriendsLocation = null;

    private UiSettings mUiSettings = null;

    private NavgationConfig mConfig = null;

    /**
     * (非 Javadoc)
     * 
     * @Title: onCreate
     * @Description:
     * @param savedInstanceState
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        setupMap(savedInstanceState);
        initViews();
    }

    /**
     * @Title: initViews
     * @Description: 初始化视图
     * @throws
     */
    private void initViews() {
        mLocationButton = (Button) findViewById(R.id.location_btn);
        mLocationButton.setOnClickListener(this);

        mNavButton = (Button) findViewById(R.id.nav_btn);
        mNavButton.setOnClickListener(this);
    }

    /**
     * @Title: getClipboardText
     * @Description:
     * @throws
     */
    private String getClipboardText() {
        String text = ClipboardUtil.getContent(getApplicationContext());
        if (!TextUtils.isEmpty(text)) {
            Log.d("", "### 粘贴板内容 : " + text);
        }
        return text;
    }

    /**
     * 初始化AMap对象
     */
    private void setupMap(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.map);
        // 必须要写
        mMapView.onCreate(savedInstanceState);
        if (mGaoDeMap == null) {
            mGaoDeMap = mMapView.getMap();
        }

        CameraUpdateFactory.zoomTo(18.0f);
        // 设置缩放级别
        mGaoDeMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));

        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.location));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // // 将自定义的 myLocationStyle 对象添加到地图上
        mGaoDeMap.setMyLocationStyle(myLocationStyle);

        // 设置默认定位按钮是否显示
        mGaoDeMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mGaoDeMap.setMyLocationEnabled(true);

        mGaoDeMap.setLocationSource(this);
        mGaoDeMap.setMyLocationEnabled(true);

        // 设置缩放级别
        mGaoDeMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));

        mUiSettings = mGaoDeMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(true);

        // 点击覆盖物图标
        mGaoDeMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.equals(mFriendMarker)) {
                    Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        addMarkerOnMap();

    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onClick
     * @Description:
     * @param v
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (v == mLocationButton) {
            if (mLocation != null) {
                mContent = LocationUtil.locationToAddress(mLocation);
            }
        } else if (v == mNavButton) {

        }
        bundle.putString(SogouEntryActivity.APP_RESULT_CONTENT_TAG, mContent);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * 
     */
    private Marker mFriendMarker = null;
    LatLng mLatLng = new LatLng(39.54, 116.23);

    /**
     * @Title: addMarkerOnMap
     * @Description:
     * @throws
     */
    private void addMarkerOnMap() {

        mFriendMarker = mGaoDeMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title("我的朋友")
                .snippet("所在位置为: " + mLatLng.toString())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.friend))
                .perspective(true).draggable(true));// 设置远小近大效果,2.1.0版本新增
        // 设置默认显示一个infowinfow
        mFriendMarker.showInfoWindow();
        // 移动到目标点
        mGaoDeMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        Toast.makeText(getApplicationContext(), getClipboardText(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**
     * 此方法已经废弃
     */
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aLocation) {
        if (mListener != null && aLocation != null) {
            // 显示系统小蓝点
            mListener.onLocationChanged(aLocation);
            float bearing = mGaoDeMap.getCameraPosition().bearing;
            // 设置小蓝点旋转角度
            mGaoDeMap.setMyLocationRotateAngle(bearing);
            mLocation = aLocation;
            Log.d("", "" + mLocation.getLongitude() + ", latitude : " + mLocation.getLatitude());
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            // 设置缩放级别
            mGaoDeMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
            /*
             * mAMapLocManager.setGpsEnable(false);
             * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
             * API定位采用GPS和网络混合定位方式
             * ，第一个参数是定位provider，第二个参数时间最短是5000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
             */
            if (!GpsUtil.isGpsOPen(MainActivity.this)) {
                buildDialog();
            } else {
                mAMapLocationManager.setGpsEnable(true);
            }
            mAMapLocationManager.requestLocationUpdates(
                    LocationProviderProxy.AMapNetwork, 5000, 10, this);

        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destory();
        }
        mAMapLocationManager = null;
    }

    /**
     * @Title: buildDialog
     * @Description:
     * @throws
     */
    private void buildDialog() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("提示")
                .setMessage("是否打开GPS ?")
                .setNegativeButton("取消", null)
                .setPositiveButton("打开", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAMapLocationManager.setGpsEnable(true);
                        startActivity(new
                        Intent(Settings.ACTION_WIFI_SETTINGS)); // 直接进入手机中的wifi网络设置界面
                    }
                })
                .create();
        dialog.show();
    }

}
