package com.haofeng.apps.dst.ui.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WheelView extends ScrollView {

	public static final String TAG = WheelView.class.getSimpleName();

	public static class OnWheelViewListener {
		public void onSelected(int selectedIndex) {
		}
	}

	private Context mContext;

	private LinearLayout views;
	private List<Map<String, Object>> items;
	private int itemHeight = 60;// 每行的高度

	private int selectedIndex = 0;
	private int offset = 2; // 偏移量（需要在最前面和最后面补全）,默认上下两个，这样一页就是5个

	private int initialY;

	private Runnable scrollerTask;
	private int newCheck = 50;

	public WheelView(Context context) {
		super(context);
		this.mContext = context;
		this.setVerticalScrollBarEnabled(false);
		this.setOverScrollMode(OVER_SCROLL_NEVER);
		this.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		views = new LinearLayout(mContext);
		views.setOrientation(LinearLayout.VERTICAL);
		views.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));

		this.addView(views);

		scrollerTask = new Runnable() {

			public void run() {

				int newY = getScrollY();
				if (initialY - newY == 0) { // stopped
					final int remainder = initialY % itemHeight;
					final int divided = initialY / itemHeight;
					if (remainder == 0) {
						selectedIndex = divided + offset;

						onSeletedCallBack();
					} else {
						if (remainder > itemHeight / 2) {
							WheelView.this.post(new Runnable() {
								@Override
								public void run() {
									WheelView.this.smoothScrollTo(0, initialY
											- remainder + itemHeight);
									selectedIndex = divided + offset + 1;
									onSeletedCallBack();
								}
							});
						} else {
							WheelView.this.post(new Runnable() {
								@Override
								public void run() {
									WheelView.this.smoothScrollTo(0, initialY
											- remainder);
									selectedIndex = divided + offset;
									onSeletedCallBack();
								}
							});
						}

					}

				} else {
					initialY = getScrollY();
					WheelView.this.postDelayed(scrollerTask, newCheck);
				}
			}
		};

	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * 设置数据
	 * 
	 * @param list
	 */
	public void setDate(List<Map<String, Object>> list) {
		selectedIndex = 0;
		if (null == items) {
			items = new ArrayList<Map<String, Object>>();
		}
		items.clear();
		items.addAll(list);

		// 前面和后面补全
		for (int i = 0; i < offset; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("infor", "");
			items.add(0, map);
			items.add(map);
		}
		float layoutHeigh = mContext.getResources()
				.getDimension(R.dimen.hpx450);
		if (layoutHeigh > 0) {
			itemHeight = (int) (layoutHeigh / 5);// 每页显示的数量
		}
		initialY = 0;

		this.scrollTo(0, 0);
		initData();

	}

	private void initData() {
		views.removeAllViews();

		for (int i = 0; i < items.size(); i++) {
			views.addView(createView(String.valueOf(items.get(i).get("infor"))));
		}

		refreshItemView(0);
	}

	public void startScrollerTask() {

		initialY = getScrollY();
		this.postDelayed(scrollerTask, newCheck);
	}

	/**
	 * 这里可以设置字体的属性 大小啦,边距啦,什么东西都可以在这里设置;
	 */
	private TextView createView(String item) {
		TextView tv = new TextView(mContext);
		tv.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, itemHeight));
		tv.setSingleLine(true);
		// 设置字体大小为20sp
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		// 设置文字内容
		tv.setText(item);
		// 设置文字居中
		tv.setGravity(Gravity.CENTER);

		return tv;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		refreshItemView(t);

		if (t > oldt) {
			// Log.d(TAG, "向下滚动");
			// scrollDirection = SCROLL_DIRECTION_DOWN;
		} else {
			// Log.d(TAG, "向上滚动");
			// scrollDirection = SCROLL_DIRECTION_UP;

		}

	}

	private void refreshItemView(int y) {
		int position = y / itemHeight + offset;
		int remainder = y % itemHeight;
		int divided = y / itemHeight;

		if (remainder == 0) {
			position = divided + offset;
		} else {
			if (remainder > itemHeight / 2) {
				position = divided + offset + 1;
			}

		}

		int childSize = views.getChildCount();
		for (int i = 0; i < childSize; i++) {
			TextView itemView = (TextView) views.getChildAt(i);
			if (null == itemView) {
				return;
			}
			if (position == i) {
				itemView.setTextColor(mContext.getResources().getColor(
						R.color.gray4));
				itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			} else {
				itemView.setTextColor(mContext.getResources().getColor(
						R.color.gray));
				itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			}
		}
	}

	/**
	 * 选中回调
	 */
	private void onSeletedCallBack() {
		if (null != onWheelViewListener) {
			onWheelViewListener.onSelected((selectedIndex - offset));
		}

	}

	public void setSeletion(int position) {
		final int p = position;
		selectedIndex = p + offset;
		this.post(new Runnable() {
			@Override
			public void run() {
				WheelView.this.smoothScrollTo(0, p * itemHeight);
			}
		});

	}

	// /**
	// * 获得“value”值
	// *
	// * @return
	// */
	// public String getSeletedItem() {
	// return items.get(selectedIndex - offset).get("value");
	// }

	/**
	 * 获得标志位
	 * 
	 * @return
	 */
	public int getSeletedIndex() {

		if ((selectedIndex - offset) < 0) {
			return 0;
		} else if ((selectedIndex - offset) >= items.size()) {
			return (items.size() - 1);
		} else {
			return selectedIndex - offset;
		}

	}

	@Override
	public void fling(int velocityY) {
		super.fling(velocityY / 3);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP) {

			startScrollerTask();
		}
		return super.onTouchEvent(ev);
	}

	private OnWheelViewListener onWheelViewListener;

	public OnWheelViewListener getOnWheelViewListener() {
		return onWheelViewListener;
	}

	public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
		this.onWheelViewListener = onWheelViewListener;
	}

}
