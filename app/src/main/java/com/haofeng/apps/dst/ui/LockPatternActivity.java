package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.application.MyApplication;
import com.haofeng.apps.dst.lockpattern.LockPatternUtils;
import com.haofeng.apps.dst.lockpattern.LockPatternView;
import com.haofeng.apps.dst.lockpattern.LockPatternView.Cell;
import com.haofeng.apps.dst.lockpattern.LockPatternView.DisplayMode;
import com.haofeng.apps.dst.lockpattern.LockPatternView.OnPatternListener;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class LockPatternActivity extends Activity implements OnClickListener {
	@SuppressWarnings("unused")
	private TextView forgetView, otherView;
	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;
	private ImageView userImageView;
	private boolean isCheck = false;// 是否验证密码

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// 透明状态栏
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		setContentView(R.layout.activity_lockpattern);
		((MyApplication) getApplication()).addActivity(this);
//		PushAgent.getInstance(this).onAppStart();
		lockPatternView = (LockPatternView) findViewById(R.id.lockpattern_lockview);
		forgetView = (TextView) findViewById(R.id.lockpattern_forget);
		otherView = (TextView) findViewById(R.id.lockpattern_other);
		userImageView = (ImageView) findViewById(R.id.lockpattern_userimage);

		lockPatternUtils = new LockPatternUtils(this);
		forgetView.setOnClickListener(this);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon);

		userImageView.setImageBitmap(createCircleImage(bitmap));
		isCheck = true;
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			@Override
			public void onPatternStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPatternDetected(List<Cell> pattern) {
				// TODO Auto-generated method stub
				if (isCheck) {
					int result = lockPatternUtils.checkPattern(pattern);
					if (result != 1) {
						if (result == 0) {
							lockPatternView.setDisplayMode(DisplayMode.Wrong);
							Toast.makeText(LockPatternActivity.this, "密码错误",
									Toast.LENGTH_LONG).show();
						} else {
							lockPatternView.clearPattern();
							Toast.makeText(LockPatternActivity.this, "请设置密码",
									Toast.LENGTH_LONG).show();
							lockPatternView.clearPattern();
						}

					} else {
						Toast.makeText(LockPatternActivity.this, "密码正确",
								Toast.LENGTH_LONG).show();
						lockPatternView.clearPattern();
						finish();
					}
				} else {
					lockPatternUtils.saveLockPattern(pattern);
					Toast.makeText(LockPatternActivity.this, "密码已经设置",
							Toast.LENGTH_LONG).show();
					lockPatternView.clearPattern();
					isCheck = true;
				}
			}

			@Override
			public void onPatternCleared() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPatternCellAdded(List<Cell> pattern) {
				// TODO Auto-generated method stub

			}
		});
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

	/**
	 * 画圆图
	 * 
	 * @param source
	 * @return
	 */
	private Bitmap createCircleImage(Bitmap source) {

		int w = source.getWidth();
		int h = source.getHeight();
		if (w > h) {
			w = h;
		}

		Paint paint = new Paint();
		paint.setAntiAlias(true);

		Bitmap target = Bitmap.createBitmap(w, w, Config.ARGB_4444);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */
		canvas.drawCircle(w / 2, w / 2, w / 2, paint);
		/**
		 * 使用SRC_IN
		 */
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.lockpattern_forget:

			// 重置密码
			lockPatternView.clearPattern();
			lockPatternUtils.clearLock();
			isCheck = false;
			break;

		case R.id.lockpattern_other:

			break;

		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
