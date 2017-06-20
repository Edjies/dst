package com.haofeng.apps.dst.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListAdapter extends BaseAdapter {
    private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Context mContext;
    private String association_car_no;
    private AlertDialog alertDialog;
    private TextView carInfoView;

    public UserListAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.inflater = LayoutInflater.from(context);
        mContext = context;

    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        // 在此适配器中所代表的数据集中的条目数
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        // 获取数据集中与指定索引对应的数据项
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        // 获取在列表中与指定索引对应的行id
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        UserViewHolder viewHolder = null;
        if (arg1 == null) {
            arg1 = inflater.inflate(R.layout.listview_item_userlist, null);
            viewHolder = new UserViewHolder();
            viewHolder.titleView = (TextView) arg1
                    .findViewById(R.id.listview_item_userlist_title);

            viewHolder.inforView = (TextView) arg1
                    .findViewById(R.id.listview_item_userlist_infor);
            viewHolder.inforwithimageView = (TextView) arg1
                    .findViewById(R.id.listview_item_userlist_inforwithimage);
            viewHolder.imageView = (ImageView) arg1
                    .findViewById(R.id.listview_item_userlist_image);
            viewHolder.layout = (LinearLayout) arg1
                    .findViewById(R.id.listview_item_userlist_layout);
            arg1.setTag(viewHolder);

        } else {
            viewHolder = (UserViewHolder) arg1.getTag();
        }

        if (data.size() > 0) {
            // 这里根据实际情况，取值设值

            viewHolder.titleView.setText((CharSequence) data.get(arg0).get(
                    "title"));

            viewHolder.imageView.setImageResource((Integer) data.get(arg0).get(
                    "image"));
            viewHolder.titleView.setTextColor(mContext.getResources().getColor(
                    R.color.gray4));

            String infor = (String) data.get(arg0).get("infor");
            if ("-1".equals(infor)) {
                viewHolder.inforView.setVisibility(View.GONE);
            } else {
                if ("6".equals(infor)) {
                    viewHolder.inforView.setText(mContext.getResources()
                            .getString(R.string.kefu));
                    viewHolder.inforView.setTextColor(mContext.getResources()
                            .getColor(R.color.gray));

                } else if ("5".equals(infor)) {
                    viewHolder.inforView.setText("即将开启");
                    viewHolder.inforView.setTextColor(mContext.getResources()
                            .getColor(R.color.gray));
                    viewHolder.titleView.setTextColor(mContext.getResources()
                            .getColor(R.color.gray));
                } else if ("2".equals(infor)) {
                    viewHolder.inforView.setText("未通过");
                    viewHolder.inforView.setTextColor(mContext.getResources()
                            .getColor(R.color.gray));
                } else if ("0".equals(infor)) {
                    viewHolder.inforView.setText("审核中");
                    viewHolder.inforView.setTextColor(mContext.getResources()
                            .getColor(R.color.gray));
                } else if ("1".equals(infor)) {
                    viewHolder.inforView.setText("已通过");
                    viewHolder.inforView.setTextColor(mContext.getResources()
                            .getColor(R.color.gray));
                } else if ("3".equals(infor)) {
                    viewHolder.inforView.setText("未认证");
                    viewHolder.inforView.setTextColor(mContext.getResources()
                            .getColor(R.color.gray));
                } else if ("7".equals(infor)) {  //有车牌号
                    association_car_no = (String) data.get(arg0).get("association_car_no");
                    viewHolder.inforView.setText(association_car_no);
                    if (arg0 == 0) {
                        carInfoView = viewHolder.inforView;
                    }
                    viewHolder.inforView.setTextColor(mContext.getResources()
                            .getColor(R.color.gray));
                    viewHolder.inforView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("取消车辆关联")
                                    .setMessage("确定吗？")
                                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Map<String, String> map = new HashMap<>();
                                            map.put("act", Constent.ACT_GET_CONCEL_ASSOCIATE);
                                            map.put("car_no", association_car_no);
                                            AnsynHttpRequest.httpRequest(mContext, AnsynHttpRequest.GET, callBack, Constent.ID_ACT_GET_CONCEL_ASSOCIATE, map, false, true, true);
                                        }
                                    })
                                    .setNegativeButton("否", null);
                            alertDialog = builder.create();
                            alertDialog.show();

                        }
                    });
                }
                viewHolder.inforView.setVisibility(View.VISIBLE);

            }


        }

        String inforwithimage = (String) data.get(arg0).get(
                "inforwithimage");
        if ("-1".equals(inforwithimage)) {
            viewHolder.inforwithimageView.setVisibility(View.GONE);
        } else {

            viewHolder.inforwithimageView.setVisibility(View.VISIBLE);
            viewHolder.inforwithimageView.setText(inforwithimage);
        }
        return arg1;
    }

    private class UserViewHolder {

        public TextView titleView, inforView, inforwithimageView;
        public ImageView imageView;
        public LinearLayout layout;

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
                if (carInfoView != null) {
                    carInfoView.setVisibility(View.GONE);
                }

                alertDialog.dismiss();
            } else if (msg.what == 2) {
                Toast.makeText(mContext, "取消失败", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        }
    };
}

