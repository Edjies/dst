package com.haofeng.apps.dst.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApplication extends Application {
	private List<Activity> activitysList = new ArrayList<Activity>();
	private List<Bitmap> imagebtmList = new ArrayList<Bitmap>();
	private List<Bitmap> mainiconbtmList = new ArrayList<Bitmap>();
	private boolean isback = false;// 主界面手势密码控制
	private int selectiontab = 0;
	private boolean isregstToken = false;// 是否已经提交注册了推送token

	public int getSelectiontab() {
		return selectiontab;
	}

	public void setSelectiontab(int selectiontab) {
		this.selectiontab = selectiontab;
	}

	public boolean isIsregstToken() {
		return isregstToken;
	}

	public void setIsregstToken(boolean isregstToken) {
		this.isregstToken = isregstToken;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		SDKInitializer.initialize(getApplicationContext());

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPoolSize(3) // default 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
				.denyCacheImageMultipleSizesInMemory().writeDebugLogs() // 打印debug
				.build(); // 开始构建
		ImageLoader.getInstance().init(config);

	}

	public void addActivity(Activity activity) {
		activitysList.add(activity);
	}

	public void removeAllActivitys() {

		for (Activity activity : activitysList) {
			activity.finish();
		}

		activitysList.clear();
		activitysList = null;
	}

	public void setBtmList(List<Bitmap> imagebtmList) {
		this.imagebtmList = imagebtmList;

	}

	public List<Bitmap> getBtmList() {
		return imagebtmList;

	}

	public void setMianBtmList(List<Bitmap> imagebtmList) {
		this.mainiconbtmList = imagebtmList;

	}

	public List<Bitmap> getMainBtmList() {
		return mainiconbtmList;

	}

	public boolean isIsback() {
		return isback;
	}

	public void setIsback(boolean isback) {
		this.isback = isback;
	}
}
