package com.unistrong.api.services.busline;

import android.content.Context;
import android.net.Uri;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.JsonResultHandler;
import com.unistrong.api.services.core.UnistrongException;

import org.json.JSONObject;

import java.net.Proxy;

class BusLineServerHandler extends
		JsonResultHandler<BusLineQuery, BusLineResult> {
	/**
	 * 线路id查询的url。
	 */
	String id_url = "/search/busline/byid?";
	/**
	 * 线路名称查询的url。
	 */
	String line_url = "/search/busline/byname?";
	String search = "";

	public BusLineServerHandler(Context context, BusLineQuery district,
			Proxy prx, String device) {
		super(context, district, prx, device);
		search = district.getCategory() == BusLineQuery.SearchType.BY_LINE_ID ? id_url
				: line_url;
	}

	@Override
	protected String[] getRequestLines() {
		if (task != null) {
			String[] str = new String[1];
			StringBuilder sb = new StringBuilder();

			if (this.task.getCategory() == BusLineQuery.SearchType.BY_LINE_ID) {
				sb.append("busIds=").append(this.task.getQueryString());
			} else {
				sb.append("busLine=").append(this.task.getQueryString());
			}
			sb.append("&city=").append(Uri.encode(this.task.getCity()));
			sb.append("&page_size=").append(this.task.getPageSize());
			sb.append("&page_num=").append(this.task.getPageNumber());
			str[0] = sb.toString();
			return str;
		}
		return null;
	}

	@Override
	protected int getServiceCode() {
		return 20;
	}

	public BusLineQuery getQuery() {
		return this.task;
	}

	@Override
	protected String getUrl() {
		String url = null;
		url = CoreUtil.getSearchUrl(this.context) + search;

		return url;
	}

	@Override
	protected boolean getRequestType() {
		return true;
	}

	@Override
	protected BusLineResult parseJson(JSONObject obj) throws UnistrongException {
		BusLineResult busLineResult = null;

		processServerErrorcode(getJsonStringValue(obj, "status", ""),
				getJsonStringValue(obj, "message", ""));
		busLineResult = new BusJsonParser().busLineParse(obj);
		if (busLineResult != null)
			busLineResult.setQuery(this.task);

		return busLineResult;

	}

}
