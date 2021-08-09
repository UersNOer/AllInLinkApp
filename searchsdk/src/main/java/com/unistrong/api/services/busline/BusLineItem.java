package com.unistrong.api.services.busline;

import android.os.Parcel;
import android.os.Parcelable;

import com.unistrong.api.services.core.LatLonPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 此类定义了公交线路信息。
 * 
 */
public class BusLineItem implements android.os.Parcelable {
	/**
	 * 线路名称。
	 */
	private String busLineName;
	/**
	 * 线路总长度。
	 */
	private float distance;
	/**
	 * 线路id。
	 */
	private String busLineId;
	/**
	 * 线路类型。
	 */
	private String busLineType;
	/**
	 * 站点数目。
	 */
	private int stationNum;
	/**
	 * 起始站名称。
	 */
	private String originatingStation;
	/**
	 * 终点站名称。
	 */
	private String terminalStation;
	/**
	 * 始发车时间。
	 */
	private String firstBusTime;
	/**
	 * 末班车时间。
	 */
	private String lastBusTime;
	/**
	 * 城市名称。
	 */
	private String cityName;
	/**
	 * 城市编码。
	 */
	private String cityCode;
	/**
	 * 总票价。
	 */
	private float totalPrice;
	/**
	 * 公交起步价。
	 */
	private float basicPrice;
    /**
     * 是否环线。
     */
    private  String  isLoop;
	/**
	 * 线路关键字。
	 */
	private String keyName;
	/**
	 * 线路通过的坐标点集合。
	 */
	private List<LatLonPoint> directionsCoordinates = new ArrayList<LatLonPoint>();
	/**
	 * 站点信息集合。
	 */
	private List<BusStationItem> busStations = new ArrayList<BusStationItem>();
    /**
     *公交线路所属的公交公司。
     */
    private String busCompany;
    /**
     * 行政区划编码。
     */
    private String  adcode;
    /**
     * 发车间隔。
     */
    private String interval;
	/**
	 * 线路状态。0：停运； 1：正常。
	 */
	private int line_status;
	/**
	 * 是否是自动（无人）售票。0：可人工售票； 1：无人售票。
	 */
	private int is_auto;
	/**
	 * 数据源名称。
	 */
	private String datasource;

	public static final Parcelable.Creator<BusLineItem> CREATOR = new BusLineItemCreator();

	/**
	 * 构造函数。
	 */
	public BusLineItem() {

	}

	public BusLineItem(Parcel source) {
		this.cityCode = source.readString();
		this.busLineName = source.readString();
		this.originatingStation = source.readString();
		this.terminalStation = source.readString();
		this.firstBusTime = source.readString();
		this.lastBusTime = source.readString();
		this.cityName = source.readString();
		this.keyName = source.readString();
		this.distance = source.readFloat();
		this.totalPrice = source.readFloat();
		this.busLineType = source.readString();
		this.stationNum = source.readInt();
		this.busLineId = source.readString();
		this.directionsCoordinates = source
				.createTypedArrayList(LatLonPoint.CREATOR);
		this.busStations = source.createTypedArrayList(BusStationItem.CREATOR);
        this.busCompany = source.readString();
        this.adcode = source.readString();
        this.isLoop = source.readString();
        this.interval = source.readString();
        this.basicPrice = source.readFloat();
		this.line_status = source.readInt();
		this.is_auto = source.readInt();
		this.datasource = source.readString();

	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(this.cityCode);
		dest.writeString(this.busLineName);
		dest.writeString(this.originatingStation);
		dest.writeString(this.terminalStation);
		dest.writeString((this.firstBusTime));
		dest.writeString(this.lastBusTime);
		dest.writeString(this.cityName);
		dest.writeString(this.keyName);
		dest.writeFloat(this.distance);
		dest.writeFloat(this.totalPrice);
		dest.writeString(this.busLineType);
		dest.writeInt(this.stationNum);
		dest.writeString(this.busLineId);
		dest.writeTypedList(directionsCoordinates);
		dest.writeTypedList(busStations);
        dest.writeString(this.busCompany);
        dest.writeString(this.adcode);
        dest.writeString(this.isLoop);
        dest.writeString(this.interval);
        dest.writeFloat(this.basicPrice);
		dest.writeInt(this.line_status);
		dest.writeInt(this.is_auto);
		dest.writeString(this.datasource);
	}


