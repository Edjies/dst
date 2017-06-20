package com.haofeng.apps.dst.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.UserDataManager;
import com.haofeng.apps.dst.bean.Order;
import com.haofeng.apps.dst.bean.StateConst;
import com.haofeng.apps.dst.ui.BaseActivity;
import com.haofeng.apps.dst.ui.OrderDetailActivity;
import com.haofeng.apps.dst.ui.OrderPayActivity;
import com.haofeng.apps.dst.ui.fragment.ShortOrderListFragment;
import com.haofeng.apps.dst.utils.UDate;
import com.haofeng.apps.dst.utils.UNumber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * Created by WIN10 on 2017/6/12.
 */

public class ShortOrderAdapter extends BaseAdapter {
    private BaseActivity mContext;
    private ShortOrderListFragment mFragment;
    private ArrayList<Order> mDatas = new ArrayList<>();

    public ShortOrderAdapter(BaseActivity context, ShortOrderListFragment mFragment) {
        this.mContext = context;
        this.mFragment = mFragment;
    }

    public void updateData(ArrayList<Order> mDatas) {
        this.mDatas = mDatas;
        Collections.sort(this.mDatas, new Comparator<Order>() {
            @Override
            public int compare(Order lhs, Order rhs) {

                return rhs.m_order_time.compareTo(lhs.m_order_time);
            }
        });
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Order getItem(int position) {
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
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_short_order, null);
            viewHolder = new ItemViewHolder();
            viewHolder.mTvOrderState = (TextView) convertView.findViewById(R.id.tv_state);
            viewHolder.mIvType = (ImageView) convertView.findViewById(R.id.tv_type);
            viewHolder.mTvOrderNo = (TextView) convertView.findViewById(R.id.tv_order_no);
            viewHolder.mTvPickDate = (TextView) convertView.findViewById(R.id.tv_pick_date);
            viewHolder.mTvPickTime = (TextView) convertView.findViewById(R.id.tv_pick_time);
            viewHolder.mTvReturnDate = (TextView) convertView.findViewById(R.id.tv_return_date);
            viewHolder.mTvReturnTime = (TextView) convertView.findViewById(R.id.tv_return_time);
            viewHolder.mTvDuration = (TextView) convertView.findViewById(R.id.tv_rent_time);
            viewHolder.mTvDurationUnit = (TextView) convertView.findViewById(R.id.tv_time_unit);
            viewHolder.mLlRent = (LinearLayout) convertView.findViewById(R.id.ll_rent);
            viewHolder.mTvRent = (TextView) convertView.findViewById(R.id.tv_rent_amount);

            viewHolder.mRlExtra = (RelativeLayout) convertView.findViewById(R.id.rl_extra);
            viewHolder.mTvExtra1 = (TextView) convertView.findViewById(R.id.tv_extra_1);
            viewHolder.mTvExtra2 = (TextView) convertView.findViewById(R.id.tv_extra_2);

            viewHolder.mTvOrderDetail = (TextView) convertView.findViewById(R.id.tv_order_detail);
            viewHolder.mRlAmountExtra = (RelativeLayout) convertView.findViewById(R.id.rl_amount_extra);
            viewHolder.mTvAmountExtra1 = (TextView) convertView.findViewById(R.id.tv_amount_extra_1);
            viewHolder.mTvAmountExtra2 = (TextView) convertView.findViewById(R.id.tv_amount_extra_2);
            viewHolder.mTvOrderAction = (TextView) convertView.findViewById(R.id.tv_order_action);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ItemViewHolder) convertView.getTag();
        final Order item = getItem(position);

        // 订单状态
        viewHolder.mTvOrderState.setText(item.getOrderStateName());
        // 类型 租车类型:0，时租 1，日租
        if(StateConst.ORDER_TYPE_HOUR.equals(item.m_type)) {
            viewHolder.mIvType.setImageResource(R.drawable.shizu);
        }else {
            viewHolder.mIvType.setImageResource(R.drawable.rizu);
        }
        // 订单号
        viewHolder.mTvOrderNo.setText(item.m_order_no);
        // 日期 和 使用时间
        // 1. 待提车之前 提车取预计时间， 提车后提车取实际时间； 2. 还车之前取预计，还车后取实际
        String qc_datetime = getPickDateTime(item);
        String hc_datetime = getReturnDateTime(item);
        if(qc_datetime.length() > 16) {
            viewHolder.mTvPickDate.setText(qc_datetime.substring(5, 10));
            viewHolder.mTvPickTime.setText(UDate.getWeekName(qc_datetime, "yyyy-MM-dd HH:mm:ss") + " " + qc_datetime.substring(11, 16));
        }else {
            viewHolder.mTvPickDate.setText("");
            viewHolder.mTvPickTime.setText("");
        }

