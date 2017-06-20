package com.haofeng.apps.dst.ui.view;


import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.bean.RentConfig;
import com.haofeng.apps.dst.utils.UDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**多账户列表选择弹出窗*/
public class DateTimePopWindow implements OnClickListener{
	private PopupWindow popWindow;
	private Context context;
	private LayoutInflater inflater;
	
	private TextView mTvCancel;
	private TextView mTvConfirm;
	private ListView mLvAccountList;
	private View parent;
	private LinearLayout mLlDateWheel;
	private LinearLayout mLlTimeWheel;
	private WheelView mCvDateWheel;
	private WheelView mCvTimeWheel;

	private List<Map<String, Object>> mDates;
	private List<Map<String, Object>> mTimes;


	private RentConfig mConfig;
	private Calendar mSystime;
	
	private Callback listener;
	
	public DateTimePopWindow(Context context, View parent, RentConfig config, Calendar sysTime, Callback listener) {
		this.context = context;
		this.parent = parent;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mConfig = config;
		this.mSystime = sysTime;
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
			if(mDates != null && mTimes != null) {
				int positionDate = mCvDateWheel.getSeletedIndex();
				int positionTime = mCvTimeWheel.getSeletedIndex();
				if(positionDate < mDates.size() && positionTime < mTimes.size()) {
					Date date = (Date)mDates.get(positionDate).get("date");
					int hour = (Integer) mTimes.get(positionTime).get("hour");
					int minute = (Integer) mTimes.get(positionTime).get("minute");
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					c.set(Calendar.HOUR_OF_DAY, hour);
					c.set(Calendar.MINUTE, minute);
					c.set(Calendar.SECOND, 0);
					if(listener != null) {
						listener.onSelected(c);
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
		mLlDateWheel.removeAllViews();
		mCvDateWheel = new WheelView(context);
		mDates = getValidDate(mConfig.mStartHour * 60 + mConfig.mStartMinute, mConfig.mEndHour * 60 + mConfig.mEndMinute, mSystime);
		mCvDateWheel.setDate(mDates);

		mLlDateWheel.addView(mCvDateWheel);

		mLlTimeWheel.removeAllViews();
		mCvTimeWheel = new WheelView(context);
		mTimes = getTimeList(mConfig.mStartHour, mConfig.mStartMinute, mConfig.mEndHour, mConfig.mEndMinute);
		mCvTimeWheel.setDate(mTimes);
		mLlTimeWheel.addView(mCvTimeWheel);

		popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}
	
	
	
	private void setViews() {
		View popupView = inflater.inflate(R.layout.view_pick_date_time, null);
		 popWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		 popWindow.setFocusable(true);
		 popWindow.setOutsideTouchable(true);
		 popWindow.setTouchable(true);
	     // 1. make popupWindow dismissed when touch outside, 2. disable popupWindow background
		 popWindow.setBackgroundDrawable(new PaintDrawable(0x0000));

		 mLlDateWheel = (LinearLayout) popupView.findViewById(R.id.ll_date_wheel);
		 mLlTimeWheel = (LinearLayout) popupView.findViewById(R.id.ll_time_wheel);


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
	 * @param start minuteInDay
	 * @param end  minuteInDay
	 * @param sys 系统时间
	 * @return
	 */
	private List<Map<String, Object>> getValidDate(int start, int end, Calendar sys) {
		// 如果系统时间 + 2小时  仍在营业时间内， 则从今天开始， 否则从明天开始后的10天
		List<Map<String, Object>> mDates = new ArrayList<>();
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(sys.getTimeInMillis());

		int nowMinuteInDay = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);

//		if(nowMinuteInDay > start && nowMinuteInDay < end || nowMinuteInDay < start) {
//			// do nothing
//		}else{
//			// 从明天起
//			now.add(Calendar.DAY_OF_MONTH, 1);
//		}


		for (int i = 0; i < 20; i++) {
			HashMap<String, Object> dateObj = new HashMap<>();
			dateObj.put("infor", UDate.getFormatDate(now, "MM-dd" + " " + UDate.getWeekName(now)));
			dateObj.put("date", new Date(now.getTimeInMillis()));
			mDates.add(dateObj);
			now.add(Calendar.DAY_OF_MONTH, 1);
		}
		return mDates;
	}

	private List<Map<String, Object>> getTimeList(int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
		List<Map<String, Object>> mTimes = new ArrayList<>();
		int hour = hourStart, minute = minuteStart;

		do {
			String time = (hour <= 9 ? "0" + hour : hour) + ":" + (minute <= 9 ? "0" + minute : minute);
			HashMap<String, Object> timeObj = new HashMap<>();
			timeObj.put("infor", time);
			timeObj.put("hour", hour);
			timeObj.put("minute", minute);
			mTimes.add(timeObj);
			minute += 60;
			if(minute >= 60) {
				minute = 0;
				hour += 1;
			}
		} while((hour * 60 + minute) < (hourEnd * 60 + minute));

		return mTimes;
	}
	
	public static interface Callback {
		public void onSelected(Calendar date);
	}

	
}
