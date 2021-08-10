package com.unistrong.api.services.route;

import android.os.Parcel;

import com.unistrong.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.List;

class Step implements android.os.Parcelable {

	private String area;// 该路段是否在城市内部 0，不在; 1,在
	private String direction;// 进入道路的角度
	private float distance;// 路段距离
	private float duration;// 路段耗时
	private String road;// 路段描述,道路名称。
	private List<LatLonPoint> path = new ArrayList<LatLonPoint>();// 路段位置坐标
	private String instruction;// 行进指令
	private int turn; // 行进指令状态值
	public static final Creator<Step> CREATOR = new StepCreator();

	/**
	 * Step的构造方法。
	 */
	public Step() {
		super();
	}

	/**
	 * 序列化实现。
	 */
	public Step(android.os.Parcel source) {
		this.area = source.readString();
		this.direction = source.readString();
		this.distance = source.readFloat();
		this.duration = source.readFloat();
		this.road = source.readString();
		this.path = source.createTypedArrayList(LatLonPoint.CREATOR);
		this.instruction = source.readString();
		this.turn = source.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.area);
		dest.writeString(this.direction);
		dest.writeFloat(this.distance);
		dest.writeFloat(this.duration);
		dest.writeString(this.road);
		dest.writeTypedList(this.path);
		dest.writeString(this.instruction);
		dest.writeInt(this.turn);
	}

	/**
	 * 返回此路段是否在城市内部。
	 * 
	 * @return 此路段是否在城市内部。
	 */
	private boolean isInCity() {
		if ("1".equals(this.area))
			return true;
		else
			return false;
	}

	/**
	 * 设置此路段是否在城市内部。
	 * 
	 * @param isOrNO
	 *            此路段是否在城市内部。
	 */
    private void setIsInCity(boolean isOrNO) {
		if (isOrNO)
			this.area = "1";
		else
			this.area = "0";
	}

	/**
	 * 返回路段的行进方向。
	 * 
	 * @return 路段的行进方向。
	 */
	public String getOrientation() {
		return this.direction;
	}

	/**
	 * 设置路段的行进方向。
	 * 
	 * @param orientation
	 *            路段的行进方向。
	 */
	public void setOrientation(String orientation) {
		this.direction = orientation;
	}

	/**
	 * 设置路段的距离，单位米。
	 * 
	 * @param distance
	 *            路段的距离，单位米。
	 * 
	 */
	public void setDistance(float distance) {
		this.distance = distance;
	}

	/**
	 * 返回路段的距离，单位米。
	 * 
	 * @return 路段的距离，单位米。
	 */
	public float getDistance() {
		return this.distance;
	}

	/**
	 * 设置路段的预计时间，单位为秒。
	 * 
	 * @param duration
	 *            路段的预计时间，单位为秒。
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}

	/**
	 * 返回路段的预计时间，单位为秒。
	 * 
	 * @return 。路段的预计时间，单位为秒。
	 */
	public float getDuration() {
		return this.duration;
	}

	/**
	 * 设置路段的道路名称。
	 * 
	 * @param name
	 *            道路名称。
	 */
	public void setRoad(String name) {
		this.road = name;
	}

	/**
	 * 返回路段的道路名称。
	 * 
	 * @return 路段的道路名称。
	 */
	public String getRoad() {
		return this.road;
	}

	/**
	 * 返回路段的坐标点集合。
	 * 
	 * @return 路段的坐标点集合。
	 */
	public List<LatLonPoint> getPolyline() {
		return this.path;
	}

	/**
	 * 设置路段的坐标点集合。
	 * 
	 * @param path
	 *            路段的坐标点集合。
	 */

	public void setPolyline(List<LatLonPoint> path) {
		this.path = path;
	}

	/**
	 * 设置路段行进指示。
	 * 
	 * @param instruction
	 *            路段行进指示。
	 */
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	/**
	 * 返回路段的行进指示。
	 * 
	 * @return 路段的行进指示。
	 */
	public String getInstruction() {
		return this.instruction;
	}

	/**
	 * 返回路段的行进指令状态值。
	 * @return 路段的行进指令状态值(0-"无效", 1-"左转", 2-"右转", 3-"向左前方行驶", 4-"向右前方行驶", 5-"向左后方行驶", 6-"向右后方行驶", 7-"左转掉头", 8-"直行", 9-"靠左", 10-"靠右", 11-"进入环岛", 12-"离开环岛", 13-"减速行驶")。
	 * @since 1.6.0
	 */
	public int getTurn() {
		return this.turn;
	}

	/**
	 * 设置路段的行进指令状态值。
	 * @param turn 路段的行进指令状态值。
	 */
	public void setTurn(int turn) {
		this.turn = turn;
	}

	@Override
	public int describeContents() {
		return 0;
	}
}
