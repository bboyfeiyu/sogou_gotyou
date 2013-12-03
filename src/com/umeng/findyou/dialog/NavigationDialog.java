
package com.umeng.findyou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.umeng.findyou.R;
import com.umeng.findyou.beans.NavConfig;
import com.umeng.findyou.beans.NavConfig.Vehicle;

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
    private NavConfig mConfig = null;
    /**
     * 
     */
    private Button mNavButton = null;
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

        mNavButton = (Button) findViewById(R.id.nav_btn);
        mNavButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
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
        initAddress();
        super.show();
    }

    /**
     * @Title: initAddress
     * @Description:
     * @throws
     */
    private void initAddress() {
        if (mConfig != null) {
            String startAddr = mConfig.getStartEntity().getAddress();
            if (startAddr.contains("#")) {
                startAddr = startAddr.split("#")[0].trim();
            }
            String destAddr = mConfig.getDestEntity().getAddress();
            if (destAddr.contains("#")) {
                destAddr = destAddr.split("#")[0].trim();
            }
            mStartEditText.setText(startAddr);
            mDestEditText.setText(destAddr);
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
    public NavConfig getConfig() {
        return mConfig;
    }

    /**
     * 设置 mConfig
     * 
     * @param 对mConfig进行赋值
     */
    public void setConfig(NavConfig config) {
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
        public void onClick(WhitchButton button, NavConfig config);
    }

}
