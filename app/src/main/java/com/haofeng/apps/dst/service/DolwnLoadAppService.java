package com.haofeng.apps.dst.service;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import com.haofeng.apps.dst.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DolwnLoadAppService extends Service {
	// 外存sdcard存放路径
	private final String FILE_PATH = Environment.getExternalStorageDirectory()
			+ "/dst/";
	// 下载应用存放全路径
	private String filename;
	// 更新应用版本标记
	private static final int UPDARE_TOKEN = 1;
	// 准备安装新版本应用标记
	private static final int INSTALL_TOKEN = 2;
	// 下载应用的对话框
	private AlertDialog progressBarDialog;
	// 下载应用的进度条
	private ProgressBar progressBar;
	// 进度条的当前刻度值
	private int curProgress;
	// app下载地址
	private String spec;
	private String newVerString = "new";
	private static Context mcontext;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void setContent(Context context) {//这里设置context，service中开启dialog，样式不好控制，
		mcontext = context;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		if (intent != null) {
			newVerString = intent.getStringExtra("newVerString");
			spec = intent.getStringExtra("spec");
			filename = FILE_PATH + "dst" + newVerString + ".apk";

			if (spec != null) {
				showDownloadDialog();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 显示下载进度对话框
	 */
	private void showDownloadDialog() {

		if (mcontext != null) {
			View view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.view_downloadprogress_dialog, null);

			progressBar = (ProgressBar) view
					.findViewById(R.id.view_downloadprogress_dialog_progressBar);
			progressBar.setProgress(curProgress);
			progressBarDialog = new AlertDialog.Builder(mcontext,
					R.style.dialog_nostroke).show();
			progressBarDialog.addContentView(view, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			progressBarDialog.setCancelable(false);
			progressBarDialog.setCanceledOnTouchOutside(false);
		}
		downloadApp();
	}

	/**
	 * 下载新版本应用
	 */
	private void downloadApp() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				URL url = null;
				InputStream in = null;
				FileOutputStream out = null;
				HttpURLConnection conn = null;
				try {
					url = new URL(spec);
					conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					long fileLength = conn.getContentLength();
					in = conn.getInputStream();
					File filePath = new File(FILE_PATH);
					if (!filePath.exists()) {
						filePath.mkdir();
					}
					out = new FileOutputStream(new File(filename));
					byte[] buffer = new byte[1024];
					int len = 0;
					long readedLength = 0l;
					while ((len = in.read(buffer)) != -1) {
						out.write(buffer, 0, len);
						readedLength += len;
						curProgress = (int) (((float) readedLength / fileLength) * 100);
						handler.sendEmptyMessage(UPDARE_TOKEN);
						if (readedLength >= fileLength) {
							if (progressBarDialog != null) {
								progressBarDialog.dismiss();
							}

							// 下载完毕，通知安装
							handler.sendEmptyMessage(INSTALL_TOKEN);
							break;
						}
					}
					out.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		}).start();
	}

	/**
	 * 安装新版本应用
	 */
	private void installApp() {
		File appFile = new File(filename);
		if (!appFile.exists()) {
			return;
		}
		// 跳转到新版本应用安装页面
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + appFile.toString()),
				"application/vnd.android.package-archive");
		getApplicationContext().startActivity(intent);
		stopSelf();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDARE_TOKEN:
				if (progressBar != null) {
					progressBar.setProgress(curProgress);
				}

				break;

			case INSTALL_TOKEN:
				installApp();
				break;
			}
		}
	};

}
