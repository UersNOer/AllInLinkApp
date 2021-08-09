package com.unistrong.api.services.route;

import android.os.Parcel;

/**
 * 定义了公交换乘路径规划的一个换乘段的步行信息。
 * 
 */
public class RouteBusWalkItem extends WalkPath implements android.os.Parcelable {
    /**
     * 此路段步行导航信息的起点名称。
     */
    private String originatingName;

    /**
     * 此路段步行导航信息的终点名称。
     */
    private String terminalName;

	public static final Creator<RouteBusWalkItem> CREATOR = new RouteBusWalkItemCreator();

	/**
	 * RouteBusWalkItem 构造函数
	 */
	public RouteBusWalkItem() {

	}

    /**
     * 返回此路段步行导航信息的起点名称。
     * @return   此路段步行导航信息的起点名称。
     */
    public String getOriginatingName() {
        return originatingName;
    }

    /**
     * 设置此路段步行导航信息的起点名称。
     * @param originatingName 此路段步行导航信息的起点名称。
     */
    public void setOriginatingName(String originatingName) {
        this.originatingName = originatingName;
    }

    /**
     * 返回此路段步行导航信息的终点名称。
     * @return   此路段步行导航信息的终点名称。
     */
    public String getTerminalName() {
        return terminalName;
    }
    /**
     * 设置此路段步行导航信息的终点名称。
     * @param terminalName 此路段步行导航信息的终点名称。
     */
    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }


	public RouteBusWalkItem(Parcel source) {
		super(source);
		this.originatingName = source.readString();
		this.terminalName = source.readString();

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.originatingName);
		dest.writeString(this.terminalName);
	}

}
