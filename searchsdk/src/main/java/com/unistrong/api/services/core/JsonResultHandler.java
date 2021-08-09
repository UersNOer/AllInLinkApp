package com.unistrong.api.services.core;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.net.Proxy;

/**
 * 使用Json解析返回结果。
 * 
 * @author changbao.wang
 * @date 2016年1月15日
 */
public abstract class JsonResultHandler<T, V> extends ProtocalHandler<T, V> {

	/**
	 * 使用Json解析返回结果。
	 * 
	 * @param tsk
	 * @param prx
	 * @param device
	 */
	public JsonResultHandler(Context context, T tsk, Proxy prx, String device) {
		super(context, tsk, prx, device);
	}

	@Override
	protected V loadData(InputStream inputStream, boolean isGZip) throws UnistrongException {
		String json = "";
		if (isGZip){
			json = zipInputStream(inputStream);
		} else {
			json = readString(inputStream);
		}
		JSONTokener parser = new JSONTokener(json);
		JSONObject obj = null;
		V result = null;
		try {
			Object value = parser.nextValue();
			if (value != null && value instanceof JSONObject) {
				obj = (JSONObject) value;
				result = parseJson(obj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 解析Json。
	 * 
	 * @param obj
	 *            需要解析的json对象。
	 * @return
	 * @author changbao.wang
	 * @date 2016年1月15日
	 */
	abstract protected V parseJson(JSONObject obj) throws UnistrongException;

	/**
	 * 获得JSON对象中的属性值。
	 * 
	 * @param jsonObj
	 *            json对象。
	 * @param keyName
	 *            json的key值。
	 * @param defaultValue
	 *            默认属性值。
	 * @return 获取的属性值。
	 */
	public static String getJsonStringValue(JSONObject jsonObj, String keyName,
			String defaultValue) {
		if (null == jsonObj || null == keyName)
			return defaultValue;

		if (jsonObj.has(keyName)) {
			try {
				return jsonObj.getString(keyName);
			} catch (JSONException e) {
			}
		}

		return defaultValue;
	}
	/**
	 * 获得JSON对象中的属性值。
	 *
	 * @param jsonObj
	 *            json对象。
	 * @param keyName
	 *            json的key值。
	 * @param defaultValue
	 *            默认属性值。
	 * @return 获取的属性值。
	 */
	public static long getJsonIntValue(JSONObject jsonObj, String keyName,
			int defaultValue) {
		if (null == jsonObj || null == keyName)
			return defaultValue;

		if (jsonObj.has(keyName)) {
			try {
				return jsonObj.getInt(keyName);
			} catch (JSONException e) {
			}
		}

		return defaultValue;
	}

	/**
	 * 处理异常。
	 * 
	 * @param status
	 *            状态码。
	 * @param message
	 *            状态码对应的信息。
	 * @throws UnistrongException
	 *             服务异常。
	 */
	protected void processServerErrorcode(String status, String message)
			throws UnistrongException {
		if (!status.equals("0")) {
//			throw new UnistrongException(UnistrongException.ERROR_PB_STATE1);
			throw new UnistrongException(message);
		}
	}
}