    /**
     * 返回发车间隔。
     * @return  发车间隔
     */
    public String getInterval() {
        return interval;
    }

    /**
     * 设置发车间隔。
     * @param interval 发车间隔。
     */
    public void setInterval(String interval) {
        this.interval = interval;
    }
    /**
     * 返回线路是否环线。
     *
     * @return  true是，false不是。
     */
    public boolean isLoop() {
        if ("1".equals(this.isLoop))
            return true;
        else
            return false;
    }

    /**
     * 设置线路是否环线。
     *
     * @param isLoop
     *            true是，false不是。
     */
    public void setIsLoop(boolean isLoop) {
        if (isLoop)
            this.isLoop = "1";
        else
            this.isLoop = "0";
    }

	/**
	 * 返回公交线路的站点信息列表。
	 * 
	 * @return 公交线路的站点信息列表。
	 */
	public List<BusStationItem> getBusStations() {
		return busStations;
	}

	/**
	 * 设置公交线路的站点信息列表。
	 * 
	 * @param busStations
	 *            公交线路的站点信息列表。
	 */
	public void setBusStations(List<BusStationItem> busStations) {
		this.busStations = busStations;
	}

	/**
	 * 返回公交线路的名称。
	 * 
	 * @return 公交线路的名称。
	 */
	public String getBusLineName() {
		return busLineName;
	}

	/**
	 * 设置公交线路的名称。
	 * 
	 * @param busLineName
	 *            公交线路的名称。
	 */
	public void setBusLineName(String busLineName) {
		this.busLineName = busLineName;
	}

	/**
	 * 返回公交线路全程里程，单位千米。
	 * 
	 * @return 公交线路全程里程。
	 */
	public float getDistance() {
		return distance;
	}

	/**
	 * 设置公交线路全程里程，单位千米。
	 * 
	 * @param distance
	 *            公交线路全程里程数。
	 */
	public void setDistance(float distance) {
		this.distance = distance;
	}

	/**
	 * 返回公交线路的唯一ID。
	 * 
	 * @return 公交线路的唯一ID。
	 */
	public String getBusLineId() {
		return busLineId;
	}

	/**
	 * 设置公交线路的唯一ID。
	 * 
	 * @param busLineId
	 *            公交线路的唯一ID。
	 */
	public void setBusLineId(String busLineId) {
		this.busLineId = busLineId;
	}

	/**
	 * 返回线路类型。
	 * 
	 * @return 线路类型。
	 */
	public String getBusLineType() {
		return busLineType;
	}

	/**
	 * 设置线路类型。
	 * 
	 * @param busLineType
	 *            线路类型。
	 */
	public void setBusLineType(String busLineType) {
		this.busLineType = busLineType;
	}

	/**
	 * 返回站点数目。
	 * 
	 * @return 站点数目。
	 */
	public int getStationNum() {
		return stationNum;
	}

	/**
	 * 设置站点数目。
	 * 
	 * @param stationNum
	 *            站点数目。
	 */
	public void setStationNum(int stationNum) {
		this.stationNum = stationNum;
	}

	/**
	 * 返回公交线路的始发站名称。
	 * 
	 * @return 公交线路的始发站名称。
	 */
	public String getOriginatingStation() {
		return originatingStation;
	}

	/**
	 * 设置公交线路的始发站名称。
	 * 
	 * @param originatingStation
	 *            公交线路的始发站名称。
	 */
	public void setOriginatingStation(String originatingStation) {
		this.originatingStation = originatingStation;
	}

	/**
	 * 返回公交线路的终点站名称。
	 * 
	 * @return 公交线路的终点站名称。
	 */
	public String getTerminalStation() {
		return terminalStation;
	}

