
package com.umeng.findyou.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKLine;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.umeng.findyou.R;
import com.umeng.findyou.adapter.RouteDetailAdapter;
import com.umeng.findyou.adapter.RoutePlanAdapter;
import com.umeng.findyou.beans.FriendOverlay;
import com.umeng.findyou.beans.FriendOverlay.OnOverlayTapListener;
import com.umeng.findyou.beans.LocationEntity;
import com.umeng.findyou.beans.SearchConfig;
import com.umeng.findyou.beans.SearchConfig.SearchType;
import com.umeng.findyou.dialog.NavigationDialog;
import com.umeng.findyou.dialog.NavigationDialog.WhitchButton;
import com.umeng.findyou.dialog.RoutePlanDialog;
import com.umeng.findyou.shake.BaseSensor;
import com.umeng.findyou.shake.BaseSensor.OnSensorListener;
import com.umeng.findyou.shake.ShakeSensorImpl;
import com.umeng.findyou.sogouapi.SogouEntryActivity;
import com.umeng.findyou.utils.ClipboardUtil;
import com.umeng.findyou.utils.Constants;
import com.umeng.findyou.utils.SearchUtil;
import com.umeng.findyou.views.MyLocationMapView;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MainActivity
 * @Description: 应用名: 搜索输入法插件 - FindYou 概 述: 包含定位功能、路径导航、公交搜索、周边搜索。 描述:
 *               1、当用户进入程序，则自动定位到用户的位置。用户点击“发送”按钮则可以将描述我的位置的文字返回给输入法。
 *               2、例如用户的好友将自己的位置发给了本机用户， 本机用户拷贝好友的位置信息后，进入插件
 *               (从粘贴板里读取位置数据)，此时会在地图上显示好友的位置，
 *               点击好友位置图标就可以获取到好友的乘车路线，也可以将乘车信息返回给输入法。
 *               3、用戶可以查詢公交信息，輸入城市名、公交路线即可搜索到相关信息 . 例如611、10号线等。
 *               4、周边搜索，用户可以搜索周边的兴趣点， 例如ATM、医院、银行、景点等等。
 * @email bboyfeiyu@gmail.com
 * @author Honghui He
 */
public class MainActivity extends Activity {

    /**
     * 百度地图管理器
     */
    private BMapManager mBMapMan = null;

    /**
     * 地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
     * 如果不处理touch事件，则无需继承，直接使用MapView即可
     */
    private MyLocationMapView mMapView = null;
    /**
     * 地图控制器
     */
    private MapController mMapController = null;

    /**
     * 定位client
     */
    private LocationClient mLocClient;
    /**
     * 我的位置的数据
     */
    private LocationData mMyLocationData = null;
    /**
     * 定位监听器
     */
    public LocationListenner mLocationListenner = new LocationListenner();

    // 定位图层
    private LocationOverlay mMyLocationOverlay = null;

    /**
     * 我的位置
     */
    private GeoPoint mGeoPoint = new GeoPoint(0, 0);
    /**
     * 我的位置entity
     */
    private LocationEntity mMyLocationEntity = new LocationEntity();
    /**
     * 好友位置 entity
     */
    private LocationEntity mFriendEntity = new LocationEntity();
    /**
     * 是否是第一次获取我的位置
     */
    private boolean isFirstTime = false;
    /**
     * 摇一摇对象
     */
    private BaseSensor mShakeSensor = null;
    /**
     * 声明一个振动器对象
     */
    private Vibrator mVibrator = null;
    /**
     * 最上端的我的位置的布局
     */
    // private LinearLayout mMyLocationLayout = null;
    //
    // private TextView mLocationTextView = null;
    // private ProgressBar mProgressBar = null;
    // private Button mSendButton = null;

    private ImageButton mZoomOutBtn = null;
    private ImageButton mZoomInBtn = null;

