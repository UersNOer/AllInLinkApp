package com.unistrong.api.services.route;

import com.unistrong.api.services.core.LatLonPoint;

/**
 * 定义了路径规划的结果集。
 */
public class RouteResult implements android.os.Parcelable {
	private RoutePoint startPos;
	private RoutePoint targetPos;
	private int traffic_condition;// 路况评价 ：
	private int type;// 数据类型，1为模糊，2为明确
	private String message;

    public static final Creator<RouteResult> CREATOR = new RouteResultCreator();

	/**
	 * 序列化实现。
	 */
	public RouteResult(android.os.Parcel source) {
		this.startPos = ((RoutePoint) source.readParcelable(RoutePoint.class
				.getClassLoader()));
		this.targetPos = ((RoutePoint) source.readParcelable(RoutePoint.class
				.getClassLoader()));
		this.traffic_condition = source.readInt();
		this.type = source.readInt();
        this.message = source.readString();

	}

	public void writeToParcel(android.os.Parcel dest, int flags) {
		dest.writeParcelable(this.startPos, flags);
		dest.writeParcelable(this.targetPos, flags);
		dest.writeInt(this.traffic_condition);
		dest.writeInt(this.type);
        dest.writeString(this.message);
	}

	public void setStartPosInfo(RoutePoint startPos) {
		this.startPos = startPos;
	}

	public void setTargetPosInfo(RoutePoint targetPos) {
		this.targetPos = targetPos;
	}

	/**
	 * 返回路径规划起点的位置信息。
	 * 
	 * @return 起点的位置信息。
	 */
	public RoutePoint getStartPosInfo() {
		return this.startPos;
	}

	/**
	 * 返回路径规划终点的位置信息。
	 *
	 * @return 终点的位置信息
	 */
	public RoutePoint getTargetPosInfo() {
		return this.targetPos;
	}

	/**
	 * 获取数据类型。
	 * @return 返回数据类型 ，1为模糊结果，2为明确结果
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * 设置数据类型。
	 * @param type
	 *            设置数据类型。
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * RouteResult的构造方法。
	 */
	public RouteResult() {
		super();
	}

	/**
	 * 返回路径规划起点的位置。
	 * 
	 * @return 路径规划起点的位置。
	 */
	public LatLonPoint getStartPos() {
		return this.startPos.getLocation();
	}

	/**
	 * 返回路径规划终点的位置。
	 * 
	 * @return 路径规划终点的位置
	 */
	public LatLonPoint getTargetPos() {
		return this.targetPos.getLocation();
	}

	/**
	 * 设置路径规划起点的位置。
	 * 
	 * @param mStartPos
	 *            起点坐标
	 */
	public void setStartPos(LatLonPoint mStartPos) {
		this.startPos.setLocation(mStartPos);
	}

	/**
	 * 设置路径规划终点的位置
	 * 
	 * @param mStartPos
	 *            起点坐标
	 * 
	 */
	public void setTargetPos(LatLonPoint mStartPos) {
		this.targetPos.setLocation(mStartPos);
	}

	/**
	 * 返回路况 取值：0,无路况;1,畅通;2，缓行;3，拥堵
	 * 
	 * @return 路况
	 */
	private int getRoadCondition() {
		return this.traffic_condition;
	}

	/**
	 * 设置路况 取值：0,无路况;1,畅通;2，缓行;3，拥堵
	 * 
	 * @param mRoadCondition
	 */
    private void setRoadCondition(int mRoadCondition) {
		this.traffic_condition = mRoadCondition;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 返回结果状态说明
	 * 
	 * @return 结果状态说明
	 */
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
