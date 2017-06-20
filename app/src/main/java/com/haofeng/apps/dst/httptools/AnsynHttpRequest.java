package com.haofeng.apps.dst.httptools;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.utils.PublicUtil;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 异步请求数据
 *
 * @author Administrator
 *
 */
public class AnsynHttpRequest {

	public static String TAG = AnsynHttpRequest.class.getSimpleName();
	// 0xFF00-0xFFFF用在网络请求的参数配置上
	public static final int POST = 0XFF00; // post 提交
	public static final int GET = 0XFF01; // get 提交
	public static final int POSTIMAGE = 0XFF02; // post提交图片
	public static final int POSTIMAGE2 = 0XFF03; // post二期提交图片 一张
	public static final int POSTIMAGE3 = 0XFF04; // post二期企业长租
	public static final int POSTIMAGE4 = 0XFF05; // post二期 两张图片
	public static final int WEATHERGET = 0XFF06; // 获取天气信息
	public static String _loginkey;// 一期的登录KEY
	public static String loginkey;// 二期的登录KEY
	public static String userid;// 二期用户id

	/**
	 *
	 * 访问网络初始化函数
	 *
	 * @param context
	 * @param requestType
	 *            请求类型get，post
	 * @param callBack
	 *            返回回调
	 * @param backId
	 *            返回标识
	 * @param map
	 *            参数，用来拼接url
	 * @param isCache
	 *            是否缓存数据（暂不支持）
	 * @param isshownettoast
	 *            是否提示网络无连接
	 */
	public static void httpRequest(Context context, int requestType,
			HttpRequestCallBack callBack, int backId, Map<String, String> map,
			boolean isCache, boolean isShowDialog, boolean isshownettoast) {

		switch (Network.checkNetWorkType(context)) {
		case /** 网络不可用 */
		Network.NONETWORK:
			// 发送显示消息广播
			if (isshownettoast) {
				Intent intent = new Intent("com.myreceiver.showtoast");
				intent.putExtra("msgid", R.string.network_show_noerror);
				context.sendBroadcast(intent);
			}

			callBack.back(backId, false, true, null, null);
			break;
		case /** 是wifi连接 */
		Network.WIFI:

		case /** 不是wifi连接 */
		Network.NOWIFI:
			// 异步请求数据

			if (requestType == AnsynHttpRequest.POST
					|| requestType == AnsynHttpRequest.POSTIMAGE) {
				// String key = getKey(context);
				String key = getKey2(context);

				if (key != null) {
					map.put("_loginkey", key);
				}
			} else if (requestType == AnsynHttpRequest.WEATHERGET) {

			} else {
				String key = getKey2(context);

				if (TextUtils.isEmpty(key)) {
					key = "";
				}
				map.put("loginkey", key);

				String userid = getUserid(context);
				if (TextUtils.isEmpty(userid)) {
					userid = "";
				}
				map.put("user_id", userid);
				map.put("secret", Constent.secret);
				map.put("ver", Constent.VER);
			}
			doAsynRequest(context, requestType, callBack, backId, map, isCache,
					isShowDialog);
			break;
		default:
			break;
		}

	}

	/**
	 * 一期key
	 *
	 * @param context
	 * @return
	 */
	public static String getKey(Context context) {
		String backString = _loginkey;

		PublicUtil.logDbug(TAG, backString, 0);
		if (TextUtils.isEmpty(backString)) {
			SharedPreferences preferences = context.getSharedPreferences(
					"dst_local", Context.MODE_PRIVATE);

			backString = preferences.getString("loginkey", null);
		}

		return backString;

	}

	/**
	 * 二期key
	 *
	 * @param context
	 * @return
	 */
	public static String getKey2(Context context) {
		String backString = loginkey;

		PublicUtil.logDbug(TAG, backString, 0);
		if (TextUtils.isEmpty(backString)) {
			SharedPreferences preferences = context.getSharedPreferences(
					"dst_local", Context.MODE_PRIVATE);

			backString = preferences.getString("loginkey2", null);
		}

		return backString;

	}

	/**
	 * 二期userid
	 *
	 * @param context
	 * @return
	 */
	public static String getUserid(Context context) {
		String backString = userid;

		PublicUtil.logDbug(TAG, backString, 0);
		if (TextUtils.isEmpty(backString)) {
			SharedPreferences preferences = context.getSharedPreferences(
					"dst_local", Context.MODE_PRIVATE);

			backString = preferences.getString("userid", null);
		}

		return backString;

	}

