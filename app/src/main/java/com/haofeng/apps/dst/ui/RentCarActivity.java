package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.CarDetail;
import com.haofeng.apps.dst.bean.CarSite;
import com.haofeng.apps.dst.bean.OrderParams;
import com.haofeng.apps.dst.bean.RentConfig;
import com.haofeng.apps.dst.bean.ResRentConfig;
import com.haofeng.apps.dst.bean.ResSystime;
import com.haofeng.apps.dst.bean.StateConst;
import com.haofeng.apps.dst.bean.Systime;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.ui.view.DateTimePopWindow;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.UDate;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.UNumber;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by WIN10 on 2017/6/16.
 */

public class RentCarActivity extends BaseActivity implements View.OnClickListener{
    public final static String TAG = RentCarActivity.class.getSimpleName();

    private final int REQUESTCODE = 0x7410;

    private TextView mTvStatusBar;
    private ImageView mIvBack;

    private ScrollView mSvContent;

    private RadioGroup mRgType;
    private RadioButton mRbHour;
    private RadioButton mRbDay;
    private TextView mTvPickSite;
    private TextView mTvReturnSite;

    private RelativeLayout mRlPickDatetime;
    private RelativeLayout mRlReturnDatetime;
    private TextView mTvPickDate;
    private TextView mTvPickTime;
    private TextView mTvReturnDate;
    private TextView mTvReturnTime;
    private TextView mTvDuration;
    private TextView mTvDurationUnit;
    private LinearLayout mLlRent;
    private TextView mTvRent;
    private TextView mTvHintPick;
    private TextView mTvHintReturn;

    private TextView mTvPredictRent;

    private TextView mTvConfirm;

