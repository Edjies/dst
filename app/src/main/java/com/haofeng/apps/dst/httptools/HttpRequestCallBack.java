package com.haofeng.apps.dst.httptools;

import org.json.JSONArray;

public interface HttpRequestCallBack {
	/**
	 * http请求返回
	 * 
	 * @param backId
	 *            标识id
	 * @param isRequestSuccess
	 *            请求是否成功(此值为false时，data一定不能为空)
	 * @param isString
	 *            返回值是否是String
	 * @param data
	 *            isString为true时，返回的String类型的数据
	 * @param jsonObject
	 *            isString为false时，返回JSONObject类型数据
	 */
	public void back(int backId, boolean isRequestSuccess, boolean isString,
			String data, JSONArray jsonArray);
}