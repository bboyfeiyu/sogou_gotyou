
package com.umeng.findyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.umeng.findyou.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: RoutePlanAdapter.java
 * @Package com.umeng.findyou.adapter
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class RoutePlanAdapter extends BaseAdapter {

    /**
     * 
     */
    private Context mContext = null;
    /**
     * 
     */
    private List<MKTransitRoutePlan> mRoutePlans = new ArrayList<MKTransitRoutePlan>();

    /**
     * 
     */
    private ViewHolder mViewHolder = new ViewHolder();

    /**
     * 
     */
    public RoutePlanAdapter(Context context, List<MKTransitRoutePlan> data) {
        mContext = context;
        if (data != null) {
            mRoutePlans.addAll(data);
        }
    }

    @Override
    public int getCount() {
        return mRoutePlans.size();
    }

    @Override
    public Object getItem(int position) {
        return mRoutePlans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * (非 Javadoc)
     * 
     * @Title: getView
     * @Description:
     * @param position
     * @param convertView
     * @param parent
     * @return
     * @see android.widget.Adapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.route_item, null);
            mViewHolder.mTextView = (TextView) convertView.findViewById(R.id.route_description);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        String text = mRoutePlans.get(position).getContent();
        mViewHolder.mTextView.setText(text);
        return convertView;
    }

    /**
     * @ClassName: ViewHolder
     * @Description:
     * @author Honghui He
     */
    private static class ViewHolder {
        protected TextView mTextView = null;
    }

}
