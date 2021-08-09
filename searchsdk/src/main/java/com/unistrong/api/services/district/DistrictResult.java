package com.unistrong.api.services.district;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 行政区域查询结果类。
 * 
 */
public class DistrictResult implements Parcelable {
	/**
	 * 查询条件。
	 */
	private DistrictSearchQuery query;
	/**
	 * 查询行政区的结果。
	 */
	private ArrayList<DistrictItem> district = new ArrayList<DistrictItem>();

	public static final Parcelable.Creator<DistrictResult> CREATOR = new DistrictResultCreator();

	/**
	 * 构造一个行政区划查询结果类。
	 */
	public DistrictResult() {
		super();
	}

	/**
	 * 依据参数构造行政区域查询结果。
	 * 
	 * @param query
	 *            查询条件。
	 * @param districts
	 *            行政区划类的列表。
	 */
	public DistrictResult(DistrictSearchQuery query,
			ArrayList<DistrictItem> districts) {
		this.query = query;
		this.district = districts;

	}

	protected DistrictResult(Parcel source) {
		this.query = ((DistrictSearchQuery) source
				.readParcelable(DistrictSearchQuery.class.getClassLoader()));
		this.district = source.createTypedArrayList(DistrictItem.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.query, flags);
		dest.writeTypedList(this.district);

	}

	/**
	 * 返回查询结果对应的查询参数。
	 * 
	 * @return 查询结果对应的查询参数。
	 */
	public DistrictSearchQuery getQuery() {
		return query;
	}

	/**
	 * 设置查询结果对应的查询参数。
	 * 
	 * @param query
	 *            查询结果对应的查询参数。
	 */
	public void setQuery(DistrictSearchQuery query) {
		this.query = query;
	}

	/**
	 * 返回查询行政区的结果，只有UnistrongException.getErrorCode()返回0时，才有查询结果。
	 * 
	 * @return 查询行政区的结果。
	 */
	public ArrayList<DistrictItem> getDistrict() {
		return district;
	}

	/**
	 * 设置查询行政区的结果。
	 * 
	 * @param district
	 *            查询行政区的结果。
	 */
	public void setDistrict(ArrayList<DistrictItem> district) {
		this.district = district;
	}


	@Override
	public int describeContents() {
		return 0;
	}



}
