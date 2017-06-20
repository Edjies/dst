package com.haofeng.apps.dst.adapter;

/**
 * Created by WIN10 on 2017/4/11.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 我的车辆Adapter
 */
public class MyCarAdapter extends BaseAdapter {
    private int type;
    private List<Map<String, String>> carDetailList;
    private Context mContext;
    private AlertDialog alertDialog;

    public MyCarAdapter(List<Map<String, String>> carDetailList, int type, Context context) {
        this.type = type;
        this.carDetailList = carDetailList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return carDetailList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (type == 0) {  //当前车辆
            View view = View.inflate(mContext, R.layout.list_item_current_car, null);
            TextView currentCarType = (TextView) view.findViewById(R.id.list_item_curent_car_type);
            TextView currentCarNumber = (TextView) view.findViewById(R.id.list_item_curent_car_number);
            ImageView currentCarImage = (ImageView) view.findViewById(R.id.list_item_curent_car_image);
            TextView currentCarStartDate = (TextView) view.findViewById(R.id.list_item_curent_car_start_date);
            currentCarType.setText(carDetailList.get(position).get("car_type"));
            currentCarNumber.setText(carDetailList.get(position).get("car_no"));
            currentCarStartDate.setText(carDetailList.get(position).get("start_time"));
            Picasso.with(mContext).load(carDetailList.get(position).get("img")).into(currentCarImage);
            return view;
        } else {   //历史车辆
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.list_item_history_car, null);
                viewHolder = new ViewHolder();
                viewHolder.historyCarType = (TextView) convertView.findViewById(R.id.list_item_history_car_type);
                viewHolder.historyCarNumber = (TextView) convertView.findViewById(R.id.list_item_history_car_number);
                viewHolder.historyStartCarDate = (TextView) convertView.findViewById(R.id.list_item_history_car_start_date);
                viewHolder.historyEndCarDate = (TextView) convertView.findViewById(R.id.list_item_history_car_end_date);
                viewHolder.historyCarImage = (ImageView) convertView.findViewById(R.id.list_item_history_car_image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.historyCarType.setText(carDetailList.get(position).get("car_type"));
            viewHolder.historyCarNumber.setText(carDetailList.get(position).get("car_no"));
            viewHolder.historyStartCarDate.setText(carDetailList.get(position).get("start_time"));
            viewHolder.historyEndCarDate.setText(carDetailList.get(position).get("end_time"));
            Picasso.with(mContext).load(carDetailList.get(position).get("img")).into(viewHolder.historyCarImage);
        }
        return convertView;
    }

    class ViewHolder {
        TextView historyCarType;
        TextView historyCarNumber;
        TextView historyStartCarDate;
        TextView historyEndCarDate;
        ImageView historyCarImage;
    }

    private HttpRequestCallBack callBack = new HttpRequestCallBack() {
        @Override
        public void back(int backId, final boolean isRequestSuccess, final boolean isString, String data, final JSONArray jsonArray) {
            if (isRequestSuccess) {
                if (!isString) {
                    try {
                        String backStr = jsonArray.getString(1);
                        JSONObject jsonObject = new JSONObject(backStr);
                        if (jsonObject.getString("errcode").equals("0")) {
                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(2);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(mContext, "取消成功", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            } else if (msg.what == 2) {
                Toast.makeText(mContext, "取消失败", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        }
    };
}
