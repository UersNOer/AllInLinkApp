package com.unistrong.api.services.busline;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.core.MessageManager;

/**
 * 本类为公交线路搜索的“入口”类，定义此类，开始搜索。在类BusLineSearch 中，使用BusLineQuery 类设定搜索参数。
 */
public class BusLineSearch {
	/**
	 * 当前 Activity上下文。
	 */
	private Context context;
	/**
	 * 公交查询条件。
	 */
	private BusLineQuery busLineQuery;
	/**
	 * 公交线路查询异步处理回调接口。
	 */
	private OnBusLineSearchListener busLineSearchListener;
	private Handler handler = null;

	/**
	 * BusLineSearch构造函数。
	 */
	public BusLineSearch(android.content.Context act) {
		this.context = act.getApplicationContext();
		CoreUtil.getApiKey(context);
		handler = MessageManager.getInstance();
	}

	/**
	 * BusLineSearch 构造函数。
	 *
	 * @param act
	 *            当前 Activity。
	 * @param query
	 *            公交查询条件。
	 */
	public BusLineSearch(android.content.Context act, BusLineQuery query) {
		this.context = act.getApplicationContext();
		this.busLineQuery = query;
		CoreUtil.getApiKey(context);
		handler = MessageManager.getInstance();
	}

	/**
	 * 搜索公交线路。
	 * 如果此时查询条件（BusLineQuery）中的行政区划代码已定义，则查询为该城市（地区）内的所有符合条件的公交线路,否则范围为全国。
	 * 根据指定查询类型和关键字搜索公交线路结果。
	 *
	 * @return BusLineResult查询公交线路的结果。
	 * @throws UnistrongException
	 */
	public BusLineResult searchBusLine() throws UnistrongException {
		BusLineServerHandler handler = new BusLineServerHandler(context,
				getQuery(), CoreUtil.getProxy(context), null);
		return handler.GetData();

	}

	/**
	 * 搜索公交线路的异步处理调用，根据指定查询类型和关键字搜索公交线路结果。
	 */
	public void searchBusLineAsyn() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message localMessage = MessageManager.getInstance()
						.obtainMessage();

				try {
					localMessage.arg1 = MessageManager.MESSAGE_TYPE_BUSLINE;
					localMessage.arg2 = 0;
					MessageManager.BusLineWrapper locale = new MessageManager.BusLineWrapper();
					localMessage.obj = locale;
					locale.listener = BusLineSearch.this.busLineSearchListener;
					locale.result = BusLineSearch.this.searchBusLine();
				} catch (UnistrongException unistrongException) {
					localMessage.arg2 = unistrongException.getErrorCode();
				} finally {
					if (null != BusLineSearch.this.handler)
						BusLineSearch.this.handler.sendMessage(localMessage);
				}

			}
		}).start();

	}

	/**
	 * 公交路线搜索结果监听接口设置。
	 *
	 * @param onBusLineSearchListener
	 *            - 公交路线搜索结果监听接口。
	 */
	public void setOnBusLineSearchListener(
			BusLineSearch.OnBusLineSearchListener onBusLineSearchListener) {
		this.busLineSearchListener = onBusLineSearchListener;
	}

	/**
	 * 设置查询条件。
	 *
	 * @param query
	 *            -新的查询条件。
	 */
	public void setQuery(BusLineQuery query) {
		this.busLineQuery = query;
	}

	/**
	 * 返回查询条件。
	 *
	 * @return 返回查询条件。
	 */
	public BusLineQuery getQuery() {
		return busLineQuery;

	}

	/**
	 * 此接口定义了公交线路查询异步处理回调接口。
	 */
	public static abstract interface OnBusLineSearchListener {
        /**
         *公交路线的查询异步处理。
         * @param busLinePagedResult  - 公交线路搜索结果。
         * @param resultID - 返回结果成功或者失败的响应码。0为成功，其他为失败。
         */
		public abstract void onBusLineSearched(
				BusLineResult busLinePagedResult, int resultID);
	}

}
