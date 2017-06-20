package com.haofeng.apps.dst.ui.view;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.bean.Order;
import com.haofeng.apps.dst.bean.StateConst;
import com.haofeng.apps.dst.ui.BaseActivity;
import com.haofeng.apps.dst.utils.UDate;

/**
 * Created by WIN10 on 2017/6/14.
 */

public class RentTimeInfoViewHolder {
    public final static int resId = R.layout.view_rent_car_time;

    private BaseActivity mContext;
    private View mParent;

    private TextView mTvPickDate;
    private TextView mTvPickTime;
    private TextView mTvReturnDate;
    private TextView mTvReturnTime;
    private TextView mTvDuration;
    private TextView mTvDurationUnit;
    private LinearLayout mLlRent;
    private TextView mTvRent;

    public RentTimeInfoViewHolder(BaseActivity context, View parent) {
        this.mContext = context;
        this.mParent = parent;
        setViews();
    }

    private void setViews() {
         mTvPickDate = (TextView)  mParent.findViewById(R.id.tv_pick_date);
         mTvPickTime = (TextView)  mParent.findViewById(R.id.tv_pick_time);
         mTvReturnDate = (TextView)  mParent.findViewById(R.id.tv_return_date);
         mTvReturnTime = (TextView)  mParent.findViewById(R.id.tv_return_time);
         mTvDuration = (TextView)  mParent.findViewById(R.id.tv_rent_time);
         mTvDurationUnit = (TextView)  mParent.findViewById(R.id.tv_time_unit);
         mLlRent = (LinearLayout)  mParent.findViewById(R.id.ll_rent);
         mTvRent = (TextView)  mParent.findViewById(R.id.tv_rent_amount);
    }

    public void updateData(Order order) {
        // 1. 待提车之前 提车取预计时间， 提车后提车取实际时间； 2. 还车之前取预计，还车后取实际
        String qc_datetime = getPickDateTime(order);
        String hc_datetime = getReturnDateTime(order);
        if(qc_datetime.length() > 16) {
             mTvPickDate.setText(qc_datetime.substring(5, 10));
             mTvPickTime.setText(UDate.getWeekName(qc_datetime, "yyyy-MM-dd HH:mm:ss") + " " + qc_datetime.substring(11, 16));
        }else {
             mTvPickDate.setText("");
             mTvPickTime.setText("");
        }

        if(hc_datetime.length() > 16) {
             mTvReturnDate.setText(hc_datetime.substring(5, 10));
             mTvReturnTime.setText(UDate.getWeekName(hc_datetime, "yyyy-MM-dd HH:mm:ss") + " " + hc_datetime.substring(11, 16));
        }else {
             mTvReturnDate.setText("");
             mTvReturnTime.setText("");
        }

        if(StateConst.ORDER_TYPE_HOUR.equals(order.m_type)) {// 1，时租 2，日租
             mTvDurationUnit.setText("小时");
             mTvDuration.setText(String.valueOf(UDate.getDiff(qc_datetime, hc_datetime, "yyyy-MM-dd HH:mm:ss") /  (1000 * 60 * 60L)));
        }else {
             mTvDurationUnit.setText("天");
             mTvDuration.setText(String.valueOf(UDate.getDiff(qc_datetime, hc_datetime, "yyyy-MM-dd HH:mm:ss") /  (1000 * 60 * 60 * 24L)));
        }
        // 预计租车费用
        if(StateConst.ORDER_STATE_0.equals(order.m_order_state) || StateConst.ORDER_STATE_1.equals(order.m_order_state) || StateConst.ORDER_STATE_2.equals(order.m_order_state) || StateConst.ORDER_STATE_3.equals(order.m_order_state) || StateConst.ORDER_STATE_6.equals(order.m_order_state)) {
             mTvRent.setText("¥" + order.m_rent_amount);
             mLlRent.setVisibility(View.VISIBLE);
        }else {
             mLlRent.setVisibility(View.INVISIBLE);
        }
    }

    private String getPickDateTime(Order order) {
        if(StateConst.ORDER_STATE_0.equals(order.m_order_state) || StateConst.ORDER_STATE_1.equals(order.m_order_state) || StateConst.ORDER_STATE_6.equals(order.m_order_state)) {
            return order.m_qc_time;
        }else {
            return order.m_qc_real_time;
        }
    }

    private String getReturnDateTime(Order order) {
        if(StateConst.ORDER_STATE_3.equals(order.m_order_state) || StateConst.ORDER_STATE_4.equals(order.m_order_state) || StateConst.ORDER_STATE_5.equals(order.m_order_state)) {
            return order.m_hc_real_time;
        }else {
            return order.m_hc_time;
        }
    }
}
