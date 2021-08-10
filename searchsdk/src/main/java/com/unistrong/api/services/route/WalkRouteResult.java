package com.unistrong.api.services.route;

import com.unistrong.api.services.route.RouteSearch.DriveRouteQuery;
import com.unistrong.api.services.route.RouteSearch.WalkRouteQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义了步行路径规划的结果集。
 */
public class WalkRouteResult extends RouteResult implements  android.os.Parcelable{
	private List<WalkPath> paths = new ArrayList<WalkPath>();
	private WalkRouteQuery query;

    public static final Creator<WalkRouteResult> CREATOR = new WalkRouteResultCreator();

	/**
	 * WalkRouteResult的构造方法。
	 */
	public WalkRouteResult() {
		super();
	}

	/**
	 * 序列化实现.
	 */
	public WalkRouteResult(android.os.Parcel source) {
		super(source);
		this.paths = source.createTypedArrayList(WalkPath.CREATOR);
		this.query = source.readParcelable(DriveRouteQuery.class
				.getClassLoader());
	}

	/**
	 * 返回步行路径规划方案。
	 * 
	 * @return 步行路径规划方案。
	 */
	public List<WalkPath> getPaths() {
		return this.paths;
	}

	/**
	 * 设置步行路径规划方案.
	 * 
	 * @param mPaths
	 *            步行路径规划方案。
	 */
	public void setPaths(List<WalkPath> mPaths) {
		this.paths = mPaths;
	}

	/**
	 * 返回该结果对应的查询参数。
	 * 
	 * @return 该结果对应的查询参数。
	 */
	public RouteSearch.WalkRouteQuery getWalkQuery() {
		return this.query;
	}

	/**
	 * 设置结果对应的查询参数.
	 * 
	 * @param walkQuery
	 *            结果对应的查询参数.
	 */
	public void setWalkQuery(RouteSearch.WalkRouteQuery walkQuery) {
		this.query = walkQuery;
	}

	public void writeToParcel(android.os.Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeTypedList(this.paths);
		dest.writeParcelable(this.query, flags);
	}

}
