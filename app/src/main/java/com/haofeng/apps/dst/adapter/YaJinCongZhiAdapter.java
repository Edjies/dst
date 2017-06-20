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
 * 押金充值Adapter
 */

public class YaJinCongZhiAdapter extends BaseAdapter {
    public List<Map<String, String>> yaJInCongZhiList;
    public Context context;

    public YaJinCongZhiAdapter(List<Map<String, String>> yaJInCongZhiList, Context context) {
        this.yaJInCongZhiList = yaJInCongZhiList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return yaJInCongZhiList.size();
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
            convertView = View.inflate(context, R.layout.view_yajin_congzhi, null);
            viewHolder = new ViewHolder();
            viewHolder.orderNumber = (TextView) convertView.findViewById(R.id.view_yajin_congzhi_number);  //订单号
            viewHolder.payDate = (TextView) convertView.findViewById(R.id.view_yajin_congzhi_date);//充值日期
            viewHolder.xiaofei = (TextView) convertView.findViewById(R.id.view_yajin_congzhi_xiaofei);//支付金额
            viewHolder.payWay = (TextView) convertView.findViewById(R.id.view_yajin_congzhi_car_pay_way);//支付方式
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orderNumber.setText(yaJInCongZhiList.get(position).get("order_no"));
        viewHolder.payDate.setText(yaJInCongZhiList.get(position).get("create_time"));
        viewHolder.xiaofei.setText("消费：" + yaJInCongZhiList.get(position).get("money"));
        if ("alipay".equals(yaJInCongZhiList.get(position).get("pay_way"))) {
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
