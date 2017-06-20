package com.haofeng.apps.dst.ui.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.bean.CarDetail;
import com.haofeng.apps.dst.ui.BaseActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by WIN10 on 2017/6/13.
 */

public class CarInfoViewHolder {
    public final static int resId = R.layout.view_car_info;

    private BaseActivity mContext;
    private View mParent;
    private ImageView mIvIcon;
    private TextView mTvName;
    private TextView mTvTypeName;
    private TextView mTvEndurance;
    private TextView mTvCubage;
    private TextView mTvModelName;

    public CarInfoViewHolder(BaseActivity context, View parent) {
        this.mContext = context;
        this.mParent = parent;
        mIvIcon = (ImageView) parent.findViewById(R.id.iv_car_img);
        mTvName = (TextView) parent.findViewById(R.id.tv_brand_name);
        mTvTypeName = (TextView) parent.findViewById(R.id.tv_type_name);
        mTvEndurance = (TextView) parent.findViewById(R.id.tv_endurance);
        mTvCubage = (TextView) parent.findViewById(R.id.tv_cubage);
        mTvModelName = (TextView) parent.findViewById(R.id.tv_model_name);
    }

    public void updateData(CarDetail car) {

        String img_url = ServerManager.getInstance().getImageServer() + car.m_car_full_img;
        if(!TextUtils.isEmpty(img_url)) {
            Picasso.with(mContext).load(img_url).placeholder(R.drawable.no_pic2).into(mIvIcon);
        }
        mTvModelName.setText(car.m_car_model_name);
        mTvName.setText(car.m_brand_name);
        mTvTypeName.setText(car.m_car_type_name);
        mTvEndurance.setText("续航 : " + car.m_endurance_mileage + "km");
        mTvCubage.setText("货箱容积 : " + car.m_cubage + "m³");
    }



}
