
package com.umeng.findyou.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: MyLocationMapView.java
 * @Package com.umeng.findyou.views
 * @Description: 继承MapView重写onTouchEvent实现泡泡处理操作
 * @author Honghui He
 * @version V1.0
 */
public class MyLocationMapView extends MapView {
    public static PopupOverlay pop = null;// 弹出泡泡图层，点击图标使用
    public static int mLeft = 0;
    public static int mTop = 0;

    /**
     * @Title: MyLocationMapView
     * @Description: MyLocationMapView Constructor
     * @param context
     */
    public MyLocationMapView(Context context) {
        super(context);
    }

    public MyLocationMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLocationMapView(Context context, AttributeSet attrs, int defStyle) {
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
            mLeft = (int) event.getX();
            mTop = (int) event.getY();
            // 消隐泡泡
            if (pop != null && event.getAction() == MotionEvent.ACTION_UP) {
                pop.hidePop();
            }
        }
        return true;
    }
}