	/**
	 * 请求开始
	 *
	 * @param context
	 * @param requestType
	 * @param callBack
	 * @param backId
	 * @param map
	 * @param isCache
	 */
	private static void doAsynRequest(Context context, int requestType,
			HttpRequestCallBack callBack, int backId, Map<String, String> map,
			boolean isCache, boolean isShowDialog) {

		if (map != null && map.size() > 0 && map.containsKey("act")
				&& map.get("act") != null) {
			if (requestType == GET) {

				// 请求
				ThreadPoolUtils.execute(new MyRunnable(context, requestType,
						callBack, backId, isCache, map, isShowDialog));

			} else if (requestType == POST) {

				// 请求
				ThreadPoolUtils.execute(new MyRunnable(context, requestType,
						callBack, backId, isCache, map, isShowDialog));

			} else if (requestType == POSTIMAGE) {

				// 请求
				ThreadPoolUtils.execute(new MyRunnable(context, requestType,
						callBack, backId, isCache, map, isShowDialog));

			} else if (requestType == POSTIMAGE2) {

				// 请求
				ThreadPoolUtils.execute(new MyRunnable(context, requestType,
						callBack, backId, isCache, map, isShowDialog));

			} else if (requestType == POSTIMAGE3) {

				// 请求
				ThreadPoolUtils.execute(new MyRunnable(context, requestType,
						callBack, backId, isCache, map, isShowDialog));

			} else if (requestType == POSTIMAGE4) {

				// 请求
				ThreadPoolUtils.execute(new MyRunnable(context, requestType,
						callBack, backId, isCache, map, isShowDialog));

			} else if (requestType == WEATHERGET) {

				// 天气
				ThreadPoolUtils.execute(new MyRunnable(context, requestType,
						callBack, backId, isCache, map, isShowDialog));

			} else {
				callBack.back(backId, false, true, context.getResources()
						.getString(R.string.network_show_error), null);
			}

		} else {
			callBack.back(backId, false, true, context.getResources()
					.getString(R.string.network_show_error), null);
		}

	}
}

class MyRunnable implements Runnable {

	public static final int SHOWDIALOG = 0XFF05; // 显示进度圈
	public static final int HIDEDIALOG = 0XFF06; // 隐藏进度圈

