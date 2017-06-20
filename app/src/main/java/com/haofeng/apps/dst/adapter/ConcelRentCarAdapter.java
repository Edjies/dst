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
 * 取消租车Adapter
 */

public class ConcelRentCarAdapter extends BaseAdapter {
    public List<Map<String, String>> concelRentCarList;
    public Context context;

    public ConcelRentCarAdapter(List<Map<String, String>> concelRentCarList, Context context) {
        this.concelRentCarList = concelRentCarList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return concelRentCarList.size();
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
            convertView = View.inflate(context, R.layout.view_concel_rentcar, null);
            viewHolder = new ViewHolder();
            viewHolder.orderNumber = (TextView) convertView.findViewById(R.id.view_concel_rent_car_number);  //订单号
            viewHolder.payDate = (TextView) convertView.findViewById(R.id.view_concel_rent_car_date);//取消日期
            viewHolder.payMoney = (TextView) convertView.findViewById(R.id.view_concel_rent_car_money);//支付金额
            viewHolder.payWay = (TextView) convertView.findViewById(R.id.view_concel_rent_car_pay_way);//支付金额
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orderNumber.setText(concelRentCarList.get(position).get("order_no"));
        viewHolder.payDate.setText(concelRentCarList.get(position).get("create_time"));
        viewHolder.payMoney.setText("退款金额：" + concelRentCarList.get(position).get("money"));
        if ("alipay".equals(concelRentCarList.get(position).get("pay_way"))) {
            viewHolder.payWay.setText("支付宝");
        } else {
            viewHolder.payWay.setText("微信");
        }
        return convertView;
    }

    class ViewHolder {
        TextView orderNumber;
        TextView payDate;
        TextView payMoney;
        TextView payWay;
    }
}
