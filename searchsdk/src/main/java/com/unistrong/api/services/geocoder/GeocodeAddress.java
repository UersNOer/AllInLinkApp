package com.unistrong.api.services.geocoder;

import android.os.Parcel;
import android.os.Parcelable;

import com.unistrong.api.services.core.LatLonPoint;

/**
 * 地理编码返回的结果。
 */
public class GeocodeAddress implements android.os.Parcelable {
	/**
	 * 经纬度坐标。
	 */
	private LatLonPoint location;

	/**
	 * 位置的附加信息，是否精确查找。1为精确查询，0为非精确查询。
	 */
	private String precise;

	/**
	 * 可信度。
	 */
	private String confidence;

	/**
	 * 地址类型。
	 */
	private String level;

    /**
     * 所在省名称、直辖市的名称。
     */
    private String province;

    /**
     * 所在城市名称。
     */
    private String city;

    /**
     * 所在区（县）名称。
     */
    private String district;

    /**
     * 所在城市编码。
     */
    private String cityCode;

    /**
     * 所在区（县）的编码。
     */
    private String adCode;

    /**
     * 地理编码返回的格式化地址。
     */
    private String formatAddress;



	public static final Parcelable.Creator<GeocodeAddress> CREATOR = new GeocodeAddressCreator();

	/**
	 * GeocodeAddress 构造函数
	 */
	public GeocodeAddress() {

	}

	public GeocodeAddress(Parcel paramParcel) {
		this.location = ((LatLonPoint) paramParcel.readValue(LatLonPoint.class
				.getClassLoader()));
		this.precise = paramParcel.readString();
		this.confidence = paramParcel.readString();
		this.level = paramParcel.readString();
        this.province = paramParcel.readString();
        this.city = paramParcel.readString();
        this.district = paramParcel.readString();
        this.adCode = paramParcel.readString();
        this.formatAddress = paramParcel.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel paramParcel, int paramInt) {
		paramParcel.writeValue(location);
		paramParcel.writeString(precise);
		paramParcel.writeString(confidence);
		paramParcel.writeString(level);
        paramParcel.writeString(this.province);
        paramParcel.writeString(this.city);
        paramParcel.writeString(this.district);
        paramParcel.writeString(this.adCode);
        paramParcel.writeString(this.formatAddress);
	}

	/**
	 * 返回经纬度坐标。
	 * 
	 * @return 经纬度坐标。
	 */
	public LatLonPoint getLatLonPoint() {
		return location;
	}

	/**
	 * 设置经纬度坐标。
	 * 
	 * @param location
	 *            经纬度坐标。
	 */
	public void setLatLonPoint(LatLonPoint location) {
		this.location = location;
	}

	/**
	 * 返回位置的附加信息，是否精确查找。
	 * 
	 * @return 位置的附加信息，是否精确查找。
	 */
	public String getPrecise() {
		return precise;
	}

	/**
	 * 设置位置的附加信息，是否精确查找。
	 * 
	 * @param precise
	 *            位置的附加信息，是否精确查找。
	 */

	public void setPrecise(String precise) {
		this.precise = precise;
	}

	/**
	 * 返回可信度。
	 * 
	 * @return 可信度。
	 */

	public String getConfidence() {
		return confidence;
	}

	/**
	 * 设置可信度。
	 * 
	 * @param confidence
	 *            可信度。
	 */
	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}

	/**
	 * 返回地址类型。
	 * 
	 * @return 地址类型。
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * 设置地址类型。
	 * 
	 * @param level
	 *            地址类型。
	 */
	public void setLevel(String level) {
		this.level = level;
	}


    /**
     * 返回逆地理编码返回的所在省名称、直辖市的名称。
     *
     * @return 逆地理编码返回的所在省名称、直辖市的名称。
     */
    public java.lang.String getProvince() {
        return province;
    }

    /**
     * 设置地理编码返回的所在省名称、直辖市的名称。
     *
     * @param province
     *            地理编码返回的所在省名称、直辖市的名称。
     */
    public void setProvince(java.lang.String province) {
        this.province = province;
    }

    /**
     * 地理编码返回的所在城市名称。
     *
     * @return 地理编码返回的所在城市名称。
     */
    public java.lang.String getCity() {
        return city;

    }

    /**
     * 设置地理编码返回的所在城市名称。
     *
     * @param city
     *            地理编码返回的所在城市名称。
     */
    public void setCity(java.lang.String city) {
        this.city = city;
    }

    /**
     * 设置地理编码结果所在城市编码。
     *
     * @param cityCode
     *            地理编码结果所在城市编码。
     */
    private void setCityCode(java.lang.String cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * 返回地理编码结果所在城市编码。
     *
     * @return 地理编码结果所在城市编码。
     */
    private java.lang.String getCityCode() {
        return cityCode;

    }

    /**
     * 返回地理编码结果所在区（县）的编码。
     *
     * @return 地理编码结果所在区（县）的编码。
     */
    public java.lang.String getAdCode() {
        return adCode;

    }

    /**
     * 设置地理编码结果所在区（县）的编码。
     *
     * @param adCode
     *            地理编码结果所在区（县）的编码。
     */
    public void setAdCode(java.lang.String adCode) {
        this.adCode = adCode;

    }

    /**
     * 返回地理编码返回的格式化地址。
     *
     * @return 地理编码返回的格式化地址。
     */
    public String getFormatAddress() {
        return formatAddress;
    }

    /**
     * 设置地理编码返回的格式化地址。
     *
     * @param formatAddress
     *            地理编码返回的格式化地址。
     */
    public void setFormatAddress(String formatAddress) {
        this.formatAddress = formatAddress;
    }

    /**
     * 返回地理编码返回的所在区（县）名称。
     *
     * @return 地理编码返回的所在区（县）名称。
     */
    public java.lang.String getDistrict() {
        return district;

    }

    /**
     * 设置地理编码返回的所在区（县）名称。
     *
     * @param district
     *            地理编码返回的所在区（县）名称。
     */
    public void setDistrict(java.lang.String district) {
        this.district = district;
    }


}