    private ImageButton mLocButton = null;
    private ImageButton mBusButton = null;
    private ImageButton mPoiButton = null;

    /**
     * 最下面的几个按钮布局， 在获取到我的位置后再显示
     */
    private LinearLayout mButtonLayout = null;

    /**
     * 最上面的路线详情布局
     */
    private LinearLayout mRouteDetailLayout = null;
    private TextView mSummarylTextView = null;
    private TextView mDistacneTextView = null;
    private TextView mTimeTextView = null;
    private Button mDetailButton = null;
    private List<String> mRouteData = new ArrayList<String>();

    /**
     * Waitting Dialog
     */
    private ProgressDialog mWaittingDialog = null;
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
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mBMapMan = new BMapManager(getApplication());
        mBMapMan.init(Constants.BAIDU_MAP_KEY, null);

        setContentView(R.layout.main_activity);

        initViews();

        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.setAK(Constants.BAIDU_MAP_KEY);
        mMyLocationData = new LocationData();
        mLocClient.registerLocationListener(mLocationListenner);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mLocClient.requestLocation();

        checkClipboardText();

        // 定位图层初始化
        mMyLocationOverlay = new LocationOverlay(mMapView);
        mMyLocationOverlay.setMarker(getResources().getDrawable(
                R.drawable.location));
        // 更新定位数据
        mMyLocationOverlay.setData(mMyLocationData);
        // 添加定位图层
        mMapView.getOverlays().add(mMyLocationOverlay);
        mMyLocationOverlay.enableCompass();
        // 修改定位数据后刷新图层生效
        mMapView.refresh();

        // 初始化搜索
        SearchUtil.init(mBMapMan, mSearchListener);

