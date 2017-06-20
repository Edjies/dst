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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**多账户列表选择弹出窗*/
public class DurationPopWindow implements OnClickListener{
	private PopupWindow popWindow;
	private Context context;
	private LayoutInflater inflater;

	private TextView mTvCancel;
	private TextView mTvConfirm;
	private View parent;
	private LinearLayout mLlDuration;
	private WheelView mCvDurationWheel;
	private List<Map<String, Object>> mDurations;


	private Callback listener;

	public DurationPopWindow(Context context, View parent, Callback listener) {
		this.context = context;
		this.parent = parent;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.listener = listener;
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
			if(mDurations != null) {
				int positionDate = mCvDurationWheel.getSeletedIndex();
				if(positionDate < mDurations.size()) {
					if(listener != null) {
						Map<String, Object> item = mDurations.get(positionDate);
						listener.onSelected((String) item.get("infor"), (String) item.get("value"));
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
		mLlDuration.removeAllViews();
		mCvDurationWheel = new WheelView(context);
		mDurations = getDurations();
		mCvDurationWheel.setDate(mDurations);
		mLlDuration.addView(mCvDurationWheel);
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

		 mLlDuration = (LinearLayout) popupView.findViewById(R.id.ll_container);



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
	private List<Map<String, Object>> getDurations() {
		mDurations = new ArrayList<>();
		String[] times = new String[]{"一年", "十一个月", "十个月", "九个月", "八个月", "七个月", "六个月", "五个月", "四个月"};
		String[] values = new String[]{"12", "11", "10", "9", "8", "7", "6", "5", "4"};
		for(int i = 0; i < times.length; i++) {
			HashMap<String, Object> item = new HashMap<>();
			item.put("infor", times[i]);
			item.put("value", values[i]);
			mDurations.add(item);
		}
		return mDurations;
	}
	
	public static interface Callback {
		public void onSelected(String info, String value);
	}

	
}
