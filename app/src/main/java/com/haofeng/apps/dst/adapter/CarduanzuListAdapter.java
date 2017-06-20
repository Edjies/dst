package com.haofeng.apps.dst.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.bean.Car;
import com.haofeng.apps.dst.httptools.Constent;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 租车页面的要显示的adapter
 *
 * @author WIN10
 */
public class CarduanzuListAdapter extends BaseAdapter {
    private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
    private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    private ArrayList<Car> mNewData = new ArrayList<>();
    private Context mContext;

    public CarduanzuListAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.inflater = LayoutInflater.from(context);
        mContext = context;
    }

    public interface Callback {
        public void click(View v);
    }

    ;

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    public void updateData(ArrayList<Car> datas) {
        this.mNewData = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // 在此适配器中所代表的数据集中的条目数
        return mNewData.size();
    }

    @Override
    public Car getItem(int position) {
        // 获取数据集中与指定索引对应的数据项
        return mNewData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // 获取在列表中与指定索引对应的行id
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CarduanzuViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item_carduanzu, null);
            viewHolder = new CarduanzuViewHolder();
            viewHolder.imaView = (ImageView) convertView
                    .findViewById(R.id.listview_item_carduanzu_car_image);
            viewHolder.idView = (TextView) convertView
                    .findViewById(R.id.listview_item_carduanzu_id);
            viewHolder.nameView = (TextView) convertView
                    .findViewById(R.id.listview_item_carduanzu_car_name);

            viewHolder.typeView = (TextView) convertView
                    .findViewById(R.id.listview_item_carduanzu_car_type);
            viewHolder.xuhangView = (TextView) convertView
                    .findViewById(R.id.listview_item_carduanzu_car_xuhang);

            viewHolder.dizhiView = (TextView) convertView
                    .findViewById(R.id.listview_item_carduanzu_car_dizhi);
            viewHolder.zujinView = (TextView) convertView
                    .findViewById(R.id.listview_item_carduanzu_car_zujin);
            viewHolder.zujinView2 = (TextView) convertView
                    .findViewById(R.id.listview_item_carduanzu_car_zujin2);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (CarduanzuViewHolder) convertView.getTag();
        }


        Car item = mNewData.get(position);
        // 这里根据实际情况，取值设值
        viewHolder.imaView.setImageResource(0);
        viewHolder.imaView.setImageResource(R.drawable.img_car_loding);

        ImageLoader.getInstance().displayImage(
                Constent.URLHEAD_NEW_IMAGE + item.mCarFullImg,
                viewHolder.imaView);


        //viewHolder.idView.setText(data.get(position).get("id"));
//        viewHolder.nameView.setText(data.get(position).get("brand_name")
//                + data.get(position).get("car_model_name") + "("
//                + data.get(position).get("car_type_name") + ")");
        viewHolder.nameView.setText(item.mBrandName  + item.mCarModelName+ "(" + item.mCarTypeName +")");
        viewHolder.xuhangView.setText("续航:"
                + item.mEnduranceMileage + "Km");
        viewHolder.dizhiView.setText("货箱容积:"
                + item.mCubage + "m³");
        viewHolder.zujinView
                .setText("￥" + item.mTimePrice);
        viewHolder.zujinView2
                .setText("￥" + item.mDayPrice);



        return convertView;
    }

    private class CarduanzuViewHolder {

        public TextView idView, nameView, typeView, xuhangView, dizhiView,
                zujinView, zujinView2;

        public ImageView imaView;

    }

}