	private static final int REQUEST_TIMEOUT = 10 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟
	private Context context;
	private int requestType;
	private HttpRequestCallBack callBack;
	private int backId;
	@SuppressWarnings("unused")
	private boolean isCache, isShowDialog;
	private Map<String, String> params;
	private ProgressDialog dialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == SHOWDIALOG) {
				showProgressDialog();
			} else if (msg.what == HIDEDIALOG) {
				if (dialog != null) {

					try {
						dialog.dismiss();
					} catch (Exception e) {
						// TODO: handle exception
					}

				}

			}

		};
	};

	public MyRunnable(Context context, int requestType,
			HttpRequestCallBack callBack, int backId, boolean isCache,
			Map<String, String> params, boolean isShowDialog) {
		this.context = context;
		this.requestType = requestType;
		this.callBack = callBack;
		this.backId = backId;
		this.isCache = isCache;
		this.params = params;
		this.isShowDialog = isShowDialog;

	}

	@Override
	public void run() {
		// HttpsURLConnection connection = null;
		if (isShowDialog && mHandler != null) {
			mHandler.sendEmptyMessage(SHOWDIALOG);
		}

		HttpURLConnection connection = null;
		InputStream is = null;
		if (requestType == AnsynHttpRequest.GET) {
			try {

				StringBuilder sb = new StringBuilder();

				sb.append(Constent.URLHEAD_GET);
				sb.append(params.get("act"));
				sb.append("?");
				params.remove("act");

				String data = params.toString();
				data = data.replace("{", "");
				data = data.replace("}", "");
				data = data.replace(" ", "");
				data = data.replace(",", "&");
				sb.append(data);
				PublicUtil.logDbug(AnsynHttpRequest.TAG, sb.toString(), 0);

				URL url = new URL(sb.toString());
				// 获得URL对象
				connection = (HttpURLConnection) url.openConnection();

				// 获得HttpURLConnection对象
				// connection.setSSLSocketFactory(sslcontext.getSocketFactory());

				connection.setRequestMethod("GET");
				// 默认为GET
				connection.setUseCaches(false);
				// 不使用缓存
				// connection.setRequestProperty("urlencode", "UTF-8");
				connection.setConnectTimeout(REQUEST_TIMEOUT);
				// 设置超时时间
				connection.setReadTimeout(SO_TIMEOUT);
				// 设置读取超时时间
				connection.setDoInput(true);
				// 设置是否从httpUrlConnection读入，默认情况下是true;
				// connection.connect();
				PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
						+ connection.getResponseCode(), 0);
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					// 相应码是否为200
					is = connection.getInputStream();
					// 获得输入流
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "UTF-8"));
					// 包装字节流为字符流
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}

					PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
							+ Encoder.convert(response.toString()), 0);
					JSONArray newArray = new JSONArray();
					// JSONObject object = new JSONObject(response.toString());
					newArray.put(0, getHttpResponseHeader(connection));
					newArray.put(1, Encoder.convert(response.toString()));
					callBack.back(backId, true, false, null, newArray);
				} else {
					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}
					callBack.back(backId, false, true, context.getResources()
							.getString(R.string.network_show_error3), null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				callBack.back(backId, false, true, context.getResources()
						.getString(R.string.network_show_error3), null);
			} finally {
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				if (connection != null) {
					connection.disconnect();
					connection = null;
				}
				if (is != null) {
					try {
						is.close();
						is = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} else if (requestType == AnsynHttpRequest.WEATHERGET) {
			try {

				StringBuilder sb = new StringBuilder();

				sb.append(Constent.WEATHER_GET);
				sb.append("?");
				params.remove("act");
				String data = params.toString();
				data = data.replace("{", "");
				data = data.replace("}", "");
				data = data.replace(" ", "");
				data = data.replace(",", "&");
				sb.append(data);
				PublicUtil.logDbug(AnsynHttpRequest.TAG, sb.toString(), 0);

				URL url = new URL(sb.toString());
				// 获得URL对象
				connection = (HttpURLConnection) url.openConnection();

				// 获得HttpURLConnection对象
				// connection.setSSLSocketFactory(sslcontext.getSocketFactory());

				connection.setRequestMethod("GET");
				// 默认为GET
				connection.setUseCaches(false);
				// 不使用缓存
				// connection.setRequestProperty("urlencode", "UTF-8");
				connection.setConnectTimeout(REQUEST_TIMEOUT);
				// 设置超时时间
				connection.setReadTimeout(SO_TIMEOUT);
				// 设置读取超时时间
				connection.setDoInput(true);
				// 设置是否从httpUrlConnection读入，默认情况下是true;
				// connection.connect();
				PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
						+ connection.getResponseCode(), 0);
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					// 相应码是否为200
					is = connection.getInputStream();
					// 获得输入流
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "UTF-8"));
					// 包装字节流为字符流
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}

					PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
							+ Encoder.convert(response.toString()), 0);
					JSONArray newArray = new JSONArray();
					// JSONObject object = new JSONObject(response.toString());
					newArray.put(0, getHttpResponseHeader(connection));
					newArray.put(1, Encoder.convert(response.toString()));
					callBack.back(backId, true, false, null, newArray);
				} else {
					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}
					callBack.back(backId, false, true, context.getResources()
							.getString(R.string.network_show_error3), null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				callBack.back(backId, false, true, context.getResources()
						.getString(R.string.network_show_error3), null);
			} finally {
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				if (connection != null) {
					connection.disconnect();
					connection = null;
				}
				if (is != null) {
					try {
						is.close();
						is = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} else if (requestType == AnsynHttpRequest.POST) {

			OutputStream os = null;
			StringBuffer body = getParamString(params);

			try {
				byte[] data = body.toString().getBytes("UTF-8");

				// // 信任自己的证书
				// CertificateFactory cf =
				// CertificateFactory.getInstance("X.509");
				// InputStream in = context.getAssets().open("server.crt");
				// Certificate ca = cf.generateCertificate(in);
				//
				// KeyStore keystore = KeyStore.getInstance(KeyStore
				// .getDefaultType());
				// keystore.load(null, null);
				// keystore.setCertificateEntry("ca", ca);
				//
				// String tmfAlgorithm =
				// TrustManagerFactory.getDefaultAlgorithm();
				// TrustManagerFactory tmf = TrustManagerFactory
				// .getInstance(tmfAlgorithm);
				// tmf.init(keystore);
				//
				// // Create an SSLContext that uses our TrustManager
				// SSLContext sslcontext = SSLContext.getInstance("TLS");
				// sslcontext.init(null, tmf.getTrustManagers(), null);
				//
				// // // 信任所有证书
				// // SSLContext sc = SSLContext.getInstance("TLS");
				// // sc.init(null, new TrustManager[] { new MyTrustManager() },
				// // new SecureRandom());
				// // HttpsURLConnection.setDefaultSSLSocketFactory(sc
				// // .getSocketFactory());
				// // HttpsURLConnection
				// // .setDefaultHostnameVerifier(new MyHostnameVerifier());
				// HttpsURLConnection
				// .setDefaultHostnameVerifier(new HostnameVerifier() {
				// public boolean verify(String string, SSLSession ssls) {
				// return true;
				// }
				// });

				URL url = new URL(Constent.URLHEAD);
				// 获得URL对象
				connection = (HttpURLConnection) url.openConnection();
				// 获得HttpURLConnection对象

				// connection.setSSLSocketFactory(sslcontext.getSocketFactory());
				// // /设置信任自己的证书
				//
				// connection.setRequestProperty("Content-Type",
				// "application/json");
				// connection.setRequestProperty("Content-Length",
				// String.valueOf(body.length()));

				connection.setRequestMethod("POST");
				// 设置请求方法为post
				connection.setUseCaches(false);
				// 不使用缓存
				// connection.setConnectTimeout(REQUEST_TIMEOUT);
				// // 设置超时时间
				// connection.setReadTimeout(SO_TIMEOUT);
				// // 设置读取超时时间
				connection.setDoInput(true);
				// 设置是否从httpUrlConnection读入，默认情况下是true;
				connection.setDoOutput(true);
				// 设置为true后才能写入参数

				// // 获取URLConnection对象对应的输出流
				// PrintWriter printWriter = new PrintWriter(
				// connection.getOutputStream());
				// // 发送请求参数
				// printWriter.write(body.toString());
				// // flush输出流的缓冲
				// printWriter.flush();
				// printWriter.close();

				// //
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// connection.setRequestProperty("Content-Length", "0");
				connection.setRequestProperty("Content-Length",
						String.valueOf(data.length));
				// connection.setRequestProperty("Cookie", "");
				// 设置请求头里的信息

				os = connection.getOutputStream();
				os.write(data);
				// 写入参数
				// 刷新、关闭
				os.flush();
				os.close();
				PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
						+ connection.getResponseCode(), 0);
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					// 相应码是否为200
					is = connection.getInputStream();
					// 获得输入流
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "UTF-8"));
					// 包装字节流为字符流
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}

					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}
					String backString = Encoder.convert(response.toString());
					PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
							+ backString, 0);

					if (backString == null || TextUtils.isEmpty(backString)) {
						callBack.back(
								backId,
								false,
								true,
								context.getResources().getString(
										R.string.network_show_error2), null);
					} else {
						JSONArray newArray = new JSONArray();
						// JSONObject object = new
						// JSONObject(response.toString());
						newArray.put(0, getHttpResponseHeader(connection));
						newArray.put(1, Encoder.convert(response.toString()));
						callBack.back(backId, true, false, null, newArray);
					}

				} else {
					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}
					callBack.back(backId, false, true, context.getResources()
							.getString(R.string.network_show_error3), null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				callBack.back(backId, false, true, context.getResources()
						.getString(R.string.network_show_error3), null);
			} finally {
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				// 关闭
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (connection != null) {
					connection.disconnect();
					connection = null;
				}
			}
		} else if (requestType == AnsynHttpRequest.POSTIMAGE) {

			String end = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			String fileName = params.get("mobile") + ".png";
			String uploadFile = params.get("imagepath");

			try {
				// 构造param参数部分的数据内容，格式都是相同的，依次添加param1和param2
				StringBuilder sb = new StringBuilder();
				sb.append(twoHyphens + boundary + end);
				sb.append("Content-Disposition: form-data; name=\"act\"" + end);
				sb.append(end);
				sb.append(params.get("act") + end);

				sb.append(twoHyphens + boundary + end);
				sb.append("Content-Disposition: form-data; name=\"mobile\""
						+ end);
				sb.append(end);
				sb.append(params.get("mobile") + end);

				sb.append(twoHyphens + boundary + end);
				sb.append("Content-Disposition: form-data; name=\"filefor\""
						+ end);
				sb.append(end);
				sb.append(params.get("filefor") + end);

				sb.append(twoHyphens + boundary + end);
				sb.append("Content-Disposition: form-data; name=\"ver\"" + end);
				sb.append(end);
				sb.append(params.get("ver") + end);

				sb.append(twoHyphens + boundary + end);
				sb.append("Content-Disposition: form-data; name=\"_loginkey\""
						+ end);
				sb.append(end);
				sb.append(params.get("_loginkey") + end);

				// 构造要上传文件的前段参数内容，和普通参数一样，在这些设置后就可以紧跟文件内容了。
				sb.append(twoHyphens + boundary + end);
				sb.append("Content-Disposition: form-data; name=\"myFile\"; filename=\""
						+ fileName + "\"" + end);
				sb.append("Content-Type: image/png" + end);
				sb.append(end);

				PublicUtil.logDbug("post", sb.toString(), 0);

				byte[] before = sb.toString().getBytes("UTF-8");
				byte[] after = (twoHyphens + boundary + end).getBytes("UTF-8");
				// // 信任自己的证书
				// CertificateFactory cf =
				// CertificateFactory.getInstance("X.509");
				// InputStream in = context.getAssets().open("server.crt");
				// Certificate ca = cf.generateCertificate(in);
				//
				// KeyStore keystore = KeyStore.getInstance(KeyStore
				// .getDefaultType());
				// keystore.load(null, null);
				// keystore.setCertificateEntry("ca", ca);
				//
				// String tmfAlgorithm =
				// TrustManagerFactory.getDefaultAlgorithm();
				// TrustManagerFactory tmf = TrustManagerFactory
				// .getInstance(tmfAlgorithm);
				// tmf.init(keystore);
				//
				// // Create an SSLContext that uses our TrustManager
				// SSLContext sslcontext = SSLContext.getInstance("TLS");
				// sslcontext.init(null, tmf.getTrustManagers(), null);
				//
				// // // 信任所有证书
				// // SSLContext sc = SSLContext.getInstance("TLS");
				// // sc.init(null, new TrustManager[] { new MyTrustManager() },
				// // new SecureRandom());
				// // HttpsURLConnection.setDefaultSSLSocketFactory(sc
				// // .getSocketFactory());
				// // HttpsURLConnection
				// // .setDefaultHostnameVerifier(new MyHostnameVerifier());
				// HttpsURLConnection
				// .setDefaultHostnameVerifier(new HostnameVerifier() {
				// public boolean verify(String string, SSLSession ssls) {
				// return true;
				// }
				// });
				File file = new File(uploadFile);
				URL url = new URL(Constent.URLHEAD);
				// 获得URL对象
				connection = (HttpURLConnection) url.openConnection();
				// 获得HttpURLConnection对象

				// connection.setSSLSocketFactory(sslcontext.getSocketFactory());
				// // /设置信任自己的证书
				//
				// connection.setRequestProperty("Content-Type",
				// "application/json");
				// connection.setRequestProperty("Content-Length",
				// String.valueOf(body.length()));

				connection.setRequestMethod("POST");
				// 设置请求方法为post
				connection.setUseCaches(false);
				// 不使用缓存
				// connection.setConnectTimeout(REQUEST_TIMEOUT);
				// // 设置超时时间
				// connection.setReadTimeout(SO_TIMEOUT);
				// // 设置读取超时时间
				connection.setDoInput(true);
				// 设置是否从httpUrlConnection读入，默认情况下是true;
				connection.setDoOutput(true);
				// 设置为true后才能写入参数

				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("Charset", "UTF-8");
				connection.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				/* 设置DataOutputStream */
				DataOutputStream ds = new DataOutputStream(
						connection.getOutputStream());

				ds.write(before);
				/* 取得文件的InputStream */
				InputStream fStream = new FileInputStream(file);
				/* 设置每次写入1024bytes */
				byte[] buffer = new byte[1024];
				int length;
				/* 从文件读取数据至缓冲区 */
				while ((length = fStream.read(buffer)) != -1) {
					/* 将资料写入DataOutputStream中 */
					ds.write(buffer, 0, length);
				}
				/* close streams */
				ds.writeBytes(end);
				ds.write(after);
				// ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
				PublicUtil.logDbug(AnsynHttpRequest.TAG,
						"response" + ds.toString(), 0);

				ds.flush();
				fStream.close();
				/* 取得Response内容 */
				ds.close();
				PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
						+ connection.getResponseCode(), 0);
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

					is = connection.getInputStream();
					// 获得输入流
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "UTF-8"));
					// 包装字节流为字符流
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}

					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}
					String backString = Encoder.convert(response.toString());
					PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
							+ backString, 0);
					if (backString == null || TextUtils.isEmpty(backString)) {
						callBack.back(
								backId,
								false,
								true,
								context.getResources().getString(
										R.string.network_show_error2), null);
					} else {
						JSONArray newArray = new JSONArray();
						// JSONObject object = new
						// JSONObject(response.toString());
						newArray.put(0, getHttpResponseHeader(connection));
						newArray.put(1, Encoder.convert(response.toString()));
						callBack.back(backId, true, false, null, newArray);
					}
				} else {
					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}
					callBack.back(backId, false, true, context.getResources()
							.getString(R.string.network_show_error3), null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				callBack.back(backId, false, true, context.getResources()
						.getString(R.string.network_show_error3), null);
			} finally {
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				// 关闭
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (connection != null) {
					connection.disconnect();
					connection = null;
				}
			}
		} else if (requestType == AnsynHttpRequest.POSTIMAGE2) {

			String end = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			String uploadFile = params.get("datapath");

			try {

				String datastr = "Content-Disposition: form-data; name=\""
						+ params.get("request") + "\"; filename=\""
						+ System.currentTimeMillis() + "\"" + end;

				PublicUtil.logDbug("post", datastr, 0);

				StringBuilder sb = new StringBuilder();

				sb.append(Constent.URLHEAD_GET);
				sb.append(params.get("act"));
				sb.append("?");
				params.remove("act");
				params.remove("datapath");
				params.remove("request");

				String data = params.toString();
				data = data.replace("{", "");
				data = data.replace("}", "");
				data = data.replace(" ", "");
				data = data.replace(",", "&");
				sb.append(data);
				PublicUtil.logDbug(AnsynHttpRequest.TAG, sb.toString(), 0);

				URL url = new URL(sb.toString());

				// URL url = new URL(Constent.URLHEAD_GET + params.get("act"));
				// 获得URL对象
				connection = (HttpURLConnection) url.openConnection();
				// 获得HttpURLConnection对象

				connection.setRequestMethod("POST");
				// 设置请求方法为post
				connection.setUseCaches(false);
				// 不使用缓存
				// connection.setConnectTimeout(REQUEST_TIMEOUT);
				// // 设置超时时间
				// connection.setReadTimeout(SO_TIMEOUT);
				// // 设置读取超时时间
				connection.setDoInput(true);
				// 设置是否从httpUrlConnection读入，默认情况下是true;
				connection.setDoOutput(true);
				// 设置为true后才能写入参数

				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("Charset", "UTF-8");
				connection.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				/* 设置DataOutputStream */
				DataOutputStream ds = new DataOutputStream(
						connection.getOutputStream());
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes(datastr);
				ds.writeBytes(end);

				File file = new File(uploadFile);
				PublicUtil.logDbug(AnsynHttpRequest.TAG, file.isFile() + "", 0);
				/* 取得文件的InputStream */
				InputStream fStream = new FileInputStream(file);
				/* 设置每次写入1024bytes */
				byte[] buffer = new byte[1024];
				int length;
				/* 从文件读取数据至缓冲区 */
				while ((length = fStream.read(buffer)) != -1) {
					/* 将资料写入DataOutputStream中 */
					ds.write(buffer, 0, length);
				}

				fStream.close();
				/* close streams */
				ds.writeBytes(end);
				// ds.write(after);
				ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

				ds.flush();

				/* 取得Response内容 */
				ds.close();
				PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
						+ connection.getResponseCode(), 0);
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

					PublicUtil.logDbug(AnsynHttpRequest.TAG, connection
							.getHeaderFields().toString(), 0);

					is = connection.getInputStream();
					// 获得输入流
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "UTF-8"));
					// 包装字节流为字符流
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}

					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}
					String backString = Encoder.convert(response.toString());
					PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
							+ backString, 0);
					if (backString == null || TextUtils.isEmpty(backString)) {
						callBack.back(
								backId,
								false,
								true,
								context.getResources().getString(
										R.string.network_show_error2), null);
					} else {
						JSONArray newArray = new JSONArray();
						// JSONObject object = new
						// JSONObject(response.toString());
						newArray.put(0, getHttpResponseHeader(connection));
						newArray.put(1, Encoder.convert(response.toString()));
						callBack.back(backId, true, false, null, newArray);
					}
				} else {
					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}
					callBack.back(backId, false, true, context.getResources()
							.getString(R.string.network_show_error3), null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				callBack.back(backId, false, true, context.getResources()
						.getString(R.string.network_show_error3), null);
			} finally {
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				// 关闭
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (connection != null) {
					connection.disconnect();
					connection = null;
				}
			}
		} else if (requestType == AnsynHttpRequest.POSTIMAGE3) {

			String end = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			String uploadFile = params.get("datapath");

			try {

				String datastr = "Content-Disposition: form-data; name=\""
						+ params.get("request") + "\"; filename=\""
						+ System.currentTimeMillis() + "\"" + end;

				PublicUtil.logDbug("post", datastr, 0);

				// StringBuilder datasb = new StringBuilder();
				// datasb.append(twoHyphens + boundary + end);
				// datasb.append("Content-Disposition: form-data; name=\"company_name\""
				// + end);
				// datasb.append(end);
				// datasb.append(params.get("company_name") + end);
				//
				// datasb.append(twoHyphens + boundary + end);
				// datasb.append("Content-Disposition: form-data; name=\"car_type\""
				// + end);
				// datasb.append(end);
				// datasb.append(params.get("car_type") + end);
				//
				// datasb.append(twoHyphens + boundary + end);
				// datasb.append("Content-Disposition: form-data; name=\"zc_time\""
				// + end);
				// datasb.append(end);
				// datasb.append(params.get("zc_time") + end);
				//
				// datasb.append(twoHyphens + boundary + end);
				// datasb.append("Content-Disposition: form-data; name=\"zc_num\""
				// + end);
				// datasb.append(end);
				// datasb.append(params.get("zc_num") + end);
				//
				// datasb.append(twoHyphens + boundary + end);
				// datasb.append("Content-Disposition: form-data; name=\"contact_name\""
				// + end);
				// datasb.append(end);
				// datasb.append(params.get("contact_name") + end);
				//
				// datasb.append(twoHyphens + boundary + end);
				// datasb.append("Content-Disposition: form-data; name=\"contact_mobile\""
				// + end);
				// datasb.append(end);
				// datasb.append(params.get("contact_mobile") + end);

				StringBuilder sb = new StringBuilder();

				sb.append(Constent.URLHEAD_GET);
				sb.append(params.get("act"));
				sb.append("?");
				params.remove("act");
				params.remove("datapath");
				params.remove("request");
				// params.remove("company_name");
				// params.remove("car_type");
				// params.remove("zc_time");
				// params.remove("zc_num");
				// params.remove("contact_name");
				// params.remove("contact_mobile");

				String data = params.toString();
				data = data.replace("{", "");
				data = data.replace("}", "");
				data = data.replace(" ", "");
				data = data.replace(",", "&");
				sb.append(data);
				PublicUtil.logDbug(AnsynHttpRequest.TAG, sb.toString(), 0);

				URL url = new URL(sb.toString());

				// URL url = new URL(Constent.URLHEAD_GET + params.get("act"));
				// 获得URL对象
				connection = (HttpURLConnection) url.openConnection();
				// 获得HttpURLConnection对象

				connection.setRequestMethod("POST");
				// 设置请求方法为post
				connection.setUseCaches(false);
				// 不使用缓存
				// connection.setConnectTimeout(REQUEST_TIMEOUT);
				// // 设置超时时间
				// connection.setReadTimeout(SO_TIMEOUT);
				// // 设置读取超时时间
				connection.setDoInput(true);
				// 设置是否从httpUrlConnection读入，默认情况下是true;
				connection.setDoOutput(true);
				// 设置为true后才能写入参数

				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("Charset", "UTF-8");
				connection.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				/* 设置DataOutputStream */
				DataOutputStream ds = new DataOutputStream(
						connection.getOutputStream());
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes(datastr);
				ds.writeBytes(end);

				File file = new File(uploadFile);
				PublicUtil.logDbug(AnsynHttpRequest.TAG, file.isFile() + "", 0);
				/* 取得文件的InputStream */
				InputStream fStream = new FileInputStream(file);
				/* 设置每次写入1024bytes */
				byte[] buffer = new byte[1024];
				int length;
				/* 从文件读取数据至缓冲区 */
				while ((length = fStream.read(buffer)) != -1) {
					/* 将资料写入DataOutputStream中 */
					ds.write(buffer, 0, length);
				}

				fStream.close();
				/* close streams */
				ds.writeBytes(end);
				// ds.write(after);
				// ds.writeBytes(datasb.toString());
				ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

				ds.flush();

				/* 取得Response内容 */
				ds.close();
				PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
						+ connection.getResponseCode(), 0);
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

					PublicUtil.logDbug(AnsynHttpRequest.TAG, connection
							.getHeaderFields().toString(), 0);

					is = connection.getInputStream();
					// 获得输入流
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "UTF-8"));
					// 包装字节流为字符流
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}

					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}
					String backString = Encoder.convert(response.toString());
					PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
							+ backString, 0);
					if (backString == null || TextUtils.isEmpty(backString)) {
						callBack.back(
								backId,
								false,
								true,
								context.getResources().getString(
										R.string.network_show_error2), null);
					} else {
						JSONArray newArray = new JSONArray();
						// JSONObject object = new
						// JSONObject(response.toString());
						newArray.put(0, getHttpResponseHeader(connection));
						newArray.put(1, Encoder.convert(response.toString()));
						callBack.back(backId, true, false, null, newArray);
					}
				} else {
					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}
					callBack.back(backId, false, true, context.getResources()
							.getString(R.string.network_show_error3), null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				callBack.back(backId, false, true, context.getResources()
						.getString(R.string.network_show_error3), null);
			} finally {
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				// 关闭
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (connection != null) {
					connection.disconnect();
					connection = null;
				}
			}
		} else if (requestType == AnsynHttpRequest.POSTIMAGE4) {

			String end = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			String uploadFile = params.get("datapath");
			String uploadFile2 = params.get("datapath2");
			String request = params.get("request");
			String request2 = params.get("request2");

			try {

				// StringBuilder datasb = new StringBuilder();
				// datasb.append(twoHyphens + boundary + end);
				// datasb.append("Content-Disposition: form-data; name=\"name\""
				// + end);
				// datasb.append(end);
				// datasb.append(params.get("name") + end);

				StringBuilder sb = new StringBuilder();

				sb.append(Constent.URLHEAD_GET);
				sb.append(params.get("act"));
				sb.append("?");
				params.remove("act");
				params.remove("datapath");
				params.remove("request");
				params.remove("datapath2");
				params.remove("request2");

				String data = params.toString();
				data = data.replace("{", "");
				data = data.replace("}", "");
				data = data.replace(" ", "");
				data = data.replace(",", "&");
				sb.append(data);
				PublicUtil.logDbug(AnsynHttpRequest.TAG, sb.toString(), 0);

				URL url = new URL(sb.toString());

				// URL url = new URL(Constent.URLHEAD_GET + params.get("act"));
				// 获得URL对象
				connection = (HttpURLConnection) url.openConnection();
				// 获得HttpURLConnection对象

				connection.setRequestMethod("POST");
				// 设置请求方法为post
				connection.setUseCaches(false);
				// 不使用缓存
				// connection.setConnectTimeout(REQUEST_TIMEOUT);
				// // 设置超时时间
				// connection.setReadTimeout(SO_TIMEOUT);
				// // 设置读取超时时间
				connection.setDoInput(true);
				// 设置是否从httpUrlConnection读入，默认情况下是true;
				connection.setDoOutput(true);
				// 设置为true后才能写入参数

				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("Charset", "UTF-8");
				connection.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				/* 设置DataOutputStream */
				DataOutputStream ds = new DataOutputStream(
						connection.getOutputStream());
				ds.writeBytes(twoHyphens + boundary + end);
				String datastr = "Content-Disposition: form-data; name=\""
						+ request + "\"; filename=\""
						+ System.currentTimeMillis() + "\"" + end;
				// 写入一张图片
				PublicUtil.logDbug(AnsynHttpRequest.TAG, datastr, 0);
				ds.writeBytes(datastr);
				ds.writeBytes(end);

				File file = new File(uploadFile);
				PublicUtil.logDbug(AnsynHttpRequest.TAG, file.isFile() + "", 0);
				/* 取得文件的InputStream */
				InputStream fStream = new FileInputStream(file);
				/* 设置每次写入1024bytes */
				byte[] buffer = new byte[1024];
				int length;
				/* 从文件读取数据至缓冲区 */
				while ((length = fStream.read(buffer)) != -1) {
					/* 将资料写入DataOutputStream中 */
					ds.write(buffer, 0, length);
				}

				fStream.close();
				/* close streams */
				ds.writeBytes(end);
				ds.writeBytes(twoHyphens + boundary + end);
				String datastr2 = "Content-Disposition: form-data; name=\""
						+ request2 + "\"; filename=\""
						+ System.currentTimeMillis() + "\"" + end;

				PublicUtil.logDbug(AnsynHttpRequest.TAG, datastr2, 0);
				// 写入另一张图片
				ds.writeBytes(datastr2);
				ds.writeBytes(end);

				File file2 = new File(uploadFile2);
				PublicUtil
						.logDbug(AnsynHttpRequest.TAG, file2.isFile() + "", 0);
				/* 取得文件的InputStream */
				InputStream fStream2 = new FileInputStream(file2);
				/* 设置每次写入1024bytes */
				byte[] buffer2 = new byte[1024];
				int length2;
				/* 从文件读取数据至缓冲区 */
				while ((length2 = fStream2.read(buffer2)) != -1) {
					/* 将资料写入DataOutputStream中 */
					ds.write(buffer2, 0, length2);
				}

				fStream2.close();
				/* close streams */
				ds.writeBytes(end);

				// 写入其他参数
				// ds.writeBytes(datasb.toString());
				ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

				ds.flush();

				/* 取得Response内容 */
				ds.close();
				PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
						+ connection.getResponseCode(), 0);
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

					PublicUtil.logDbug(AnsynHttpRequest.TAG, connection
							.getHeaderFields().toString(), 0);

					is = connection.getInputStream();
					// 获得输入流
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "UTF-8"));
					// 包装字节流为字符流
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}

					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}
					String backString = Encoder.convert(response.toString());
					PublicUtil.logDbug(AnsynHttpRequest.TAG, "response"
							+ backString, 0);
					if (backString == null || TextUtils.isEmpty(backString)) {
						callBack.back(
								backId,
								false,
								true,
								context.getResources().getString(
										R.string.network_show_error2), null);
					} else {
						JSONArray newArray = new JSONArray();
						// JSONObject object = new
						// JSONObject(response.toString());
						newArray.put(0, getHttpResponseHeader(connection));
						newArray.put(1, Encoder.convert(response.toString()));
						callBack.back(backId, true, false, null, newArray);
					}
				} else {
					if (isShowDialog && mHandler != null) {
						mHandler.sendEmptyMessage(HIDEDIALOG);
					}
					callBack.back(backId, false, true, context.getResources()
							.getString(R.string.network_show_error3), null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				callBack.back(backId, false, true, context.getResources()
						.getString(R.string.network_show_error3), null);
			} finally {
				if (isShowDialog && mHandler != null) {
					mHandler.sendEmptyMessage(HIDEDIALOG);
				}
				// 关闭
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (connection != null) {
					connection.disconnect();
					connection = null;
				}
			}
		}
	}

	/**
	 * 获得请求返回头
	 *
	 * @param http
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private Map<String, String> getHttpResponseHeader(HttpURLConnection http)
			throws UnsupportedEncodingException {
		Map<String, String> header = new LinkedHashMap<String, String>();
		for (int i = 0;; i++) {
			String mine = http.getHeaderField(i);
			if (mine == null)
				break;
			header.put(http.getHeaderFieldKey(i), mine);
		}
		return header;
	}

	/**
	 * post请求用来拼接参数的
	 *
	 * @param params
	 * @return
	 */
	private StringBuffer getParamString(Map<String, String> params) {
		StringBuffer result = new StringBuffer();
		Iterator<Map.Entry<String, String>> iterator = params.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> param = iterator.next();
			String key = param.getKey();
			String value = param.getValue();
			result.append(key).append('=').append(value);
			if (iterator.hasNext()) {
				result.append('&');
			}
		}
		return result;
	}

	/**
	 * 显示ProgressDialog
	 *
	 */
	public void showProgressDialog() {

		if (context != null) {
			try {
				dialog = ProgressDialog.show(context, null, "加载中..");
				dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置风格为圆形进度条
				dialog.setCancelable(true);// 设置点击返回键对话框消失
				dialog.setCanceledOnTouchOutside(true);// 设置点击进度对话框外的区域对话框消失
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

}
