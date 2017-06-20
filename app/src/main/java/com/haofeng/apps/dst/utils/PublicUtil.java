package com.haofeng.apps.dst.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class PublicUtil {

    public static String PAGESIZE = "10";// 列表数据请求时，每页显示的数据数量
    private static int pagesize = 10;// 列表数据请求时，每页显示的数据数量

    /**
     * 显示tost
     *
     * @param context
     * @param msg
     * @param isLongshow
     */
    public static void showToast(Context context, String msg, boolean isLongshow) {
        if (isLongshow) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据年 月 日 获取星期几
     */
    public static String getWeekByYearMonth(int year, int month, int day) {

        String str = "";

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, day);
        a.roll(Calendar.DAY_OF_WEEK, -1);
        int firstweek = a.get(Calendar.DAY_OF_WEEK);

        switch (firstweek) {

            case 1:
                str = "周一";
                break;
            case 2:
                str = "周二";
                break;
            case 3:
                str = "周三";
                break;
            case 4:
                str = "周四";
                break;
            case 5:
                str = "周五";
                break;
            case 6:
                str = "周六";
                break;
            case 7:
                str = "周日";
                break;

            default:
                break;
        }

        return str;
    }
    /**
     * 时间戳转日期
     */
     /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String javaStamp = s + "000";
        long lt = new Long(javaStamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String getWeekOfDate(String dateStr) {
        Date date = new Date();
        date.setTime(getTimeFromDate(dateStr));
        String[] weekDaysName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysCode[intWeek];
    }

    /**
     * 两个时间之间有多少个分
     *
     * @param start
     * @param end
     * @return
     */
    public static int compareMin(String start, String end) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(end);
            Date d2 = df.parse(start);
            long diff = d1.getTime() - d2.getTime();
            int min = (int) (diff / (1000 * 60));
            return min;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 两个时间之间有多少分钟
     *
     * @param start
     * @param end
     * @return
     */
    public static int compareFen(String start, String end) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date d1 = df.parse(end);
            Date d2 = df.parse(start);
            long diff = d1.getTime() - d2.getTime();
            int fen = (int) (diff / (1000 * 60));
            return fen;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 两个时间之间有多少个小时
     *
     * @param start
     * @param end
     * @return
     */
    public static int compareHour2(String start, String end) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date d1 = df.parse(end);
            Date d2 = df.parse(start);
            long diff = d1.getTime() - d2.getTime();
            int hours = (int) (diff / (1000 * 60 * 60));
            return hours;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 两个时间之间有多少个小时
     *
     * @param start
     * @param end
     * @return
     */
    public static int compareHour(String start, String end) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date d1 = df.parse(end);
            Date d2 = df.parse(start);
            long diff = d1.getTime() - d2.getTime();
            int hours = (int) (diff / (1000 * 60 * 60));
            return hours;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 得到一个固定时间的时间戳
     *
     * @param time
     * @return
     */
    public static int getTimeFromDate(String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date d1 = df.parse(time);

            long diff = d1.getTime();
            int miao = (int) (diff / 1000);
            return miao;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 两个时间相差多少天
     *
     * @param start
     * @param end
     * @return
     */
    public static int compareDay(String start, String end) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date d1 = df.parse(end);
            Date d2 = df.parse(start);
            long diff = d1.getTime() - d2.getTime();
            int hours = (int) (diff / (1000 * 60 * 60 * 24));
            return hours;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 小时
     */
    private static final int hour = 1000 * 60 * 60;

    // private static final int hour = 1000 * 60;

    /**
     * 返回两个时差小时数 支持 yyyy-MM-dd HH精确到小时
     *
     * @param start 开始时间 if：2012-2-10 11
     * @param end   结束时间 if:2012-02-11 13:12:21
     * @return 小时数
     */
    @SuppressLint("SimpleDateFormat")
    public static int compareHourTime(String start, String end) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date d1 = df.parse(end);
            Date d2 = df.parse(start);
            long diff = d1.getTime() - d2.getTime();
            int hours = (int) (diff / hour);
            return hours;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 根据当前时间 获取时间差 支持 yyyy-MM-dd HH精确到小时
     *
     * @param start 开始时间 if：2012-2-10 11
     * @return 分钟数
     */
    @SuppressLint("SimpleDateFormat")
    public static int compareHourTime(String start) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date d2 = sdf.parse(start);
            long diff = System.currentTimeMillis() - d2.getTime();
            int hours = (int) (diff / (1000 * 60));
            return hours;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 时间转为24小时格式
     *
     * @param LgTime
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTime(String LgTime) {

        long time = Long.parseLong(LgTime);
        Date date = new Date(time * 1000);
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制

        return sdformat.format(date);
    }

    /**
     * 时间转为24小时格式
     *
     * @param LgTime
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTime2(String LgTime) {

        long time = Long.parseLong(LgTime);
        Date date = new Date(time * 1000);
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");// 24小时制

        return sdformat.format(date);
    }

    /**
     * 保存本地数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setStorage_string(Context context, String key,
                                         String value) {
        SharedPreferences preferences = context.getSharedPreferences(
                "dst_local", Context.MODE_PRIVATE);

        Editor editor = preferences.edit();

        editor.putString(key, value);
        editor.commit();

    }

    /**
     * 返回本地数据
     *
     * @param context
     * @param key
     * @param errorstr
     * @return
     */
    public static String getStorage_string(Context context, String key,
                                           String errorstr) {

        String backstr = errorstr;
        SharedPreferences preferences = context.getSharedPreferences(
                "dst_local", Context.MODE_PRIVATE);
        backstr = preferences.getString(key, errorstr);

        return backstr;
    }

    /**
     * 移除字段
     *
     * @param context
     * @param key
     */
    public static void removeStorage_string(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                "dst_local", Context.MODE_PRIVATE);

        Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();

    }

    public static void removeStorage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                "dst_local", Context.MODE_PRIVATE);

        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String phone) {
        /*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[34578]"代表第二位可以为34578中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return phone.matches(telRegex);
    }

    /**
     * 与用来算汉字
     *
     * @param i
     * @return
     */
    public static String setcode(int i) {
        String str = null, str2, str3 = null;
        int j, k;
        if (i < 10) {
            str = i + "";
            str = str.replace("1", "一");
            str = str.replace("2", "二");
            str = str.replace("3", "三");
            str = str.replace("4", "四");
            str = str.replace("5", "五");
            str = str.replace("6", "六");
            str = str.replace("7", "七");
            str = str.replace("8", "八");
            str = str.replace("9", "九");

        } else if (i >= 10 && i < 100) {
            j = (int) i / 10;

            str2 = j + "";
            str2 = str2.replace("1", "十");
            str2 = str2.replace("2", "二十");
            str2 = str2.replace("3", "三十");
            str2 = str2.replace("4", "四十");
            str2 = str2.replace("5", "五十");
            str2 = str2.replace("6", "六十");
            str2 = str2.replace("7", "七十");
            str2 = str2.replace("8", "八十");
            str2 = str2.replace("9", "九十");
            k = i - j * 10;
            if (k != 0) {
                str3 = k + "";
                str3 = str3.replace("1", "一");
                str3 = str3.replace("2", "二");
                str3 = str3.replace("3", "三");
                str3 = str3.replace("4", "四");
                str3 = str3.replace("5", "五");
                str3 = str3.replace("6", "六");
                str3 = str3.replace("7", "七");
                str3 = str3.replace("8", "八");
                str3 = str3.replace("9", "九");
            }
            if (str3 != null) {
                str = str2 + str3;
            } else {
                str = str2;
            }

        } else if (i >= 100) {
            str = i + "";
        }

        return str;

    }

    /**
     * 显示进度圈
     *
     * @param context
     * @return
     */
    public static ProgressDialog showDialog(Context context) {
        ProgressDialog dialog = ProgressDialog.show(context, null, "加载中..");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置风格为圆形进度条
        dialog.setCancelable(true);// 设置点击返回键对话框消失
        dialog.setCanceledOnTouchOutside(true);// 设置点击进度对话框外的区域对话框消失
        return dialog;
    }

    public static int getAllPage(String alltotal) {

        int page = 1;
        Log.d("getAllPage", alltotal);
        if (alltotal != null && !TextUtils.isEmpty(alltotal)) {

            int all = Integer.parseInt(alltotal);
            if (all > pagesize) {

                if (all % pagesize == 0) {
                    page = all / pagesize;
                } else {
                    page = (all / pagesize + 1);
                }

            }

        }

        return page;

    }

    /**
     * log调试
     *
     * @param msg  信息
     * @param type 1为w，2为e，其余为d
     */
    public static void logDbug(String tag, String msg, int type) {

        if (type == 1) {
            Log.e(tag, msg + "");
        } else if (type == 2) {
            Log.w(tag, msg + "");
        } else {
            Log.d(tag, msg + "");
        }

    }

    /**
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {

                    return true;
                } else {

                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 对中文进行转码
     *
     * @param str
     * @return
     */
    public static String toUTF(String str) {

        try {
            str = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return str;

    }

    /**
     * 算两个时间差
     *
     * @param starttime
     * @param endtime
     * @return
     */
    public static String timeWithTwoTime(String starttime, String endtime) {

        String str = "";
        int time = compareMin(starttime, endtime);

        if (time > 0) {
            if (time < 60) {

                str = time + "分";
            } else {

                int zc_fen = time % 60;

                time = time / 60;

                int zc_month = time / (24 * 30);
                int zc_day = (time - zc_month * 24 * 30) / 24;
                int zc_hour = time - zc_month * 24 * 30 - zc_day * 24;

                if (zc_month > 0) {
                    str = zc_month + "个月";
                }

                if (zc_day > 0) {
                    str = str + zc_day + "天";
                }

                if (zc_hour > 0) {
                    str = str + zc_hour + "小时";
                }

                if (zc_fen > 0) {
                    str = str + zc_fen + "分";
                }

            }
        }

        return str;

    }

    public static String oneTo2(String str) {

        if (str.length() == 2) {
            return str;
        } else {
            return "0" + str;
        }

    }

    /**
     * 保留小数点后两位
     *
     * @param money
     * @return
     */
    public static float toTwo(float money) {

        try {
            BigDecimal b = new BigDecimal(money);
            money = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();// 两位小数，四舍五入
            return money;
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }

    }

    /**
     * 将double格式化为指定小数位的String，不足小数位用0补全
     *
     * @param v     需要格式化的数字
     * @param scale 小数点后保留几位
     * @return
     */
    public static String roundByScale(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        if (scale == 0) {
            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for (int i = 0; i < scale; i++) {
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }


}
