package com.unistrong.api.services.route;

import android.os.Parcel;
import com.unistrong.api.services.route.RouteSearch.BusRouteQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义了公交路径规划的结果集
 */
public class BusRouteResult extends RouteResult implements android.os.Parcelable {
	/**
	 * 公交路径规划方案。
	 */
	private List<BusPath> paths = new ArrayList<BusPath>();
	/**
	 * 查询条件。
	 */
	private BusRouteQuery query;
	/**
	 * 公交路线对应的打车费用。
	 */
	private float taxiCost=0;

    public static final Creator<BusRouteResult> CREATOR = new BusRouteResultCreator();

	/**
	 * BusRouteResult的构造方法。
	 */
	public BusRouteResult() {
	}

	public BusRouteResult(Parcel source) {
		super(source);
		this.paths = source.createTypedArrayList(BusPath.CREATOR);
		this.taxiCost = source.readFloat();
		this.query = (BusRouteQuery)source .readParcelable(BusRouteQuery.class.getClassLoader());
	}

    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.paths);
        dest.writeFloat(this.taxiCost);
        dest.writeParcelable(this.query, flags);
    }

	/**
	 * 返回公交路径规划方案。
	 * 
	 * @return 公交换乘路径规划的方案。
	 */
	public List<BusPath> getPaths() {
		return paths;
	}

	/**
	 * 设置公交路径规划方案。
	 * 
	 * @param paths
	 *            公交换乘路径规划的方案。
	 */
	public void setPaths(List<BusPath> paths) {
		this.paths = paths;
	}

	/**
	 * 返回该结果对应的查询参数。
	 * 
	 * @return 该结果对应的查询参数。
	 */
	public BusRouteQuery getBusLineQuery() {
		return query;
	}

	/**
	 * 设置该结果对应的查询参数。
	 * 
	 * @param query
	 *            该结果对应的查询参数。
	 */
	public void setBusLineQuery(BusRouteQuery query) {
		this.query = query;
	}

	/**
	 * 返回规划的公交路线对应的打车费用，单位元。 费用是以驾车路线最短距离估计出租车的费用。
	 * 
	 * @return 规划的公交路线对应的打车费用。
	 */
	public float getTaxiCost() {
		return taxiCost;
	}

	/**
	 * 设置 规划的公交路线对应的打车费用，单位元。 费用是以驾车路线最短距离估计出租车的费用。
	 * 
	 * @param taxiCost
	 *            规划的公交路线对应的打车费用
	 */
	public void setTaxiCost(float taxiCost) {
		this.taxiCost = taxiCost;
	}

	public int describeContents() {
		return 0;
	}



}
