
package com.umeng.findyou.beans;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: FriendOverlay.java
 * @Package com.umeng.findyou.beans
 * @Description:
 * @author Honghui He
 * @version V1.0
 */
/*
 * 要处理overlay点击事件时需要继承ItemizedOverlay 不处理点击事件时可直接生成ItemizedOverlay.
 */
public class FriendOverlay extends ItemizedOverlay<OverlayItem> {

    private OnOverlayTapListener mListener = null;

    /**
     * @Title: FriendOverlay
     * @Description: FriendOverlay Constructor 用MapView构造ItemizedOverlay
     * @param mark
     * @param mapView
     */
    public FriendOverlay(Drawable mark, MapView mapView) {
        super(mark, mapView);
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onTap
     * @Description:
     * @param index
     * @return
     * @see com.baidu.mapapi.map.ItemizedOverlay#onTap(int)
     */
    protected boolean onTap(int index) {
        // 在此处理item点击事件
        Log.d("", "#### item onTap: " + index);
        mListener.onTap(index);
        return true;
    }

    public boolean onTap(GeoPoint pt, MapView mapView) {
        // 在此处理MapView的点击事件，当返回 true时
        super.onTap(pt, mapView);
        return false;
    }

    /**
     * @Title: setOnTapListener
     * @Description:
     * @param listener
     * @throws
     */
    public void setOnTapListener(OnOverlayTapListener listener) {
        mListener = listener;
    }

    /**
     * @Title: setOnTapListener
     * @Description:
     * @param listener
     * @throws
     */
    public OnOverlayTapListener getOnTapListener() {
        return mListener;
    }

    /**
     * @ClassName: OnOverlayTapListener
     * @Description:
     * @author Honghui He
     */
    public interface OnOverlayTapListener {
        public void onTap(int index);
    }
}
