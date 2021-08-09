package com.unistrong.api.services.district;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.core.MessageManager;

import java.util.ArrayList;

/**
 * 行政区域查询类。请用 DistrictSearchQuery 类设定搜索参数。
 */
public class DistrictSearch {
	/**
	 * 查询条件。
	 */
	private DistrictSearchQuery query;
	/**
	 * 对应的 Context。
	 */
	private Context context;
	/**
	 * 行政区划搜索结果的异步处理回调接口。
	 */
	private OnDistrictSearchListener searchListener;
	private Handler handler;

	/**
	 * 根据给定的参数构造一个 DistrictSearch 的新对象。
	 * 
	 * @param cnt
	 *            对应的 Context。
	 */
	public DistrictSearch(Context cnt) {
		this.context = cnt.getApplicationContext();
		handler = MessageManager.getInstance();
	}

	/**
	 * 设置查询条件。
	 * 
	 * @param districtSearchQuery
	 *            查询条件。
	 */
	public void setQuery(DistrictSearchQuery districtSearchQuery) {
		query = districtSearchQuery;
	}

	/**
	 * 返回查询条件。
	 * 
	 * @return 查询条件。
	 */
	public DistrictSearchQuery getQuery() {
		return query;
	}

	/**
	 * 查询行政区的异步接口。
	 */
	public void searchDistrictAsyn() {
		new Thread(new Runnable() {
			public void run() {
				Message localMessage = MessageManager.getInstance()
						.obtainMessage();
				try {
					localMessage.what = MessageManager.MESSAGE_DISTRICT;
					localMessage.arg1 = MessageManager.MESSAGE_TYPE_DISTRICT;
					localMessage.arg2 = 0;
					MessageManager.DistrictWrapper locale = new MessageManager.DistrictWrapper();
					locale.listener = DistrictSearch.this.searchListener;
					DistrictSearchQuery districtSearchQuery = getQuery();

					localMessage.obj = locale;
					locale.result = new DistrictResult(districtSearchQuery, null);
					ArrayList<DistrictItem> localList = DistrictSearch.this .getPageLocal().getDistrict();
					locale.result.setDistrict(localList);

				} catch (UnistrongException unistrongException) {
					localMessage.arg2 = unistrongException.getErrorCode();
//					MessageManager.DistrictWrapper wrapper = (MessageManager.DistrictWrapper) localMessage.obj;
//

				} finally {
					if (null != DistrictSearch.this.handler)
						DistrictSearch.this.handler.sendMessage(localMessage);
				}
			}
		}).start();

	}

	/**
	 * 查询行政区的同步接口。
	 * 
	 * @return DistrictResult 行政区域查询结果类。
	 * @throws UnistrongException
	 *             异常
	 */
	public DistrictResult searchDistrict()
			throws UnistrongException {
		DistrictSearchServerHandler handler = new DistrictSearchServerHandler(
				context, this.query, CoreUtil.getProxy(context), null);

		return handler.GetData();
	}

	/**
	 * 查询行政区的同步接口。
	 * 
	 * @return DistrictResult 行政区域查询结果类。
	 * @throws UnistrongException
	 *             异常。
	 */
	protected DistrictResult getPageLocal() throws UnistrongException {
		if (!getQuery().checkKeyWords()) {
			throw new UnistrongException(UnistrongException.ERROR_INVALID_PARAMETER);
		}
		DistrictSearchServerHandler handler = new DistrictSearchServerHandler(
				context, getQuery(), CoreUtil.getProxy(context), null);
		return handler.GetData();
	}

	/**
	 * 设置搜索结果的异步处理回调接口。
	 * 
	 * @param onDistrictSearchListener
	 *            搜索结果的异步处理回调接口。
	 */
	public void setOnDistrictSearchListener(
			OnDistrictSearchListener onDistrictSearchListener) {
		this.searchListener = onDistrictSearchListener;
	}

	/**
	 * 本类为 District（行政区域）搜索结果的异步处理回调接口。
	 */
	public static interface OnDistrictSearchListener {
        /***
         * 返回District（行政区划）异步处理的结果。
         * @param districtResult  - 行政区域的搜索结果，具体说明见 {@link DistrictResult} 。
         * @param resultID - 返回结果成功或者失败的响应码。0为成功，其他为失败。
         */
		public abstract void onDistrictSearched(DistrictResult districtResult,int resultID );
	}

}
