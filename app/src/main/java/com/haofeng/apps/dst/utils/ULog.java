package com.haofeng.apps.dst.utils;


import android.util.Log;

/**
 * 日志工具
 * @author  hubble
 */
public class ULog {
	
	public static final boolean DEBUG = true;
	
	
	private static boolean I = DEBUG;
	private static boolean E = DEBUG;
	private static boolean D = DEBUG;
	private static boolean W = DEBUG;

	
	
	public static void i(String tag, String message){
		if(I){
			Log.i("dsti-" + tag, message);
		}
	}

	
	public static void e(String tag, String message){
		if(E){
			Log.e("dste-" + tag,message);
		}
	}
	
	public static void d(String tag, String message){
		if(D){
			Log.d(tag, message);
		}
	}
	
	public static void w(String tag, String message){
		if(W){
			Log.w(tag, message);
		}
	}
	
	
	

}
