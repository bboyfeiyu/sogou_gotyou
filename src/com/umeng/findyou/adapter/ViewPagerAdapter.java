
package com.umeng.findyou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.umeng.findyou.R;
import com.umeng.findyou.activities.MainActivity;
import com.umeng.findyou.utils.Constants;

import java.util.List;

/**
 * @ClassName: ViewPagerAdapter
 * @Description: 引导页面适配器
 * @author Honghui He
 */
public class ViewPagerAdapter extends PagerAdapter {

    /**
     * 界面列表
     */
    private List<View> mViews;
    /**
     * 
     */
    private Activity mActivity;

    /**
     * @Title: ViewPagerAdapter
     * @Description: ViewPagerAdapter Constructor
     * @param views
     * @param activity
     */
    public ViewPagerAdapter(List<View> views, Activity activity) {
        this.mViews = views;
        this.mActivity = activity;
    }

    /**
     *  销毁arg1位置的界面(非 Javadoc)
     * @Title: destroyItem
     * @Description: 
     * 
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.View, int, java.lang.Object)
     */
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(mViews.get(arg1));
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    /**
     *  获得当前界面数(非 Javadoc)
     * @Title: getCount
     * @Description: 
     * 
     * 
     * @return
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        if (mViews != null) {
            return mViews.size();
        }
        return 0;
    }

    /**
     *  初始化arg1位置的界面(非 Javadoc)
     * @Title: instantiateItem
     * @Description: 
     * 
     * 
     * @param arg0
     * @param arg1
     * @return
     * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.View, int)
     */
    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(mViews.get(arg1), 0);
        if (arg1 == mViews.size() - 1) {
            ImageView mStartWeiboImageButton = (ImageView) arg0
                    .findViewById(R.id.iv_start_weibo);
            mStartWeiboImageButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 设置已经引导
                    setGuided();
                    goHome();

                }

            });
        }
        return mViews.get(arg1);
    }

    /**
     * @Title: goHome
     * @Description: 
     *
     *       
     * @throws
     */
    private void goHome() {
        // 跳转
        Intent intent = new Intent(mActivity, MainActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    /**
     * method desc：设置已经引导过了，下次启动不用再次引导
     */
    private void setGuided() {
        SharedPreferences preferences = mActivity.getSharedPreferences(
                Constants.SHARE_PREF, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        // 存入数据
        editor.putBoolean("isFirstIn", false);
        // 提交修改
        editor.commit();
    }

    /**
     *  判断是否由对象生成界面(非 Javadoc)
     * @Title: isViewFromObject
     * @Description: 
     * 
     * 
     * @param arg0
     * @param arg1
     * @return
     * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }

}