        if(hc_datetime.length() > 16) {
            viewHolder.mTvReturnDate.setText(hc_datetime.substring(5, 10));
            viewHolder.mTvReturnTime.setText(UDate.getWeekName(hc_datetime, "yyyy-MM-dd HH:mm:ss") + " " + hc_datetime.substring(11, 16));
        }else {
            viewHolder.mTvReturnDate.setText("");
            viewHolder.mTvReturnTime.setText("");
        }

        if(StateConst.ORDER_TYPE_HOUR.equals(item.m_type)) {// 1，时租 2，日租
            viewHolder.mTvDurationUnit.setText("小时");
            viewHolder.mTvDuration.setText(String.valueOf(UDate.getDiff(qc_datetime, hc_datetime, "yyyy-MM-dd HH:mm:ss") /  (1000 * 60 * 60L)));
        }else {
            viewHolder.mTvDurationUnit.setText("天");
            viewHolder.mTvDuration.setText(String.valueOf(UDate.getDiff(qc_datetime, hc_datetime, "yyyy-MM-dd HH:mm:ss") /  (1000 * 60 * 60 * 24L)));
        }
        // 预计租车费用
        if("0".equals(item.m_order_state) || "1".equals(item.m_order_state) || "2".equals(item.m_order_state) || "3".equals(item.m_order_state) || "6".equals(item.m_order_state)) {
            viewHolder.mTvRent.setText("¥" + item.m_rent_amount);
            viewHolder.mLlRent.setVisibility(View.VISIBLE);
        }else {
            viewHolder.mLlRent.setVisibility(View.INVISIBLE);
        }
        // extra信息
        viewHolder.mRlExtra.setVisibility(View.GONE);
        if("0".equals(item.m_order_state) || "1".equals(item.m_order_state) || "6".equals(item.m_order_state)) { // 押金未付， 待提车， 取消 隐藏
            // do nothing
        }
        else if(StateConst.ORDER_STATE_4.equals(item.m_order_state) || StateConst.ORDER_STATE_5.equals(item.m_order_state)) { // 已付款， 已完结显示已付的租金
            viewHolder.mRlExtra.setVisibility(View.VISIBLE);
            viewHolder.mTvExtra1.setText("已付租金");
            viewHolder.mTvExtra2.setText("¥" + item.m_has_pay_amount);
            viewHolder.mTvExtra2.setTextColor(0xFFCB7A);
        }
        else if(StateConst.ORDER_STATE_3.equals(item.m_order_state) || (StateConst.ORDER_TYPE_HOUR.equals(item.m_type) && StateConst.ORDER_STATE_2.equals(item.m_order_state)) ){ // 已还车 或者 已提车的时租 显示产生的租金
            viewHolder.mRlExtra.setVisibility(View.VISIBLE);
            viewHolder.mTvExtra1.setText("已产生的租金");
            viewHolder.mTvExtra2.setText("¥" + item.m_rent_real_amount);
            viewHolder.mTvExtra2.setTextColor(0xFFFFCB7A);
        }else {  // 已提车的日租
            viewHolder.mRlExtra.setVisibility(View.VISIBLE);
            viewHolder.mTvExtra1.setText("距离下次结算时间");
            viewHolder.mTvExtra2.setText("");
            viewHolder.mTvExtra2.setTextColor(0xFFCB7A);
        }

