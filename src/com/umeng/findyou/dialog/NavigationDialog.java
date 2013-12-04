
package com.umeng.findyou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.umeng.findyou.R;
import com.umeng.findyou.beans.LocationEntity;
import com.umeng.findyou.beans.SearchConfig;
import com.umeng.findyou.beans.SearchConfig.SearchType;
import com.umeng.findyou.beans.SearchConfig.Vehicle;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: NavgationDialog.java
 * @Package com.umeng.gotme.activities
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class NavigationDialog extends Dialog {

    /**
     * 配置
     */
    private SearchConfig mConfig = null;
    /**
     * 
     */
    private Button mSearchButton = null;
    /**
     * 
     */
    private Button mCancelButton = null;
    /**
     * 
     */
    private ImageButton mBusButton = null;
    /**
     * 
     */
    private ImageButton mCarButton = null;
    /**
     * 
     */
    private ImageButton mWalkButton = null;

    /**
     * 
     */
    private OnClickListener mClickListener = null;
    private EditText mStartEditText = null;
    private EditText mDestEditText = null;

    private LinearLayout mVehicleLayout = null;

    /**
     * @Title: NavgationDialog
     * @Description: NavgationDialog Constructor
     * @param context
     */
    public NavigationDialog(Context context) {
        super(context);
        initDialog();
    }

    public NavigationDialog(Context context, int theme) {
        super(context, theme);
        initDialog();
    }

    public NavigationDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog();
    }

    /**
     * @Title: initViews
     * @Description:
     * @throws
     */
    private void initDialog() {
        setContentView(R.layout.navigation_dialog);

        mBusButton = (ImageButton) findViewById(R.id.bus_toggle_btn);
        mBusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setVehicle(Vehicle.BUS);
                setImageButton(Vehicle.BUS);
            }
        });

        mCarButton = (ImageButton) findViewById(R.id.car_toggle_btn);
        mCarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setVehicle(Vehicle.CAR);
                setImageButton(Vehicle.CAR);
            }
        });

        mWalkButton = (ImageButton) findViewById(R.id.walk_toggle_btn);
        mWalkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setVehicle(Vehicle.WALK);
                setImageButton(Vehicle.WALK);
            }
        });

        mSearchButton = (Button) findViewById(R.id.nav_btn);
        mSearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    configChange();
                    mClickListener.onClick(WhitchButton.OK, mConfig);
                }
                dismiss();
            }
        });

        mCancelButton = (Button) findViewById(R.id.cancel_btn);
        mCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(WhitchButton.CANCEL, mConfig);
                }
                dismiss();
            }
        });

        mStartEditText = (EditText) findViewById(R.id.start_edit);
        mDestEditText = (EditText) findViewById(R.id.dest_edit);

        mVehicleLayout = (LinearLayout) findViewById(R.id.vechcle_layout);

    }

    /**
     * (非 Javadoc)
     * 
     * @Title: show
     * @Description:
     * @see android.app.Dialog#show()
     */
    @Override
    public void show() {
        initViews();
        super.show();
    }

    /**
     * @Title: initViews
     * @Description:
     * @throws
     */
    private void initViews() {
        SearchType type = mConfig.getSearchType();
        if (type == SearchType.BUS) { // 公交搜索
            mVehicleLayout.setVisibility(View.GONE);
            mStartEditText.setHint("城市");
            mDestEditText.setHint("公交路线");
            String city = mConfig.getStartEntity().getCity();
            if (!TextUtils.isEmpty(city)) {
                mStartEditText.setText(city);
            }

            layoutChange(type);
        } else if (type == SearchType.POI) { // 周报搜索
            mVehicleLayout.setVisibility(View.GONE);
            mStartEditText.setVisibility(View.GONE);
            mDestEditText.setHint("关键字");
            // 修改布局
            layoutChange(type);

        } else { // 路线搜索
            initAddress();
        }
    }

    /**
     * @Title: initAddress
     * @Description:
     * @throws
     */
    private void initAddress() {
        if (mConfig != null) {
            LocationEntity startEntity = mConfig.getStartEntity();
            if (startEntity != null) {
                String startAddr = startEntity.getAddress();
                if (startAddr.contains("#")) {
                    startAddr = startAddr.split("#")[0].trim();
                }

                mStartEditText.setText(startAddr);
            }

            LocationEntity destEntity = mConfig.getDestEntity();
            if (destEntity != null) {
                String destAddr = destEntity.getAddress();
                if (destAddr.contains("#")) {
                    destAddr = destAddr.split("#")[0].trim();
                }
                mDestEditText.setText(destAddr);
            }
        }
    }

    /**
     * @Title: configChange
     * @Description: 修改搜索配置
     * @throws
     */
    private void configChange() {
        if (mConfig.getSearchType() == SearchType.BUS) {
            String city = mStartEditText.getText().toString().trim();
            String line = mDestEditText.getText().toString().trim();
            mConfig.getSearchEntity().setCity(city);
            mConfig.getSearchEntity().setKeyWord(line);
        } else if (mConfig.getSearchType() == SearchType.POI) {
            String keyword = mDestEditText.getText().toString().trim();
            mConfig.getSearchEntity().setKeyWord(keyword);
        }
    }

    /**
     * @Title: layoutChange
     * @Description: 动态修改布局
     * @throws
     */
    private void layoutChange(SearchType type) {
        if (type == SearchType.BUS) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
                    android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(10, 30, 10, 10);
            params.width = LayoutParams.MATCH_PARENT;
            params.height = LayoutParams.WRAP_CONTENT;
            mStartEditText.setLayoutParams(params);

            RelativeLayout.LayoutParams destDarams = new RelativeLayout.LayoutParams(
                    android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
                    android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
            destDarams.setMargins(10, 10, 10, 10);
            destDarams.width = LayoutParams.MATCH_PARENT;
            destDarams.height = LayoutParams.WRAP_CONTENT;
            destDarams.addRule(RelativeLayout.BELOW, mStartEditText.getId());
            mDestEditText.setLayoutParams(destDarams);
        } else if (type == SearchType.POI) {
            // 设置参数
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
                    android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(10, 30, 10, 10);
            params.width = LayoutParams.MATCH_PARENT;
            params.height = LayoutParams.WRAP_CONTENT;
            mDestEditText.setLayoutParams(params);
        }
    }

    /**
     * @Title: setVehicle
     * @Description:
     * @param vehicle
     * @throws
     */
    private void setVehicle(Vehicle vehicle) {
        if (mConfig != null) {
            mConfig.setVehicle(vehicle);
        }
    }

    /**
     * @Title: setImageButton
     * @Description:
     * @throws
     */
    private void setImageButton(Vehicle vehicle) {
        if (vehicle == Vehicle.BUS) {
            mBusButton.setImageResource(R.drawable.bus_pressed);
            mCarButton.setImageResource(R.drawable.car_normal);
            mWalkButton.setImageResource(R.drawable.foot_normal);
        } else if (vehicle == Vehicle.CAR) {
            mBusButton.setImageResource(R.drawable.bus_normal);
            mCarButton.setImageResource(R.drawable.car_pressed);
            mWalkButton.setImageResource(R.drawable.foot_normal);
        } else if (vehicle == Vehicle.WALK) {
            mBusButton.setImageResource(R.drawable.bus_normal);
            mCarButton.setImageResource(R.drawable.car_normal);
            mWalkButton.setImageResource(R.drawable.foot_pressed);
        }
    }

    /**
     * 获取 mConfig
     * 
     * @return 返回 mConfig
     */
    public SearchConfig getConfig() {
        return mConfig;
    }

    /**
     * 设置 mConfig
     * 
     * @param 对mConfig进行赋值
     */
    public void setConfig(SearchConfig config) {
        this.mConfig = config;
    }

    /**
     * @Title: setOnClickListener
     * @Description: 设置按钮点击监听器
     * @param listener
     * @throws
     */
    public void setOnClickListener(OnClickListener listener) {
        mClickListener = listener;
    }

    /**
     * @Title: getOnClickListener
     * @Description:
     * @return
     * @throws
     */
    public OnClickListener getOnClickListener() {
        return mClickListener;
    }

    /**
     * @ClassName: WhitchButton
     * @Description:
     * @author Honghui He
     */
    public enum WhitchButton {
        CANCEL,
        OK
    }

    /**
     * @ClassName: OnClickListener
     * @Description:
     * @author Honghui He
     */
    public interface OnClickListener {
        /**
         * @Title: onClick
         * @Description:
         * @param button
         * @param config
         * @throws
         */
        public void onClick(WhitchButton button, SearchConfig config);
    }

}
