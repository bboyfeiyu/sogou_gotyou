
package com.umeng.findyou.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.umeng.findyou.R;
import com.umeng.findyou.shake.ShakeSensor;
import com.umeng.findyou.shake.ShakeSensor.OnSensorListener;
import com.umeng.findyou.shake.ShakeSensorImpl;
import com.umeng.findyou.sogouapi.SogouEntryActivity;
import com.umeng.findyou.utils.ClipboardUtil;
import com.umeng.findyou.utils.Constants;
import com.umeng.findyou.utils.LocationUtil;
import com.umeng.findyou.views.MyLocationMapView;

/**
 * @ClassName: MainActivity
 * @Description:
 * @author Honghui He
 */
public class MainActivity extends Activity {

    private BMapManager mBMapMan = null;
    // 地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    private MyLocationMapView mMapView = null; // 地图View
    private MapController mMapController = null;

    // 定位相关
    private LocationClient mLocClient;
    private LocationData locData = null;
    public LocationListenner myListener = new LocationListenner();

    // 定位图层
    private LocationOverlay myLocationOverlay = null;

    private GeoPoint mGeoPoint = new GeoPoint(0, 0);
    private GeoPoint mFriendGeoPoint = null;
    private boolean isLocationOnitialized = false;
    // 摇一摇对象
    private ShakeSensor mShakeSensor = null;
    // 声明一个振动器对象
    private Vibrator mVibrator = null;
    private static final String TAG = MainActivity.class.getName();

    /**
     * (非 Javadoc)
     * 
     * @Title: onCreate
     * @Description:
     * @param savedInstanceState
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBMapMan = new BMapManager(getApplication());
        mBMapMan.init(Constants.BAIDU_MAP_KEY, null);

        setContentView(R.layout.main_mapview_activity);

        // 地图初始化
        mMapView = (MyLocationMapView) findViewById(R.id.baidu_mapView);
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(15);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);

        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.setAK(Constants.BAIDU_MAP_KEY);
        locData = new LocationData();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mLocClient.requestLocation();

        checkClipboardText();
        registerShake();

        // 定位图层初始化
        myLocationOverlay = new LocationOverlay(mMapView);
        myLocationOverlay.setMarker(getResources().getDrawable(R.drawable.location));
        // 设置定位数据
        myLocationOverlay.setData(locData);
        // 添加定位图层
        mMapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableCompass();
        // 修改定位数据后刷新图层生效
        mMapView.refresh();

    }

    /**
     * @Title: registerShake
     * @Description: 注册摇一摇定位功能
     * @throws
     */
    private void registerShake() {
        if (mShakeSensor == null) {
            mShakeSensor = new ShakeSensorImpl(MainActivity.this);
        } else {
            mShakeSensor.setParentActivity(MainActivity.this);
        }
        mShakeSensor.setSensorListener(new OnSensorListener() {

            @Override
            public void onComplete() {
                Toast.makeText(getApplicationContext(), "定位到我的位置", Toast.LENGTH_SHORT).show();
                vibrate();
                animToMyLocation();
            }
        });
        mShakeSensor.register();
    }

    /**
     * @Title: vibrate
     * @Description: 震动效果
     * @throws
     */
    private void vibrate() {
        if (mVibrator == null) {
            mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        }
        mVibrator.vibrate(new long[] {
                100, 30, 100, 80
        }, -1);

    }

    /**
     * @Title: getClipboardText
     * @Description: 获取剪切板中的内容
     * @throws
     */
    private void checkClipboardText() {
        // 获取剪切板中的地址
        String addr = ClipboardUtil.getContent(getApplicationContext());
        if (!TextUtils.isEmpty(addr) && addr.contains(Constants.ADDR_FLAG)) {
            Log.d(TAG, "### 粘贴板内容 : " + addr);
            GeoPoint geoPoint = LocationUtil.stringToGeoPoint(MainActivity.this, addr);
            if (geoPoint != null) {
                mFriendGeoPoint = geoPoint;
                Log.d(TAG, "#### my friend geopoint : " + mFriendGeoPoint.toString());
            }
        }
    }

