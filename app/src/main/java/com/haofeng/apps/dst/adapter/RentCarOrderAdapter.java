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
 * 租车订单Adapter
 */

public class RentCarOrderAdapter extends BaseAdapter {
    public List<Map<String, String>> RentCarOrderList;
    public Context context;

    public RentCarOrderAdapter(List<Map<String, String>> RentCarOrderList, Context context) {
        this.RentCarOrderList = RentCarOrderList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return RentCarOrderList.size();
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
            convertView = View.inflate(context, R.layout.view_rent_car_order, null);
            viewHolder = new ViewHolder();
            viewHolder.orderNumber = (TextView) convertView.findViewById(R.id.view_rent_car_order_number);  //订单号
            viewHolder.payDate = (TextView) convertView.findViewById(R.id.view_rentcar_order_date);//充值日期
            viewHolder.xiaofei = (TextView) convertView.findViewById(R.id.view_rentcar_order_xiaofei);//支付金额
            viewHolder.payWay = (TextView) convertView.findViewById(R.id.view_rentcar_order_pay_way);//支付方式
            viewHolder.explanation = (TextView) convertView.findViewById(R.id.view_rentcar_order_explanation);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orderNumber.setText(RentCarOrderList.get(position).get("order_no"));
        viewHolder.payDate.setText(RentCarOrderList.get(position).get("create_time"));
        viewHolder.xiaofei.setText("消费：" + RentCarOrderList.get(position).get("money"));
        viewHolder.explanation.setText(RentCarOrderList.get(position).get("explanation"));
        if ("alipay".equals(RentCarOrderList.get(position).get("pay_way"))) {
            viewHolder.payWay.setText("支付宝");
        } else {
            viewHolder.payWay.setText("微信");
        }
        return convertView;
    }
    class ViewHolder {
        TextView orderNumber;
        TextView payDate;
        TextView xiaofei;
        TextView payWay;
        TextView explanation;
    }
}
