package com.haofeng.apps.dst.httptools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class Encoder {
	/**
	 * @param value
	 *            string to be encoded
	 * @return encoded parameters string
	 */
	public static String encode(String value) {
		String encoded = null;
		try {
			encoded = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException ignore) {
		}
		StringBuffer buf = new StringBuffer(encoded.length());
		char focus;
		for (int i = 0; i < encoded.length(); i++) {
			focus = encoded.charAt(i);
			if (focus == '*') {
				buf.append("%2A");
			} else if (focus == '+') {
				buf.append("%20");
			} else if (focus == '%' && (i + 1) < encoded.length()
					&& encoded.charAt(i + 1) == '7'
					&& encoded.charAt(i + 2) == 'E') {
				buf.append('~');
				i += 2;
			} else {
				buf.append(focus);
			}
		}
		return buf.toString();
	}

	//
	// /**
	// * Unicode编码转中文
	// *
	// * @param utfString
	// * @return
	// */
	// public static String convert(String utfString) {
	// StringBuilder sb = new StringBuilder();
	// int i = -1;
	// int pos = 0;
	//
	// while ((i = utfString.indexOf("\\u", pos)) != -1) {
	// sb.append(utfString.substring(pos, i));
	// if (i + 5 < utfString.length()) {
	// pos = i + 6;
	// sb.append((char) Integer.parseInt(
	// utfString.substring(i + 2, i + 6), 16));
	// }
	// }
	//
	// return sb.toString();
	// }

	/**
	 * 
	 * unicode 转换成 中文
	 * 
	 * @author fanhui
	 * 
	 *         2007-3-15
	 * 
	 * @param theString
	 * 
	 * @return
	 */

	public static String convert(String theString) {

		char aChar;

		int len = theString.length();

		StringBuffer outBuffer = new StringBuffer(len);

		for (int x = 0; x < len;) {

			aChar = theString.charAt(x++);

			if (aChar == '\\') {

				aChar = theString.charAt(x++);

				if (aChar == 'u') {

					// Read the xxxx

					int value = 0;

					for (int i = 0; i < 4; i++) {

						aChar = theString.charAt(x++);

						switch (aChar) {

						case '0':

						case '1':

						case '2':

						case '3':

						case '4':

						case '5':

						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';

					else if (aChar == 'n')

						aChar = '\n';

					else if (aChar == 'f')

						aChar = '\f';

					outBuffer.append(aChar);

				}

			} else

				outBuffer.append(aChar);

		}

		return outBuffer.toString();

	}

	/**
	 * 把string转换为int。
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 失败返回defaultValue .
	 */
	public static int toInt(String value, int defaultValue) {
		if (value == null) {
			return defaultValue;
		}

		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param object
	 * @param pass
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, String> toStrMap(JSONObject object, boolean pass) {
		Map<String, String> map = new HashMap<String, String>();

		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value;
			try {
				value = object.get(key);
				if (value instanceof String) {
					map.put(key, (String) value);
				} else if (!pass) {
					map.put(key, value.toString());
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return map;
	}

	public static Map<String, Object> jsonToMap(JSONObject json)
			throws JSONException {
		Map<String, Object> retMap = new HashMap<String, Object>();

		if (json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap;
	}

	public static Map<String, Object> toMap(JSONObject object)
			throws JSONException {
		Map<String, Object> map = null;

		if (object == null) {
			return map;
		}

		map = new HashMap<String, Object>();
		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	/**
	 * 保存对象
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public static boolean saveObject(Context context, Serializable ser,
			String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = context.openFileOutput(file, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 读取对象
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Serializable readObjectA(Context context, String file) {
		if (!isExistDataCache(context, file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = context.getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param cachefile
	 * @return
	 */
	public static boolean isExistDataCache(Context context, String cachefile) {
		if (context == null)
			return false;
		boolean exist = false;
		File data = context.getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}

	public static String vst_cut(String str, String start, String end) {
		if (str != null && str.indexOf(start) > -1) {
			String string = str.split(start)[1];
			if (string.indexOf(end) > -1) {
				return string.split(end)[0];
			} else {
				return string;
			}
		}
		return str;
	}

	/**
	 * 组合json数据
	 * 
	 * @param code
	 * @param msg
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject FormatRes(String code, String msg, JSONObject data)
			throws JSONException {
		// JSONArray jArray = new JSONArray();
		JSONObject jObj = new JSONObject();
		jObj.put("errcode", code);
		jObj.put("errmsg", msg);
		jObj.put("data", data);
		// jArray.put(jObj);
		return jObj;
	}

	/***
	 * 将map数据转成json格式
	 * 
	 * @param srcMap
	 *            需要转换的map
	 * @return 返回转换后的json数据
	 */
	public static JSONObject MapToJSon(Map<String, String> srcMap) {
		JSONObject jsonObject = null;
		if (!srcMap.isEmpty()) {
			jsonObject = new JSONObject();
			for (Map.Entry<String, String> entry : srcMap.entrySet()) {
				System.out.println(entry.getKey() + "--->" + entry.getValue());
				String key = entry.getKey();
				String value = entry.getValue();
				try {
					jsonObject.putOpt(key, value);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonObject;
	}

}
