package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ui.view.WheelView;
import com.haofeng.apps.dst.utils.UDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WIN10 on 2017/6/16.
 */

public class TestActivity extends BaseActivity {
    private WheelView mCvWheel;
    private LinearLayout mLlContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_2);
        setViews();
        setListeners();
        initData();
    }

    private void setViews() {


        mLlContainer = (LinearLayout) findViewById(R.id.ll_container);
        mCvWheel = new WheelView(this);
        mCvWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex) {
                super.onSelected(selectedIndex);

            }
        });

    }

    private void setListeners() {

    }

    private void initData() {
        List<Map<String, Object>> data = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            Map<String, Object> item =  new HashMap<>();
            item.put("infor", String.valueOf(i));
            data.add(item);
        }
        mCvWheel.setDate(getValidDate("07:00", "23:00", Calendar.getInstance()));

        mLlContainer.removeAllViews();
        mLlContainer.addView(mCvWheel);
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

    /**
     * 获取日期列表
     * @param start
     * @param end
     * @param cur
     * @return
     */
    private List<Map<String, Object>> getValidDate(String start, String end, Calendar cur) {
        List<Map<String, Object>> mDates = new ArrayList<>();
        cur.add(Calendar.HOUR_OF_DAY, 2); // 提前两小时
        String curTime = UDate.getFormatDate(cur, "HH-mm");
        if(curTime.compareTo(start) > 0 && curTime.compareTo(end) < 0) {

        }else{
            // 从明天起
            cur.add(Calendar.DAY_OF_MONTH, 1);
        }


        for (int i = 0; i < 20; i++) {
            HashMap<String, Object> dateObj = new HashMap<>();
            dateObj.put("infor", UDate.getFormatDate(cur, "MM-dd"));
            dateObj.put("Date", new Date(cur.getTimeInMillis()));
            mDates.add(dateObj);
        }
        return mDates;
    }

}
