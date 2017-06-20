package com.haofeng.apps.dst.ui;

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
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JiaZhaoRZActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "JiaZhaoRZActivity";
    private FrameLayout topLayout;
    private TextView backView;

    private ImageView jiazhaoImageView, jiazhaoImageView2;
    private TextView okTextView;
    private TextView inforView;

    private static final int RESULT_CAMERA = 100;// 相机
    private static final int RESULT_LOAD_IMAGE = 200;// 相册
    private String filePath = null;
    private String sdFilePath = null;
    private File tempFile = null;
    private String uid = AnsynHttpRequest.userid;
    private boolean isPostJiazhaoImagesuccess = true;
    private String infor = "本人有效机动车驾驶证;确保正副页拍在一张照片中;拍摄时确保驾驶证边框完整,字体清晰,亮度均匀";
    private String drive_auth = "3";// 权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiazhaorz);
       addActivity(this);
        init();

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        File sdcardstr = new File("/mnt/sdcard");
        if (sdcardstr != null && sdcardstr.exists()
                && sdcardstr.getAbsolutePath().contains("sdcard")) {
            sdFilePath = sdcardstr.getAbsolutePath() + "/dst/";
        } else {
            sdFilePath = null;
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
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if (!isPostJiazhaoImagesuccess) {
            String picpath = filePath + uid + "dirve.png";
            File file = new File(picpath);
            if (file.exists()) {
                file.delete();
            }

        }
    }

    /*
     * 初始化组件
     */
    public void init() {

        topLayout = (FrameLayout) findViewById(R.id.jiazhaorz_top_layout);
       setTopLayoutPadding(topLayout);

        backView = (TextView) findViewById(R.id.jiazhaorz_back);
        inforView = (TextView) findViewById(R.id.jiazhaorz_shenfenzheng_infor);

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(infor);
        stringBuilder.setSpan(
                new ForegroundColorSpan(getResources().getColor(
                        R.color.textgreen)), 0, 2,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.gray)),
                2, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(
                new ForegroundColorSpan(getResources().getColor(
                        R.color.textgreen)), 13, 24,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.gray)),
                24, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(
                new ForegroundColorSpan(getResources().getColor(
                        R.color.textgreen)), 32, 46,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        inforView.setText(stringBuilder);

        jiazhaoImageView = (ImageView) findViewById(R.id.jiazhaorz_shenfenzheng_image);
        jiazhaoImageView2 = (ImageView) findViewById(R.id.jiazhaorz_shenfenzheng_image2);
        jiazhaoImageView2.setOnClickListener(this);
        okTextView = (TextView) findViewById(R.id.jiazhaorz_ok);
        backView.setOnClickListener(this);
        jiazhaoImageView.setOnClickListener(this);
        okTextView.setOnClickListener(this);

        drive_auth = getIntent().getStringExtra("drive_auth");
        if ("1".equals(drive_auth) || "0".equals(drive_auth)) {
            okTextView
                    .setBackgroundResource(R.drawable.chongdianyemian5_1_03gary);
        }
        filePath = this.getFilesDir().getAbsolutePath() + "/imags/";

        handler.sendEmptyMessage(showimage);
        String driverimageString = PublicUtil.getStorage_string(
                JiaZhaoRZActivity.this, "driving_card_url", "0");

        if (!TextUtils.isEmpty(driverimageString)) {
            downloadDriveimage(driverimageString);
        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.jiazhaorz_back:

                finish();
                break;

            case R.id.jiazhaorz_shenfenzheng_image:
                //判断是否已经认证
                if (!"1".equals(drive_auth) && !"0".equals(drive_auth)) {
                    showMenu();
                }
                break;
            case R.id.jiazhaorz_shenfenzheng_image2:
                //判断是否已经认证
                if (!"1".equals(drive_auth) && !"0".equals(drive_auth)) {
                    showMenu();
                }
                break;

            case R.id.jiazhaorz_ok:

                if ("0".equals(drive_auth)) {
                    PublicUtil.showToast(JiaZhaoRZActivity.this,
                            "您已提交过认证资料，不能重复提交哦！", false);
                } else if ("1".equals(drive_auth)) {
                    PublicUtil.showToast(JiaZhaoRZActivity.this, "您已完成认证，不能重复认证哦！",
                            false);
                } else {
                    postImage();
                }
                break;

            default:
                break;
        }

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
                JiaZhaoRZActivity.this).setView(view).show();

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
                    PublicUtil.showToast(JiaZhaoRZActivity.this,
                            "获取手机内存失败，无法启动照相功能。请从相册选择图片", false);
                    return;
                }

                File file = new File(sdFilePath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                tempFile = new File(sdFilePath + uid + "dirve.png");

                if (!tempFile.exists()) {

                    try {
                        tempFile.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        // 创建文件失败
                        PublicUtil.showToast(JiaZhaoRZActivity.this,
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
            case RESULT_CAMERA:// 照相返回

                if (resultCode == RESULT_OK) {
                    if (tempFile == null) {

                        tempFile = new File(sdFilePath + uid + "dirve.png");

                    }
                    uri = Uri.fromFile(tempFile);
                    Bitmap cropBitmap = getBitmapFromUri(uri);
                    if (cropBitmap != null) {
                        cropBitmap = comp(cropBitmap);
                        saveFile(cropBitmap);
                    }
                }

                break;

            case RESULT_LOAD_IMAGE:
                if (resultCode == RESULT_OK && data != null) {
                    uri = data.getData();
                    Bitmap cropBitmap = getBitmapFromUri(uri);
                    if (cropBitmap != null) {
                        cropBitmap = comp(cropBitmap);
                        saveFile(cropBitmap);
                    }
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
                    JiaZhaoRZActivity.this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 图片比例压缩
     *
     * @param image
     * @return
     */
    private Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // // 显示图片的为一个横向的imageview
        // float hh = 480f;// 这里设置高度为800f
        // float ww = 800f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片质量压缩
     *
     * @param image
     * @return
     */
    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 下载驾照图片
     *
     * @param imagepath
     */
    private void downloadDriveimage(final String imagepath) {

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
                            saveFile(arg2);
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
     */
    private void saveFile(final Bitmap bm) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                File temp = new File(filePath);// 先检查文件夹存在
                if (!temp.exists()) {
                    temp.mkdir();
                }

                tempFile = new File(filePath + uid + "dirve.png");

                if (tempFile.exists()) {
                    tempFile.delete();
                }

                try {
                    FileOutputStream outStreamz = new FileOutputStream(tempFile);
                    bm.compress(Bitmap.CompressFormat.PNG, 50, outStreamz);
                    outStreamz.close();
                    PublicUtil.logDbug(TAG, "@@@" + bm.getHeight(), 0);

                    handler.sendEmptyMessage(showimage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    /**
     * 上传图片
     */
    private void postImage() {
        String drivepicpath = filePath + uid + "dirve.png";
        File dFile = new File(drivepicpath);

        if (!dFile.exists()) {
            PublicUtil.showToast(JiaZhaoRZActivity.this, "请上传驾驶证照片", false);
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_AUTH_DRIVING_CARD_AUTH);
        map.put("datapath", drivepicpath);
        map.put("request", "driving_card");

        AnsynHttpRequest.httpRequest(JiaZhaoRZActivity.this,
                AnsynHttpRequest.POSTIMAGE2, callBack,
                Constent.ID_AUTH_DRIVING_CARD_AUTH, map, false, true, true);

    }

    private JSONObject jsonObject = null;
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, boolean isRequestSuccess,
                         boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub

            if (backId == Constent.ID_AUTH_DRIVING_CARD_AUTH) {

                if (isRequestSuccess) {
                    if (!isString) {

                        try {
                            String backstr = jsonArray.getString(1);
                            jsonObject = new JSONObject(backstr);
                            handler.sendEmptyMessage(success_http_auth_driving);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                        }
                    }
                } else {
                    Message message = new Message();
                    message.what = error_http_auth_driving;
                    message.obj = data;
                    handler.sendMessage(message);
                }

            }

        }
    };

    private int showimage = 0x6902;

    private int error_http_auth_driving = 0x6903;
    private int success_http_auth_driving = 0x6904;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == showimage) {

                String drivepicpath = filePath + uid + "dirve.png";

                File dFile = new File(drivepicpath);
                PublicUtil.logDbug(TAG, dFile.exists() + ":" + dFile.length(),
                        0);
                if (dFile.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(drivepicpath);
                    PublicUtil.logDbug(TAG, bm + "", 0);
                    jiazhaoImageView2.setBackgroundResource(0);
                    jiazhaoImageView2.setImageBitmap(bm);
                    jiazhaoImageView2.setVisibility(View.VISIBLE);
                    jiazhaoImageView.setVisibility(View.GONE);

                } else {
                    jiazhaoImageView2.setVisibility(View.GONE);
                    jiazhaoImageView.setVisibility(View.VISIBLE);
                }

            }

            if (msg != null && msg.what == success_http_auth_driving) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            PublicUtil.showToast(JiaZhaoRZActivity.this,
                                    "提交认证成功", false);
                            Intent intent = new Intent();
                            intent.putExtra("result", "success");
                            JiaZhaoRZActivity.this.setResult(22, intent);
                            finish();

                        } else {
                            PublicUtil.showToast(JiaZhaoRZActivity.this,
                                    jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(
                                        JiaZhaoRZActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);

                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception

                        e.printStackTrace();
                    }

                }

            } else if (msg != null && msg.what == error_http_auth_driving) {
                if (msg.obj != null) {
                    PublicUtil.showToast(JiaZhaoRZActivity.this,
                            msg.obj.toString(), false);
                    isPostJiazhaoImagesuccess = false;

                }
            }

        }

        ;
    };

}
