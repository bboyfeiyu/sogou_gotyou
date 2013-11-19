
package com.umeng.findyou.shake;

/**   
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved 
 *
 * @Title: ShakeListener.java
 * @Package com.example.shaketoshare
 * @Description: 
 *        摇一摇的功能实现类， 需要用户注册onShareListener来事件回调 .  
 *        
 *    特别注意 : 
 *            用户在使用时需要手动调用start()方法来启动传感器检测，而更重要的是用户
 *        需要手动将传感器注销掉，必须调用stop()方法， 建议放在Activity的onPause()回调方法中，
 *        或者功能类似的方法，以避免传感器监听器对象泄漏.
 * @author Honghui He  
 * @version V1.0   
 */

import android.app.Activity;
import android.hardware.SensorEvent;
import android.util.Log;

/**
 * @ClassName: UMShakeSensor
 * @Description: 摇一摇传感器，内部原理为加速度传感器，摇一摇到达速度阀值则出发相应的动作，
 *               包括摇一摇截图后分享、摇一摇截图、摇一摇打开分享面板等。但所有的这样摇一摇后的动作都要通过
 *               UMSensorStrategy的子类策略来实现，只需将特定的策略设置给UMSensor即可。
 * @author Mr.Simple
 * @date Oct 5, 2013 2:51:37 PM
 */
public class ShakeSensorImpl extends BaseSensor {
    /**
     * 速度阈值，当摇晃速度达到这值后产生作用
     */
    private int mSpeedShreshold = DEFAULT_SHAKE_SPEED;
    /**
     * 传感器检测变化的时间间隔
     */
    private static final int UPTATE_INTERVAL_TIME = 100;
    /**
     * 两次有效摇晃的时间间隔
     */
    private int mShakeInterval = 1000;
    /**
     * 默认的摇一摇传感器阀值
     */
    public static final int DEFAULT_SHAKE_SPEED = 2000;

    /**
     * 手机上一个位置时重力感应坐标
     */
    private float mLastX = 0.0f;
    private float mLastY = 0.0f;
    private float mLastZ = 0.0f;
    /**
     * 上次检测时间
     */
    private long mLastUpdateTime;
    /**
     * 上次摇晃的有效时间
     */
    private long mLastShakeTime;

    /**
     * @Title: ShakeListener Constructor
     * @Description: UMShakeSensor Constructor
     * @param activity 用户所在的activity
     */
    public ShakeSensorImpl(Activity activity) {
        this(activity, ShakeSensorImpl.DEFAULT_SHAKE_SPEED);
    }

    /**
     * @Title: UMShakeSensor
     * @Description: UMShakeSensor Constructor
     * @param activity 用户所在的activity
     * @param speedShreshold 摇一摇的速度阀值，默认为2000
     */
    protected ShakeSensorImpl(Activity activity, int speedShreshold) {
        super(activity);
        mSpeedShreshold = speedShreshold;
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: onSensorChanged
     * @Description: 重力感应器感应获得变化数据
     * @param event
     * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
     */
    public void onSensorChanged(SensorEvent event) {
        // 现在检测时间
        long currentUpdateTime = System.currentTimeMillis();
        // 两次检测的时间间隔
        long timeInterval = currentUpdateTime - mLastUpdateTime;
        if (timeInterval < UPTATE_INTERVAL_TIME) {
            return;
        }
        // 现在的时间变成last时间
        mLastUpdateTime = currentUpdateTime;

        // 获得x,y,z坐标
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // 获得x,y,z的变化值
        float deltaX = x - mLastX;
        float deltaY = y - mLastY;
        float deltaZ = z - mLastZ;

        // 将现在的坐标变成last坐标
        mLastX = x;
        mLastY = y;
        mLastZ = z;

        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                * deltaZ)
                / timeInterval * 10000;
        // 达到速度阀值，发出提示
        if (speed >= mSpeedShreshold) {
            // 1秒之内只能有效摇晃一次
            if (currentUpdateTime - mLastShakeTime <= mShakeInterval) {
                return;
            }
            // 摇一摇完成
            mSensorBaseListener.onComplete();
            mLastShakeTime = currentUpdateTime;
        }// end if
    } // end of onSensorChanged

    /**
     * 获取摇一摇的速度阀值 mSpeedShreshold
     * 
     * @return 返回 mSpeedShreshold
     */
    public int getSpeedShreshold() {
        return mSpeedShreshold;
    }

    /**
     * 设置摇一摇的速度阀值 mSpeedShreshold
     * 
     * @param 对mSpeedShreshold进行赋值
     */
    public void setSpeedShreshold(int speedShreshold) {
        if (speedShreshold < 0) {
            speedShreshold = 0;
            Log.e(TAG, "speedShreshold速度阀值不能小于0，自动重置为0.");
        }
        this.mSpeedShreshold = speedShreshold;
    }

    /**
     * 获取 mshakeinterval
     * 
     * @return 返回 mshakeinterval
     */
    public int getShakeinterval() {
        return mShakeInterval;
    }

    /**
     * @Title: setShakeinterval
     * @Description: 用户设置两次有效摇一摇的时间间隔
     * @param interval
     * @return void
     * @throws
     */
    public void setShakeinterval(int interval) {
        mShakeInterval = interval;
    }

}
