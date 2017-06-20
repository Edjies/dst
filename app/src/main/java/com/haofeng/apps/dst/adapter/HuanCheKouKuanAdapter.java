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
 * 还车扣款Adapter
 */

public class HuanCheKouKuanAdapter extends BaseAdapter {
    public List<Map<String, String>> HuanCheKouKuanZhiList;
    public Context context;

    public HuanCheKouKuanAdapter(List<Map<String, String>> yaJInCongZhiList, Context context) {
        this.HuanCheKouKuanZhiList = yaJInCongZhiList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return HuanCheKouKuanZhiList.size();
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
            convertView = View.inflate(context, R.layout.view_huanche_koukuan, null);
            viewHolder = new ViewHolder();
            viewHolder.orderNumber = (TextView) convertView.findViewById(R.id.view_huancehe_koukuan_number);  //订单号
            viewHolder.payDate = (TextView) convertView.findViewById(R.id.view_huance_koukuan_date);//支付日期
            viewHolder.xiaofei = (TextView) convertView.findViewById(R.id.view_huance_koukuan_xiaofei);//消费
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orderNumber.setText(HuanCheKouKuanZhiList.get(position).get("order_no"));
        viewHolder.payDate.setText(HuanCheKouKuanZhiList.get(position).get("create_time"));
        viewHolder.xiaofei.setText("消费：" + HuanCheKouKuanZhiList.get(position).get("money"));
        return convertView;
    }

    class ViewHolder {
        TextView orderNumber;
        TextView payDate;
        TextView xiaofei;
    }
}
