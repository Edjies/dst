package com.haofeng.apps.dst.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.application.MyApplication;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class ChargeStationImagesDialogActivity extends BaseActivity {
	private TextView textView;
	private List<Bitmap> imageList;
	private int counts = 0, nowcount = 0;
	private ViewPager viewPager;
	private List<View> listViews = new ArrayList<View>(); // Tab页面列表
	private String chargeNameString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_chargestationinfor_images_dialog);
		((MyApplication) getApplication()).addActivity(this);
		viewPager = (ViewPager) findViewById(R.id.chargestationinfor_images_vPager);
		textView = (TextView) findViewById(R.id.chargestationinfor_images_count);

		imageList = ((MyApplication) getApplication()).getBtmList();

		if (imageList != null) {
			counts = imageList.size();
			chargeNameString = getIntent().getStringExtra("name");
			// imageView.setImageBitmap(imageList.get(nowcount));

			textView.setText(chargeNameString + " " + (nowcount + 1) + "/"
					+ counts);

			for (int i = 0; i < imageList.size(); i++) {

				ImageView imageView = new ImageView(this);

				imageView.setLayoutParams(new LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setPadding(2, 0, 2, 0);
				imageView.setImageBitmap(imageList.get(i));

				listViews.add(imageView);
			}

			viewPager.setAdapter(new MyPagerAdapter(listViews));
			viewPager.setCurrentItem(0);
			viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// 友盟统计开始
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// 友盟统计结束
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			finish();
			return true;

		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return super.onTouchEvent(event);

	}

	/**
	 * ViewPager适配器
	 */
	private class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			// Animation animation = null;
			nowcount = arg0;
			textView.setText(chargeNameString + " " + (nowcount + 1) + "/"
					+ counts);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

}
