
package com.umeng.findyou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.findyou.R;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: RoutePlanDialog.java
 * @Package com.umeng.findyou.dialog
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class RoutePlanDialog extends Dialog {

    /**
     * 
     */
    private ListView mListView = null;

    /**
     * @Title: RoutePlanDialog
     * @Description: RoutePlanDialog Constructor
     * @param context
     */
    public RoutePlanDialog(Context context) {
        this(context, R.style.dialog_style);
        initView();
    }

    /**
     * @Title: RoutePlanDialog
     * @Description: RoutePlanDialog Constructor
     * @param context
     * @param theme
     */
    public RoutePlanDialog(Context context, int theme) {
        super(context, theme);
        initView();
    }

    /**
     * @Title: RoutePlanDialog
     * @Description: RoutePlanDialog Constructor
     * @param context
     * @param cancelable
     * @param cancelListener
     */
    public RoutePlanDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    /**
     * @Title: initView
     * @Description: 初始化视图
     * @throws
     */
    private void initView() {
        setContentView(R.layout.routeplan_select_dialog);
        mListView = (ListView) findViewById(R.id.route_listview);
    }


    /**
     * @Title: setListViewAdapter
     * @Description:
     * @param adapter
     * @throws
     */
    public void setListViewAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    /**
     * @Title: setOnItemClickListener
     * @Description: 设置listView的点击事件
     * @throws
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListView.setOnItemClickListener(listener);
    }
}
