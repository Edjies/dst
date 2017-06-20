package com.haofeng.apps.dst.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore.Audio;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.ui.CarRestListDetail2Activity;
import com.haofeng.apps.dst.ui.HuiYuanRZActivity;
import com.haofeng.apps.dst.ui.MainActivity;
import com.haofeng.apps.dst.ui.WeiZhangjiesuanActivity;
import com.haofeng.apps.dst.ui.WelcomeActivity;
import com.haofeng.apps.dst.utils.PublicUtil;

public class NotificationService extends Service {
	private int NOTIFYID = 1;// 通知id
	private static Handler mHandler;
	private static String TAG = "NotificationService";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (intent != null) {

			if ("1".equals(intent.getStringExtra("pushtype"))) {

				String title = intent.getStringExtra("title");
				String content = intent.getStringExtra("content");
				String customContent = intent.getStringExtra("customcontent");
				notificationShow(title, content, customContent);
			}

		}

		return START_NOT_STICKY;

	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void notificationShow(String tick, String content,
			String customContent) {

		// 通过Intent，使得点击Notification之后会启动新的Activity

		PublicUtil.logDbug(
				TAG,
				"isServiceRunning="
						+ PublicUtil.isBackground(getApplicationContext()), 0); // 自定义消息的内容

		Intent startIntent;
		if (!PublicUtil.isBackground(getApplicationContext())
				&& isExsitMianActivity(MainActivity.class)) {
			startIntent = new Intent(getApplicationContext(),
					MainActivity.class);
			if ("MemberAudit".equals(tick)) {
				startIntent = new Intent(getApplicationContext(),
						HuiYuanRZActivity.class);
			} else if ("OrderTimeout".equals(tick)
					|| "ExtractCountdown".equals(tick)
					|| "ExtractCar".equals(tick)
					|| "ChangeCarAudit".equals(tick)
					|| "ReturnCarCountdown".equals(tick)
					|| "ConfirmMileage".equals(tick)
					|| "StopCharging".equals(tick)) {

				String order_no = null;
				if (customContent != null && customContent.length() != 0) {
					try {
						JSONObject obj = new JSONObject(customContent);
						// key1为前台配置的key
						if (!obj.isNull("order_no")) {
							order_no = obj.getString("order_no");

						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				if (!TextUtils.isEmpty(order_no)) {
					startIntent = new Intent(getApplicationContext(),
							CarRestListDetail2Activity.class);
					startIntent.putExtra("order_no", order_no);
				}

			} else if ("WzSettlement".equals(tick)) {

				String order_no = null, wz_foregift_state = null;
				if (customContent != null && customContent.length() != 0) {
					try {
						JSONObject obj = new JSONObject(customContent);
						// key1为前台配置的key
						if (!obj.isNull("order_no")) {
							order_no = obj.getString("order_no");

						}

						if (!obj.isNull("wz_foregift_state")) {
							wz_foregift_state = obj
									.getString("wz_foregift_state");

						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if (!TextUtils.isEmpty(order_no)
						&& !TextUtils.isEmpty(wz_foregift_state)) {
					startIntent = new Intent(getApplicationContext(),
							WeiZhangjiesuanActivity.class);
					startIntent.putExtra("order_no", order_no);
					startIntent
							.putExtra("wz_foregift_state", wz_foregift_state);
				}
			}

		} else {
			startIntent = new Intent(getApplicationContext(),
					WelcomeActivity.class);

			if ("MemberAudit".equals(tick)) {
				startIntent.putExtra("tick", tick);
			} else if ("OrderTimeout".equals(tick)
					|| "ExtractCountdown".equals(tick)
					|| "ExtractCar".equals(tick)
					|| "ChangeCarAudit".equals(tick)
					|| "ReturnCarCountdown".equals(tick)
					|| "ConfirmMileage".equals(tick)
					|| "StopCharging".equals(tick)) {

				String order_no = null;
				if (customContent != null && customContent.length() != 0) {
					try {
						JSONObject obj = new JSONObject(customContent);
						// key1为前台配置的key
						if (!obj.isNull("order_no")) {
							order_no = obj.getString("order_no");

						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				if (!TextUtils.isEmpty(order_no)) {
					startIntent.putExtra("tick", tick);
					startIntent.putExtra("order_no", order_no);
				}

			} else if ("WzSettlement".equals(tick)) {

				String order_no = null, wz_foregift_state = null;
				if (customContent != null && customContent.length() != 0) {
					try {
						JSONObject obj = new JSONObject(customContent);
						// key1为前台配置的key
						if (!obj.isNull("order_no")) {
							order_no = obj.getString("order_no");

						}

						if (!obj.isNull("wz_foregift_state")) {
							wz_foregift_state = obj
									.getString("wz_foregift_state");

						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if (!TextUtils.isEmpty(order_no)
						&& !TextUtils.isEmpty(wz_foregift_state)) {
					startIntent.putExtra("tick", tick);
					startIntent.putExtra("order_no", order_no);
					startIntent
							.putExtra("wz_foregift_state", wz_foregift_state);
				}
			}
		}

		// 该标志位表示如果Intent要启动的Activity在栈顶，则无须创建新的实例
		startIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 100, startIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		int b = Build.VERSION.SDK_INT;

		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = null;

		if (b < 16) {
			notification = new Notification(R.drawable.icon, null,
					System.currentTimeMillis());
			notification.setLatestEventInfo(getApplicationContext(), null,
					content, pendingIntent);

		} else {
			Notification.Builder builder = new Notification.Builder(
					getApplicationContext()).setSmallIcon(R.drawable.icon);
			notification = builder.setContentIntent(pendingIntent)
					.setContentText(content).build();
		}
		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.notification_layout);

		contentView.setTextViewText(R.id.notification_layout_content, content);
		notification.contentView = contentView;
		// if ("1".equals(notiType)) {
		// 声音和震动貌似不能同时支持
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.sound = Uri.withAppendedPath(
				Audio.Media.INTERNAL_CONTENT_URI, "6");
		// // 震动
		// notification.defaults = Notification.DEFAULT_VIBRATE;
		// 闪光灯
		// notification.defaults |= Notification.DEFAULT_LIGHTS;
		// notification.ledARGB = 0xffff7000;
		// notification.ledOnMS = 600;
		// notification.ledOffMS = 800;
		// notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击后消失
		// }

		manager.notify(NOTIFYID, notification);

	}

	private boolean isExsitMianActivity(Class<?> cls) {
		Intent intent = new Intent(getApplicationContext(), cls);
		ComponentName cmpName = intent.resolveActivity(getApplicationContext()
				.getPackageManager());
		boolean flag = false;
		if (cmpName != null) { // 说明系统中存在这个activity
			ActivityManager am = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
			List<RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
			for (RunningTaskInfo taskInfo : taskInfoList) {
				if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
					flag = true;
					break; // 跳出循环，优化效率
				}
			}
		}
		return flag;
	}
}