        // 底部行
        viewHolder.mRlAmountExtra.setVisibility(View.GONE);
        viewHolder.mTvOrderAction.setVisibility(View.GONE);
        // 已付款和已完结，已取消
        if(StateConst.ORDER_STATE_4.equals(item.m_order_state) || StateConst.ORDER_STATE_5.equals(item.m_order_state) || StateConst.ORDER_STATE_6.equals(item.m_order_state)) {
            viewHolder.mRlAmountExtra.setVisibility(View.GONE);
            viewHolder.mTvOrderAction.setVisibility(View.GONE);
        }
        // 已还车为立即支付, 并显示待支付租金
        else if(StateConst.ORDER_STATE_3.equals(item.m_order_state)) {
            viewHolder.mRlAmountExtra.setVisibility(View.VISIBLE);
            viewHolder.mTvOrderAction.setVisibility(View.VISIBLE);
            viewHolder.mTvAmountExtra1.setText("待支付租金 : ");
            viewHolder.mTvAmountExtra2.setText(item.getPayAmount());
            viewHolder.mTvOrderAction.setText("立即支付");
            viewHolder.mTvOrderAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderPayActivity.intentMe(mContext, item);
                }
            });
        }
        // 已提车
        else if(StateConst.ORDER_STATE_2.equals(item.m_order_state)) {
            // 时租的情况：如果 确认费用的状态为 0，则隐藏按钮， 否则显示按钮
            if(StateConst.ORDER_TYPE_HOUR.equals(item.m_type)) {
                if(StateConst.ORDER_AMOUNT_CONFIRM_0.equals(item.m_amount_confirm)) {
                    viewHolder.mTvOrderAction.setVisibility(View.GONE);
                }else {
                    viewHolder.mTvOrderAction.setVisibility(View.VISIBLE);
                    viewHolder.mTvOrderAction.setText("确认费用");
                    viewHolder.mTvOrderAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OrderDetailActivity.intentMe(mContext, item.m_order_no);
                        }
                    });
                }
            }
            // 日租的情况
            else {
                if(StateConst.ORDER_AMOUNT_CONFIRM_0.equals(item.m_amount_confirm)) {
                    viewHolder.mRlAmountExtra.setVisibility(View.VISIBLE);
                    viewHolder.mTvOrderAction.setVisibility(View.VISIBLE);
                    viewHolder.mTvOrderAction.setText("立即支付");
                    viewHolder.mTvAmountExtra1.setText("待支付租金 : ");
                    viewHolder.mTvAmountExtra2.setText(item.getPayAmount());
                    viewHolder.mTvOrderAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OrderPayActivity.intentMe(mContext, item);
                        }
                    });
                }else {
                    viewHolder.mTvOrderAction.setVisibility(View.VISIBLE);
                    viewHolder.mTvOrderAction.setText("确认费用");
                    viewHolder.mTvOrderAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OrderDetailActivity.intentMe(mContext, item.m_order_no);
                        }
                    });
                }
            }


        }
        // 押金未未付
        else if(StateConst.ORDER_STATE_0.equals(item.m_order_state)) {
            viewHolder.mRlAmountExtra.setVisibility(View.VISIBLE);
            viewHolder.mTvOrderAction.setVisibility(View.VISIBLE);
            viewHolder.mTvAmountExtra1.setText("充值押金 : ");
            float carDeposit = UNumber.getFloat(item.m_foregift_amount, 0);
            float userAmount = UNumber.getFloat(UserDataManager.getInstance().getAccount().m_foregift_acount, 0);
            String amount = UNumber.formatFloat(Math.max(carDeposit - userAmount, 0), "0.00");
            viewHolder.mTvAmountExtra2.setText(amount);
            viewHolder.mTvOrderAction.setText("立即充值");
            viewHolder.mTvOrderAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderPayActivity.intentMe(mContext, item);
                }
            });
        }
        // 待提车
        else{
            viewHolder.mRlAmountExtra.setVisibility(View.GONE);
            viewHolder.mTvOrderAction.setVisibility(View.VISIBLE);
            viewHolder.mTvOrderAction.setText("取消订单");
            viewHolder.mTvOrderAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment.execCancelOrder(item.m_order_no);
                }
            });
        }




        viewHolder.mTvOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到订单详情
                OrderDetailActivity.intentMe(mContext, item.m_order_no);
            }
        });








        return convertView;
    }

    private class ItemViewHolder{
        TextView mTvOrderState;
        ImageView mIvType;
        TextView mTvOrderNo;
        TextView mTvPickDate;
        TextView mTvPickTime;
        TextView mTvReturnDate;
        TextView mTvReturnTime;
        TextView mTvDuration;
        TextView mTvDurationUnit;
        LinearLayout mLlRent;
        TextView mTvRent;

        RelativeLayout mRlExtra;
        TextView mTvExtra1;
        TextView mTvExtra2;

        TextView mTvOrderDetail;
        RelativeLayout mRlAmountExtra;
        TextView mTvAmountExtra1;
        TextView mTvAmountExtra2;
        TextView mTvOrderAction;

    }

    private String getPickDateTime(Order order) {
        if("0".equals(order.m_order_state) || "1".equals(order.m_order_state) || "6".equals(order.m_order_state)) {
            return order.m_qc_time;
        }else {
            return order.m_qc_real_time;
        }
    }

    private String getReturnDateTime(Order order) {
        if("3".equals(order.m_order_state) || "4".equals(order.m_order_state) || "5".equals(order.m_order_state)) {
            return order.m_hc_real_time;
        }else {
            return order.m_hc_time;
        }
    }



}