        mWaittingDialog = new ProgressDialog(MainActivity.this);
        mWaittingDialog.setMessage("搜索中,请稍侯...");
        mWaittingDialog.show();
    }

    /**
     * @Title: initViews
     * @Description:
     * @return void
     * @throws
     */
    private void initViews() {

        // 地图初始化
        mMapView = (MyLocationMapView) findViewById(R.id.baidu_mapView);
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(18.0f);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(false);

        // mLocationTextView = (TextView) findViewById(R.id.location_addr_tv);
        //
        // mProgressBar = (ProgressBar) findViewById(R.id.locate_prgb);
        //
        // //
        // mSendButton = (Button) findViewById(R.id.send_btn);
        // mSendButton.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // 将内容发送到搜狗
        // sendMessageToSogou();
        // }
        // });

        mZoomOutBtn = (ImageButton) findViewById(R.id.zoom_out_btn);
        mZoomOutBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 确认缩放级别
                int zoomLevel = (int) mMapView.getZoomLevel();
                if (zoomLevel == mMapView.getMaxZoomLevel()) {
                    mZoomOutBtn.setEnabled(false);
                    Toast.makeText(MainActivity.this, "已经是最大缩放级别",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mMapController.zoomIn();
                mZoomInBtn.setEnabled(true);
            }
        });

        mZoomInBtn = (ImageButton) findViewById(R.id.zoom_in_btn);
        mZoomInBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int zoomLevel = (int) mMapView.getZoomLevel();
                if (zoomLevel == mMapView.getMinZoomLevel()) {
                    mZoomInBtn.setEnabled(false);
                    Toast.makeText(MainActivity.this, "已经是最小缩放级别",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mMapController.zoomOut();
                mZoomOutBtn.setEnabled(true);
            }
        });

        mLocButton = (ImageButton) findViewById(R.id.my_location_btn);
        mLocButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mLocClient.requestLocation();
                animToMyLocation();
            }
        });

        mBusButton = (ImageButton) findViewById(R.id.bus_btn);
        mBusButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 设置配置文件， 并且显示dialog
                SearchConfig config = new SearchConfig();
                config.setStartEntity(mMyLocationEntity);
                config.setSearchType(SearchType.BUS);
                showSearchDialog(config);
            }
        });

        mPoiButton = (ImageButton) findViewById(R.id.poi_btn);
        mPoiButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 设置配置文件， 并且显示dialog
                SearchConfig config = new SearchConfig();
                config.setStartEntity(mMyLocationEntity);
                config.setSearchType(SearchType.POI);
                showSearchDialog(config);
            }
        });

        // mMyLocationLayout = (LinearLayout)
        // findViewById(R.id.my_location_layout);

        mRouteDetailLayout = (LinearLayout) findViewById(R.id.route_detail_layout);
        mSummarylTextView = (TextView) findViewById(R.id.summary_tv);
        mDistacneTextView = (TextView) findViewById(R.id.distance_tv);
        mTimeTextView = (TextView) findViewById(R.id.time_tv);
        mDetailButton = (Button) findViewById(R.id.detail_btn);
        mDetailButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                RouteDetailAdapter adapter = new RouteDetailAdapter(MainActivity.this, mRouteData);

                final RoutePlanDialog routeDetailDialog = new RoutePlanDialog(MainActivity.this);
                routeDetailDialog.setListViewAdapter(adapter);
                routeDetailDialog.setOnSendButtonClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        routeDetailDialog.dismiss();
                        // 将mRouteData中的字符串格式化后返回给输入法
                        sendMessageToSogou("这是我的路线");
                    }
                });
                routeDetailDialog.setCanceledOnTouchOutside(true);
                routeDetailDialog.show();
            }
        });

        // 最下面的布局， 我的位置、公交查询等等
        mButtonLayout = (LinearLayout) findViewById(R.id.button_layout);
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
                Toast.makeText(getApplicationContext(), "定位到我的位置",
                        Toast.LENGTH_SHORT).show();
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
            mVibrator = (Vibrator) getApplication().getSystemService(
                    Service.VIBRATOR_SERVICE);
        }
        mVibrator.vibrate(new long[] {
                100, 10, 100, 60
        }, -1);

    }

    /**
     * @Title: getClipboardText
     * @Description: 获取剪切板中的内容
     * @throws
     */
    private void checkClipboardText() {
        // 获取剪切板中的地址
        String addr = ClipboardUtil.getContent(MainActivity.this);
        if (!TextUtils.isEmpty(addr) && addr.contains(Constants.ADDR_FLAG)) {
            Log.d(TAG, "### 粘贴板内容 : " + addr);
            GeoPoint geoPoint = SearchUtil.stringToGeoPoint(MainActivity.this,
                    addr);
            if (geoPoint != null) {
                mFriendEntity.setGeoPoint(geoPoint);
                mFriendEntity.setAddress(addr);
                addFriendToMap();
            }
        }
    }

    /**
     * @Title: addFriendToMap
     * @Description:
     * @throws
     */
    private void addFriendToMap() {
        Drawable mark = getResources().getDrawable(R.drawable.friend);

        // 用OverlayItem准备Overlay数据
        OverlayItem friendItem = new OverlayItem(mFriendEntity.getGeoPoint(),
                "", "");

        FriendOverlay friendOverlay = new FriendOverlay(mark, mMapView);
        friendOverlay.setOnTapListener(new OnOverlayTapListener() {

            @Override
            public void onTap(int index) {
                /**
                 * 导航配置
                 */
                SearchConfig navConfig = new SearchConfig();
                navConfig.setStartEntity(mMyLocationEntity);
                navConfig.setDestEntity(mFriendEntity);
                showSearchDialog(navConfig);
            }
        });
        friendOverlay.addItem(friendItem);

        mMapView.getOverlays().add(friendOverlay);
        mMapView.refresh();
    }

    /**
     * @Title: sendMessageToSogou
     * @Description: 将数据发送给搜狗输入法
     * @throws
     */
    // private void sendMessageToSogou() {
    // String addr = mLocationTextView.getText().toString().trim();
    // if (TextUtils.isEmpty(addr)) {
    // return;
    // }
    // Intent intent = new Intent();
    // Bundle bundle = new Bundle();
    // bundle.putString(SogouEntryActivity.APP_RESULT_CONTENT_TAG, addr);
    // intent.putExtras(bundle);
    // setResult(Activity.RESULT_OK, intent);
    // finish();
    // }

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
     * @Title: showSearchDialog
     * @Description:
     * @param config
     * @throws
     */
    private void showSearchDialog(SearchConfig config) {
        /**
         * 导航配置
         */
        SearchConfig navConfig = new SearchConfig();
        navConfig.setStartEntity(mMyLocationEntity);
        navConfig.setDestEntity(mFriendEntity);

        NavigationDialog dialog = new NavigationDialog(MainActivity.this,
                R.style.dialog_style);
        dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setConfig(config);
        dialog.setOnClickListener(new NavigationDialog.OnClickListener() {

            @Override
            public void onClick(WhitchButton button, SearchConfig config) {
                if (button == WhitchButton.OK) {
                    // 路线搜索
                    if (config.getSearchType() == SearchType.ROUTE) {
                        mWaittingDialog.show();
                        SearchUtil.routeSearch(MainActivity.this, config);
                    } else if (config.getSearchType() == SearchType.BUS) {
                        Log.d(TAG, "#### 公交路线搜索");
                        if (!TextUtils.isEmpty(config.getSearchEntity().getKeyWord())) {
                            mWaittingDialog.show();
                            SearchUtil.busSearch(MainActivity.this, config);
                        }

                    } else {
                        Log.d(TAG, "#### POI搜索");
                        if (!TextUtils.isEmpty(config.getSearchEntity().getKeyWord())) {
                            mWaittingDialog.show();
                            SearchUtil.poiSearch(MainActivity.this, config);
                        }
                    }
                }
            }
        });
        dialog.show();
    }

    /**
     * @Title: animToMyLocation
     * @Description: 移动到我的位置
     * @throws
     */
    private void animToMyLocation() {
        if (mMapController != null) {
            mMapController.animateTo(mGeoPoint);
        }
    }

    /**
     * @Title: showAddrDialog
     * @Description: 显示我的位置的dialog
     * @throws
     */
    private void showAddrDialog() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View addrView = inflater.inflate(R.layout.my_location_dialog, null);
        // 提示框
        final Dialog alertDialog = new Dialog(MainActivity.this,
                R.style.dialog_style);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        alertDialog.setContentView(addrView);
        alertDialog.show();

        // 文本编辑框
        final EditText editText = (EditText) addrView
                .findViewById(R.id.address_edit);
        editText.setText(mMyLocationEntity.getAddress());
        // 确定按钮
        Button okButton = (Button) addrView.findViewById(R.id.addr_ok_btn);
        okButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String content = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    sendMessageToSogou(content);
                } else {
                    Toast.makeText(MainActivity.this, "还没有获取到您的位置信息, 请重试...",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 取消
        Button cancelButton = (Button) addrView
                .findViewById(R.id.addr_cancel_btn);
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
        return addr.strAddr + "  " + Constants.ADDR_FLAG + " ("
                + mMyLocationData.latitude + "," + mMyLocationData.longitude
                + ")";
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

            mMyLocationData.latitude = location.getLatitude();
            mMyLocationData.longitude = location.getLongitude();
            // 如果不显示定位精度圈，将accuracy赋值为0即可
            mMyLocationData.accuracy = location.getRadius();
            // 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
            mMyLocationData.direction = location.getDerect();
            // 更新定位数据
            mMyLocationOverlay.setData(mMyLocationData);
            // 更新图层数据执行刷新后生效
            mMapView.refresh();

            // 计算位置
            int latitude = (int) (mMyLocationData.latitude * 1E6);
            int lontitude = (int) (mMyLocationData.longitude * 1E6);
            mGeoPoint.setLatitudeE6(latitude);
            mGeoPoint.setLongitudeE6(lontitude);
            // 第一次移动到我的位置
            if (!isFirstTime) {
                animToMyLocation();
                mWaittingDialog.dismiss();
                mButtonLayout.setVisibility(View.VISIBLE);
                mButtonLayout.startAnimation(AnimationUtils.loadAnimation(
                        MainActivity.this, R.anim.button_layout_enter_anim));

            }
            // 解析地址
            SearchUtil.locationToAddress(mGeoPoint);
            isFirstTime = true;
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
     * @Title: initRouteData
     * @Description:
     * @param route
     * @throws
     */
    private void setRouteData(String desc, MKRoute route) {
        // mMyLocationLayout.setVisibility(View.GONE);
        mRouteDetailLayout.setVisibility(View.VISIBLE);
        mRouteData.clear();
        for (int i = 0; i < route.getNumSteps(); i++) {
            String tip = route.getStep(i).getContent();
            Log.d(TAG, "### " + desc + " : " + tip);
            mRouteData.add(tip);
        }
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
            mWaittingDialog.dismiss();
            String myAddr = buildAddress(addr);
            mMyLocationEntity.setCity(addr.addressComponents.city);
            // 构建地址
            mMyLocationEntity.setAddress(myAddr);
            mMyLocationEntity.setGeoPoint(addr.geoPt);
            if (myAddr.contains(Constants.ADDR_FLAG)) {
                // mProgressBar.setVisibility(View.GONE);
                // mLocationTextView.setText(myAddr);
                // mSendButton.setVisibility(View.VISIBLE);
            }
        }

        /**
         * (非 Javadoc)
         * 
         * @Title: onGetDrivingRouteResult
         * @Description: 路线搜索， 驾车
         * @param arg0
         * @param arg1
         * @see com.baidu.mapapi.search.MKSearchListener#onGetDrivingRouteResult(com.baidu.mapapi.search.MKDrivingRouteResult,
         *      int)
         */
        @Override
        public void onGetDrivingRouteResult(MKDrivingRouteResult result,
                int error) {
            mWaittingDialog.dismiss();
            if (result == null || error != 0) {
                Log.d(TAG, "### 搜索失败 ");
                return;
            }
            Log.d(TAG, "#### 打车大约 " + result.getTaxiPrice() + " 元.");
            // TODO : 这里弹出一个路线选择Dialog, 使得用户可以选择路线
            // 显示路线
            RouteOverlay routeOverlay = new RouteOverlay(MainActivity.this,
                    mMapView);

            MKRoute route = result.getPlan(0).getRoute(0);
            routeOverlay.setData(route);
            mMapView.getOverlays().add(routeOverlay);
            mMapView.refresh();
            setRouteData("驾车", route);
            Log.d(TAG, "### 搜索结果 : " + result.toString());
        }

        /**
         * (非 Javadoc)
         * 
         * @Title: onGetWalkingRouteResult
         * @Description: 步行搜索
         * @param arg0
         * @param arg1
         * @see com.baidu.mapapi.search.MKSearchListener#onGetWalkingRouteResult(com.baidu.mapapi.search.MKWalkingRouteResult,
         *      int)
         */
        @Override
        public void onGetWalkingRouteResult(MKWalkingRouteResult result,
                int error) {
            mWaittingDialog.dismiss();
            // 起点或终点有歧义，需要选择具体的城市列表或地址列表
            if (error == MKEvent.ERROR_ROUTE_ADDR) {
                // 遍历所有地址
                // ArrayList<MKPoiInfo> stPois =
                // res.getAddrResult().mStartPoiList;
                // ArrayList<MKPoiInfo> enPois =
                // res.getAddrResult().mEndPoiList;
                // ArrayList<MKCityListInfo> stCities =
                // res.getAddrResult().mStartCityList;
                // ArrayList<MKCityListInfo> enCities =
                // res.getAddrResult().mEndCityList;
                return;
            }
            if (error != 0 || result == null) {
                Toast.makeText(MainActivity.this, "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            RouteOverlay routeOverlay = new RouteOverlay(MainActivity.this,
                    mMapView);
            MKRoute route = result.getPlan(0).getRoute(0);
            // 此处仅展示一个方案作为示例
            routeOverlay.setData(route);
            // 清除其他图层
            mMapView.getOverlays().clear();
            // 添加路线图层
            mMapView.getOverlays().add(routeOverlay);
            mMapView.getOverlays().add(mMyLocationOverlay);
            // 执行刷新使生效
            mMapView.refresh();
            // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
            mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
                    routeOverlay.getLonSpanE6());
            // 移动地图到起点
            mMapView.getController().animateTo(result.getStart().pt);

            setRouteData("步行", route);
            mWaittingDialog.dismiss();
        }

        /**
         * (非 Javadoc)
         * 
         * @Title: onGetTransitRouteResult
         * @Description: 路线搜索， 公交车
         * @param arg0
         * @param arg1
         * @see com.baidu.mapapi.search.MKSearchListener#onGetTransitRouteResult(com.baidu.mapapi.search.MKTransitRouteResult,
         *      int)
         */
        @Override
        public void onGetTransitRouteResult(final MKTransitRouteResult result,
                int error) {
            mWaittingDialog.dismiss();
            // // TODO : 这里弹出一个路线选择Dialog, 使得用户可以选择路线

            // 起点或终点有歧义，需要选择具体的城市列表或地址列表
            if (error == MKEvent.ERROR_ROUTE_ADDR) {
                Toast.makeText(MainActivity.this, "ERROR_ROUTE_ADDR",
                        Toast.LENGTH_SHORT).show();
                // 遍历所有地址
                // ArrayList<MKPoiInfo> stPois =
                // res.getAddrResult().mStartPoiList;
                // ArrayList<MKPoiInfo> enPois =
                // res.getAddrResult().mEndPoiList;
                // ArrayList<MKCityListInfo> stCities =
                // res.getAddrResult().mStartCityList;
                // ArrayList<MKCityListInfo> enCities =
                // res.getAddrResult().mEndCityList;
                return;
            }
            if (error != 0 || result == null) {
                Toast.makeText(MainActivity.this, "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            List<MKTransitRoutePlan> routePlans = new ArrayList<MKTransitRoutePlan>();
            // 将路线添加到routePlans中
            int totalRoutes = result.getNumPlan();
            for (int i = 0; i < totalRoutes; i++) {
                MKTransitRoutePlan routePlan = result.getPlan(i);
                routePlans.add(routePlan);
            }
            //
            RoutePlanAdapter routePlanAdapter = new RoutePlanAdapter(
                    MainActivity.this, routePlans);

            //
            final RoutePlanDialog routeDialog = new RoutePlanDialog(
                    MainActivity.this);
            routeDialog.setCanceledOnTouchOutside(true);
            // 设置listview适配器
            routeDialog.setListViewAdapter(routePlanAdapter);
            routeDialog.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> listview, View view,
                        int position, long arg3) {
                    // 路线层
                    TransitOverlay transitOverlay = new TransitOverlay(
                            MainActivity.this, mMapView);
                    MKTransitRoutePlan routePlan = result.getPlan(position);
                    Log.d(TAG, "### 距离: " + routePlan.getDistance() / 1000
                            + " 千米");
                    Log.d(TAG, "### 耗时: " + routePlan.getTime() / 60 + " 分钟");
                    Log.d(TAG, "#### 打车大约 " + result.getTaxiPrice() + " 元.");
                    Log.d(TAG, "### 描述: " + routePlan.getContent());
                    mSummarylTextView.setText("公交方案");
                    mDistacneTextView.append(routePlan.getDistance() / 1000 + " 公里");
                    mTimeTextView.append(routePlan.getTime() / 60 + " 分钟");
                    // 获取每一步的文字描述
                    int lineNum = routePlan.getNumLines();
                    mRouteData.clear();
                    // 获取路线描述
                    for (int i = 0; i < routePlan.getNumRoute(); i++) {
                        MKRoute route = routePlan.getRoute(i);
                        Log.d(TAG, "### 步行 : " + route.getTip());
                        mRouteData.add(route.getTip());
                        if (i < lineNum) {
                            MKLine line = routePlan.getLine(i);
                            Log.d(TAG, "### 乘车 : " + line.getTip());
                            mRouteData.add(line.getTip());
                        }
                    }

                    // 此处仅展示一个方案作为示例
                    transitOverlay.setData(routePlan);
                    // 清除其他图层
                    mMapView.getOverlays().clear();
                    // 添加路线图层
                    mMapView.getOverlays().add(transitOverlay);
                    mMapView.getOverlays().add(mMyLocationOverlay);
                    // 执行刷新使生效
                    mMapView.refresh();
                    // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                    mMapView.getController().zoomToSpan(
                            transitOverlay.getLatSpanE6(),
                            transitOverlay.getLonSpanE6());
                    // 移动地图到起点
                    mMapView.getController().animateTo(result.getStart().pt);
                    routeDialog.dismiss();
                    // mMyLocationLayout.setVisibility(View.GONE);
                    mRouteDetailLayout.setVisibility(View.VISIBLE);
                }

            });
            routeDialog.show();

            // // 路线层
            // TransitOverlay transitOverlay = new
            // TransitOverlay(MainActivity.this, mMapView);
            // MKTransitRoutePlan routePlan = result.getPlan(1);
            // Log.d(TAG, "### 距离: " + routePlan.getDistance() / 1000 + " 千米");
            // Log.d(TAG, "### 耗时: " + routePlan.getTime() / 60 + " 分钟");
            // Log.d(TAG, "#### 打车大约 " + result.getTaxiPrice() + " 元.");
            // Log.d(TAG, "### 描述: " + routePlan.getContent());
            //
            // int lineNum = routePlan.getNumLines();
            // // 获取路线描述
            // for (int i = 0; i < routePlan.getNumRoute(); i++) {
            // MKRoute route = routePlan.getRoute(i);
            // Log.d(TAG, "### 步行 : " + route.getTip());
            // if (i < lineNum) {
            // MKLine line = routePlan.getLine(i);
            // Log.d(TAG, "### 乘车 : " + line.getTip());
            // }
            // }
            //
            // // 此处仅展示一个方案作为示例
            // transitOverlay.setData(routePlan);
            // // 清除其他图层
            // mMapView.getOverlays().clear();
            // // 添加路线图层
            // mMapView.getOverlays().add(transitOverlay);
            // mMapView.getOverlays().add(mMyLocationOverlay);
            // // 执行刷新使生效
            // mMapView.refresh();
            // // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
            // mMapView.getController().zoomToSpan(transitOverlay.getLatSpanE6(),
            // transitOverlay.getLonSpanE6());
            // // 移动地图到起点
            // mMapView.getController().animateTo(result.getStart().pt);

        }

        /**
         * (非 Javadoc)
         * 
         * @Title: onGetPoiResult
         * @Description: POI搜索
         * @param res
         * @param type
         * @param error
         * @see com.baidu.mapapi.search.MKSearchListener#onGetPoiResult(com.baidu.mapapi.search.MKPoiResult,
         *      int, int)
         */
        @Override
        public void onGetPoiResult(MKPoiResult res, int type, int error) {
            mWaittingDialog.dismiss();
            // 错误号可参考MKEvent中的定义
            if (error == MKEvent.ERROR_RESULT_NOT_FOUND) {
                Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG)
                        .show();
                return;
            } else if (error != 0 || res == null) {
                Toast.makeText(MainActivity.this, "搜索出错啦..", Toast.LENGTH_LONG)
                        .show();
                return;
            }

            /**
             * 公交路线搜索, 找到公交路线poi node poi类型，0：普通点，1：公交站，2：公交线路，3：地铁站，4：地铁线路
             */
            MKPoiInfo curPoi = null;
            boolean isBuslineSearch = false;
            int totalPoiNum = res.getNumPois();
            for (int idx = 0; idx < totalPoiNum; idx++) {
                curPoi = res.getPoi(idx);
                if (curPoi != null
                        && (2 == curPoi.ePoiType || 4 == curPoi.ePoiType)) {
                    isBuslineSearch = true;
                    break;
                }
            }
            // 公交搜索
            if (isBuslineSearch) {
                Log.d(TAG, "### 原始 UID = " + curPoi.uid);
                SearchUtil.busLineSearch(curPoi.uid);
                return;
            }

            // 将poi结果显示到地图上
            PoiOverlay poiOverlay = new PoiOverlay(MainActivity.this, mMapView);
            poiOverlay.setData(res.getAllPoi());
            mMapView.getOverlays().clear();
            mMapView.getOverlays().add(mMyLocationOverlay);
            mMapView.getOverlays().add(poiOverlay);
            mMapView.refresh();
            // 当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
            for (MKPoiInfo info : res.getAllPoi()) {
                if (info.pt != null) {
                    mMapView.getController().animateTo(info.pt);
                    break;
                }
            }

            mMapController.setZoom(18.0f);
            animToMyLocation();
        }

        /**
         * (非 Javadoc)
         * 
         * @Title: onGetBusDetailResult
         * @Description: 公交车搜索, 获得公交线路以后找到里用户最近的位置， 并且提供导航。
         * @param arg0
         * @param arg1
         * @see com.baidu.mapapi.search.MKSearchListener#onGetBusDetailResult(com.baidu.mapapi.search.MKBusLineResult,
         *      int)
         */
        public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            mWaittingDialog.dismiss();
            if (iError != 0 || result == null) {
                Toast.makeText(MainActivity.this, "抱歉，该路线公交车未找到",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // 公交线路图层
            RouteOverlay routeOverlay = new RouteOverlay(MainActivity.this,
                    mMapView); // 此处仅展示一个方案作为示例
            MKRoute busRoute = result.getBusRoute();
            String name = result.getBusName();
            Log.d(TAG, "#### 公交车 : " + name);
            mSummarylTextView.setText(name);
            mDistacneTextView.setText("");
            mTimeTextView.setText("");

            // 设置数据
            routeOverlay.setData(busRoute);
            mMapView.getOverlays().clear();
            mMapView.getOverlays().add(routeOverlay);
            mMapView.getOverlays().add(mMyLocationOverlay);
            mMapView.refresh();
            mMapView.getController().animateTo(result.getBusRoute().getStart());

            // 获取所有的路线, 计算两点的距离 DistanceUtil.getDistance(arg0, arg1) ;
            ArrayList<ArrayList<GeoPoint>> allPoints = busRoute
                    .getArrayPoints();
            for (ArrayList<GeoPoint> subArrayList : allPoints) {
                for (GeoPoint geoPoint : subArrayList) {
                    Log.d(TAG, "##### point = " + geoPoint);
                }
            }

            busRoute.getTip();
            // 获取每一步的文字描述
            int totalStep = busRoute.getNumSteps();
            for (int i = 0; i < totalStep; i++) {
                String content = busRoute.getStep(i).getContent();
                Log.d(TAG, "#### bus step : "
                        + content);
            }
            setRouteData("公交查询", busRoute);
            mWaittingDialog.dismiss();
        }

        @Override
        public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {

        }

        @Override
        public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
                int arg2) {

        }

        @Override
        public void onGetPoiDetailSearchResult(int arg0, int arg1) {

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
        registerShake();
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
