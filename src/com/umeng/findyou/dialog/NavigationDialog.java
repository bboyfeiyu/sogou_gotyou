
package com.umeng.findyou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.umeng.findyou.R;
import com.umeng.findyou.beans.NavigationConfig;

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
    private NavigationConfig mConfig = new NavigationConfig();
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
    private ImageButton mBusToggleButton = null;
    /**
     * 
     */
    private ImageButton mCarToggleButton = null;
    /**
     * 
     */
    private ImageButton mWalkToggleButton = null;

    /**
     * 
     */
    private OnClickListener mClickListener = null;

    /**
     * @Title: NavgationDialog
     * @Description: NavgationDialog Constructor
     * @param context
     */
    public NavigationDialog(Context context) {
        super(context);
        initViews();
    }

    public NavigationDialog(Context context, int theme) {
        super(context, theme);
        initViews();
    }

    public NavigationDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initViews();
    }

    /**
     * @Title: initViews
     * @Description:
     * @throws
     */
    private void initViews() {
        setContentView(R.layout.navigation_dialog);

        mBusToggleButton = (ImageButton) findViewById(R.id.bus_toggle_btn);
        mBusToggleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        mCarToggleButton = (ImageButton) findViewById(R.id.car_toggle_btn);
        mCarToggleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        mWalkToggleButton = (ImageButton) findViewById(R.id.walk_toggle_btn);
        mWalkToggleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        mNavButton = (Button) findViewById(R.id.nav_btn);
        mNavButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(WhitchButton.OK);
                }
                dismiss();
            }
        });

        mCancelButton = (Button) findViewById(R.id.cancel_btn);
        mCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(WhitchButton.CANCEL);
                }
                dismiss();
            }
        });
    }

    /**
     * 获取 mConfig
     * 
     * @return 返回 mConfig
     */
    public NavigationConfig getConfig() {
        return mConfig;
    }

    /**
     * 设置 mConfig
     * 
     * @param 对mConfig进行赋值
     */
    public void setConfig(NavigationConfig config) {
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
        public void onClick(WhitchButton button);
    }

}
