package com.unistrong.api.services.busline;

import android.content.Context;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.JsonResultHandler;
import com.unistrong.api.services.core.UnistrongException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;

class BusStationServerHandler extends
		JsonResultHandler<BusStationQuery, BusStationResult> {
	/**
	 * 站点id查询的url。
	 */
	private String id_url = "/search/busstop/byid?";
	/**
	 * 站点名称查询的url。
	 */
	private String line_url = "/search/busstop/byname?";
	private String search = "";

	public BusStationServerHandler(Context context, BusStationQuery district,
			Proxy prx, String device) {
		super(context, district, prx, device);
		search = district.getCategory() == BusStationQuery.StopSearchType.BY_STOP_ID ? id_url
				: line_url;
	}

	@Override
	protected String[] getRequestLines() {
		if (task != null) {
			String[] str = new String[1];
			StringBuilder sb = new StringBuilder();

			if (this.task.getCategory() == BusStationQuery.StopSearchType.BY_STOP_ID) {
				sb.append("ids=").append(this.task.getQueryString());
			} else {
                try {
                    sb.append("name=").append(URLEncoder.encode(this.task.getQueryString(), "UTF-8"));
                    sb.append("&city=").append(URLEncoder.encode(this.task.getCity(),"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
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

	public BusStationQuery getQuery() {
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
	protected BusStationResult parseJson(JSONObject obj) throws UnistrongException {
		BusStationResult busStationResult = new BusStationResult();

		processServerErrorcode(getJsonStringValue(obj, "status", ""),
				getJsonStringValue(obj, "message", ""));
		busStationResult = new BusJsonParser().busStationParse(obj);
		if (busStationResult != null)
			busStationResult.setQuery(this.task);

		return busStationResult;

	}

}
