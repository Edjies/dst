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
 * 电卡充值Adapter
 */

public class PowerCarCongZhiAdapter extends BaseAdapter {
    public List<Map<String, String>> powerCarCongZhiList;
    public Context context;

    public PowerCarCongZhiAdapter(List<Map<String, String>> powerCarCongZhiList, Context context) {
        this.powerCarCongZhiList = powerCarCongZhiList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return powerCarCongZhiList.size();
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
            convertView = View.inflate(context, R.layout.view_powercar_congzhi, null);
            viewHolder = new ViewHolder();
            viewHolder.orderNumber = (TextView) convertView.findViewById(R.id.view_power_congzhi_number);  //订单号
            viewHolder.payDate = (TextView) convertView.findViewById(R.id.view_power_congzhi_date);//充值日期
            viewHolder.xiaofei = (TextView) convertView.findViewById(R.id.view_power_congzhi_xiaofei);//支付金额
            viewHolder.payWay = (TextView) convertView.findViewById(R.id.view_power_congzhi_car_pay_way);//支付方式
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orderNumber.setText(powerCarCongZhiList.get(position).get("order_no"));
        viewHolder.payDate.setText(powerCarCongZhiList.get(position).get("create_time"));
        viewHolder.xiaofei.setText("消费：" + powerCarCongZhiList.get(position).get("money"));
        if ("alipay".equals(powerCarCongZhiList.get(position).get("pay_way"))) {
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
    }

}
