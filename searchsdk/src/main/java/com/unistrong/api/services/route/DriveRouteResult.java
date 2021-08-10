package com.unistrong.api.services.route;

import com.unistrong.api.services.route.RouteSearch.DriveRouteQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义了驾车路径规划的结果集。
 */
public class DriveRouteResult extends RouteResult implements   android.os.Parcelable{
	private List<DrivePath> paths = new ArrayList<DrivePath>();
	private DriveRouteQuery query;

    public static final Creator<DriveRouteResult> CREATOR = new DriveRouteResultCreator();

	/**
	 * DriveRouteResult的构造方法。
	 */
	public DriveRouteResult() {
		super();
	}

	/**
	 * 序列化实现。
	 */
	public DriveRouteResult(android.os.Parcel source) {
		super(source);
		this.paths = source.createTypedArrayList(DrivePath.CREATOR);
		this.query = source.readParcelable(DriveRouteQuery.class
				.getClassLoader());
	}

	public void writeToParcel(android.os.Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeTypedList(this.paths);
		dest.writeParcelable(this.query, flags);
	}

	/**
	 * 返回该结果对应的查询参数。
	 * 
	 * @return 该结果对应的查询参数。
	 */
	public RouteSearch.DriveRouteQuery getDriveQuery() {
		return this.query;
	}

	/**
	 * 返回驾车路径规划方案。
	 * 
	 * @return 驾车路径规划方案。
	 */
	public List<DrivePath> getPaths() {
		return this.paths;
	}

	/**
	 * 设置结果对应的查询参数。
	 * 
	 * @param driveQuery
	 *            对应的查询参数。
	 */
	public void setDriveQuery(RouteSearch.DriveRouteQuery driveQuery) {
		this.query = driveQuery;
	}

	/**
	 * 设置驾车路径规划方案。
	 * 
	 * @param mPaths
	 *            驾车路径规划方案。
	 */
	public void setPaths(List<DrivePath> mPaths) {
		this.paths = mPaths;
	}

}
