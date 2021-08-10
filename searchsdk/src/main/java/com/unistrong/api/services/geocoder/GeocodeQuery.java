package com.unistrong.api.services.geocoder;

/**
 * 此类定义了地理编码查询的关键字和查询城市。
 */
public class GeocodeQuery {
	/**
	 * 查询关键字。
	 */
	private String locationName;
	/**
	 * 查询城市编码。
	 */
	private String city;

	/**
	 * GeocodeQuery构造函数。
	 * 
	 * @param locationName
	 *            查询关键字。
	 * @param city
	 *            地址所在的城市中文名称、citycode、adcode。
	 */
	public GeocodeQuery(String locationName, String city) {
		this.locationName = locationName;
		this.city = city;
	}

	/**
	 * 返回查询地理名称。
	 * 
	 * @return 查询地理名称。
	 */
	public String getLocationName() {
		return this.locationName;
	}

	/**
	 * 设置查询的地理名称。
	 * 
	 * @param paramString
	 *            查询的地理名称。
	 */
	public void setLocationName(String paramString) {
		this.locationName = paramString;
	}

	/**
	 * 返回查询城市编码/城市名称/行政区划代码。
	 * 
	 * @return 查询城市编码/城市名称/行政区划代码。
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * 设置查询城市名称、城市编码或行政区划。
	 * 
	 * @param paramString
	 *            查询的cityname。
	 */
	public void setCity(String paramString) {
		this.city = paramString;
	}

	public int hashCode() {
		int i = 31;
		int j = 1;
		j = i * j + (this.city == null ? 0 : this.city.hashCode());
		j = i
				* j
				+ (this.locationName == null ? 0 : this.locationName.hashCode());

		return j;
	}

	public boolean equals(Object geocodeQuery) {
		if (this == geocodeQuery)
			return true;
		if (geocodeQuery == null)
			return false;
		if (getClass() != geocodeQuery.getClass())
			return false;
		GeocodeQuery localGeocodeQuery = (GeocodeQuery) geocodeQuery;
		if (this.city == null) {
			if (localGeocodeQuery.city != null)
				return false;
		} else if (!this.city.equals(localGeocodeQuery.city))
			return false;
		if (this.locationName == null) {
			if (localGeocodeQuery.locationName != null)
				return false;
		} else if (!this.locationName.equals(localGeocodeQuery.locationName))
			return false;
		return true;
	}
}
