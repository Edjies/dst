package com.haofeng.apps.dst.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.bean.LongOrder;
import com.haofeng.apps.dst.ui.BaseActivity;
import com.haofeng.apps.dst.ui.LongOrderDetailActivity;
import com.haofeng.apps.dst.utils.UDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * Created by WIN10 on 2017/6/12.
 */

public class LongOrderAdapter extends BaseAdapter {
    private BaseActivity mContext;
    private ArrayList<LongOrder> mDatas = new ArrayList<>();

    public LongOrderAdapter(BaseActivity context) {
        this.mContext = context;
    }

    public void updateData(ArrayList<LongOrder> mDatas) {
        this.mDatas = mDatas;
        Collections.sort(this.mDatas, new Comparator<LongOrder>() {
            @Override
            public int compare(LongOrder lhs, LongOrder rhs) {

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
    public LongOrder getItem(int position) {
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
            convertView = mContext.getLayoutInflater().inflate(R.layout.item_long_order, null);
            viewHolder = new ItemViewHolder();
            viewHolder.mTvOrderNo = (TextView) convertView.findViewById(R.id.tv_order_no);
            viewHolder.mTvOrderTime = (TextView) convertView.findViewById(R.id.tv_order_time);
            viewHolder.mTvNumber = (TextView) convertView.findViewById(R.id.tv_number);
            viewHolder.mTvPickTime = (TextView) convertView.findViewById(R.id.tv_pick_time);
            viewHolder.mTvMessage = (TextView) convertView.findViewById(R.id.tv_message);
            viewHolder.mTvResult = (TextView) convertView.findViewById(R.id.tv_result);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ItemViewHolder) convertView.getTag();
        final LongOrder item = getItem(position);
        viewHolder.mTvOrderNo.setText("需求单号: " + item.m_apply_no);
        viewHolder.mTvOrderTime.setText(item.m_order_time);
        viewHolder.mTvNumber.setText(item.m_total_num + "台");
        viewHolder.mTvPickTime.setText(UDate.getFormatDate(item.m_es_take_car_time, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"));
        String status = item.m_status;
        // 响应状态，0等待响应，1已经响应指派了服务专员
        if("0".equals(status)) {
            viewHolder.mTvMessage.setVisibility(View.GONE);
            viewHolder.mTvResult.setText("等待响应...");
            viewHolder.mTvResult.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }else {
            viewHolder.mTvMessage.setVisibility(View.VISIBLE);
            viewHolder.mTvMessage.setText("地上铁服务专员：" + item.m_sales);
            viewHolder.mTvResult.setText(item.m_sales_mobile);
            viewHolder.mTvResult.setCompoundDrawablesWithIntrinsicBounds(R.drawable.changzuzhuanyuandianhua, 0, 0, 0);
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongOrderDetailActivity.intentMe(mContext, item.m_apply_id);
            }
        });


        return convertView;
    }

    private class ItemViewHolder{
        TextView mTvOrderNo;
        TextView mTvOrderTime;
        TextView mTvNumber;
        TextView mTvPickTime;
        TextView mTvMessage;
        TextView mTvResult;

    }



}
