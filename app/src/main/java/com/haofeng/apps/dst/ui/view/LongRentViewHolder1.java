package com.haofeng.apps.dst.ui.view;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.bean.Car;
import com.haofeng.apps.dst.bean.CitySite;
import com.haofeng.apps.dst.bean.LongOrderParams;
import com.haofeng.apps.dst.ui.CitySiteActivity;
import com.haofeng.apps.dst.ui.LongRentActivity;
import com.haofeng.apps.dst.utils.UDate;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by WIN10 on 2017/6/18.
 */

public class LongRentViewHolder1 implements View.OnClickListener{
    private final int REQUESTCODE = 0x7410;
    private LongRentActivity mContext;
    private View mParent;

    private TextView mTvPickSite;
    private TextView mTvChangeSite;

    private LinearLayout mLlCars;
    private TextView mTvAddCar;

    private TextView mTvPickTime;

    private ArrayList<LongRentCarTypeViewHolder> mVhCars;

    private ArrayList<Car> mCars;
    private CitySite mCity = new CitySite();

    public Calendar mPickTime;

    public LongRentViewHolder1(LongRentActivity context, View parent) {
        this.mContext = context;
        this.mParent = parent;
        mTvPickSite = (TextView) mParent.findViewById(R.id.tv_pick_site);
        mTvChangeSite = (TextView) mParent.findViewById(R.id.tv_change_site);

        mLlCars = (LinearLayout) mParent.findViewById(R.id.ll_cars);
        mTvAddCar = (TextView) mParent.findViewById(R.id.tv_add_car);

        mTvPickTime = (TextView) mParent.findViewById(R.id.tv_pick_time);


        mVhCars = new ArrayList<>();
        addCarTypeView(false);
        mTvAddCar.setOnClickListener(this);
        mTvChangeSite.setOnClickListener(this);
        mTvPickTime.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.tv_add_car == id) {
            addCarTypeView(true);
        }

        else if(R.id.tv_change_site == id) {
//            Intent intent = new Intent(mContext, AddrListActivity.class);
//            mContext.startActivityForResult(intent, REQUESTCODE);
            CitySiteActivity.intentMe(mContext, REQUESTCODE);
        }

        else if(R.id.tv_pick_time == id) {
            new DateTimePopWindow2(mContext, mParent, mContext.mConfig, mContext.mSystime.mCalendar, new DateTimePopWindow2.Callback() {
                @Override
                public void onSelected(Calendar date) {
                    mPickTime = date;
                    mTvPickTime.setText(UDate.getFormatDate(date, "yyyy-MM-dd HH:mm:ss"));
                }
            }).show();
        }
    }



    private void addCarTypeView(boolean couldDelete) {
        View view = mContext.getLayoutInflater().inflate(R.layout.view_long_rent_car_demand, null);
        LongRentCarTypeViewHolder vh = new LongRentCarTypeViewHolder(mContext, view, couldDelete, new LongRentCarTypeViewHolder.OnDeleteListener() {
            @Override
            public void onDelete(LongRentCarTypeViewHolder vh) {
                deleteCarTypeView(vh);
            }
        });
        mVhCars.add(vh);
        mLlCars.addView(view);

        for(int i = 0; i < mVhCars.size(); i++) {
            mVhCars.get(i).setPosition(i + 1);
        }
    }

    private void deleteCarTypeView(LongRentCarTypeViewHolder vh) {
        mLlCars.removeView(vh.getView());
        mVhCars.remove(vh);
        for(int i = 0; i < mVhCars.size(); i++) {
            mVhCars.get(i).setPosition(i + 1);
        }

    }

    public void show() {
        mParent.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mParent.setVisibility(View.GONE);
    }

    public boolean checkParams() throws Exception{
        for(int i = 0; i < mVhCars.size(); i++) {
            if(!mVhCars.get(i).isParamsReady()) {
                throw new Exception("请完善车型需求信息");
            }
        }

        if(mPickTime == null) {
            throw  new Exception("请选择预计取车时间");
        }
        return true;
    }

    public LongOrderParams putOrderParams(LongOrderParams params) {
        params.m_city_id = mCity.m_region_id;
        params.m_es_take_time = UDate.getFormatDate(mPickTime, "yyyy-MM-dd HH:mm:ss");
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < mVhCars.size(); i++) {
            jsonArray.put(mVhCars.get(i).getParams());
        }
        params.m_car_models = jsonArray.toString();
        return params;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE && resultCode == mContext.RESULT_OK && data != null) {
            mCity = (CitySite) data.getSerializableExtra("city");
            mTvPickSite.setText(mCity.m_region_name);
            for(int i = 0; i < mVhCars.size(); i++) {
                mVhCars.get(i).setCityId(mCity.m_region_id);
            }
            LongRentCarTypeViewHolder.mCars = null; // 清除缓存车型数据
        }

    }


}