    private CarDetail mCar;
    private CarSite mCarSite;
    private Calendar mPickDate;
    private Calendar mReturnDate;
    private RentConfig mConfig = new RentConfig();
    private Systime mSystime = new Systime();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_car);
        initIntentData();
        setViews();
        setListeners();
        initData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.iv_back == id) {
            finish();
        }

        else if(R.id.tv_pick_site == id || R.id.tv_return_site == id) {
            Intent getCarintent = new Intent(this, GetCarAdressActivity.class);
            startActivityForResult(getCarintent, REQUESTCODE);
        }

        else if(R.id.rl_pick_datetime == id) {
            new DateTimePopWindow(this, mRlPickDatetime, mConfig, mSystime.mCalendar, new DateTimePopWindow.Callback() {
                @Override
                public void onSelected(Calendar date) {
                    mPickDate = date;
                    mTvPickDate.setText(UDate.getFormatDate(date, "MM-dd"));
                    mTvPickTime.setText(UDate.getWeekName(date) + " " + UDate.getFormatDate(date, "HH:mm"));
                    mTvHintPick.setVisibility(View.GONE);
                    execRefreshDurationAndAmount();
                }

            }).show();
        }

        else if(R.id.rl_return_datetime == id) {
            new DateTimePopWindow(this, mRlPickDatetime, mConfig, mSystime.mCalendar, new DateTimePopWindow.Callback() {
                @Override
                public void onSelected(Calendar date) {
                    mReturnDate = date;
                    mTvReturnDate.setText(UDate.getFormatDate(date, "MM-dd"));
                    mTvReturnTime.setText(UDate.getWeekName(date) + " " + UDate.getFormatDate(date, "HH:mm"));
                    mTvHintReturn.setVisibility(View.GONE);
                    execRefreshDurationAndAmount();
                }
            }).show();
        }

        else  if(R.id.tv_confirm == id) {
            if (mCarSite == null) {
                Toast.makeText(this, "请选择取车还车门店", Toast.LENGTH_SHORT).show();
            } else if(mPickDate == null) {
                Toast.makeText(this, "请先选择时间", Toast.LENGTH_SHORT).show();
            } else if(mReturnDate == null) {
                Toast.makeText(this, "请先选择时间", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(this, ShengQingGetCarActivity.class);
                intent.putExtra("car", getIntent().getSerializableExtra("car"));
                OrderParams params = execGetOrderParams();
                intent.putExtra("orderParams", params);
                startActivity(intent);
            }
        }
    }

    private void initIntentData() {
        mCar = (CarDetail) getIntent().getExtras().getSerializable("car");
    }

    private void setViews() {
        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mSvContent = (ScrollView) findViewById(R.id.sv_content);
        mSvContent.setVisibility(View.GONE);

        mRgType = (RadioGroup) findViewById(R.id.rg_type);
        mRbHour = (RadioButton) findViewById(R.id.rb_hour);
        mRbDay = (RadioButton) findViewById(R.id.rb_day);
        mTvPickSite = (TextView) findViewById(R.id.tv_pick_site);
        mTvReturnSite = (TextView) findViewById(R.id.tv_return_site);


        mRlPickDatetime = (RelativeLayout) findViewById(R.id.rl_pick_datetime);
        mRlReturnDatetime = (RelativeLayout) findViewById(R.id.rl_return_datetime);
        mTvPickDate = (TextView) findViewById(R.id.tv_pick_date);
        mTvPickTime = (TextView) findViewById(R.id.tv_pick_time);
        mTvReturnDate = (TextView) findViewById(R.id.tv_return_date);
        mTvReturnTime = (TextView) findViewById(R.id.tv_return_time);
        mTvDuration = (TextView) findViewById(R.id.tv_rent_time);
        mTvDurationUnit = (TextView) findViewById(R.id.tv_time_unit);
        mLlRent = (LinearLayout) findViewById(R.id.ll_rent);
        mTvRent = (TextView) findViewById(R.id.tv_rent_amount);
        mTvHintPick = (TextView) findViewById(R.id.tv_hint_pick);
        mTvHintReturn = (TextView) findViewById(R.id.tv_hint_return);
        mTvHintPick.setVisibility(View.VISIBLE);
        mTvHintReturn.setVisibility(View.VISIBLE);

        mTvPredictRent = (TextView) findViewById(R.id.tv_predict_rent);
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);

        mLlRent.setVisibility(View.GONE);
    }

    private void setListeners() {
        mIvBack.setOnClickListener(this);
        mTvPickSite.setOnClickListener(this);
        mTvReturnSite.setOnClickListener(this);
        mRlPickDatetime.setOnClickListener(this);
        mRlReturnDatetime.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        mRgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                execResetPage();
            }
        });
    }

    private void initData() {
        execResetPage();
        execGetRentConfig();
        mRbHour.setText("时租(¥ " + mCar.m_time_price +"/时)");
        mRbDay.setText("日租(¥ " + mCar.m_day_price +"/天)");
    }


    private void execResetPage() {
        mCarSite = null;
        mPickDate = null;
        mReturnDate = null;
        mTvPickSite.setText("");
        mTvReturnSite.setText("");

        mTvHintPick.setVisibility(View.VISIBLE);
        mTvHintReturn.setVisibility(View.VISIBLE);
        mTvPickDate.setText("");
        mTvPickTime.setText("");
        mTvReturnDate.setText("");
        mTvReturnTime.setText("");
        mTvDuration.setText("");
        mTvDurationUnit.setText(mRgType.getCheckedRadioButtonId() == R.id.rb_hour ? "时" : "天");

        mTvPredictRent.setText("");
    }


    private void execRefreshDurationAndAmount() {
        if(mPickDate != null && mReturnDate != null) {
            if(mRbHour.isChecked()) {
                long hours = UDate.getDiff(mPickDate, mReturnDate) / (1000 * 60 * 60L);
                mTvDuration.setText(String.valueOf(hours));
                mTvDurationUnit.setText("时");
                mTvPredictRent.setText(UNumber.formatFloat(UNumber.getFloat(mCar.m_time_price, 0)  * hours, "0.00"));
            }else {
                long days = UDate.getDiff(mPickDate, mReturnDate) / (1000 * 60 * 60 *24L);
                mTvDuration.setText(String.valueOf(days));
                mTvDurationUnit.setText("天");
                mTvPredictRent.setText(UNumber.formatFloat(UNumber.getFloat(mCar.m_day_price, 0)  * days, "0.00"));
            }
        }
    }

    /**
     * 获取可选择的时间
     * @param start
     * @param end
     * @param cur
     * @return
     */
    private List<Map<String, Object>> getValidTime(Calendar start, Calendar end, Calendar cur) {
        cur.add(Calendar.HOUR_OF_DAY, 2); // 提前两小时
        int startHour = start.get(Calendar.HOUR_OF_DAY);
        int startMinute =  start.get(Calendar.MINUTE);
        if(startMinute > 30) {
            startHour += 1;
        }
        startMinute = startMinute + (30 - startMinute % 30);

        int endHour = end.get(Calendar.HOUR_OF_DAY);
        int endMinute = end.get(Calendar.MINUTE);
        if(endMinute > 30) {
            endHour += 1;
        }
        endMinute = endMinute + (30 - endMinute % 30);

        int curHour = end.get(Calendar.HOUR_OF_DAY);
        int curMinute = end.get(Calendar.MINUTE);
        if(curMinute > 30) {
            curHour += 1;
        }

        curMinute = endMinute + (30 - curMinute % 30);


        // 如果当期时间在营业时间范围内，则为 当前时间至营业结束时间， 否则为营业开始时间 至 结束时间
        if(cur.compareTo(start) > 0 && cur.compareTo(end) < 0) {
            // 当前时间至结束时间

        }else {
            // 营业时间

        }

        return null;

    }

    private List<Map<String, Object>> getTimeList(int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
        List<Map<String, Object>> mTimes = new ArrayList<>();
        int hour = hourStart, minute = minuteStart;

        do {
            String time = (hour <= 9 ? "0" + hour : hour) + ":" + (minute <= 9 ? "0" + minute : minute);
            minute += 30;
            if(minute >= 60) {
                minute = 0;
                hour += 1;
            }
            HashMap<String, Object> timeObj = new HashMap<>();
            timeObj.put("infor", time);
            timeObj.put("hour", hour);
            timeObj.put("minute", minute);
            mTimes.add(timeObj);
        } while((hour * 60 + minute) < (hourEnd * 60 + minute));

        return mTimes;
    }



    private OrderParams execGetOrderParams() {
        OrderParams params = new OrderParams();

        params.m_type = mRgType.getCheckedRadioButtonId() == R.id.rb_hour ? StateConst.ORDER_TYPE_HOUR : StateConst.ORDER_TYPE_DAY;
        params.m_qc_time = UDate.getFormatDate(mPickDate, "yyyy-MM-dd HH:mm:ss");
        params.m_hc_time = UDate.getFormatDate(mReturnDate, "yyyy-MM-dd HH:mm:ss");
        params.m_store_id = mCarSite.mId;
        params.m_way = "1";
        params.m_yu_ji_zu_jing = mTvPredictRent.getText().toString(); // TODO 预计租金
        return params;
    }

    private void execGetSystime() {
        String url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/system/time")
                .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                .append("secret", Constent.secret)
                .append("ver", Constent.VER)
                .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                .build();
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgress();
                ULog.e(TAG, response.toString());
                if(isPageResumed) {
                    ResSystime res = BeanParser.parseSystime(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        mSvContent.setVisibility(View.VISIBLE);

                        mSystime = res.mTime;
                        // 更新时间

                    }else {
                        Toast.makeText(RentCarActivity.this, res.mMessage, Toast.LENGTH_SHORT).show();
                    }

                    // 更新页面

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
            }
        }));
    }

    private void execGetRentConfig() {
        showProgress("加载中...");
        String url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/rent/config")
                .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                .append("secret", Constent.secret)
                .append("ver", Constent.VER)
                .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                .build();
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ULog.e(TAG, response.toString());
                if(isPageResumed) {
                    execGetSystime();
                    ResRentConfig res = BeanParser.parseRentConfig(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        mConfig = res.mConfig;
                    }else {
                        Toast.makeText(RentCarActivity.this, res.mMessage, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
            }
        }));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            if (resultCode == 12 && data != null) {
                CarSite carSite = new CarSite();
                carSite.mName = data.getStringExtra("adressName");
                carSite.mId = data.getStringExtra("carAdressId");
                mCarSite = carSite;
                mTvPickSite.setText(carSite.mName);
                mTvReturnSite.setText(carSite.mName);
            }
        }
    }

    public static void intentMe(BaseActivity act, CarDetail car) {
        Intent intent = new Intent(act, RentCarActivity.class);
        intent.putExtra("car", car);
        act.startActivity(intent);
    }
}
