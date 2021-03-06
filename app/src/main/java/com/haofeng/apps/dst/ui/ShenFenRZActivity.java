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

public class ShenFenRZActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "ShenFenRZActivity";
    private FrameLayout topLayout;
    private TextView backView;

    private ImageView shenfenImageView_top, shenfenImageView_top2,
            shenfenImageView_bottom, shenfenImageView_bottom2;
    private TextView okTextView;
    private TextView inforView;

    private static final int RESULT_CAMERA = 100;// 相机
    private static final int RESULT_LOAD_IMAGE = 200;// 相册
    private String filePath = null;
    private String sdFilePath = null;
    private File tempFile = null;
    private String uid = AnsynHttpRequest.userid;
    private boolean isPostShenfenImagesuccess = true,
            isPostShenfenImagesuccess2 = true;//
    private int imagetype = 1;// 1正面 2反面
    private String infor = "本人有效二代身份证;拍摄时确保身份证边框完整,字体清晰,亮度均匀";
    private String id_auth = "3";// 权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenfenrz);
        addActivity(this);
        init();

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

        if (!isPostShenfenImagesuccess) {
            String picpath = filePath + uid + "uid.png";
            File file = new File(picpath);
            if (file.exists()) {
                file.delete();
            }

        }

        if (!isPostShenfenImagesuccess2) {

            String picpath2 = filePath + uid + "uid2.png";
            File file2 = new File(picpath2);
            if (file2.exists()) {
                file2.delete();
            }

        }
    }

    /*
     * 初始化组件
     */
    public void init() {

        topLayout = (FrameLayout) findViewById(R.id.shenfenrz_top_layout);
        setTopLayoutPadding(topLayout);
        backView = (TextView) findViewById(R.id.shenfenrz_back);
        inforView = (TextView) findViewById(R.id.shenfenrz_shenfenzheng_infor);

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(infor);
        stringBuilder.setSpan(
                new ForegroundColorSpan(getResources().getColor(
                        R.color.textgreen)), 0, 2,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.gray)),
                2, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(
                new ForegroundColorSpan(getResources().getColor(
                        R.color.textgreen)), 18, 32,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        inforView.setText(stringBuilder);

        shenfenImageView_top = (ImageView) findViewById(R.id.shenfenrz_shenfenzheng_image_top);
        shenfenImageView_top2 = (ImageView) findViewById(R.id.shenfenrz_shenfenzheng_image_top2);
        shenfenImageView_bottom = (ImageView) findViewById(R.id.shenfenrz_shenfenzheng_image_bottom);
        shenfenImageView_bottom2 = (ImageView) findViewById(R.id.shenfenrz_shenfenzheng_image_bottom2);

        okTextView = (TextView) findViewById(R.id.shenfenrz_ok);
        backView.setOnClickListener(this);
        shenfenImageView_top.setOnClickListener(this);
        shenfenImageView_bottom.setOnClickListener(this);
        okTextView.setOnClickListener(this);
        shenfenImageView_bottom2.setOnClickListener(this);
        shenfenImageView_top2.setOnClickListener(this);
        id_auth = getIntent().getStringExtra("id_auth");
        if ("1".equals(id_auth) || "0".equals(id_auth)) {
            okTextView
                    .setBackgroundResource(R.drawable.chongdianyemian5_1_03gary);
        }
        filePath = this.getFilesDir().getAbsolutePath() + "/imags/";

        handler.sendEmptyMessage(showimage);
        String useridimageString = PublicUtil.getStorage_string(
                ShenFenRZActivity.this, "id_card_url", "0");
        String useridimageString2 = PublicUtil.getStorage_string(
                ShenFenRZActivity.this, "id_card_url1", "0");

        PublicUtil.logDbug(TAG, useridimageString2 + ":" + useridimageString
                + ":" + uid, 0);

        if (!TextUtils.isEmpty(useridimageString2)) {
            downloadUidimage2(useridimageString2);
        }

        if (!TextUtils.isEmpty(useridimageString)) {
            downloadUidimage(useridimageString);
        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.shenfenrz_back:

                finish();
                break;

            case R.id.shenfenrz_shenfenzheng_image_top:
                if ("0".equals(id_auth)) {
                    PublicUtil.showToast(ShenFenRZActivity.this,
                            "您已提交过认证资料，不能重复提交哦！", false);
                } else if ("1".equals(id_auth)) {
                    PublicUtil.showToast(ShenFenRZActivity.this, "您已完成认证，不能重复认证哦！",
                            false);
                } else {
                    imagetype = 1;
                    showMenu();
                }
                break;
            case R.id.shenfenrz_shenfenzheng_image_top2:
                if ("0".equals(id_auth)) {
                    PublicUtil.showToast(ShenFenRZActivity.this,
                            "您已提交过认证资料，不能重复提交哦！", false);
                } else if ("1".equals(id_auth)) {
                    PublicUtil.showToast(ShenFenRZActivity.this, "您已完成认证，不能重复认证哦！",
                            false);
                } else {
                    imagetype = 1;
                    showMenu();
                }
                break;

            case R.id.shenfenrz_shenfenzheng_image_bottom:
                if ("0".equals(id_auth)) {
                    PublicUtil.showToast(ShenFenRZActivity.this,
                            "您已提交过认证资料，不能重复提交哦！", false);
                } else if ("1".equals(id_auth)) {
                    PublicUtil.showToast(ShenFenRZActivity.this, "您已完成认证，不能重复认证哦！",
                            false);
                } else {
                    imagetype = 2;
                    showMenu();
                }
                break;
            case R.id.shenfenrz_shenfenzheng_image_bottom2:
                if ("0".equals(id_auth)) {
                    PublicUtil.showToast(ShenFenRZActivity.this,
                            "您已提交过认证资料，不能重复提交哦！", false);
                } else if ("1".equals(id_auth)) {
                    PublicUtil.showToast(ShenFenRZActivity.this, "您已完成认证，不能重复认证哦！",
                            false);
                } else {
                    imagetype = 2;
                    showMenu();
                }
                break;
            case R.id.shenfenrz_ok:

                if ("0".equals(id_auth)) {
                    PublicUtil.showToast(ShenFenRZActivity.this,
                            "您已提交过认证资料，不能重复提交哦！", false);
                } else if ("1".equals(id_auth)) {
                    PublicUtil.showToast(ShenFenRZActivity.this, "您已完成认证，不能重复认证哦！",
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
                ShenFenRZActivity.this).setView(view).show();

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
                    PublicUtil.showToast(ShenFenRZActivity.this,
                            "获取手机内存失败，无法启动照相功能。请从相册选择图片", false);
                    return;
                }

                File file = new File(sdFilePath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                if (imagetype == 2) {// 反面
                    tempFile = new File(sdFilePath + uid + "uid2.png");
                } else {// 身份证
                    tempFile = new File(sdFilePath + uid + "uid.png");
                }

                if (!tempFile.exists()) {

                    try {
                        tempFile.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        // 创建文件失败
                        PublicUtil.showToast(ShenFenRZActivity.this,
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

                        if (imagetype == 2) {// 反面
                            tempFile = new File(sdFilePath + uid + "uid2.png");
                        } else {// 身份证
                            tempFile = new File(sdFilePath + uid + "uid.png");
                        }

                    }
                    uri = Uri.fromFile(tempFile);
                    Bitmap cropBitmap = getBitmapFromUri(uri);
                    if (cropBitmap != null) {
                        cropBitmap = comp(cropBitmap);
                        saveFile(cropBitmap, imagetype);
                    }
                }

                break;

            case RESULT_LOAD_IMAGE:
                if (resultCode == RESULT_OK && data != null) {
                    uri = data.getData();
                    Bitmap cropBitmap = getBitmapFromUri(uri);
                    if (cropBitmap != null) {
                        cropBitmap = comp(cropBitmap);
                        saveFile(cropBitmap, imagetype);
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
                    ShenFenRZActivity.this.getContentResolver(), uri);
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
     * 下载身份证背面
     *
     * @param imagepath
     */
    private void downloadUidimage2(final String imagepath) {

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
                            saveFile(arg2, 2);
                        }

                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        // TODO Auto-generated method stub

                    }
                });

    }

    /**
     * 下载身份证图片
     *
     * @param imagepath
     */
    private void downloadUidimage(final String imagepath) {

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
                            saveFile(arg2, 1);
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
    private void saveFile(final Bitmap bm, final int type) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                File temp = new File(filePath);// 先检查文件夹存在
                if (!temp.exists()) {
                    temp.mkdir();
                }

                if (type == 2) {// 反面
                    tempFile = new File(filePath + uid + "uid2.png");
                } else {// 正面
                    tempFile = new File(filePath + uid + "uid.png");

                }

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

    /**
     * 上传图片
     */
    private void postImage() {
        String uid2picpath = filePath + uid + "uid2.png";
        String uidpicpath = filePath + uid + "uid.png";
        File uFile = new File(uidpicpath);
        File uFile2 = new File(uid2picpath);

        if (!uFile.exists()) {
            PublicUtil.showToast(ShenFenRZActivity.this, "请上传身份证照片", false);
            return;
        }

        if (!uFile2.exists()) {
            PublicUtil.showToast(ShenFenRZActivity.this, "请上传身份证背面照片", false);
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_AUTH_ID_CARD_AUTH);
        map.put("ver", Constent.VER);
        map.put("datapath", uidpicpath);
        map.put("request", "id_card");
        map.put("datapath2", uid2picpath);
        map.put("request2", "id_card1");

        AnsynHttpRequest.httpRequest(ShenFenRZActivity.this,
                AnsynHttpRequest.POSTIMAGE4, callBack,
                Constent.ID_AUTH_ID_CARD_AUTH, map, false, true, true);

    }

    private JSONObject jsonObject = null;
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, boolean isRequestSuccess,
                         boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub

            if (backId == Constent.ID_AUTH_ID_CARD_AUTH) {

                if (isRequestSuccess) {
                    if (!isString) {

                        try {
                            String backstr = jsonArray.getString(1);
                            jsonObject = new JSONObject(backstr);
                            handler.sendEmptyMessage(success_http_auth_id);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                        }
                    }
                } else {
                    Message message = new Message();
                    message.what = error_http_auth_id;
                    message.obj = data;
                    handler.sendMessage(message);
                }

            }

        }
    };

    private int showimage = 0x6802;
    private int error_http_auth_id = 0x6803;
    private int success_http_auth_id = 0x6804;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == showimage) {

                String uid2picpath = filePath + uid + "uid2.png";
                String uidpicpath = filePath + uid + "uid.png";

                PublicUtil.logDbug(TAG, uidpicpath + ":" + uid2picpath, 0);

                File dFile = new File(uidpicpath);
                PublicUtil.logDbug(TAG, dFile.exists() + ":" + dFile.length(),
                        0);
                if (dFile.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(uidpicpath);
                    PublicUtil.logDbug(TAG, bm + "", 0);
                    shenfenImageView_top2.setBackgroundResource(0);
                    shenfenImageView_top2.setImageBitmap(bm);
                    shenfenImageView_top2.setVisibility(View.VISIBLE);
                    shenfenImageView_top.setVisibility(View.GONE);

                } else {
                    shenfenImageView_top2.setVisibility(View.GONE);
                    shenfenImageView_top.setVisibility(View.VISIBLE);
                }

                File uFile = new File(uid2picpath);
                PublicUtil.logDbug(TAG, uFile.exists() + ":" + uFile.length(),
                        0);
                if (uFile.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(uid2picpath);
                    PublicUtil.logDbug(TAG, bm + "", 0);
                    shenfenImageView_bottom2.setBackgroundResource(0);
                    shenfenImageView_bottom2.setImageBitmap(bm);
                    shenfenImageView_bottom2.setVisibility(View.VISIBLE);
                    shenfenImageView_bottom.setVisibility(View.GONE);
                } else {
                    shenfenImageView_bottom2.setVisibility(View.GONE);
                    shenfenImageView_bottom.setVisibility(View.VISIBLE);

                }

            }

            if (msg != null && msg.what == success_http_auth_id) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            PublicUtil.showToast(ShenFenRZActivity.this,
                                    "提交认证成功", false);
                            Intent intent = new Intent();
                            intent.putExtra("result", "success");
                            ShenFenRZActivity.this.setResult(21, intent);
                            finish();

                        } else {
                            PublicUtil.showToast(ShenFenRZActivity.this,
                                    jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(
                                        ShenFenRZActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);

                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception

                        e.printStackTrace();

                    }

                }

            } else if (msg != null && msg.what == error_http_auth_id) {
                if (msg.obj != null) {
                    PublicUtil.showToast(ShenFenRZActivity.this,
                            msg.obj.toString(), false);
                    isPostShenfenImagesuccess = false;
                    isPostShenfenImagesuccess2 = false;

                }
            }

        }

        ;
    };

}
