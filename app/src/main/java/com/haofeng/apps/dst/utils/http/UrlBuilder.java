package com.haofeng.apps.dst.utils.http;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * url 构造类
 * @author hubble
 *
 */
public class UrlBuilder {
	private String host;
	private String path;
	private ArrayList<UrlParams> mParams = new ArrayList<UrlParams>();
	/**
	 * 服务器名
	 * @param host ex: http://192.168.0.1:8080
	 * @return
	 */
	public UrlBuilder setHost(String host) {
		this.host = host;
		return this;
	}
	
	/**
	 * 路径名  
	 * @param path ex: /stock/price/ol
	 * @return
	 */
	public UrlBuilder setPath(String path) {
		this.path = path;
		return this;
	}
	
	/**
	 * url 参数
	 * @param name
	 * @param value
	 * @return
	 */
	public UrlBuilder append(String name, String value) {
		try {
			value = URLEncoder.encode(value, "utf-8");
			this.mParams.add(new UrlParams(name, value));
		}catch (Exception e) {

		}

		return this;
	}
	
	/**
	 * url 参数集
	 * @param params
	 * @return
	 */
	public UrlBuilder append(ArrayList<UrlParams> params) {
		for(int i = 0; i< params.size(); i++) {
			UrlParams item = params.get(i);
			try {
				item.value = URLEncoder.encode(item.value, "utf-8");
			}catch (Exception e) {

			}
		}
		this.mParams.addAll(params);
		return this;
	}
	
	public String build() {
		StringBuilder sb = new StringBuilder();
		sb.append(host).append(path);
		
		if(mParams.size() > 0) {
			sb.append("?");
		}
		
		for(int i = 0; i < mParams.size(); i++) {
			UrlParams param = mParams.get(i);
			sb.append(param.name).append("=").append(param.value);
			if(i < mParams.size() - 1) {
				sb.append("&");
			}
		}
		return sb.toString();
	}
	
	public static class UrlParams {
		public String name;
		public String value;
		
		public UrlParams(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}
	}
}
