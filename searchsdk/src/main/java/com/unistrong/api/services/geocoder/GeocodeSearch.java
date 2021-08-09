package com.unistrong.api.services.geocoder;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.core.MessageManager;

/**
 * 地理编码与逆地理编码类。
 */
public final class GeocodeSearch {
	/**
	 * 上下文。
	 */
	private Context mContext;
	/**
	 * 异步事件处理。
	 */
	private Handler handler = null;
	/**
	 * 逆地理编码异步处理回调接口。
	 */
	private OnGeocodeSearchListener searchListener;

	/**
	 * 根据给定的参数来构造一个GeocodeSearch 新对象。
	 * 
	 * @param cnt
	 *            上下文。
	 */
	public GeocodeSearch(Context cnt) {
		mContext = cnt.getApplicationContext();
		CoreUtil.getApiKey(mContext);
		handler = MessageManager.getInstance();
	}

	/**
	 * 根据给定的经纬度和最大结果数返回逆地理编码的结果列表。
	 * 
	 * @param regeocodeQuery
	 *            要进行逆地理编码查询的条件。
	 * @return RegeocodeResult 逆地理编码的搜索结果。
	 * @throws UnistrongException
	 *             服务异常。
	 */
	public RegeocodeResult getFromLocation(RegeocodeQuery regeocodeQuery)
			throws UnistrongException {
		RegeocodeServerHandler handler = new RegeocodeServerHandler(mContext,
				regeocodeQuery, CoreUtil.getProxy(mContext), null);
		return handler.GetData();
	}

	/**
	 * 根据给定的地理名称和查询城市，返回地理编码的结果列表。
	 * 
	 * @param geocodeQuery
	 *            地理编码的查询条件。
	 * @return GeocodeResult 理编码的搜索结果。
	 * @throws UnistrongException
	 *             服务异常。
	 */
	public GeocodeResult getFromLocationName(GeocodeQuery geocodeQuery)
			throws UnistrongException {
		GeocodeServerHandler handler = new GeocodeServerHandler(mContext,
				geocodeQuery, CoreUtil.getProxy(mContext), null);
		return handler.GetData();
	}

	/**
	 * 异步处理：根据给定的经纬度和最大结果数返回逆地理编码的结果列表。
	 * 
	 * @param regeocodeQuery
	 *            查询条件。
	 */
	public void getFromLocationAsyn(final RegeocodeQuery regeocodeQuery) {
		new Thread(new Runnable() {
			public void run() {
				Message localMessage = MessageManager.getInstance()
						.obtainMessage();
				try {
					localMessage.what = MessageManager.MESSAGE_GEOCODE_REGEOCODE;
					localMessage.arg1 = MessageManager.MESSAGE_TYPE_GEOCODE;
					localMessage.arg2 = 0;

					MessageManager.RegeocodeWrapper locale = new MessageManager.RegeocodeWrapper();
					locale.listener = GeocodeSearch.this.searchListener;

					localMessage.obj = locale;
					locale.result = GeocodeSearch.this
							.getFromLocation(regeocodeQuery);

				} catch (UnistrongException unistrongException) {
					localMessage.arg2 = unistrongException.getErrorCode();
				} finally {
					if (null != GeocodeSearch.this.handler)
						GeocodeSearch.this.handler.sendMessage(localMessage);
				}
			}
		}).start();
	}

	/**
	 * 异步处理。根据给定的地理名称和查询城市返回地理编码的结果列表。
	 * 
	 * @param geocodeQuery
	 *            查询条件。
	 */
	public void getFromLocationNameAsyn(final GeocodeQuery geocodeQuery) {
		new Thread(new Runnable() {
			public void run() {
				Message localMessage = MessageManager.getInstance()
						.obtainMessage();
				try {
					localMessage.what = MessageManager.MESSAGE_GEOCODE_GEOCODE;
					localMessage.arg1 = MessageManager.MESSAGE_TYPE_GEOCODE;
					localMessage.arg2 = 0;

					MessageManager.GeocodeWrapper locale = new MessageManager.GeocodeWrapper();
					locale.listener = GeocodeSearch.this.searchListener;

					localMessage.obj = locale;
					locale.result = GeocodeSearch.this
							.getFromLocationName(geocodeQuery);
				} catch (UnistrongException unistrongException) {
					localMessage.arg2 = unistrongException.getErrorCode();
				} finally {
					if (null != GeocodeSearch.this.handler)
						GeocodeSearch.this.handler.sendMessage(localMessage);
				}
			}
		}).start();
	}

	/**
	 * 地理编码和逆地理查询结果监听接口设置。
	 * 
	 * @param paramOnGeocodeSearchListener
	 *            地理编码和逆地理查询结果监听接口。
	 */
	public void setOnGeocodeSearchListener(
			OnGeocodeSearchListener paramOnGeocodeSearchListener) {
		this.searchListener = paramOnGeocodeSearchListener;
	}

	/**
	 * 逆地理编码异步处理回调接口。
	 */
	public static abstract interface OnGeocodeSearchListener {
		/**
		 * 根据给定的经纬度和最大结果数返回逆地理编码的结果列表。
		 * 
		 * @param paramRegeocodeResult
		 *            逆地理编码返回的结果。
		 * @param paramInt
		 *            返回结果成功或者失败的响应码。0为成功，其他为失败。
		 */
		public abstract void onRegeocodeSearched(
				RegeocodeResult paramRegeocodeResult, int paramInt);

		/**
		 * 根据给定的地理名称和查询城市，返回地理编码的结果列表。
		 * 
		 * @param paramGeocodeResult
		 *            地理编码返回的结果。
		 * @param paramInt
		 *            返回结果成功或者失败的响应码。0为成功，其他为失败。
		 */
		public abstract void onGeocodeSearched(
				GeocodeResult paramGeocodeResult, int paramInt);
	}
}
