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
 * 违章扣款Adapter
 */

public class WeiZhangKouKuanAdapter extends BaseAdapter {
    public List<Map<String, String>> WeiZhangKouKuanList;
    public Context context;

    public WeiZhangKouKuanAdapter(List<Map<String, String>> WeiZhangKouKuanList, Context context) {
        this.WeiZhangKouKuanList = WeiZhangKouKuanList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return WeiZhangKouKuanList.size();
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
            convertView = View.inflate(context, R.layout.view_weizhang_koukuan, null);
            viewHolder = new ViewHolder();
            viewHolder.orderNumber = (TextView) convertView.findViewById(R.id.view_weizhang_koukuan_number);  //订单号
            viewHolder.payDate = (TextView) convertView.findViewById(R.id.view_weizhang_koukuan_date);//支付日期
            viewHolder.payMoney = (TextView) convertView.findViewById(R.id.view_weizhang_koukuan_money);//支付金额
            viewHolder.xiaofei = (TextView) convertView.findViewById(R.id.view_weizhang_koukuan_xiaofei);//消费
            viewHolder.explanation = (TextView) convertView.findViewById(R.id.view_weizhang_koukuan_explanation);//消费
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orderNumber.setText(WeiZhangKouKuanList.get(position).get("order_no"));
        viewHolder.payDate.setText(WeiZhangKouKuanList.get(position).get("create_time"));
        viewHolder.payMoney.setText("违章金额：" + WeiZhangKouKuanList.get(position).get("money"));
        viewHolder.xiaofei.setText("消费："+WeiZhangKouKuanList.get(position).get("money"));
        viewHolder.explanation.setText(WeiZhangKouKuanList.get(position).get("explanation"));
        return convertView;

    }

    class ViewHolder {
        TextView orderNumber;
        TextView payDate;
        TextView payMoney;
        TextView xiaofei;
        TextView explanation;
    }
}
