
package com.umeng.findyou.shake;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: UMSensorBase.java
 * @Package com.example.shaketoshare
 * @Description: 传感器抽象基类 ,目前只支持加速度传感器. You must always include implementations
 *               of both the onAccuracyChanged() and onSensorChanged() callback
 *               methods. Also, be sure that you always unregister a sensor when
 *               an activity pauses. This prevents a sensor from continually
 *               sensing data and draining the battery.
 * @author Honghui He
 * @version V1.0
 */

public abstract class ShakeSensor implements SensorEventListener {

    /**
     * 用户传递进来的Activity
     */
    protected Activity mActivity = null;
    /**
     * 传感器管理器
     */
    protected SensorManager mSensorManager = null;
    /**
     * 目标传感器
     */
    protected Sensor mSensor = null;
    /**
     * 传感器监听器
     */
    protected OnSensorListener mSensorBaseListener = null;

    /**
     * 类的tag
     */
    protected final String TAG = this.getClass().getName();
    /**
     * 传感器是否启动
     */
    private boolean isStart = false;
    /**
     * 是否需要音效
     */
    protected boolean isNeedSound = true;

    /**
     * @Title: UMSensorBase
     * @Description: UMSensorBase Constructor
     * @param context
     */
    protected ShakeSensor(Activity activity) {
        mActivity = activity;
    }

    /**
     * @Title: register
     * @Description: 注册传感器，返回是否注册成功
     * @return boolean 注册是否成功的标识
     * @throws
     */
    public boolean register() {
        // 获得传感器管理器
        mSensorManager = (SensorManager) mActivity
                .getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            // 获得重力传感器
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        boolean result = false;
        // 注册传感器
        if (mSensor != null) {
            result = mSensorManager.registerListener(this, mSensor,
                    SensorManager.SENSOR_DELAY_GAME);
            isStart = true;
        } else {
            Log.d(TAG, "### 传感器初始化失败!");
        }
        return result;
    }

    /**
     * @Title: stop
     * @Description: 注销传感器，并且清理一些对象和状态
     * @throws
     */
    public void unregister() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
            isStart = false;
            mSensorBaseListener = null;
        }
    }

    /**
     * @Title: getSensor
     * @Description:获取传感器
     * @return
     * @throws
     */
    public Sensor getSensor() {
        return mSensor;
    }

    /**
     * @Title: setSensor
     * @Description: 设置传感器
     * @param sensor
     * @throws
     */
    public void setSensor(Sensor sensor) {
        this.mSensor = sensor;
    }

    /**
     * @Title: setOnShakeListener
     * @Description: 设置传感器监听器
     * @param listener 用户设置的传感器监听器
     * @throws
     */
    public void setSensorListener(OnSensorListener listener) {
        mSensorBaseListener = listener;
    }

    /**
     * @Title: getSensorBaseListener
     * @Description: 返回传感器监听器
     * @return
     * @throws
     */
    public OnSensorListener getSensorBaseListener() {
        return mSensorBaseListener;
    }

    /**
     * 获取 mActivity
     * 
     * @return 返回 mActivity
     */
    public Activity getParentActivity() {
        return mActivity;
    }

    /**
     * 设置 mActivity
     * 
     * @param 对mActivity进行赋值
     */
    public void setParentActivity(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 获取 isStart
     * 
     * @return 返回 isStart
     */
    public boolean isStart() {
        return isStart;
    }

    /**
     * 获取 isNeedSound
     * 
     * @return 返回 isNeedSound
     */
    public boolean isSoundEnable() {
        return isNeedSound;
    }

    /**
     * 设置 isNeedSound, 是否开启音效
     * 
     * @param 对isNeedSound进行赋值
     */
    public void setSoundEnable(boolean sound) {
        this.isNeedSound = sound;
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onSensorChanged
     * @Description:
     * @param event
     * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onAccuracyChanged
     * @Description:
     * @param sensor
     * @param accuracy
     * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor,
     *      int)
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged --> " + " accuracy: " + accuracy);
    }

    /**
     * @ClassName: OnSensorListener
     * @Description:
     * @author Honghui He
     */
    public interface OnSensorListener {
        public void onComplete();
    }

}
