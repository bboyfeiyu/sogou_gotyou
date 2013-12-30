
package com.umeng.findyou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.umeng.findyou.R;
import com.umeng.findyou.beans.LocationEntity;
import com.umeng.findyou.beans.SearchConfig;
import com.umeng.findyou.beans.SearchConfig.SearchType;
import com.umeng.findyou.beans.SearchConfig.Vehicle;
import com.umeng.findyou.utils.SharePrefUtil;

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

    private ImageButton mChangeButton = null;

    private LocationEntity mStartEntity = null;
    private LocationEntity mDestEntity = null;
    private boolean isChangeAddr = false;

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
        // setContentView(R.layout.navigation_dialog);
        setContentView(R.layout.search_dialog);

        mBusButton = (ImageButton) findViewById(R.id.bus_image_btn);
        mBusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setVehicle(Vehicle.BUS);
                setImageButton(Vehicle.BUS);
            }
        });

        mCarButton = (ImageButton) findViewById(R.id.car_image_btn);
        mCarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setVehicle(Vehicle.CAR);
                setImageButton(Vehicle.CAR);
            }
        });

        mWalkButton = (ImageButton) findViewById(R.id.walk_image_btn);
        mWalkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setVehicle(Vehicle.WALK);
                setImageButton(Vehicle.WALK);
            }
        });

        mSearchButton = (Button) findViewById(R.id.search_btn);
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

        mChangeButton = (ImageButton) findViewById(R.id.change_btn);
        mChangeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isChangeAddr = !isChangeAddr;
                initEditText();
            }
        });

        mStartEditText = (EditText) findViewById(R.id.start_edit);
        mDestEditText = (EditText) findViewById(R.id.dest_edit);

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
            mChangeButton.setVisibility(View.GONE);
            mCarButton.setVisibility(View.GONE);
            mWalkButton.setVisibility(View.GONE);
            mStartEditText.setHint("城市");
            mDestEditText.setHint("公交路线");
            String city = mConfig.getStartEntity().getCity();
            if (!TextUtils.isEmpty(city)) {
                mStartEditText.setText(city);
            } else {
                String cityCache = SharePrefUtil.getCity(getContext());
                mStartEditText.setText(city);
                if (!TextUtils.isEmpty(cityCache)) {
                    mConfig.getStartEntity().setCity(cityCache);
                }
            }
        } else if (type == SearchType.POI) { // 周报搜索
            mBusButton.setBackgroundResource(R.drawable.nearby);
            mCarButton.setVisibility(View.GONE);
            mWalkButton.setVisibility(View.GONE);
            mStartEditText.setVisibility(View.GONE);
            mChangeButton.setVisibility(View.GONE);
            mDestEditText.setHint("关键字");

        } else { // 路线搜索
            mCarButton.setVisibility(View.VISIBLE);
            mWalkButton.setVisibility(View.VISIBLE);
            mStartEditText.setVisibility(View.VISIBLE);
            mChangeButton.setVisibility(View.VISIBLE);
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
            mStartEntity = mConfig.getStartEntity();
            mDestEntity = mConfig.getDestEntity();
        }
        initEditText();
    }

    /**
     * @Title: initEditText
     * @Description: 初始化地址EditText
     * @throws
     */
    private void initEditText() {
        String startAddr = "";
        String destAddr = "";
        if (mStartEntity != null) {
            startAddr = mStartEntity.getAddress();
            if (startAddr.contains("#")) {
                startAddr = startAddr.split("#")[0].trim();
            }

        }
        if (mDestEntity != null) {
            destAddr = mDestEntity.getAddress();
            if (destAddr.contains("#")) {
                destAddr = destAddr.split("#")[0].trim();
            }

        }
        if (isChangeAddr) {
            mStartEditText.setText(destAddr);
            mDestEditText.setText(startAddr);
        } else {
            mStartEditText.setText(startAddr);
            mDestEditText.setText(destAddr);
        }
        mConfig.setChangeAddr(isChangeAddr);
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
            mBusButton.setBackgroundResource(R.drawable.bus_pressed);
            mCarButton.setBackgroundResource(R.drawable.taxi);
            mWalkButton.setBackgroundResource(R.drawable.walk);
        } else if (vehicle == Vehicle.CAR) {
            mBusButton.setBackgroundResource(R.drawable.bus);
            mCarButton.setBackgroundResource(R.drawable.taxi_pressed);
            mWalkButton.setBackgroundResource(R.drawable.walk);
        } else if (vehicle == Vehicle.WALK) {
            mBusButton.setBackgroundResource(R.drawable.bus);
            mCarButton.setBackgroundResource(R.drawable.taxi);
            mWalkButton.setBackgroundResource(R.drawable.walk_pressed);
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