    /**
     * @Title: sendMessageToSogou
     * @Description: 将数据发送给搜狗输入法
     * @throws
     */
    private void sendMessageToSogou(String addr) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(SogouEntryActivity.APP_RESULT_CONTENT_TAG, addr);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * @Title: animToMyLocation
     * @Description:
     * @throws
     */
    private void animToMyLocation() {
        if (mMapController != null) {
            mMapController.animateTo(mGeoPoint);
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class LocationListenner implements BDLocationListener {

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

            // 计算位置
            int latitude = (int) (locData.latitude * 1E6);
            int lontitude = (int) (locData.longitude * 1E6);
            mGeoPoint.setLatitudeE6(latitude);
            mGeoPoint.setLongitudeE6(lontitude);
            // 第一次移动到我的位置
            if (!isLocationOnitialized) {
                animToMyLocation();
            }
            // 解析地址
            LocationUtil.locationToAddress(mGeoPoint, mBMapMan, mSearchListener);
            isLocationOnitialized = true;
        }

        /**
         * (非 Javadoc)
         * 
         * @Title: onReceivePoi
         * @Description: POI搜索的结果
         * @param poiLocation
         * @see com.baidu.location.BDLocationListener#onReceivePoi(com.baidu.location.BDLocation)
         */
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    /**
     * @Title: showAddrDialog
     * @Description: 显示我的位置的dialog
     * @throws
     */
    private void showAddrDialog() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View addrView = inflater.inflate(R.layout.address_dialog, null);
        // 提示框
        final Dialog alertDialog = new Dialog(MainActivity.this, R.style.addr_dialog);
        alertDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        alertDialog.setContentView(addrView);
        alertDialog.show();

        // 文本编辑框
        final EditText editText = (EditText) addrView.findViewById(R.id.address_edit);
        editText.setText(Constants.ADDRESS);
        // 确定按钮
        Button okButton = (Button) addrView.findViewById(R.id.addr_ok_btn);
        okButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String content = editText.getText().toString().trim();
                sendMessageToSogou(content);
            }
        });

        // 取消
        Button cancelButton = (Button) addrView.findViewById(R.id.addr_cancel_btn);
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * @Title: buildAddress
     * @Description:
     * @return
     * @throws
     */
    private String buildAddress(MKAddrInfo addr) {
        return addr.strAddr
                + "  " + Constants.ADDR_FLAG + " (" + locData.latitude + "," + locData.longitude
                + ")";
    }

    /**
     * 搜索监听器
     */
    private MKSearchListener mSearchListener = new MKSearchListener() {

        /**
         * (非 Javadoc)
         * 
         * @Title: onGetAddrResult
         * @Description: 地址搜索结果
         * @param addr
         * @param code
         * @see com.baidu.mapapi.search.MKSearchListener#onGetAddrResult(com.baidu.mapapi.search.MKAddrInfo,
         *      int)
         */
        @Override
        public void onGetAddrResult(MKAddrInfo addr, int code) {
            // 构建地址
            Constants.ADDRESS = buildAddress(addr);
        }

        @Override
        public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {

        }

        @Override
        public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {

        }

        @Override
        public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {

        }

        @Override
        public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {

        }

        @Override
        public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {

        }

        @Override
        public void onGetPoiDetailSearchResult(int arg0, int arg1) {

        }

        @Override
        public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {

        }

        @Override
        public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {

        }
    };

    /**
     * @ClassName: locationOverlay
     * @Description: 继承MyLocationOverlay重写dispatchTap实现点击处理
     * @author Honghui He
     */
    public class LocationOverlay extends MyLocationOverlay {

        /**
         * @Title: LocationOverlay
         * @Description: LocationOverlay Constructor
         * @param mapView
         */
        public LocationOverlay(MapView mapView) {
            super(mapView);
        }

        /**
         * (非 Javadoc)
         * 
         * @Title: dispatchTap
         * @Description:
         * @return
         * @see com.baidu.mapapi.map.MyLocationOverlay#dispatchTap()
         */
        @Override
        protected boolean dispatchTap() {
            showAddrDialog();
            return true;
        }

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        mShakeSensor.unregister();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        if (mLocClient != null) {
            mLocClient.stop();
        }
        mMapView.destroy();
        super.onDestroy();
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onSaveInstanceState
     * @Description:
     * @param outState
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);

    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onRestoreInstanceState
     * @Description:
     * @param savedInstanceState
     * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onCreateOptionsMenu
     * @Description:
     * @param menu
     * @return
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
