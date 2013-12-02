
package com.umeng.findyou.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.umeng.findyou.R;
import com.umeng.findyou.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @{# GuideActivity.java Create on 2013-5-2 下午10:59:08 class desc: 引导界面
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @Author <a href="mailto:gaolei_xj@163.com">Leo</a>
 */
public class GuideActivity extends Activity implements OnPageChangeListener {

    private ViewPager mViewPager;
    private ViewPagerAdapter mVpAdapter;
    private List<View> mViews;

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    /**
     * (非 Javadoc)
     * 
     * @Title: onCreate
     * @Description:
     * @param savedInstanceState
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        // 初始化页面
        initViews();
        // 初始化底部小点
        initDots();
    }

    /**
     * @Title: initViews
     * @Description: 初始化视图
     * @throws
     */
    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        mViews = new ArrayList<View>();
        // 初始化引导图片列表, 四个页面
        mViews.add(inflater.inflate(R.layout.what_new_one, null));
        mViews.add(inflater.inflate(R.layout.what_new_two, null));
        mViews.add(inflater.inflate(R.layout.what_new_three, null));
        mViews.add(inflater.inflate(R.layout.what_new_four, null));

        // 初始化Adapter
        mVpAdapter = new ViewPagerAdapter(mViews, this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mVpAdapter);
        // 绑定回调
        mViewPager.setOnPageChangeListener(this);
    }

    /**
     * @Title: initDots
     * @Description: 初始化引导页面的点
     * @throws
     */
    private void initDots() {
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dots_layout);

        dots = new ImageView[mViews.size()];

        // 循环取得小点图片
        for (int i = 0; i < mViews.size(); i++) {
            dots[i] = (ImageView) dotLayout.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
        }

        currentIndex = 0;
        // 设置第一个为设置为白色，即选中状态
        dots[currentIndex].setEnabled(false);
    }

    /**
     * @Title: setCurrentDot
     * @Description: 设置当前点的位置
     * @param position
     * @throws
     */
    private void setCurrentDot(int position) {
        if (position < 0 || position > mViews.size() - 1
                || currentIndex == position) {
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = position;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int position) {
        // 设置底部小点选中状态
        setCurrentDot(position);
    }

}
