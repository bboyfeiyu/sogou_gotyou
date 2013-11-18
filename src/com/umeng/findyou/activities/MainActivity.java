
package com.umeng.findyou.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.umeng.findyou.R;
import com.umeng.findyou.sogouapi.SogouEntryActivity;
import com.umeng.findyou.utils.BMapUtil;
import com.umeng.findyou.utils.ClipboardUtil;
import com.umeng.findyou.utils.Constants;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: NavigationActivity.java
 * @Package com.umeng.gotme.activities
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class MainActivity extends Activity implements OnClickListener {

    private BMapManager mBMapMan = null;
    private MapView mMapView = null;
    /**
     * 用MapController完成地图控制
     */
    private MapController mMapController = null;

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
    /**
     * 
     */
    /**
     * 
     */
    /**
     * 返回的文字内容
     */
    private String mContent = "";
    /**
     * 
     */
    public LocationClient mLocationClient = null;
    public MyLocationListenner myListener = new MyLocationListenner();

    // 定位图层
    private LocationOverlay myLocationOverlay = null;

    /**
     * 好友位置
     */
    // private LocationEntity mFriendsLocation = null;

    // private NavgationConfig mConfig = null;

    private View viewCache = null;

    private TextView mPopupText = null;
    // 弹出泡泡图层
    private PopupOverlay mPopupOverlay = null;// 弹出泡泡图层，浏览节点时使用
    private LocationData locData = new LocationData();

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
        // 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
        setupMap();
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

        viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
        mPopupText = (TextView) viewCache.findViewById(R.id.textcache);

        // 泡泡点击响应回调
        PopupClickListener popListener = new PopupClickListener() {
            @Override
            public void onClickedPopup(int index) {
                Log.v("click", "clickapoapo");
            }
        };
        mPopupOverlay = new PopupOverlay(mMapView, popListener);
        MyLocationMapViewOverlay.mPopupView = mPopupOverlay;
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
     * 初始化Map对象
     */
    private void setupMap() {
        mBMapMan = new BMapManager(getApplication());
        mBMapMan.init(Constants.BAIDU_MAP_KEY, null);
        setContentView(R.layout.navigation_activity);
        // 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
        mMapView = (MapView) findViewById(R.id.bmapsView);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setDoubleClickZooming(true);

        // 设置启用内置的缩放控件
        mMapController = mMapView.getController();
        // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        GeoPoint point = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6));
        // 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        mMapController.setCenter(point);// 设置地图中心点
        mMapController.setZoom(16);// 设置地图zoom级别

        addMyLocationToMap();
        setupLocation();

    }

    /**
     * @Title: setupLocation
     * @Description:
     * @throws
     */
    private void setupLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all"); // 返回的定位结果包含地址信息
        option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
        option.disableCache(true); // 禁止启用缓存定位
        option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
        //
        mLocationClient = new LocationClient(getApplicationContext());
        //
        mLocationClient.setAK(Constants.BAIDU_MAP_KEY);
        // 注册监听函数
        mLocationClient.registerLocationListener(myListener);
        // 设置
        mLocationClient.setLocOption(option);
        mLocationClient.requestLocation();

    }

    /**
     * @Title: addMyLocationToMap
     * @Description:
     * @throws
     */
    private void addMyLocationToMap() {
        // 定位图层初始化
        myLocationOverlay = new LocationOverlay(mMapView);
        // 手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）
        locData.latitude = 39.945;
        locData.longitude = 116.404;
        locData.direction = 2.0f;
        myLocationOverlay.setData(locData);
        mMapView.getOverlays().add(myLocationOverlay);
        mMapView.refresh();
        mMapView.getController().animateTo(new GeoPoint((int) (locData.latitude * 1e6),
                (int) (locData.longitude * 1e6)));
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
        if (v == mLocationButton) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MainMapViewActivity.class);
            startActivity(intent);
            return;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putString(SogouEntryActivity.APP_RESULT_CONTENT_TAG, mContent);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        mMapView.destroy();
        if (mBMapMan != null) {
            mBMapMan.destroy();
            mBMapMan = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        if (mBMapMan != null) {
            mBMapMan.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        if (mBMapMan != null) {
            mBMapMan.start();
        }
        super.onResume();
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
                        startActivity(new
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * @ClassName: locationOverlay
     * @Description: 继承MyLocationOverlay重写dispatchTap实现点击处理
     * @author Honghui He
     */
    public class LocationOverlay extends MyLocationOverlay {

        public LocationOverlay(MapView mapView) {
            super(mapView);
        }

        @Override
        protected boolean dispatchTap() {
            // 处理点击事件,弹出泡泡
            mPopupText.setBackgroundResource(R.drawable.popup);
            mPopupText.setText("我的位置");
            mPopupOverlay.showPopup(BMapUtil.getBitmapFromView(mPopupText),
                    new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)),
                    8);
            Log.d("", "### click my location");
            return true;
        }

    }

    /**
     * @ClassName: MyLocationListener
     * @Description:
     * @author Honghui He
     */
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        /**
         * (非 Javadoc)
         * 
         * @Title: onReceiveLocation
         * @Description:
         * @param location
         * @see com.baidu.location.BDLocationListener#onReceiveLocation(com.baidu.location.BDLocation)
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }

            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            // 如果不显示定位精度圈，将accuracy赋值为0即可
            locData.accuracy = location.getRadius();
            // 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
            locData.direction = location.getDerect();
            // 更新定位数据
            myLocationOverlay.setData(locData);
            // 更新图层数据执行刷新后生效
            mMapView.refresh();
            // 是手动触发请求或首次定位时，移动到定位点
            // 移动地图到定位点
            Log.d("LocationOverlay", "#### receive location, animate to it");
            mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6),
                    (int) (locData.longitude * 1e6)));
            myLocationOverlay.setLocationMode(LocationMode.NORMAL);

        }

        /**
         * (非 Javadoc)
         * 
         * @Title: onReceivePoi
         * @Description:
         * @param poiLocation
         * @see com.baidu.location.BDLocationListener#onReceivePoi(com.baidu.location.BDLocation)
         */
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }
}

/**
 * 继承MapView重写onTouchEvent实现泡泡处理操作
 * 
 * @author hejin
 */
class MyLocationMapViewOverlay extends MapView {
    // 弹出泡泡图层，点击图标使用
    static PopupOverlay mPopupView = null;

    public MyLocationMapViewOverlay(Context context) {
        super(context);
    }

    public MyLocationMapViewOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLocationMapViewOverlay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onTouchEvent
     * @Description:
     * @param event
     * @return
     * @see com.baidu.mapapi.map.MapView#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!super.onTouchEvent(event)) {
            // 消隐泡泡
            if (mPopupView != null && event.getAction() == MotionEvent.ACTION_UP)
                mPopupView.hidePop();
        }
        return true;
    }
}
