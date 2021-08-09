package com.unistrong.api.services.poisearch;

import android.content.Context;
import android.net.Uri;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.JsonResultHandler;
import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.poisearch.PoiSearch.Query;
import com.unistrong.api.services.poisearch.PoiSearch.SearchBound;

import org.json.JSONObject;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

class PoiSearchServerHandler extends
		JsonResultHandler<QueryInternal, PoiResult> {
	public static final int PageNum = 1;
	private int mQureyPage = 1;
	private int mPageSize = 10;
	private int mItemCount = 0;
	private int mPoiSortMode = 0;
	private ArrayList<String> mSuggestions = new ArrayList<String>();
	public PoiSearchServerHandler(Context context, QueryInternal qInternal,
			Proxy prx, String device) {
		super(context, qInternal, prx, device);
	}

	public void setCurPage(int count) {
		mQureyPage = count;
	}

	public void setSortMode(int sortMode) {
		mPoiSortMode = sortMode;
	}

	public void setPageSize(int pageSize) {
		if (pageSize > 50) {
			pageSize = 50;
		}
		if (pageSize <= 0) {
			pageSize = 10;
		}
		mPageSize = pageSize;
	}

	public int getPageSize() {
		return mPageSize;
	}

	public int gerSortMode() {
		return mPoiSortMode;
	}

	public int getCurPage() {
		return mQureyPage;
	}

	public int getItemCount() {
		return mItemCount;
	}

	public Query getQuery() {
		return this.task.mQuery;
	}

	public SearchBound getBound() {
		return this.task.mBound;
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

            sb.append("query=").append(Uri.encode(getQuery().getQueryString()));
			sb.append("&scope=").append(getQuery().getScope());



			if (!isNullString(getQuery().getRegion()) || getQuery().getLocation() !=null && getQuery().getLocation().getLatitude() != 0
					&& getQuery().getLocation().getLongitude() != 0
					|| getBound() != null
					&& !isNullString(getBound().toString())) {
				if (!isNullString(getQuery().getRegion()))
					sb.append("&region=").append(Uri.encode(getQuery().getRegion()));

				if (getQuery().getLocation() != null
						&& getQuery().getLocation().getLatitude() != 0
						&& getQuery().getLocation().getLongitude() != 0)
					sb.append("&location=").append( getQuery().getLocation().getLongitude() + ","
									+ getQuery().getLocation().getLatitude());
				if (0 != getQuery().getRadius())
					sb.append("&radius=").append(getQuery().getRadius() + "");

                if (getBound() != null) {
                    if (getBound().getShape().equals("polygon")) {
                        String location = "";
                        List<LatLonPoint> polyGonList = getBound().getPolyGonList();

                        for (int i = 0; i < polyGonList.size(); i++) {
                            LatLonPoint latLonPoint = polyGonList.get(i);
                            double longitude = latLonPoint.getLongitude();
                            double latitude = latLonPoint.getLatitude();

                            if(polyGonList.size() == 0){
                                location += longitude + "," + latitude;
                            }else{
                                if(i < polyGonList.size() -1){
                                    location += longitude + "," + latitude + ";";
                                }else{
                                    location += longitude + "," + latitude;
                                }
                            }
                        }
                        sb.append("&bounds=").append(location);
                    } else {
                        LatLonPoint lowerLeft = getBound().getLowerLeft();
                        LatLonPoint upperRight = getBound().getUpperRight();
                        sb.append("&bounds=").append(lowerLeft + ";" + upperRight);
                    }
                }

                if(getBound() != null && this.getBound().getShape()!=null)
                    sb.append("&regionType=").append(getBound().getShape());
			} else {
				try {
					throw new UnistrongException(
							UnistrongException.ERROR_INVALID_PARAMETER);
				} catch (UnistrongException e) {
					e.printStackTrace();
				}
			}

			if(!isNullString(getQuery().getCategory())){
				if(!"truck".equals(getQuery().getCategory())&&!"货车".equals(getQuery().getCategory())){
                    sb.append("&type=").append(Uri.encode(getQuery().getCategory()));
                    if (!isNullString(getQuery().getDatasource())){
                        sb.append("&datasource=").append(Uri.encode(getQuery().getDatasource()));
                    }
                } else{
                    sb.append("&datasource=truckpoi,poi");
                }
			} else {
                if (!isNullString(getQuery().getDatasource())){
                    sb.append("&datasource=").append(Uri.encode(getQuery().getDatasource()));
                }
            }



			sb.append("&page_size=").append(getQuery().getPageSize());
			sb.append("&page_num=").append(getQuery().getPageNum());

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
		String url = CoreUtil.getSearchUrl(this.context);
		if("truck".equals(getQuery().getCategory())||"货车".equals(getQuery().getCategory()))
			url=CoreUtil.getTruckSearchUrl(this.context);
		String path="/search/poi?";
		return url+path;
	}

	@Override
	protected boolean getRequestType() {
		return true;
	}

	@Override
	protected PoiResult parseJson(JSONObject obj) throws UnistrongException {
		PoiResult pr = null;
		processServerErrorcode(getJsonStringValue(obj, "status", ""),
				getJsonStringValue(obj, "message", ""));
		// 解析结果
		pr = new PoiJsonParser() .Poiparser(this.task.mQuery, this.task.mBound, obj);
		return pr;
	}
}
