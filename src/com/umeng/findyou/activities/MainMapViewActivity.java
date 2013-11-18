
package com.umeng.findyou.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
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
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.umeng.findyou.R;
import com.umeng.findyou.sogouapi.SogouEntryActivity;
import com.umeng.findyou.utils.Constants;
import com.umeng.findyou.views.MyLocationMapView;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 */
public class MainMapViewActivity extends Activity implements OnClickListener {

    // 定位相关
    private LocationClient mLocClient;
    private LocationData locData = null;
    public MyLocationListenner myListener = new MyLocationListenner();

    // 定位图层
    locationOverlay myLocationOverlay = null;
    // 弹出泡泡图层
    private PopupOverlay pop = null;// 弹出泡泡图层，浏览节点时使用
    private TextView popupText = null;// 泡泡view
    private View viewCache = null;

    private BMapManager mBMapMan = null;
    // 地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    MyLocationMapView mMapView = null; // 地图View
    private MapController mMapController = null;
    private GeoPoint mLocPoint = new GeoPoint(0, 0);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBMapMan = new BMapManager(getApplication());
        mBMapMan.init(Constants.BAIDU_MAP_KEY, null);

        setContentView(R.layout.main_mapview_activity);

        // 地图初始化
        mMapView = (MyLocationMapView) findViewById(R.id.baidu_mapView);
        mMapView.setOnClickListener(this);
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(15);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);
        // 创建 弹出泡泡图层
        createPaopao();

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

        // 定位图层初始化
        myLocationOverlay = new locationOverlay(mMapView);
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
     * 创建弹出泡泡图层
     */
    public void createPaopao() {
        viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
        popupText = (TextView) viewCache.findViewById(R.id.textcache);

        // 泡泡点击响应回调
        PopupClickListener popListener = new PopupClickListener() {
            @Override
            public void onClickedPopup(int index) {
                sendMessageToSogou();
            }
        };

        pop = new PopupOverlay(mMapView, popListener);
        MyLocationMapView.pop = pop;
    }

    /**
     * @Title: sendMessageToSogou
     * @Description:
     * @throws
     */
    private void sendMessageToSogou() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(SogouEntryActivity.APP_RESULT_CONTENT_TAG, "北京市海淀区花园东路11号泰兴大厦");
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

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
            Log.d("LocationOverlay", "receive location, animate to it");
            // mMapController.animateTo(new GeoPoint((int) (locData.latitude *
            // 1e6),
            // (int) (locData.longitude * 1e6)));
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    PopupWindow popupWindow = null;
    private View contentView = null;

    /**
     * @Title: showPopupWindow
     * @Description:
     * @throws
     */
    private void showPopupWindow() {
        LayoutInflater inflater = getLayoutInflater();
        contentView = inflater.inflate(R.layout.popup_window, null);
        contentView.setOnClickListener(this);
        popupWindow = new PopupWindow(contentView,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);

        // 计算位置
        int latitude = (int) (locData.latitude * 1E6);
        int lontitude = (int) (locData.longitude * 1E6);
        mLocPoint.setLatitudeE6(latitude);
        mLocPoint.setLongitudeE6(lontitude);

        Projection projection = mMapView.getProjection();
        Point popupPoint = new Point();
        projection.toPixels(mLocPoint, popupPoint);

        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.LEFT | Gravity.TOP,
                popupPoint.x - contentView.getWidth() / 2,
                popupPoint.y - contentView.getHeight() / 2);
    }

    /**
     * @ClassName: locationOverlay
     * @Description: 继承MyLocationOverlay重写dispatchTap实现点击处理
     * @author Honghui He
     */
    public class locationOverlay extends MyLocationOverlay {

        public locationOverlay(MapView mapView) {
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
            showPopupWindow();
            //
            // // 处理点击事件,弹出泡泡
            // popupText.setBackgroundResource(R.drawable.popup);
            // popupText.setText("我的位置");
            // Bitmap clickView = BMapUtil.getBitmapFromView(popupText);
            // pop.showPopup(clickView,
            // new GeoPoint((int) (locData.latitude * 1e6), (int)
            // (locData.longitude * 1e6)),
            // 8);
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
        closePopupWindow();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * @Title: closePopupWindow
     * @Description:
     * @throws
     */
    private void closePopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
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
        closePopupWindow();
        if (v == contentView) {
            sendMessageToSogou();
        }
    }

}
