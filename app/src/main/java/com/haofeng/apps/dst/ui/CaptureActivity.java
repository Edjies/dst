package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.saoma.camera.CameraManager;
import com.haofeng.apps.dst.saoma.decoding.CaptureActivityHandler;
import com.haofeng.apps.dst.saoma.decoding.InactivityTimer;
import com.haofeng.apps.dst.saoma.view.ViewfinderView;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * google自定义的二维码扫描类
 * 
 * @author qtds
 * 
 */
@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class CaptureActivity extends BaseActivity implements Callback, OnClickListener {
	private final String TAG = "CaptureActivity";
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private TextView inptuTextView, lightTextView;
	private TextView backTextView, titleTextView;
	private LinearLayout bottominforLayout;
	private int type = 0;// 默认扫码
	private FrameLayout topLayout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		addActivity(this);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view); // 扫码框对象
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		topLayout = (FrameLayout) findViewById(R.id.capture_top_layout);
		setTopLayoutPadding(topLayout);
		inptuTextView = (TextView) findViewById(R.id.capture_input);
		lightTextView = (TextView) findViewById(R.id.capture_light);
		backTextView = (TextView) findViewById(R.id.capture_back);
		titleTextView = (TextView) findViewById(R.id.capture_title);
		bottominforLayout = (LinearLayout) findViewById(R.id.capture_bottominforlinear);
		titleTextView.setText("扫一扫");
		inptuTextView.setOnClickListener(this);
		lightTextView.setOnClickListener(this);
		backTextView.setOnClickListener(this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		surfaceinit();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(final Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();

		String backString = obj.getText().toString();
		// 扫电桩
		if (backString != null && backString.contains("pole_Id") && backString.contains("measuring_point")) {
			type = 0;
			check(obj.getText().toString());

		} else if (backString != null) {
			// 扫描设备二维码
			Map<String, String> map = new HashMap<String, String>();
			map.put("act", Constent.ACT_SHAO_MAO_LOGGING);
			map.put("uid", backString);
			AnsynHttpRequest.httpRequest(CaptureActivity.this, AnsynHttpRequest.GET, callBack,
					Constent.ID_GET_SHAO_MAO_LOGGING, map, false, false, true);
		} else {
			PublicUtil.showToast(CaptureActivity.this, "二维码数据解析错误，请重试！", false);
			if (handler != null) {
				handler.quitSynchronously();
				handler = null;
			}
			CameraManager.get().closeDriver();
			surfaceinit();
		}

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	private boolean isOpen = false;

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.capture_input:
			bottominforLayout.setVisibility(View.GONE);
			viewfinderView.setVisibility(View.GONE);
			titleTextView.setText("输入电枪编码");
			Intent intent = new Intent(CaptureActivity.this, CaptureInputDialogActivity.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.capture_light:

			try {
				if (!isOpen) {// 开灯
					CameraManager.get().enableFlash();
					lightTextView.setBackgroundResource(R.drawable.light_off);
					isOpen = true;
				} else { // 关灯
					CameraManager.get().disableFlash();
					lightTextView.setBackgroundResource(R.drawable.light_on);
					isOpen = false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			break;
		case R.id.capture_back:

			finish();

			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 2) {
			if ("ok".equals(data.getStringExtra("type"))) {
				type = 1;
				check(data.getStringExtra("input"));
			} else {
				// 取消
				bottominforLayout.setVisibility(View.VISIBLE);
				viewfinderView.setVisibility(View.VISIBLE);
				titleTextView.setText("扫一扫");
			}

		} else {
			bottominforLayout.setVisibility(View.VISIBLE);
			viewfinderView.setVisibility(View.VISIBLE);
			titleTextView.setText("扫一扫");
		}
	}

	private String typeString, inforString;

	/**
	 * 检测扫码或者输入的结果
	 */
	private void check(String msg) {
		bottominforLayout.setVisibility(View.VISIBLE);
		viewfinderView.setVisibility(View.VISIBLE);
		titleTextView.setText("扫一扫");
		inforString = msg;

		String phone = PublicUtil.getStorage_string(CaptureActivity.this, "phone", "-1");

		if (type == 1) {// 输入
			typeString = "saomainput";
			Map<String, String> map = new HashMap<String, String>();
			map.put("mobile", phone);
			map.put("gun_code", msg);
			map.put("ver", Constent.VER);
			map.put("act", Constent.ACT_GETCHARGEID);
			AnsynHttpRequest.httpRequest(CaptureActivity.this, AnsynHttpRequest.POST, callBack, Constent.ID_GETCHARGEID,
					map, false, true, true);
		} else {// 扫码
			typeString = "saoma";
			try {
				JSONObject jsonObject = new JSONObject(msg);
				String poleid = jsonObject.getString("pole_Id");
				String mpn = jsonObject.getString("measuring_point");
				Map<String, String> map = new HashMap<String, String>();
				map.put("mobile", phone);
				map.put("pole_id", poleid);
				map.put("mpn", mpn);
				map.put("ver", Constent.VER);
				map.put("act", Constent.ACT_GETCHARGEID);
				AnsynHttpRequest.httpRequest(CaptureActivity.this, AnsynHttpRequest.POST, callBack,
						Constent.ID_GETCHARGEID, map, false, true, true);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				PublicUtil.showToast(CaptureActivity.this, "无法识别当前二维码", false);
			}

		}

	}

	private JSONObject jsonObject = null;
	/**
	 * http请求回调
	 */
	private HttpRequestCallBack callBack = new HttpRequestCallBack() {

		@SuppressLint("NewApi")
		@Override
		public void back(int backId, boolean isRequestSuccess, boolean isString, String data, JSONArray jsonArray) {
			// TODO Auto-generated method stub
			switch (backId) {

			case Constent.ID_GETCHARGEID:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							httphandler.sendEmptyMessage(httprequestsuccess_getchargeid);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (httphandler != null) {
								Message message = new Message();
								message.what = httprequesterror;
								message.obj = "服务器端返回数据解析错误，请退出后重试";
								httphandler.sendMessage(message);
							}
						}
					}

				} else {
					if (httphandler != null) {
						Message message = new Message();
						message.what = httprequesterror;
						message.obj = data;
						httphandler.sendMessage(message);
					}
				}
				break;
			case Constent.ID_GET_SHAO_MAO_LOGGING:
				if(isRequestSuccess){
					
				}
				break;
			default:
				break;
			}

		}
	};

	private int httprequesterror = 0x9020;
	private int httprequestsuccess_getchargeid = 0x9021;// 查询单个电桩信息
	@SuppressLint("HandlerLeak")
	private Handler httphandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == httprequesterror) {
				if (type == 0) {
					if (handler != null) {
						handler.quitSynchronously();
						handler = null;
					}
					CameraManager.get().closeDriver();
					surfaceinit();
				}
				if (msg.obj != null) {

					PublicUtil.showToast(CaptureActivity.this, msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess_getchargeid) {
				if (jsonObject != null) {

					try {
						if ("0".equals(jsonObject.get("error").toString())) {

							Intent intent = new Intent(CaptureActivity.this, ChargeGunInforActivity.class);
							intent.putExtra("main", typeString);
							intent.putExtra("msg", inforString);
							startActivity(intent);

						} else {
							if (type == 0) {
								if (handler != null) {
									handler.quitSynchronously();
									handler = null;
								}
								CameraManager.get().closeDriver();
								surfaceinit();
							}
							PublicUtil.showToast(CaptureActivity.this, jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						PublicUtil.showToast(CaptureActivity.this, "配置解析数据错误，请退出重试", false);
					}
				}
			}

		};
	};

	/**
	 * 初始化扫码界面
	 */
	private void surfaceinit() {
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}
}