package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.ui.view.MyWheelView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.haofeng.apps.dst.R.id.carAssociation_activity_back;
import static com.haofeng.apps.dst.R.id.car_association_activity_province_layout;
import static com.haofeng.apps.dst.R.id.car_association_activity_zhimu_layout;


/**
 * 车辆关联页面
 */
public class CarAssociationActivity extends BaseActivity implements View.OnClickListener {

    private TextView backTextView;
    private LinearLayout pickProvinceLayout;
    private LinearLayout pickZhiMuLayout;
    private TextView provinceTextView;
    private TextView zhimuTextView;
    private EditText inputEditText;
    private TextView tipTextView;
    private Button confrmAssociationButton;
    private int whoClik;    //1，开头车牌号，2，第二位车牌号
    String[] provinces = {"京", "津", "沪", "渝", "辽", "黑", "吉", "冀", "鲁", "苏", "浙", "闽", "豫", "鄂", "湘", "皖", "赣", "粤",
            "桂", "甘", "宁", "晋", "蒙", "陕", "新", "青", "藏", "川", "贵", "滇", "琼"};
    String[] zhimus = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private List<String> provinceList = new ArrayList<>();
    private List<String> zhimuList = new ArrayList<>();
    private LinearLayout selectCarNumberLayout;
    private LinearLayout selectCarNumberLayout2;
    private TextView concelTextView2;
    private TextView concelTextView;
    private TextView confirmTextView2;
    private TextView confirmTextView;
    private MyWheelView myWheelView;
    private MyWheelView myWheelView2;
    private boolean isFristClickZhimu = true;
    private boolean isFristClickProvince = true;
    private String carNumber;
    private String inputStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();

    }

    private void initData() {
        for (int i = 0; i < provinces.length; i++) {
            provinceList.add(provinces[i]);
        }
        for (int i = 0; i < zhimus.length; i++) {
            zhimuList.add(zhimus[i]);
        }
    }

    private void initView() {
        View view = View.inflate(this, R.layout.activity_car_association, null);
        setContentView(view);
        backTextView = (TextView) findViewById(R.id.carAssociation_activity_back);
        pickProvinceLayout = (LinearLayout) findViewById(R.id.car_association_activity_province_layout);
        pickZhiMuLayout = (LinearLayout) findViewById(R.id.car_association_activity_zhimu_layout);
        provinceTextView = (TextView) findViewById(R.id.car_association_activity_province);
        zhimuTextView = (TextView) findViewById(R.id.car_association_activity_zhimu);
        inputEditText = (EditText) findViewById(R.id.car_association_activity_input);
        tipTextView = (TextView) findViewById(R.id.car_association_activity_tip);
        confrmAssociationButton = (Button) findViewById(R.id.car_association_activity_confirm_association);

        selectCarNumberLayout = (LinearLayout) findViewById(R.id.car_association_activity_select_layout);
        concelTextView = (TextView) findViewById(R.id.car_association_activity_concel);
        confirmTextView = (TextView) findViewById(R.id.car_association_activity_confirm);
        myWheelView = (MyWheelView) findViewById(R.id.car_association_activity_wheelview);

        selectCarNumberLayout2 = (LinearLayout) findViewById(R.id.car_association_activity_select_layout2);
        concelTextView2 = (TextView) findViewById(R.id.car_association_activity_concel2);
        confirmTextView2 = (TextView) findViewById(R.id.car_association_activity_confirm2);
        myWheelView2 = (MyWheelView) findViewById(R.id.car_association_activity_wheelview2);
        FrameLayout topLayout = (FrameLayout) findViewById(R.id.car_association_activity_toplayout);
        setTopLayoutPadding(topLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initListener() {
        backTextView.setOnClickListener(this);
        pickProvinceLayout.setOnClickListener(this);
        pickZhiMuLayout.setOnClickListener(this);
        concelTextView.setOnClickListener(this);
        confirmTextView.setOnClickListener(this);
        concelTextView2.setOnClickListener(this);
        confirmTextView2.setOnClickListener(this);
        confrmAssociationButton.setOnClickListener(this);
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                carNumber = provinceTextView.getText().toString() + zhimuTextView.getText().toString()
                        + inputEditText.getText().toString();
                inputStr = inputEditText.getText().toString();
                for (int i = 0; i < inputStr.length(); i++) {
                    if (inputStr.charAt(i) - 0 >= 97 && inputStr.charAt(i) - 0 <= 122) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                inputEditText.setText(inputStr.toUpperCase());
                                inputEditText.setSelection(inputStr.length());
                            }
                        }, 200);
                    }
                }
                if (carNumber.length() >= 3) {
                    confrmAssociationButton.setBackgroundResource(R.color.textgreen);
                    confrmAssociationButton.setTextColor(getResources().getColor(R.color.white));
                } else {
                    confrmAssociationButton.setBackgroundResource(R.color.graybtn);
                    confrmAssociationButton.setTextColor(getResources().getColor(R.color.gray4));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case carAssociation_activity_back:
                finish();
                break;
            case car_association_activity_province_layout:
                selectCarNumberLayout2.setVisibility(View.GONE);
                if (selectCarNumberLayout.getVisibility() == View.VISIBLE) {
                    selectCarNumberLayout.setVisibility(View.GONE);
                } else {
                    selectCarNumberLayout.setVisibility(View.VISIBLE);
                    myWheelView.setOffset(1);
                    if (isFristClickProvince) {
                        myWheelView.setItems(provinceList);
                    }
                    isFristClickProvince = false;
                }
                break;
            case car_association_activity_zhimu_layout:
                selectCarNumberLayout.setVisibility(View.GONE);
                if (selectCarNumberLayout2.getVisibility() == View.VISIBLE) {
                    selectCarNumberLayout2.setVisibility(View.GONE);
                } else {
                    selectCarNumberLayout2.setVisibility(View.VISIBLE);
                    myWheelView2.setOffset(1);
                    if (isFristClickZhimu) {
                        myWheelView2.setItems(zhimuList);
                    }
                    isFristClickZhimu = false;
                }
                break;

            case R.id.car_association_activity_confirm_association:

                if (carNumber != null && carNumber.length() >= 7) {
                    //关联
                    Map<String, String> map = new HashMap<>();
                    map.put("act", Constent.ACT_CREATE_CAR_ASSOCIATION);
                    map.put("car_no", carNumber);
                    AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_ACT_CREATE_CAR_ASSOCIATION, map, false, true, true);
                }
                break;
            case R.id.car_association_activity_concel:
                selectCarNumberLayout.setVisibility(View.GONE);
                break;
            case R.id.car_association_activity_confirm:
                provinceTextView.setText(myWheelView.getSeletedItem());
                selectCarNumberLayout.setVisibility(View.GONE);
                break;
            case R.id.car_association_activity_concel2:
                selectCarNumberLayout2.setVisibility(View.GONE);
                break;
            case R.id.car_association_activity_confirm2:
                zhimuTextView.setText(myWheelView2.getSeletedItem());
                selectCarNumberLayout2.setVisibility(View.GONE);
                break;
        }
    }

    private HttpRequestCallBack callBack = new HttpRequestCallBack() {
        @Override
        public void back(int backId, final boolean isRequestSuccess, final boolean isString, String data, final JSONArray jsonArray) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backStr = jsonArray.getString(1);
                                JSONObject jsonObject = new JSONObject(backStr);
                                if (jsonObject.getString("errcode").equals("0")) {
                                    //关联成功
                                    finish();
                                } else {
                                    tipTextView.setTextColor(getResources().getColor(R.color.red));
                                    tipTextView.setText("关联失败，你输入的车牌号不属于地上铁车辆");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

        }
    };
}
