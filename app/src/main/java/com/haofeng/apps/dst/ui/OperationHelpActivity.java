package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OperationHelpActivity extends BaseActivity {
	private final String TAG = "OperationHelpActivity";
	private FrameLayout topLayout;

	private TextView backView;
	private WebView webView;

	private int httprequesterror = 0x5000;
	private int httprequestsuccess = 0x5001;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == httprequesterror) {
				if (msg.obj != null) {
					PublicUtil.showToast(OperationHelpActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess) {
				if (jsonObject != null) {

					try {

						// JSONObject jsonObject =
						// backArray.getJSONObject(0);
						if ("0".equals(jsonObject.get("error").toString())) {

							String urlString = jsonObject.get("data")
									.toString();
							if (urlString != null) {
								webView.clearCache(true);
								webView.loadUrl(urlString);
								PublicUtil.setStorage_string(
										OperationHelpActivity.this,
										"url_operationhelp", urlString);
							}

						} else {
							PublicUtil.showToast(OperationHelpActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {

						// TODO: handle exception

						e.printStackTrace();
						 
					}

				}

			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operationhelp);
		addActivity(this);
		init();
	}

	private void init() {
		topLayout = (FrameLayout) findViewById(R.id.operationhelp_top_layout);
		setTopLayoutPadding(topLayout);
		webView = (WebView) findViewById(R.id.operationhelp_webview);

		backView = (TextView) findViewById(R.id.operationhelp_back);

		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (webView != null && webView.canGoBack()) {
					webView.goBack();
				} else {
					finish();
				}

			}
		});

		WebSettings webSettings = webView.getSettings();

		// 开启javascript设置
		webSettings.setJavaScriptEnabled(true);

		// 让缩放显示的最小值为起始
		// webView.setInitialScale(5);
		// 自适应屏幕

		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// 设置支持缩放
		webSettings.setSupportZoom(true);
		// 设置缩放工具的显示
		webSettings.setBuiltInZoomControls(false);
		// 设置可以使用localStorage
		webSettings.setDomStorageEnabled(true);
		// 应用可以有数据库
		webSettings.setDatabaseEnabled(true);
		String dbPath = this.getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		webSettings.setDatabasePath(dbPath);
		// 应用可以有缓存
		webSettings.setAppCacheEnabled(true);
		String appCaceDir = this.getApplicationContext()
				.getDir("cache", Context.MODE_PRIVATE).getPath();
		webSettings.setAppCachePath(appCaceDir);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setDisplayZoomControls(false);

		// webSettings.setUseWideViewPort(true); // 将图片调整到适合webview的大小

		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		// webSettings.setAppCacheMaxSize(1024 * 1024 * 8);// 设置缓冲大小，我设的是8M

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
				PublicUtil.logDbug(TAG, "onLoadResource url=" + url, 0);

				super.onLoadResource(view, url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				PublicUtil.logDbug(TAG, "intercept url=" + url, 0);
				view.loadUrl(url);
				return true;
			}

			// 页面开始时调用
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}

			// 页面加载完成调用
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);

			}

			@Override
			public void onFormResubmission(WebView view, Message dontResend,
					Message resend) {
				// TODO Auto-generated method stub
				super.onFormResubmission(view, dontResend, resend);
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				PublicUtil.logDbug(TAG, "onJsAlert " + message, 0);

				PublicUtil
						.showToast(OperationHelpActivity.this, message, false);

				result.confirm();
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {
				PublicUtil.logDbug(TAG, "onJsConfirm " + message, 0);
				return super.onJsConfirm(view, url, message, result);
			}

			@Override
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, JsPromptResult result) {
				PublicUtil.logDbug(TAG, "onJsPrompt " + url, 0);
				return super.onJsPrompt(view, url, message, defaultValue,
						result);
			}

		});

		String urlString = PublicUtil.getStorage_string(this,
				"url_operationhelp", "");

		if (urlString != null && !TextUtils.isEmpty(urlString)) {
			webView.loadUrl(urlString);
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("ver", Constent.VER);
		map.put("act", Constent.ACT_GETOPERATIONHELP);
		AnsynHttpRequest.httpRequest(OperationHelpActivity.this,
				AnsynHttpRequest.POST, callBack, Constent.ID_GETOPERATIONHELP,
				map, false, true, true);

	}

	private JSONObject jsonObject = null;
	/**
	 * http请求回调
	 */
	private HttpRequestCallBack callBack = new HttpRequestCallBack() {

		@SuppressLint("NewApi")
		@Override
		public void back(int backId, boolean isRequestSuccess,
				boolean isString, String data, JSONArray jsonArray) {
			// TODO Auto-generated method stub
			switch (backId) {
			case Constent.ID_GETOPERATIONHELP:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (handler != null) {
								Message message = new Message();
								message.what = httprequesterror;
								message.obj = "服务器端返回数据解析错误，请退出后重试";
								handler.sendMessage(message);
							}
						}
					}

				} else {
					if (handler != null) {
						Message message = new Message();
						message.what = httprequesterror;
						message.obj = data;
						handler.sendMessage(message);
					}
				}
				break;

			default:
				break;
			}

		}
	};

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
		// 检查是否为返回事件，如果有网页历史记录
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
