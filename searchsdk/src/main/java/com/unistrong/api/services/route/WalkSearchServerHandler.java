package com.unistrong.api.services.route;

import android.content.Context;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.JsonResultHandler;
import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.route.RouteSearch.WalkRouteQuery;

import org.json.JSONObject;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

class WalkSearchServerHandler extends
		JsonResultHandler<WalkRouteQuery, WalkRouteResult> {
	private ArrayList<String> mSuggestions = new ArrayList<String>();

	public WalkSearchServerHandler(Context context, WalkRouteQuery qInternal,
			Proxy prx, String device) {
		super(context, qInternal, prx, device);
	}

	public WalkRouteQuery getQuery() {
		return this.task;
	}

	public List<String> getSuggestion() {
		return mSuggestions;
	}

	@Override
	protected String[] getRequestLines() {
		if (task != null) {
			String[] str = new String[1];
			StringBuilder sb = new StringBuilder();
			// 入参设置
			sb.append("origin=")
					.append(getQuery().getFromAndTo().getFrom().getLongitude()
							+ ","
							+ getQuery().getFromAndTo().getFrom().getLatitude());// 起点坐标
			sb.append("&destination=").append(
					getQuery().getFromAndTo().getTo().getLongitude() + ","
							+ getQuery().getFromAndTo().getTo().getLatitude());// 终点坐标
			if (!isNullString(getQuery().getCoordType()))
				sb.append("&coord_type=").append(getQuery().getCoordType());// 坐标类型
			if (0 != getQuery().getTactics()){
                sb.append("&tactics=").append(getQuery().getTactics() + "");// 导航策略
            }

			str[0] = sb.toString();
			return str;
		}
		return null;
	}

	private boolean isNullString(String str) {
		if (null == str || str.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected int getServiceCode() {
		return 20;
	}

	@Override
	protected String getUrl() {
		String url = null;
		url = CoreUtil.getSearchUrl(this.context) + "/route/walk?";
		return url;
	}

	@Override
	protected boolean getRequestType() {
		return true;
	}

	@Override
	protected WalkRouteResult parseJson(JSONObject obj) throws UnistrongException {
		WalkRouteResult pr = null;
		processServerErrorcode(getJsonStringValue(obj, "status", ""),
				getJsonStringValue(obj, "message", ""));
		pr = new RouteJsonParser().walkParse(obj);
		if (pr != null)
			pr.setWalkQuery(this.task);
		return pr;
	}
}
