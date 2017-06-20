package com.haofeng.apps.dst.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具
 * @author hubble
 *
 */
public class UDate {
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sDateFormat = new SimpleDateFormat();
	
	/**
	 * 获取格式化日期时间
	 * @param calendar
	 * @param pattern  格式化pattern   "yyyyMMdd", "yyyy-MM-dd HH:mm:ss" 
	 * @return
	 */
	public static String getFormatDate(Calendar calendar, String pattern) {
		sDateFormat.applyPattern(pattern);
		return sDateFormat.format(calendar.getTime());
	}
	
	/**
	 * 获取格式化日期时间
	 * @param date
	 * @param pattern  格式化pattern   "yyyyMMdd", "yyyy-MM-dd HH:mm:ss" 
	 * @return
	 */
	public static String getFormatDate(Date date, String pattern) {
		sDateFormat.applyPattern(pattern);
		return sDateFormat.format(date);
	}

	
	/**
	 * 
	 * @param date    yyyy-MM-dd HH:mm:ss
	 * @param dstPattern 目标pattern
	 * @return
	 */
	public static String getFormatDate(String date, String srcPattern, String dstPattern) {
		try {
			sDateFormat.applyPattern(srcPattern);
			Date d = sDateFormat.parse(date);
			sDateFormat.applyPattern(dstPattern);
			return sDateFormat.format(d);
		} catch (Exception e) {
			return String.valueOf(date);
		}
	}

	
	/***
	 * 将毫秒数转化为日期格式
	 * @param time 毫秒数
	 * @param dstPattern 日期目标格式yyyy-mm-dd
	 * @return
	 */
	public static String getFormatDate(long time, String dstPattern) {
		if(time < 0) {
			return "";
		}
		Date date = new Date(time);
		sDateFormat.applyPattern(dstPattern);
		return sDateFormat.format(date);
	}
	

	private static long dateTimeToMillis (String datetime) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			calendar.setTime(sdf.parse(datetime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar.getTimeInMillis();	
	}

	/**
	 * 获取两个日期之间的差值
	 * @return
     */
	public static long getDiff(String start, String end, String pattern) {
		try {
			sDateFormat.applyPattern(pattern);
			Date d1 = sDateFormat.parse(start);
			Date d2 = sDateFormat.parse(end);
			return d2.getTime() - d1.getTime();
		}catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;

	}

	/**
	 * 获取两个日期之间的差值
	 * @return
	 */
	public static long getDiff(Calendar start, Calendar end) {
		return end.getTimeInMillis() - start.getTimeInMillis();
	}

	public static String getWeekName(Calendar calendar) {
		Calendar c = calendar;
		switch (c.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
				return "周日";
			case Calendar.MONDAY:
				return "周一";
			case Calendar.TUESDAY:
				return "周二";
			case Calendar.WEDNESDAY:
				return "周三";
			case Calendar.THURSDAY:
				return "周四";
			case Calendar.FRIDAY:
				return "周五";
			default:
				return "周六";
		}

	}

	public static String getWeekName(String datetime, String srcPattern) {
		try{
			sDateFormat.applyPattern(srcPattern);
			Date d = sDateFormat.parse(datetime);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			switch (c.get(Calendar.DAY_OF_WEEK)) {
				case Calendar.SUNDAY:
					return "周日";
				case Calendar.MONDAY:
					return "周一";
				case Calendar.TUESDAY:
					return "周二";
				case Calendar.WEDNESDAY:
					return "周三";
				case Calendar.THURSDAY:
					return "周四";
				case Calendar.FRIDAY:
					return "周五";
				default:
					return "周六";
			}
		}catch (ParseException e) {

		}
		return "";

	}
	
	
}
