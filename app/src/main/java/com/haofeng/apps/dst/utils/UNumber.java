package com.haofeng.apps.dst.utils;

import java.text.DecimalFormat;

/**
 * @author  hubble
 */
public class UNumber {
    private static DecimalFormat format = new DecimalFormat();
    public static int getInt(String value, int def) {
        try {
            return Integer.parseInt(value);
        }catch (Exception e) {
            return def;
        }
    }

    public static Long getLong(String value, Long def) {
        try {
            return Long.parseLong(value);
        }catch (Exception e) {
            return def;
        }
    }

    public static  float getFloat(String value, float def) {
        try {
            return Float.parseFloat(value);
        }catch (Exception e) {
            return def;
        }
    }

    /** 格式化double数据*/
    public static String formatDouble(double value, String pattern) {
        if(Double.isNaN(value)) {
            return "--";
        }
        format.applyPattern(pattern);
        return format.format(value);
    }

    /** 格式化float数据*/
    public static String formatFloat(float value, String pattern) {
        if(Float.isNaN(value)) {
            return "0.00";
        }
        format.applyPattern(pattern);
        return format.format(value);
    }

    public static String formatInt(int value, int width) {
        String pattern = "%0" + width + "d";
        return String.format(pattern, value);
    }

}
