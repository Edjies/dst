package com.haofeng.apps.dst.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.bean.CitySite;
import com.haofeng.apps.dst.ui.BaseActivity;

import java.util.ArrayList;

/**
 * Created by WIN10 on 2017/6/12.
 */

public class CitySiteAdapter extends BaseAdapter {
    private BaseActivity mContext;
    private ArrayList<CitySite> mDatas = new ArrayList<>();

    public CitySiteAdapter(BaseActivity context) {
        this.mContext = context;
    }

    public void updateData(ArrayList<CitySite> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public CitySite getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder viewHolder;
        if(convertView == null) {
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_city_site, null);
            viewHolder = new ItemViewHolder();
            viewHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ItemViewHolder) convertView.getTag();

        CitySite item = mDatas.get(position);
        viewHolder.mTvName.setText(item.m_region_name);
        return convertView;
    }

    private class ItemViewHolder{
        TextView mTvName;
    }
}
