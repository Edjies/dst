package com.haofeng.apps.dst.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

import java.util.List;
import java.util.Map;


/**
 * 充电订单Adapter
 */

public class AddPowerOrderAdapter extends BaseAdapter {
    public List<Map<String, String>> addPowerOrderList;
    public Context context;

    public AddPowerOrderAdapter(List<Map<String, String>> addPowerOrderList, Context context) {
        this.addPowerOrderList = addPowerOrderList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return addPowerOrderList.size();
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
            convertView = View.inflate(context, R.layout.view_add_power_order, null);
            viewHolder = new ViewHolder();
            viewHolder.orderNumber = (TextView) convertView.findViewById(R.id.view_add_power_order_number);
            viewHolder.status = (TextView) convertView.findViewById(R.id.view_add_power_order_status);
            viewHolder.startDate = (TextView) convertView.findViewById(R.id.view_add_power_order_start_date);
            viewHolder.endDate = (TextView) convertView.findViewById(R.id.view_add_power_order_end_date);
            viewHolder.money = (TextView) convertView.findViewById(R.id.view_add_power_order_money);
            viewHolder.location = (TextView) convertView.findViewById(R.id.view_add_power_order_location);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orderNumber.setText(addPowerOrderList.get(position).get("orderNumber"));
        viewHolder.status.setText(addPowerOrderList.get(position).get("statusText"));
        viewHolder.startDate.setText(addPowerOrderList.get(position).get("startTime"));
        viewHolder.endDate.setText(addPowerOrderList.get(position).get("endTime"));
        viewHolder.money.setText("消费金额：" + addPowerOrderList.get(position).get("payMoney"));
        viewHolder.location.setText(addPowerOrderList.get(position).get("stationName"));
        return convertView;
    }

    class ViewHolder {
        TextView orderNumber;
        TextView status;
        TextView startDate;
        TextView endDate;
        TextView money;
        TextView location;
    }
}
