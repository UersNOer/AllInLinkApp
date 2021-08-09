package com.unistrong.api.services.poisearch;

import android.content.Context;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.JsonResultHandler;
import com.unistrong.api.services.core.UnistrongException;

import org.json.JSONObject;

import java.net.Proxy;
import java.util.List;

class PoiIdSearchServerHandler extends JsonResultHandler<String, List<PoiItem>> {

    private String poiId;

	public PoiIdSearchServerHandler(Context context, String  poiId, Proxy prx, String device) {
		super(context, poiId, prx, device);
        this.poiId = poiId;
	}

	@Override
	protected String[] getRequestLines() {
        if (task != null) {
            String[] str = new String[1];
            StringBuilder sb = new StringBuilder();
            if(!isNullString(poiId)){
                sb.append("ids=").append(this.poiId);
            }else {
                try {
                    throw new UnistrongException(  UnistrongException.ERROR_INVALID_PARAMETER);
                } catch (UnistrongException e) {
                    e.printStackTrace();
                }
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
		url = CoreUtil.getSearchUrl(this.context)+ "/search/poiid?";
		return url;
	}

	@Override
	protected boolean getRequestType() {
		return true;
	}

	@Override
	protected List<PoiItem> parseJson(JSONObject obj) throws UnistrongException {
        List<PoiItem> result = null;
        processServerErrorcode(getJsonStringValue(obj, "status", ""),  getJsonStringValue(obj, "message", ""));
        // 解析结果
        result = new PoiJsonParser().PoiIdparser(obj);

        return result;
	}
}