	/**
	 * 设置公交线路的终点站名称。
	 * 
	 * @param terminalStation
	 *            公交线路的终点站名称。
	 */
	public void setTerminalStation(String terminalStation) {
		this.terminalStation = terminalStation;
	}

	/**
	 * 返回公交线路的首班车时间。
	 * 
	 * @return 公交线路的首班车时间。
	 */
	public String getFirstBusTime() {
		return firstBusTime;
	}

	/**
	 * 设置公交线路的首班车时间。
	 * 
	 * @param firstBusTime
	 *            公交线路的首班车时间。
	 */
	public void setFirstBusTime(String firstBusTime) {
		this.firstBusTime = firstBusTime;
	}

	/**
	 * 返回公交线路的末班车时间。
	 * 
	 * @return 公交线路的末班车时间。
	 */
	public String getLastBusTime() {
		return lastBusTime;
	}

	/**
	 * 设置公交线路的末班车时间。
	 * 
	 * @param lastBusTime
	 *            公交线路的末班车时间。
	 */
	public void setLastBusTime(String lastBusTime) {
		this.lastBusTime = lastBusTime;
	}



	/**
	 * 返回城市名称。
	 * 
	 * @return 城市名称。
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * 设置城市名称。
	 * 
	 * @param cityName
	 *            城市名称。
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * 返回城市编码。
	 * 
	 * @return 城市编码。
	 */
	public String getCityCode() {
		return cityCode;
	}

	/**
	 * 设置城市编码。
	 * 
	 * @param cityCode
	 *            城市编码。
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * 返回公交线路的总价，单位元。
	 * 
	 * @return 公交线路的总价，单位元。
	 */
	public float getTotalPrice() {
		return totalPrice;
	}

	/**
	 * 返回公交线路的总价，单位元。
	 * 
	 * @param totalPrice
	 *            公交线路的总价，单位元。
	 */
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * 返回公交线路的起步价，单位元。
	 * 
	 * @return 公交线路的起步价，单位元。
	 */
	public float getBasicPrice() {
		return basicPrice;
	}

	/**
	 * 返回公交线路的起步价，单位元。
	 * 
	 * @param basicPrice
	 *            公交线路的起步价，单位元。
	 */
	public void setBasicPrice(Float basicPrice) {
		this.basicPrice = basicPrice;
	}

	/**
	 * 返回公交线路的关键字。
	 * 
	 * @return 公交线路的关键字。
	 */
	public String getKeyName() {
		return keyName;
	}

	/**
	 * 返回公交线路的关键字。
	 * 
	 * @param keyName
	 *            公交线路的关键字。
	 */
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	/**
	 * 返回公交线路的站点列表 。
	 * 
	 * @return 公交线路的站点列表。
	 */
	public List<LatLonPoint> getDirectionsCoordinates() {
		return directionsCoordinates;
	}

	/**
	 * 设置公交线路的站点列表。
	 * 
	 * @param directionsCoordinates
	 *            公交线路的站点列表。
	 */
	public void setDirectionsCoordinates(List<LatLonPoint> directionsCoordinates) {
		this.directionsCoordinates = directionsCoordinates;
	}

    /**
     * 设置公交线路所属的公交公司。
     * @param busCompany - 公交线路所属的公交公司。
     */
    public void setBusCompany(java.lang.String busCompany){
        this.busCompany = busCompany;
    }

    /**
     * 返回公交线路所属的公交公司。
     * @return 公交线路所属的公交公司。
     */
    public java.lang.String getBusCompany(){
        return busCompany;
    }

    /**
     * 设置区域编码。
     * @param adcode - 区域编码。
     */
    public void setAdcode(java.lang.String adcode){
        this.adcode = adcode;

    }

    /**
     * 返回区域编码。
     * @return 区域编码。
     */
    public java.lang.String getAdcode(){
        return  adcode;
    }

