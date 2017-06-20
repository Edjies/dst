package com.haofeng.apps.dst.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

import java.util.List;
import java.util.Map;

/**
 * Created by WIN10 on 2017/3/29.
 */

public class ReturnYaJinReCordAdapter extends BaseAdapter {
    private List<Map<String, String>> recordList;
    private Context context;

    public ReturnYaJinReCordAdapter(List<Map<String, String>> recordList, Context context) {
        this.recordList = recordList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.listview_item_show_record, null);
            viewHolder =new ViewHolder();
            viewHolder.moneyTextView = (TextView) convertView.findViewById(R.id.activity_return_money);
            viewHolder.startTimeTextView = (TextView) convertView.findViewById(R.id.activity_return_start_time);
            viewHolder.returnMoneyLayout = (RelativeLayout) convertView.findViewById(R.id.activity_return_success_tuikuan_layout);
            viewHolder.payWayTextView = (TextView) convertView.findViewById(R.id.activity_return_pay_way);
            viewHolder.endTimeTextView = (TextView) convertView.findViewById(R.id.activity_return_end_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.moneyTextView.setText(recordList.get(position).get("amount"));
        viewHolder.startTimeTextView.setText(recordList.get(position).get("apply_time"));
        if (recordList.get(position).get("status").equals("0")) {
            viewHolder.returnMoneyLayout.setVisibility(View.GONE);
        } else {
            viewHolder.endTimeTextView.setText(recordList.get(position).get("confirm_time"));
        }
        if (recordList.get(position).get("refund_way").equals("alipay")) {
            //微信支付
            viewHolder.payWayTextView.setText("支付宝支付");
        } else {
            viewHolder.payWayTextView.setText("微信支付");
        }

        return convertView;
    }

    class ViewHolder {
        TextView moneyTextView;
        TextView startTimeTextView;
        RelativeLayout returnMoneyLayout;
        TextView payWayTextView;
        TextView endTimeTextView;
    }
}
