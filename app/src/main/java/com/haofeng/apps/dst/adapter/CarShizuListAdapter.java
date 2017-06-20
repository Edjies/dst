package com.haofeng.apps.dst.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.Constent;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CarShizuListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Context mContext;

	public CarShizuListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		mContext = context;
	}

	public interface Callback {
		public void click(View v);
	};

	public void setData(List<Map<String, String>> data) {
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
		CarShizuViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_carshizu, null);
			viewHolder = new CarShizuViewHolder();
			viewHolder.imaView = (ImageView) arg1
					.findViewById(R.id.listview_item_carshizu_car_image);
			viewHolder.idView = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_id);
			viewHolder.nameView = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_car_name);
			viewHolder.numberView = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_car_number);
			viewHolder.typeView = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_car_type);
			viewHolder.xuhangView = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_car_xuhang);
			viewHolder.xuhangView2 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_car_xuhang2);
			viewHolder.xuhangView3 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_car_xuhang3);
			viewHolder.dizhiView = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_car_dizhi);
			viewHolder.zujinView = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_car_zujin);
			viewHolder.zujinView2 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_car_zujin2);
			viewHolder.zujinView3 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_car_zujin3);
			viewHolder.timeLayout = (LinearLayout) arg1
					.findViewById(R.id.listview_item_carshizu_timelayout);
			viewHolder.timeView = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time);
			viewHolder.timeView2 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time2);
			viewHolder.timeView3 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time3);
			viewHolder.timeView4 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time4);
			viewHolder.timeView5 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time5);
			viewHolder.timeView6 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time6);
			viewHolder.timeView7 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time7);
			viewHolder.timeView8 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time8);
			viewHolder.timeView9 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time9);
			viewHolder.timeView10 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time10);
			viewHolder.timeView11 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time11);
			viewHolder.timeView12 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time12);
			viewHolder.timeView13 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time13);
			viewHolder.timeView14 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time14);
			viewHolder.timeView15 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time15);
			viewHolder.timeView16 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time16);
			viewHolder.timeView17 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time17);
			viewHolder.timeView18 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time18);
			viewHolder.timeView19 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time19);
			viewHolder.timeView20 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time20);
			viewHolder.timeView21 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time21);
			viewHolder.timeView22 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time22);
			viewHolder.timeView23 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time23);
			viewHolder.timeView24 = (TextView) arg1
					.findViewById(R.id.listview_item_carshizu_time24);

			arg1.setTag(viewHolder);

		} else {
			viewHolder = (CarShizuViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.imaView.setImageResource(0);
			viewHolder.imaView.setImageResource(R.drawable.img_car_loding);
			if (!TextUtils.isEmpty(data.get(arg0).get("car_img_url"))) {

				ImageLoader.getInstance().displayImage(
						Constent.URLHEAD_IMAGE
								+ data.get(arg0).get("car_img_url"),
						viewHolder.imaView);
			}

			viewHolder.idView.setText(data.get(arg0).get("car_no"));
			viewHolder.nameView.setText(data.get(arg0).get("car_name"));
			viewHolder.numberView.setText(data.get(arg0).get("car_no"));
			viewHolder.typeView.setText(data.get(arg0).get("car_type"));
			viewHolder.xuhangView.setText("续航:"
					+ data.get(arg0).get("endurance_mileage"));
			viewHolder.xuhangView2.setText("["
					+ data.get(arg0).get("dump_energy") + "%]");

			viewHolder.xuhangView3.setText("待充电");
			viewHolder.xuhangView3
					.setBackgroundResource(R.drawable.tag_car_yelow);
			if (!TextUtils.isEmpty(data.get(arg0).get("dump_energy"))) {

				if (Integer.parseInt(data.get(arg0).get("dump_energy")) > 60) {
					viewHolder.xuhangView3.setText("电量足");
					viewHolder.xuhangView3
							.setBackgroundResource(R.drawable.tag_car);
				}

			}

			// if ("0".equals(data.get(arg0).get("charge_state"))) {// 待充电
			// viewHolder.xuhangView3.setText("待充电");
			// viewHolder.xuhangView3
			// .setBackgroundResource(R.drawable.tag_car_yelow);
			// } else if ("1".equals(data.get(arg0).get("charge_state"))) {//
			// 充电中
			// viewHolder.xuhangView3.setText("充电中");
			// viewHolder.xuhangView3
			// .setBackgroundResource(R.drawable.tag_car_yelow);
			// } else {
			// viewHolder.xuhangView3.setText("电力充足");
			// viewHolder.xuhangView3
			// .setBackgroundResource(R.drawable.tag_car);
			//
			// }
			viewHolder.dizhiView.setText(data.get(arg0).get("addr"));

			viewHolder.zujinView3.setText("￥"
					+ data.get(arg0).get("month_price"));
			viewHolder.zujinView.setVisibility(View.VISIBLE);

			viewHolder.zujinView
					.setText("￥" + data.get(arg0).get("hour_price"));

			viewHolder.zujinView2
					.setText("￥" + data.get(arg0).get("day_price"));

			viewHolder.timeLayout.setVisibility(View.VISIBLE);
			if ("1".equals(data.get(arg0).get("1"))) {
				viewHolder.timeView.setBackgroundColor(mContext.getResources()
						.getColor(R.color.gray));
			} else {
				viewHolder.timeView.setBackgroundColor(mContext.getResources()
						.getColor(R.color.textgreen));
			}

			if ("1".equals(data.get(arg0).get("2"))) {
				viewHolder.timeView2.setBackgroundColor(mContext.getResources()
						.getColor(R.color.gray));
			} else {
				viewHolder.timeView2.setBackgroundColor(mContext.getResources()
						.getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("3"))) {
				viewHolder.timeView3.setBackgroundColor(mContext.getResources()
						.getColor(R.color.gray));
			} else {
				viewHolder.timeView3.setBackgroundColor(mContext.getResources()
						.getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("4"))) {
				viewHolder.timeView4.setBackgroundColor(mContext.getResources()
						.getColor(R.color.gray));
			} else {
				viewHolder.timeView4.setBackgroundColor(mContext.getResources()
						.getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("5"))) {
				viewHolder.timeView5.setBackgroundColor(mContext.getResources()
						.getColor(R.color.gray));
			} else {
				viewHolder.timeView5.setBackgroundColor(mContext.getResources()
						.getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("6"))) {
				viewHolder.timeView6.setBackgroundColor(mContext.getResources()
						.getColor(R.color.gray));
			} else {
				viewHolder.timeView6.setBackgroundColor(mContext.getResources()
						.getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("7"))) {
				viewHolder.timeView7.setBackgroundColor(mContext.getResources()
						.getColor(R.color.gray));
			} else {
				viewHolder.timeView7.setBackgroundColor(mContext.getResources()
						.getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("8"))) {
				viewHolder.timeView8.setBackgroundColor(mContext.getResources()
						.getColor(R.color.gray));
			} else {
				viewHolder.timeView8.setBackgroundColor(mContext.getResources()
						.getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("9"))) {
				viewHolder.timeView9.setBackgroundColor(mContext.getResources()
						.getColor(R.color.gray));
			} else {
				viewHolder.timeView9.setBackgroundColor(mContext.getResources()
						.getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("10"))) {
				viewHolder.timeView10.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView10.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("11"))) {
				viewHolder.timeView11.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView11.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("12"))) {
				viewHolder.timeView12.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView12.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("13"))) {
				viewHolder.timeView13.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView13.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("14"))) {
				viewHolder.timeView14.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView14.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("15"))) {
				viewHolder.timeView15.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView15.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("16"))) {
				viewHolder.timeView16.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView16.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("17"))) {
				viewHolder.timeView17.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView17.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("18"))) {
				viewHolder.timeView18.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView18.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("19"))) {
				viewHolder.timeView19.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView19.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("20"))) {
				viewHolder.timeView20.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView20.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("21"))) {
				viewHolder.timeView21.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView21.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("22"))) {
				viewHolder.timeView22.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView22.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("23"))) {
				viewHolder.timeView23.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView23.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}
			if ("1".equals(data.get(arg0).get("24"))) {
				viewHolder.timeView24.setBackgroundColor(mContext
						.getResources().getColor(R.color.gray));
			} else {
				viewHolder.timeView24.setBackgroundColor(mContext
						.getResources().getColor(R.color.textgreen));
			}

		}

		return arg1;
	}

	public class CarShizuViewHolder {

		public TextView idView, nameView, numberView, typeView, xuhangView,
				xuhangView2, xuhangView3, dizhiView, zujinView, zujinView2,
				zujinView3;
		public TextView timeView, timeView2, timeView3, timeView4, timeView5,
				timeView6, timeView7, timeView8, timeView9, timeView10,
				timeView11, timeView12, timeView13, timeView14, timeView15,
				timeView16, timeView17, timeView18, timeView19, timeView20,
				timeView21, timeView22, timeView23, timeView24;
		public ImageView imaView;
		private LinearLayout timeLayout;

	}

}