	/**
	 * 返回线路是否正常运营。
	 *
	 * @return true表示线路正常运营，false表示停运。
	 */
	public boolean isLineNormalRunning() {
		boolean isNormal = false;
		if (this.line_status == 1) {
			isNormal = true;
		} else {
			isNormal = false;
		}
		return isNormal;
	}

	/**
	 * 设置线路运营的状态。
	 * @param line_status
	 *                   线路运营的状态。
	 */
	public void setLineStatus(int line_status) {
		this.line_status = line_status;
	}

	/**
	 * 返回线路是否自动售票。
	 *
	 * @return 线路是否自动售票，true是，false不是。
	 */
	public boolean isAuto() {
		boolean isAuto = false;
		if (this.is_auto == 1) {
			isAuto = true;
		} else {
			isAuto = false;
		}
		return isAuto;
	}

	/**
	 * 设置线路是否自动售票。
	 * @param is_auto
	 *               线路是否自动售票，0：可人工售票； 1：无人售票。
	 */
	public void setIsAuto(int is_auto) {
		this.is_auto = is_auto;
	}

	/**
	 * 返回数据源名称。
	 * @return 数据源名称。
	 */
	public String getDatasource() {
		return datasource;
	}

	/**
	 * 设置数据源名称。
	 * @param datasource
	 *                  数据源名称。
	 */
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	@Override
	public String toString() {
		return "BusLineItem [busLineName=" + busLineName + ", distance="
				+ distance + ", busLineId=" + busLineId + ", stationNum="
				+ stationNum + ", originatingStation=" + originatingStation
				+ ", terminalStation=" + terminalStation + ", firstBusTime="
				+ firstBusTime + ", lastBusTime=" + lastBusTime
				+ ", totalPrice=" + totalPrice + ", basicPrice=" + basicPrice
				+ ", keyName=" + keyName + ", directionsCoordinates="
				+ directionsCoordinates + ", busStations=" + busStations
				+ ", busCompany" + busCompany + ", adcode" + adcode
				+ ", interval" + interval + ", line_status" + line_status
				+ ", is_auto"+is_auto + ", datasource"+ datasource+ "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 比较两个查询条件是否相同。
	 * 
	 * @param paramObject
	 *            查询条件。
	 * @return 查询条件是否相同。
	 */
	@Override
	public boolean equals(Object paramObject) {
		if (this == paramObject)
			return true;
		if (paramObject == null)
			return false;
		if (getClass() != paramObject.getClass())
			return false;
		BusLineItem localBusLineItem = (BusLineItem) paramObject;
		if (this.busLineId == null) {
			if (localBusLineItem.busLineId != null)
				return false;
		} else if (!this.busLineId.equals(localBusLineItem.busLineId))
			return false;

		if (this.busLineName == null) {
			if (localBusLineItem.busLineName != null)
				return false;
		} else if (!this.busLineName.equals(localBusLineItem.busLineName))
			return false;

		return true;
	}

	public int hashCode() {
		int i = 31;
		int j = 1;
		j = i * j + (this.busLineId == null ? 0 : this.busLineId.hashCode());
		j = i * j
				+ (this.busLineName == null ? 0 : this.busLineName.hashCode());
		return j;

	}

	/**
	 * 日期类型转换String类型（HH:MM）。
	 * 
	 * @param paramDate
	 *            时间。
	 * @return 格式化后的时间。
	 */
	private static String fromDateToStr(Date paramDate) {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("HH:mm");
		return paramDate != null ? localSimpleDateFormat.format(paramDate) : "";
	}

	/**
	 * String类型（HH:MM）转换成日期类型。
	 * 
	 * @param paramString
	 *            时间。
	 * @return 格式化后的时间。
	 */
    private static Date fromStrToDate(String paramString) {
		if ((paramString == null) || (paramString.trim().equals(""))) {
			return null;
		}
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("HH:mm");
		Date localDate = null;
		try {
			localDate = localSimpleDateFormat.parse(paramString);
		} catch (ParseException localParseException) {

		}
		return localDate;
	}

}
