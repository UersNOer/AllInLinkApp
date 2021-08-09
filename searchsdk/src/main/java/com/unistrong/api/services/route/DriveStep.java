package com.unistrong.api.services.route;

import android.os.Parcel;

import com.unistrong.api.services.core.LatLonPoint;

public class DriveStep extends Step implements android.os.Parcelable {
	private float tolls;

    private String ispasspoi;//是否是途径点 0，不是; 1,是
	public static final Creator<DriveStep> CREATOR = new DriveStepCreator();

    private LatLonPoint originLocation;

    private LatLonPoint destinationLocation;

	/**
	 * DriveStep的构造方法。
	 */
	public DriveStep() {
		super();
	}

	/**
	 * 序列化实现。
	 */
	public DriveStep(android.os.Parcel source) {
		super(source);
		this.tolls = source.readFloat();
        this.ispasspoi = source.readString();
        this.originLocation = ((LatLonPoint) source
                .readParcelable(LatLonPoint.class.getClassLoader()));
        this.destinationLocation = ((LatLonPoint) source
                .readParcelable(LatLonPoint.class.getClassLoader()));
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeFloat(this.tolls);
        dest.writeString(this.ispasspoi);
        dest.writeParcelable(this.originLocation, flags);
        dest.writeParcelable(this.destinationLocation, flags);
	}

	/**
	 * 返回驾车路段的收费价格，单位元
	 * 
	 * @return 驾车路段的收费价格，单位元
	 */
	public float getTolls() {
		return this.tolls;
	}

	/**
	 * 设置驾车路段的收费价格，单位元
	 * 
	 * @param tolls
	 *            驾车路段的收费价格，单位元
	 */
	public void setTolls(float tolls) {
		this.tolls = tolls;
	}

    /**
     * 返回时途径点。
     * @return fale 代表不是，true代表是。
     */
    public boolean isPasspoi() {
        if ("1".equals(this.ispasspoi))
            return true;
        else
            return false;
    }

    /**
     *设置是否是途径点。
     * @param isOrNO - 是否是途径点。
     */
    public void setIsPasspoi(boolean isOrNO) {
        if (isOrNO)
            this.ispasspoi = "1";
        else
            this.ispasspoi = "0";

    }



    /**
     * 返回起点坐标
     *
     * @return 起点坐标
     */
    public LatLonPoint getOriginLocation() {
        return this.originLocation;
    }

    /**
     * 设置起点坐标
     *
     * @param originLocation
     *            起点坐标
     */
    public void setOriginLocation(LatLonPoint originLocation) {
        this.originLocation = originLocation;
    }

    /**
     * 返回终点坐标
     *
     * @return 终点坐标
     */
    public LatLonPoint getDestinationLocation() {
        return this.destinationLocation;
    }

    /**
     * 设置终点坐标
     *
     * @param destinationLocation
     *            终点坐标
     */
    public void setDestinationLocation(LatLonPoint destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

}
