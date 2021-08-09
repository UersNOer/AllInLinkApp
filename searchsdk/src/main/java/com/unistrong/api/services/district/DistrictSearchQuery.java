package com.unistrong.api.services.district;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 此类定义了行政区划搜索的参数。
 * 
 */
public class DistrictSearchQuery implements Parcelable {
	/**
	 * 查询关键字。
	 */
	private String keywords;
	/**
	 * 行政区划级别。
	 */
	private String keywordsLevel;
	/**
	 * 城市。
	 */
	private String city;
	/**
	 * 是否返回边界值。
	 */
	private boolean isShowBoundary;

    /**
     * 行政区划级别-省。
     */
    public static final java.lang.String KEYWORDS_PROVINCE = "province";
    /**
     * 行政区划级别-区/县。
     */
    public static final java.lang.String KEYWORDS_DISTRICT = "district";
    /**
     * 行政区划级别-市。
     */
    public static final java.lang.String KEYWORDS_CITY = "city";


	public static final Parcelable.Creator<DistrictSearchQuery> CREATOR = new DistrictSearchQueryCreator();

	/**
	 * 构造 DistrictSearchQuery 对象。
	 */
	public DistrictSearchQuery() {
		super();
	}

	/**
	 * 构造 DistrictSearchQuery 对象。
	 * 
	 * @param keywords
	 *            查询时所用字符串关键字。
	 */
	public DistrictSearchQuery(String keywords) {
		super();
		this.keywords = keywords;
	}

	/**
	 * 构造 DistrictSearchQuery 对象。
	 * 
	 * @param keywords
	 *            查询时所用字符串关键字。
	 * @param keywordsLevel
	 *            行政区划级别。
	 * @param city
	 *            城市。
	 */
	public DistrictSearchQuery(String keywords, java.lang.String keywordsLevel,
			String city) {
		super();
		this.keywords = keywords;
		this.keywordsLevel = keywordsLevel;
		this.city = city;
	}

	/**
	 * 序列化的实现
	 */
	public DistrictSearchQuery(Parcel paramParcel) {
		this.keywords = paramParcel.readString();
		this.city = paramParcel.readString();
		this.keywordsLevel = paramParcel.readString();

	}

	/**
	 * 设置行政区域搜索是否返回边界值，true为返回，false为不返回。
	 * 
	 * @param isBoundary
	 *            政区域搜索是否返回边界值。
	 */
    private void setShowBoundary(boolean isBoundary) {
		this.isShowBoundary = isBoundary;
	}

	/**
	 * 返回行政区域查询是否返回边界值。
	 * 
	 * @return true为返回边界值，false为不返回。
	 */
    private boolean isShowBoundary() {
		return this.isShowBoundary;
	}

	/**
	 * 返回查询时所用字符串关键字。
	 * 
	 * @return 查询时所用字符串关键字。
	 */
	public String getKeywords() {
		return this.keywords;
	}

	/**
	 * 设置查询时所用字符串关键字。
	 * 
	 * @param keywords
	 *            查询时所用字符串关键字。
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * 返回查询关键字的级别。
	 * 
	 * @return 查询关键字的级别。
	 */
	public java.lang.String getKeywordsLevel() {
		return this.keywordsLevel;

	}

	/**
	 * 设置查询关键字的级别。
	 * 
	 * @param mLevel
	 *            查询关键字的级别。
	 */
	public void setKeywordsLevel(java.lang.String mLevel) {
		this.keywordsLevel = mLevel;

	}

	/**
	 * 返回行政区划查询的所在城市。
	 * 
	 * @return 行政区划查询的所在城市。
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * 设置行政区划查询的所在城市。
	 * 
	 * @param city
	 *            行政区划查询的所在城市。
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 返回行政区划查询条件的关键字是否为非空。
	 * 
	 * @return true关键字不为空，false关键字为空。
	 */
	public boolean checkKeyWords() {
		if (this.keywords == null) {
			return false;
		}

		return !this.keywords.trim().equalsIgnoreCase("");
	}

	/**
	 * 是否返回下级区划。
	 * 
	 * @return true表示返回下级区划，反之，不返回下级区划。
	 */
	private boolean isShowChild() {
		return this.isShowBoundary;

	}

	/**
	 * 设置是否返回下级区划。
	 * 
	 * @param mShowChild
	 *            是否返回下级区划。
	 */
    private void setShowChild(boolean mShowChild) {

	}

	/**
	 * 检查行政区划级别参数是否正确。
	 * 
	 * @return 行政区划级别参数是否正确。
	 */
	public boolean checkLevels() {
        if (this.keywordsLevel == null || this.keywordsLevel.equals("")) {
            return false;
        }

        return (this.keywordsLevel.trim().equals("country")) || (this.keywordsLevel.trim().equals("province")) || (this.keywordsLevel.trim().equals("city")) || (this.keywordsLevel.trim().equals("district"));
	}

	public DistrictSearchQuery clone() {
		DistrictSearchQuery localDistrictSearchQuery = new DistrictSearchQuery(this.keywords, this.keywordsLevel, this.city);
		return localDistrictSearchQuery;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.keywords);
		dest.writeString(this.city);
		dest.writeString(this.keywordsLevel);
	}
}
