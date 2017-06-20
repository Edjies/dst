package com.haofeng.apps.dst.ui.view;


import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.bean.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**多账户列表选择弹出窗*/
public class CarTypePopWindow implements OnClickListener{
	private PopupWindow popWindow;
	private Context context;
	private LayoutInflater inflater;

	private TextView mTvCancel;
	private TextView mTvConfirm;
	private View parent;
	private LinearLayout mLlNameWheel;
	private WheelView mCvNameWheel;


	private List<Map<String, Object>> mCarNames;


	private ArrayList<Car> mCars = new ArrayList<>();
	private Callback listener;

	public CarTypePopWindow(Context context, View parent, ArrayList<Car> cars, Callback listener) {
		this.context = context;
		this.parent = parent;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listener = listener;
		this.mCars = cars;
		setViews();
		setListeners();
		initData();
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.tv_cancel) {
			/*隐藏弹窗*/
			popWindow.dismiss();
		}

		else if(id == R.id.tv_confirm) {
			if(mCarNames != null) {
				int positionDate = mCvNameWheel.getSeletedIndex();
				if(positionDate < mCarNames.size()) {
					if(listener != null) {
						listener.onSelected(mCars.get(positionDate));
					}
				}
			}
			popWindow.dismiss();

		}
	}
	
	/**设置 盘型市场弹窗点击 OK 回调监听*/
	public void setOnClickOkListener(Callback listener) {
		this.listener = listener;
	}
	
	/**显示弹出窗 */
	public void show() {
		mLlNameWheel.removeAllViews();
		mCvNameWheel = new WheelView(context);
		mCarNames = getData();
		mCvNameWheel.setDate(mCarNames);

		mLlNameWheel.addView(mCvNameWheel);

		popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}
	
	
	
	private void setViews() {
		View popupView = inflater.inflate(R.layout.view_pick_car_type, null);
		 popWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		 popWindow.setFocusable(true);
		 popWindow.setOutsideTouchable(true);
		 popWindow.setTouchable(true);
	     // 1. make popupWindow dismissed when touch outside, 2. disable popupWindow background
		 popWindow.setBackgroundDrawable(new PaintDrawable(0x0000));

		 mLlNameWheel = (LinearLayout) popupView.findViewById(R.id.ll_date_wheel);



		 mTvCancel = (TextView) popupView.findViewById(R.id.tv_cancel);
		mTvConfirm = (TextView) popupView.findViewById(R.id.tv_confirm);
	}
	
	private void setListeners() {
		 mTvCancel.setOnClickListener(this);
		 mTvConfirm.setOnClickListener(this);

	}
	
	private void initData() {

	}

	/**
	 * 获取日期列表
	 * @return
	 */
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> datas = new ArrayList<>();
		for(int i = 0; i < mCars.size(); i++) {
			HashMap<String, Object> item = new HashMap<>();
			item.put("infor", mCars.get(i).mBrandName + "      " + mCars.get(i).mCarModelName);
			datas.add(item);
		}

		return datas;
	}

	
	public static interface Callback {
		public void onSelected(Car car);
	}

	
}
