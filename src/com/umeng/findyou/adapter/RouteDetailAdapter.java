
package com.umeng.findyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.findyou.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Copyright: Umeng.com, Ltd. Copyright 2011-2015, All rights reserved
 * @Title: RouteDetailAdapter.java
 * @Package com.umeng.findyou.adapter
 * @Description:
 * @author Honghui He
 * @version V1.0
 */

public class RouteDetailAdapter extends BaseAdapter {

    /**
     * 
     */
    private Context mContext = null;
    /**
     * 
     */
    private List<String> mRouteSteps = new ArrayList<String>();

    /**
     * 
     */
    private ViewHolder mViewHolder = new ViewHolder();

    /**
     * 
     */
    public RouteDetailAdapter(Context context, List<String> data) {
        mContext = context;
        if (data != null) {
            mRouteSteps.addAll(data);
        }
    }

    @Override
    public int getCount() {
        return mRouteSteps.size();
    }

    @Override
    public Object getItem(int position) {
        return mRouteSteps.get(position);
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
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.route_img);
            mViewHolder.mTextView = (TextView) convertView.findViewById(R.id.route_description);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        String text = mRouteSteps.get(position);
        mViewHolder.mTextView.setText(text);
        if (position == 0 || position == (getCount() - 1)) {
            mViewHolder.mImageView.setImageResource(R.drawable.bullet_green);
        }
        return convertView;
    }

    /**
     * @ClassName: ViewHolder
     * @Description:
     * @author Honghui He
     */
    private static class ViewHolder {
        protected ImageView mImageView = null;
        protected TextView mTextView = null;
    }
}
