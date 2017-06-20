package com.haofeng.apps.dst.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.application.MyApplication;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 身份验证
 * 
 * @author Administrator
 * 
 */
public class AuthenticaActivity extends BaseActivity implements OnClickListener {
	private static String TAG = "AuthenticaActivity";
	private static final int RESULT_CAMERA = 100;// 相机
	private static final int RESULT_LOAD_IMAGE = 200;// 相册
	private static final int RESULT_SAVE = 300;// 裁剪后保存
	private String filePath = null;
	private String sdFilePath = null;
	private File tempFile = null;
	private ImageView imageView;
	private Button okButton;
	private TextView backTextView;
	private String phone = null;
	private FrameLayout imageFrameLayout;
	private TextView infor, infor2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentica);
		addActivity(this);
		init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 初始化
	 */
	private void init() {
		imageView = (ImageView) findViewById(R.id.authentica_imageview);
		okButton = (Button) findViewById(R.id.authentica_okbutton);
		backTextView = (TextView) findViewById(R.id.authentica_back);
		imageFrameLayout = (FrameLayout) findViewById(R.id.authentica_imagelayout);
		infor = (TextView) findViewById(R.id.authentica_infor1);
		infor2 = (TextView) findViewById(R.id.authentica_infor2);

		imageFrameLayout.setOnClickListener(this);
		imageView.setOnClickListener(this);
		okButton.setOnClickListener(this);
		backTextView.setOnClickListener(this);
		imageView.setBackgroundResource(R.drawable.id_frame);
		filePath = this.getFilesDir().getAbsolutePath() + "/imags/";
		if (phone == null) {
			phone = PublicUtil.getStorage_string(AuthenticaActivity.this,
					"phone", "-1");
		}

		if ("-1".equals(phone)) {

			PublicUtil.showToast(AuthenticaActivity.this,
					"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

		} else {

			handler.sendEmptyMessage(showimage);
			String driverimageString = PublicUtil.getStorage_string(
					AuthenticaActivity.this, "drive_image", "0");
			PublicUtil.logDbug(TAG, driverimageString, 0);
			if (!"0".equals(driverimageString) && driverimageString != null) {
				infor.setVisibility(View.GONE);
				infor2.setVisibility(View.GONE);
				download(driverimageString);
			} else {
				infor.setVisibility(View.VISIBLE);
				infor2.setVisibility(View.VISIBLE);
			}
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		File sdcardstr = new File("/mnt/sdcard");// Environment.getExternalStorageDirectory();
		// System.out.println("Environment.getExternalStorageDirectory()=="+Environment.getExternalStorageDirectory());
		if (sdcardstr != null && sdcardstr.exists()
				&& sdcardstr.getAbsolutePath().contains("sdcard")) {
			sdFilePath = sdcardstr.getAbsolutePath() + "/dst/";
		} else {
			sdFilePath = null;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (!isPostImagesuccess) {
			String picpath = filePath + phone + ".png";
			File file = new File(picpath);
			if (file.exists()) {
				file.delete();
			}

		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.authentica_imageview:
		case R.id.authentica_imagelayout:
			showMenu();
			break;
		case R.id.authentica_okbutton:
			postImage();
			break;
		case R.id.authentica_back:
			finish();
			break;

		default:
			break;
		}

	}

	/**
	 * 显示选择分类
	 */
	public void showMenu() {

		View view = LayoutInflater.from(this).inflate(
				R.layout.view_image_dialog, null);
		TextView cameraTextView, photosTextView, cancelTextView;
		cameraTextView = (TextView) view
				.findViewById(R.id.imagedialog_cameratextview);
		photosTextView = (TextView) view
				.findViewById(R.id.imagedialog_photostextview);
		cancelTextView = (TextView) view
				.findViewById(R.id.imagedialog_canceltextview);

		final AlertDialog alertDialog = new AlertDialog.Builder(
				AuthenticaActivity.this).setView(view).show();

		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);
		cameraTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
				// TODO Auto-generated method stub
				if (sdFilePath == null) {
					PublicUtil.showToast(AuthenticaActivity.this,
							"无法获取手机存储目录，不能使用照相功能", false);
					return;
				}

				File file = new File(sdFilePath);
				if (!file.exists()) {
					file.mkdirs();
				}

				tempFile = new File(sdFilePath + "imag.png");
				if (!tempFile.exists()) {

					try {
						tempFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						// 创建文件失败
						PublicUtil.showToast(AuthenticaActivity.this,
								"照片路径创建失败", false);
						return;
					}
				} else {
					tempFile.delete();
				}

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 下面这句指定调用相机拍照后的照片存储的路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
				startActivityForResult(intent, RESULT_CAMERA);
			}
		});
		photosTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, RESULT_LOAD_IMAGE);
			}
		});
		cancelTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
			}
		});

	}

	private Uri uri = null;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		switch (requestCode) {
		case RESULT_CAMERA:

			if (resultCode == RESULT_OK) {
				if (tempFile == null) {
					tempFile = new File(sdFilePath + "imag.png");
				}
				uri = Uri.fromFile(tempFile);
				startPhotoZoom(uri);
			}

			break;

		case RESULT_LOAD_IMAGE:
			if (resultCode == RESULT_OK && data != null) {
				uri = data.getData();
				startPhotoZoom(uri);
			}

			break;
		case RESULT_SAVE:
			// if (data != null) {
			// saveFile(data.getExtras());
			// }
			if (uri == null) {
				break;
			}
			Bitmap cropBitmap = getBitmapFromUri(uri);
			if (cropBitmap != null) {
				saveFile(cropBitmap);
			}

			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public Bitmap getBitmapFromUri(Uri uri) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					AuthenticaActivity.this.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.id_frame);// 这里给定一个已有的图片的大小
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		// aspectX aspectY 是裁剪框宽高的比例
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", bitmap.getWidth());
		intent.putExtra("outputY", bitmap.getHeight());
		// intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);// 设置为不返回数据

		startActivityForResult(intent, RESULT_SAVE);
	}

	/**
	 * 图片保存
	 * 
	 * @param bundle
	 */
	private void saveFile(final Bitmap photo) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				File temp = new File(filePath);// 先检查文件夹存在
				if (!temp.exists()) {
					temp.mkdir();
				}
				tempFile = new File(filePath + phone + ".png");
				if (tempFile.exists()) {
					tempFile.delete();
				}

				try {
					FileOutputStream outStreamz = new FileOutputStream(tempFile);
					photo.compress(Bitmap.CompressFormat.PNG, 50, outStreamz);
					outStreamz.close();

					handler.sendEmptyMessage(upload_showimage);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

	private boolean isPostImagesuccess = false;

	/**
	 * 上传图片
	 */
	public void postImage() {

		if (phone == null) {
			phone = PublicUtil.getStorage_string(AuthenticaActivity.this,
					"phone", "-1");
		}

		if ("-1".equals(phone)) {

			PublicUtil.showToast(AuthenticaActivity.this,
					"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("act", Constent.ACT_IMAGEPOST);
			map.put("mobile", phone);
			map.put("filefor", "jiaZhao");
			map.put("imagepath", filePath + phone + ".png");
			map.put("ver", Constent.VER);

			AnsynHttpRequest.httpRequest(AuthenticaActivity.this,
					AnsynHttpRequest.POSTIMAGE, callBack,
					Constent.ID_IMAGEPOST, map, false, true, true);
		}

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
			case Constent.ID_IMAGEPOST:
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

	private int httprequesterror = 0x2400;
	private int httprequestsuccess = 0x2401;
	private int showimage = 0x2402;
	private int upload_showimage = 0x2403;// 裁剪完图片后显示
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == showimage) {
				String picpath = filePath + phone + ".png";
				PublicUtil.logDbug(TAG, picpath, 0);
				File file = new File(picpath);
				PublicUtil.logDbug(TAG, file.exists() + "", 0);
				if (file.exists()) {
					infor.setVisibility(View.GONE);
					infor2.setVisibility(View.GONE);
					Bitmap bm = BitmapFactory.decodeFile(picpath);
					PublicUtil.logDbug(TAG, bm + "", 0);
					imageView.setBackgroundResource(0);
					imageView.setImageBitmap(bm);
					isPostImagesuccess = true;
				} else {
					infor.setVisibility(View.VISIBLE);
					infor2.setVisibility(View.VISIBLE);
				}

			} else if (msg != null && msg.what == upload_showimage) {
				String picpath = filePath + phone + ".png";
				File file = new File(picpath);
				if (file.exists()) {
					Bitmap bm = BitmapFactory.decodeFile(picpath);
					imageView.setBackgroundResource(0);
					imageView.setImageBitmap(bm);
					okButton.setVisibility(View.VISIBLE);
					isPostImagesuccess = false;
				}

			} else if (msg != null && msg.what == httprequestsuccess) {
				if (jsonObject != null) {

					try {

						if ("0".equals(jsonObject.get("error").toString())) {
							PublicUtil.showToast(AuthenticaActivity.this,
									jsonObject.get("msg").toString()
											+ "后台工作人员将审核你上传的图片", false);
							okButton.setVisibility(View.INVISIBLE);
							isPostImagesuccess = true;

							if (jsonObject.getString("filePath") != null
									&& !TextUtils.isEmpty(jsonObject
											.getString("filePath"))) {
								PublicUtil.setStorage_string(
										AuthenticaActivity.this, "drive_image",
										jsonObject.getString("filePath"));
							}

						} else {
							PublicUtil.showToast(AuthenticaActivity.this,
									jsonObject.get("msg").toString(), false);
							isPostImagesuccess = false;
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(AuthenticaActivity.this,
								"配置解析数据错误，请退出重试", false);
						isPostImagesuccess = false;
					}

				}
			} else if (msg != null && msg.what == httprequesterror) {
				if (msg.obj != null) {
					PublicUtil.showToast(AuthenticaActivity.this,
							msg.obj.toString(), false);
					isPostImagesuccess = false;

				}
			}
		};
	};

	public void download(final String imagepath) {
		// if (options == null) {
		// initOptions();
		// }
		// ImageLoader.getInstance().displayImage(imagepath, imageView,
		// options);
		ImageLoader.getInstance().loadImage(imagepath,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						// TODO Auto-generated method stub
						PublicUtil.logDbug(TAG, arg2 + "", 0);
						if (arg2 != null) {
							saveFile2(arg2);
						}

					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}
				});

	}

	/**
	 * 图片保存
	 * 
	 * @param bundle
	 */
	private void saveFile2(final Bitmap bm) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				File temp = new File(filePath);// 先检查文件夹存在
				if (!temp.exists()) {
					temp.mkdir();
				}
				tempFile = new File(filePath + phone + ".png");
				if (tempFile.exists()) {
					tempFile.delete();
				}

				try {
					FileOutputStream outStreamz = new FileOutputStream(tempFile);
					bm.compress(Bitmap.CompressFormat.PNG, 50, outStreamz);
					outStreamz.close();

					handler.sendEmptyMessage(showimage);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

}
