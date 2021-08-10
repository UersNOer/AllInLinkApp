package com.unistrong.api.services.route;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义了公交路径规划的一个路段。 路段最多包含一段步行信息和公交导航信息。
 */
public class BusPath extends Path implements android.os.Parcelable {
	/**
	 * 总票价。
	 */
	private float price;
	/**
	 * 路段票价。
	 */
	private float linePrice;
	/**
	 * 此方案的总步行距离。
	 */
	private float walkDistance;

	/**
	 * 公交路径规划方案的路段列表。
	 */
	private List<BusStep> mSteps = new ArrayList<BusStep>();
	public static final Creator<BusPath> CREATOR = new BusPathCreator();

	/**
	 * BusPath 构造函数。
	 */
	public BusPath() {
		super();
	}

	/**
	 * 序列化实现
	 * 
	 * @param source
	 */
	public BusPath(Parcel source) {
		super(source);
		this.mSteps = source.createTypedArrayList(BusStep.CREATOR);
		price = source.readFloat();
		linePrice = source.readFloat();
		walkDistance = source.readFloat();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest,flags);
		dest.writeTypedList(mSteps);
        dest.writeFloat(price);
		dest.writeFloat(linePrice);
		dest.writeFloat(walkDistance);
	}

	/**
	 * 返回此方案的总步行距离，单位米。
	 * 
	 * @return 此方案的总步行距离，单位米。
	 */
	public float getWalkDistance() {
		return walkDistance;

	}

	/**
	 * 设置此方案的总步行距离，单位米。
	 * 
	 * @param mWalkDistance
	 *            此方案的总步行距离，单位米。
	 */
	public void setWalkDistance(float mWalkDistance) {
		this.walkDistance = mWalkDistance;
	}

	/**
	 * 返回公交换乘方案的花费，单位元。
	 * 
	 * @return 公交换乘方案的花费，单位元。
	 */
    public float getPrice() {
		return price;
	}

	/**
	 * 设置公交换乘方案的花费，单位元。
	 * 
	 * @param price
	 *            公交换乘方案的花费。
	 */
    public void setPrice(Float price) {
		this.price = price;
	}

	/**
	 * 返回公交路径规划方案的路段列表。
	 * 
	 * @return 公交路径规划方案的路段列表。
	 */
	public List<BusStep> getSteps() {
		return mSteps;
	}

	/**
	 * 设置公交路径规划方案的路段列表。
	 * 
	 * @param mSteps
	 *            公交路径规划方案的路段列表
	 */
	public void setSteps(List<BusStep> mSteps) {
		this.mSteps = mSteps;
	}



	@Override
	public int describeContents() {

		return 0;
	}

	/**
	 * 返回票价。
	 * 
	 * @return 票价，单位：份。
	 */
    private float getLinePrice() {
		return linePrice;
	}

	/**
	 * 设置票价。
	 * 
	 * @param linePrice
	 *            票价，单位：份。
	 */
    private void setLinePrice(float linePrice) {
		this.linePrice = linePrice;
	}

}
