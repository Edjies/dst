package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.ui.view.PickerView;
import com.haofeng.apps.dst.ui.view.WheelView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.haofeng.apps.dst.httptools.Constent.ID_ACT_GET_ALWAYS_USE_CAR;

/**
 * 违章查询
 *
 * @author WIN10
 */
public class WeizhangChaxunActivity extends BaseActivity {
    private final String TAG = "WeizhangChaxunActivity";
    private FrameLayout topLayout;
    private PickerView pickProvince;
    private List<Map<String, Object>> provinceList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> zhimuList = new ArrayList<Map<String, Object>>();
    private TextView showProvinceView;
    private PickerView pickZhiMu;
    private TextView zhimuView;
    private EditText numberView;
    private String carNumber;
    private TextView searchView;
    private TextView wzNumberView;
    private RelativeLayout searchLayout;
    private LinearLayout alwaysCarNumberLayout;
    private RelativeLayout alwaysCarNumberLine1Layout;
    private RelativeLayout alwaysCarNumberLine2Layout;
    private List<TextView> carNumberViews = new ArrayList<TextView>();
    private List<Map<String, String>> wzCarDetailList = new ArrayList<Map<String, String>>();

    private TextView carNumber1TextView;
    private TextView carNumber2TextView;
    private TextView carNumber3TextView;
    private TextView carNumber4TextView;
    private TextView carNumber5TextView;
    private TextView backView;
    private ListView wzDatailView;
    private String inputStr;
    private int searchType;     //查询类型
    private final int ALWAYS_SEARCH_TYPE = 0;
    private final int SEARCH_TYPE = 1;
    private boolean isSetEditText = true;
    private Handler handler = new Handler();
    private int currentAdapterPosition = 0;
    private ImageButton searchButton;
    String[] province = {"京", "津", "沪", "渝", "辽", "黑", "吉", "冀", "鲁", "苏", "浙", "闽", "豫", "鄂", "湘", "皖", "赣", "粤",
            "桂", "甘", "宁", "晋", "蒙", "陕", "新", "青", "藏", "川", "贵", "滇", "琼"};
    String[] zhimu = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private FrameLayout wzSelectLayout;
    private WheelView wheelView;
    private RelativeLayout showPickLayout;
    private TextView concelTextView;
    private TextView confirmTextView;
    private String whoClick;  //"0",省,"1",“字母”
    private RelativeLayout wzNoShowLayout;
    private TextView carNOWzTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weizhangcahxun);
        addActivity(this);
        initView();
        initData();
        initListerner();
    }

    private void initData() {
        for (int i = 0; i < province.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("infor", province[i]);
            provinceList.add(map);
        }
        for (int i = 0; i < zhimu.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("infor", zhimu[i]);
            zhimuList.add(map);
        }
        //获取常用车辆车牌号
        Map<String, String> map = new HashMap<>();
        map.put("act", Constent.ACT_GET_ALWAYS_USE_CAR);
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, ID_ACT_GET_ALWAYS_USE_CAR, map, false, true, true);
    }

    private void initListerner() {
        // TODO Auto-generated method stub
        showProvinceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                wzSelectLayout.setVisibility(View.VISIBLE);
                showPickLayout.removeAllViews();
                wheelView.setDate(provinceList);
                showPickLayout.addView(wheelView);
                whoClick = "0";
            }
        });
        zhimuView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                wzSelectLayout.setVisibility(View.VISIBLE);
                showPickLayout.removeAllViews();
                wheelView.setDate(zhimuList);
                showPickLayout.addView(wheelView);
                whoClick = "1";
            }
        });
        numberView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                carNumber = showProvinceView.getText().toString() + zhimuView.getText().toString()
                        + numberView.getText().toString();
                inputStr = numberView.getText().toString();
                for (int i = 0; i < inputStr.length(); i++) {
                    if (inputStr.charAt(i) - 0 >= 97 && inputStr.charAt(i) - 0 <= 122) {
                        handler.postDelayed(UpperCase, 300);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        backView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                carNumber = showProvinceView.getText().toString() + zhimuView.getText().toString()
                        + numberView.getText().toString();
                if (carNumber.length() >= 7) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("act", Constent.ACT_WEI_ZHANG_SEARCH);
                    map.put("car_no", carNumber);
                    AnsynHttpRequest.httpRequest(WeizhangChaxunActivity.this, AnsynHttpRequest.GET, callBack,
                            Constent.ID_ACT_WEI_ZHANG_SEARCH, map, false, false, true);
                } else {
                    Toast.makeText(WeizhangChaxunActivity.this, "请输入正确的车牌号！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        concelTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wzSelectLayout.setVisibility(View.GONE);
            }
        });
        confirmTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = wheelView.getSeletedIndex();
                if (whoClick.equals("0")) {  //省
                    String province = provinceList.get(index).get("infor").toString();
                    showProvinceView.setText(province);

                } else if (whoClick.equals("1")) { //字母
                    String zhimu = zhimuList.get(index).get("infor").toString();
                    zhimuView.setText(zhimu);
                }
                wzSelectLayout.setVisibility(View.GONE);
            }
        });
    }

    private void initView() {
        topLayout = (FrameLayout) findViewById(R.id.weizhangchaxun_top_layout);
        setTopLayoutPadding(topLayout);
        showProvinceView = (TextView) findViewById(R.id.activty_weizhang_show_province);
        numberView = (EditText) findViewById(R.id.activity_weizhang_number);
        wzNumberView = (TextView) findViewById(R.id.listview_wz_car_number);
        backView = (TextView) findViewById(R.id.weizhangchaxun_back);
        searchButton = (ImageButton) findViewById(R.id.activity_weizhang_search);


        zhimuView = (TextView) findViewById(R.id.activty_weizhang_show_zhimu);
        wzSelectLayout = (FrameLayout) findViewById(R.id.activity_wz_select_layout);

        alwaysCarNumberLayout = (LinearLayout) findViewById(R.id.activity_weizhangchaxun_always);
        alwaysCarNumberLine1Layout = (RelativeLayout) findViewById(R.id.activity_weizhangchaxun_always_line1);
        alwaysCarNumberLine2Layout = (RelativeLayout) findViewById(R.id.activity_weizhangchaxun_always_line2);
        carNumber1TextView = (TextView) findViewById(R.id.weizhangchaxun_car_number_1);
        carNumber2TextView = (TextView) findViewById(R.id.weizhangchaxun_car_number_2);
        carNumber3TextView = (TextView) findViewById(R.id.weizhangchaxun_car_number_3);
        carNumber4TextView = (TextView) findViewById(R.id.weizhangchaxun_car_number_4);
        carNumber5TextView = (TextView) findViewById(R.id.weizhangchaxun_car_number_5);
        wzDatailView = (ListView) findViewById(R.id.activity_wz_detail);
        carNumberViews.add(carNumber1TextView);
        carNumberViews.add(carNumber2TextView);
        carNumberViews.add(carNumber3TextView);
        carNumberViews.add(carNumber4TextView);
        carNumberViews.add(carNumber5TextView);
        wheelView = new WheelView(this);
        showPickLayout = (RelativeLayout) findViewById(R.id.show_pick_layout);
        concelTextView = (TextView) findViewById(R.id.activity_wz_select_concel);
        confirmTextView = (TextView) findViewById(R.id.activity_wz_select_confirm);
        wzNoShowLayout = (RelativeLayout) findViewById(R.id.wz_no_show_layout);
        carNOWzTextView = (TextView) findViewById(R.id.weizhangchaxun_car_number);
    }

    Runnable UpperCase = new Runnable() {

        @Override
        public void run() {
            // 小写转大写
            numberView.setText(inputStr.toUpperCase());
            // 设置EditText光标位置
            numberView.setSelection(inputStr.length());
        }

    };
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @SuppressLint("NewApi")
        @Override
        public void back(int backId, final boolean isRequestSuccess, final boolean isString, String data, final JSONArray jsonArray) {
            // TODO Auto-generated method stub
            switch (backId) {
                case Constent.ID_ACT_GET_ALWAYS_USE_CAR:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRequestSuccess) {
                                if (!isString) {
                                    try {
                                        String backStr = jsonArray.getString(1);
                                        JSONObject jsonObject = new JSONObject(backStr);
                                        if (jsonObject.getString("errcode").equals("0")) {
                                            JSONArray dataArray = jsonObject.getJSONArray("data");
                                            if (dataArray != null && dataArray.length() > 0) {
                                                alwaysCarNumberLayout.setVisibility(View.VISIBLE);
                                                if (dataArray.length() <= 3) {
                                                    alwaysCarNumberLine1Layout.setVisibility(View.VISIBLE);
                                                } else {
                                                    alwaysCarNumberLine1Layout.setVisibility(View.VISIBLE);
                                                    alwaysCarNumberLine2Layout.setVisibility(View.VISIBLE);
                                                }
                                                for (int i = 0; i < dataArray.length(); i++) {
                                                    final TextView carNumberView = carNumberViews.get(i);
                                                    carNumberView.setVisibility(View.VISIBLE);
                                                    carNumberView.setText(dataArray.getString(i));
                                                    carNumberView.setOnClickListener(new OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            alwaysCarNumberLayout.setVisibility(View.GONE);
                                                            String carnumber = carNumberView.getText().toString();
                                                            //查询该车辆的违章
                                                            char province = carnumber.charAt(0);
                                                            char zhimu = carnumber.charAt(1);
                                                            String lastnumber = carnumber.substring(2);
                                                            showProvinceView.setText(String.valueOf(province));
                                                            zhimuView.setText(String.valueOf(zhimu));
                                                            numberView.setText(lastnumber);
                                                        }
                                                    });
                                                }

                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }
                    });

                    break;
                case Constent.ID_ACT_WEI_ZHANG_SEARCH:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRequestSuccess) {
                                if (!isString) {
                                    try {
                                        String backStr = jsonArray.getString(1);
                                        JSONObject jsonObject = new JSONObject(backStr);
                                        wzCarDetailList.clear();
                                        wzDatailView.setAdapter(null);
                                        if (jsonObject.getString("errcode").equals("0")) {
                                            //隐藏
                                            JSONArray dataArray = jsonObject.optJSONArray("data");
                                            if (dataArray != null && dataArray.length() > 0) {   //有违章
                                                wzNoShowLayout.setVisibility(View.GONE);
                                                for (int i = 0; i < dataArray.length(); i++) {
                                                    String fen = ((JSONObject) dataArray.get(i)).getString("fen");
                                                    String date = ((JSONObject) dataArray.get(i)).getString("date");
                                                    String area = ((JSONObject) dataArray.get(i)).getString("area");
                                                    String act = ((JSONObject) dataArray.get(i)).getString("act");
                                                    String money = ((JSONObject) dataArray.get(i)).getString("money");
                                                    String plate_number = ((JSONObject) dataArray.get(i)).getString("plate_number");
                                                    Map<String, String> map = new HashMap<String, String>();
                                                    map.put("fen", fen);
                                                    map.put("date", date);
                                                    map.put("area", area);
                                                    map.put("act", act);
                                                    map.put("money", money);
                                                    map.put("plate_number", plate_number);
                                                    wzCarDetailList.add(map);
                                                }
                                                alwaysCarNumberLayout.setVisibility(View.GONE);
                                                wzDatailView.setVisibility(View.VISIBLE);
                                                MyBaseAdapter myBaseAdapter = new MyBaseAdapter();
                                                wzDatailView.setAdapter(myBaseAdapter);
                                            } else {  //无违章
                                                wzNoShowLayout.setVisibility(View.VISIBLE);
                                                carNOWzTextView.setText("车牌号：" + carNumber);
                                            }
                                        } else if ("1016".equals(jsonObject.get("errcode").toString())) {   //车辆不存在
                                            Toast.makeText(WeizhangChaxunActivity.this, "该车辆不存在!", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }
                    });
                    break;
                default:
                    break;
            }


        }
    };


    class MyBaseAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return wzCarDetailList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder = null;
            View view = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                view = View.inflate(WeizhangChaxunActivity.this, R.layout.listview_item_wz_detail, null);
                viewHolder.dateView = (TextView) view.findViewById(R.id.lsitview_item_detail_date);
                viewHolder.areaView = (TextView) view.findViewById(R.id.lsitview_item_detail_area);
                viewHolder.beizhuView = (TextView) view.findViewById(R.id.lsitview_item_detail_beizhu);
                viewHolder.fenView = (TextView) view.findViewById(R.id.lsitview_item_detail_fen);
                viewHolder.fakuanView = (TextView) view.findViewById(R.id.lsitview_item_detail_fakuan);
                viewHolder.daijiaoView = (TextView) view.findViewById(R.id.lsitview_item_detail_daijiao);
                viewHolder.zhixunkefuView = (TextView) view.findViewById(R.id.lsitview_item_detail_zhixun_kefu);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            Log.e(TAG, "getView: " + position);
            viewHolder.areaView.setText(wzCarDetailList.get(position).get("area"));
            viewHolder.dateView.setText(wzCarDetailList.get(position).get("date"));
            viewHolder.fakuanView.setText(wzCarDetailList.get(position).get("money"));
            viewHolder.fenView.setText(wzCarDetailList.get(position).get("fen"));
            viewHolder.beizhuView.setText(wzCarDetailList.get(position).get("act"));
            viewHolder.daijiaoView.setOnClickListener(new MyWzListener(position));
            if ((Integer.parseInt(wzCarDetailList.get(position).get("fen")) > 0)) { // 咨询客服
                viewHolder.zhixunkefuView.setVisibility(View.VISIBLE);
                viewHolder.zhixunkefuView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4008604558"));
                        startActivity(intent);
                    }
                });
                viewHolder.daijiaoView.setVisibility(View.GONE);
            } else { // 违章代缴
                viewHolder.zhixunkefuView.setVisibility(View.GONE);
                viewHolder.daijiaoView.setVisibility(View.VISIBLE);
            }
            return view;
        }

        class MyWzListener implements OnClickListener {
            private int mPosition;

            private MyWzListener(int position) {
                this.mPosition = position;
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeizhangChaxunActivity.this, WeiZhangPayActivity.class);
                intent.putExtra("fen", wzCarDetailList.get(mPosition).get("fen"));
                intent.putExtra("money", wzCarDetailList.get(mPosition).get("money"));
                intent.putExtra("date", wzCarDetailList.get(mPosition).get("date"));
                intent.putExtra("plate_number", wzCarDetailList.get(mPosition).get("plate_number"));
                Log.e(TAG, "onClick: " + wzCarDetailList.size() + mPosition + "-----" + wzCarDetailList.get(mPosition).get("date"));
                startActivity(intent);
            }
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

    static class ViewHolder {
        TextView dateView;
        TextView areaView;
        TextView beizhuView;
        TextView fenView;
        TextView fakuanView;
        TextView daijiaoView;
        TextView zhixunkefuView;
    }


}
