package com.unistrong.api.services.route;

import android.content.Context;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.JsonResultHandler;
import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.route.RouteSearch.DriveRouteQuery;

import org.json.JSONObject;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

class DriveSearchServerHandler extends
		JsonResultHandler<DriveRouteQuery, DriveRouteResult> {
	private int mItemCount = 0;
	private ArrayList<String> mSuggestions = new ArrayList<String>();

	public DriveSearchServerHandler(Context context, DriveRouteQuery qInternal,
			Proxy prx, String device) {
		super(context, qInternal, prx, device);
	}


	public int getItemCount() {
		return mItemCount;
	}

	public DriveRouteQuery getQuery() {
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

			if (!isNullString(getLocationString(getQuery().getPassedByPoints())))

				sb.append("&waypoints=").append( getLocationString(getQuery().getPassedByPoints()));// 途径点坐标
			if (!isNullString(getAvoidpolygonsString(getQuery() .getAvoidpolygons())))
               sb.append("&avoidpolygons=").append( getAvoidpolygonsString(getQuery().getAvoidpolygons()));// 避让区域坐标
			if (!isNullString(getQuery().getAvoidroad()))
				sb.append("&avoidroad=").append(getQuery().getAvoidroad());

			str[0] = sb.toString();
			return str;
    }
		return null;
	}

	private String getLocationString(List<LatLonPoint> list) {
		String str = "";
		if (list == null)
			return str;

       int size =  list.size();
		for (int i = 0; i < size; i++) {
			LatLonPoint location = list.get(i);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            if (i == size-1||size==0) {
                str += longitude + "," + latitude;
            } else {
                str += longitude + "," + latitude +";";
            }
		}
		return str;
	}

	private String getAvoidpolygonsString(List<List<LatLonPoint>> list) {
		String str = "";
		if (list == null)
			return str;

        int size = list.size();
		for (int i = 0; i < size; i++) {
			List<LatLonPoint> location = list.get(i);
            if (i == size-1||size==0) {
				str += getLocationString(location);
			} else {
				str += getLocationString(location) + "|";
			}
		}
		return str;
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
		url =CoreUtil.getSearchUrl(this.context) + "/route/car?";
		return url;
	}

	@Override
	protected boolean getRequestType() {
		return true;
	}

	@Override
	protected DriveRouteResult parseJson(JSONObject obj) throws UnistrongException {
		DriveRouteResult pr = null;
		// 解析结果
		processServerErrorcode(getJsonStringValue(obj, "status", ""),
				getJsonStringValue(obj, "message", ""));
		pr = new RouteJsonParser().driveParse(obj);
		if (pr != null)
			pr.setDriveQuery(this.task);
		return pr;
	}
}
