package com.haofeng.apps.dst.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.bean.Car;
import com.haofeng.apps.dst.ui.BaseActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by WIN10 on 2017/6/12.
 */

public class CarListAdapter extends BaseAdapter {
    private BaseActivity mContext;
    private ArrayList<Car> mDatas = new ArrayList<>();

    public CarListAdapter(BaseActivity context) {
        this.mContext = context;
    }

    public void updateData(ArrayList<Car> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Car getItem(int position) {
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
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_car_info, null);
            viewHolder = new ItemViewHolder();
            viewHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.mIvIcon = (ImageView) convertView.findViewById(R.id.iv_car_img);
            viewHolder.mTvCarModelName = (TextView) convertView.findViewById(R.id.tv_model_name);
            viewHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_brand_name);
            viewHolder.mTvTypeName = (TextView) convertView.findViewById(R.id.tv_type_name);
            viewHolder.mTvEndurance = (TextView) convertView.findViewById(R.id.tv_endurance);
            viewHolder.mTvCubage = (TextView) convertView.findViewById(R.id.tv_cubage);
            viewHolder.mTvDayRent = (TextView) convertView.findViewById(R.id.tv_day_rent);
            viewHolder.mTvHourRent = (TextView) convertView.findViewById(R.id.tv_hour_rent);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ItemViewHolder) convertView.getTag();

        Car item = mDatas.get(position);
        String img_url = ServerManager.getInstance().getImageServer() + item.mCarFullImg;
        if(!TextUtils.isEmpty(img_url)) {
            Picasso.with(mContext).load(img_url).placeholder(R.drawable.no_pic2).into(viewHolder.mIvIcon);
        }
        viewHolder.mTvName.setText(item.mBrandName);
        viewHolder.mTvTypeName.setText(item.mCarTypeName);
        viewHolder.mTvCarModelName.setText(item.mCarModelName);
        viewHolder.mTvEndurance.setText("续航 : " + item.mEnduranceMileage + "km");
        viewHolder.mTvCubage.setText("货箱容积 : " + item.mCubage + "m³");
        viewHolder.mTvHourRent.setText("¥ " + item.mTimePrice);
        viewHolder.mTvDayRent.setText("¥ " + item.mDayPrice);
        return convertView;
    }

    private class ItemViewHolder{
        private ImageView mIvIcon;
        private TextView mTvName;
        private TextView mTvTypeName;
        private TextView mTvEndurance;
        private TextView mTvCubage;
        private TextView mTvHourRent;
        private TextView mTvDayRent;
        private TextView mTvCarModelName;

    }
}
