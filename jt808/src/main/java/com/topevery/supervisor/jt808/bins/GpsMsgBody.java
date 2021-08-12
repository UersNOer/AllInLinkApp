package com.example.android_supervisor.jt808.bins;

import com.example.android_supervisor.jt808.utils.BCDUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wujie
 */
public class GpsMsgBody extends Binary {
	// 纬度， 以度为单位的纬度值乘以10的6次方
	private final Int32 latitude = new Int32();

	// 经度， 以度为单位的经度值乘以10的6次方
	private final Int32 longitude = new Int32();

	// 海拔，单位为米
	private final Int16 elevation = new Int16();

	// 速度 1/10km/h
	private final Int16 speed = new Int16();

	// 方向 0-359，正北为 0，顺时针
	private final Int16 direction = new Int16();

	// 坐标类型(0:WGS84; 1:GCJ02)
	private final Int8 coordType = new Int8();

	// 坐标点来源(0:GPS; 1:基站; 2:网络)
	private final Int8 provider = new Int8();

	// 坐标是否在网格内
	private final Int8 inGrid = new Int8();

	// 时间(BCD[6]) YY-MM-DD-hh-mm-ss
	// GMT+8 时间，本标准中之后涉及的时间均采用此时区
	private final VarChar time = new VarChar(6);

	// 用户id
	private final VarChar userId = new VarChar(10);

	public GpsMsgBody() {
	}

	public GpsMsgBody(byte[] bytes) {
		super(bytes);
	}

	public int getLatitude() {
		return latitude.get();
	}

	public void setLatitude(int latitude) {
		this.latitude.set(latitude);
	}

	public int getLongitude() {
		return longitude.get();
	}

	public void setLongitude(int longitude) {
		this.longitude.set(longitude);
	}

	public int getElevation() {
		return elevation.get();
	}

	public void setElevation(int elevation) {
		this.elevation.set(elevation);
	}

	public float getSpeed() {
		return Float.intBitsToFloat(this.speed.get());
	}

	public void setSpeed(float speed) {
		this.speed.set(Float.floatToIntBits(speed));
	}

	public int getDirection() {
		return direction.get();
	}

	public void setDirection(int direction) {
		this.direction.set(direction);
	}

	public int getCoordType() {
		return this.coordType.get();
	}

	public void setCoordType(int type) {
		this.coordType.set(type);
	}

	public int getProvider() {
		return this.provider.get();
	}

	public void setProvider(int provider) {
		this.provider.set(provider);
	}

	public boolean getInGrid() {
		return this.inGrid.get() > 0;
	}

	public void setInGrid(boolean inGrid) {
		this.inGrid.set(inGrid ? 1 : 0);
	}

	public Date getTime() {
		String str = BCDUtils.bcd2Str(this.time.get());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		Date date = null;
		try {
			date = dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public void setTime(Date time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		String str = dateFormat.format(time);
		byte[] bcdTime = BCDUtils.str2Bcd(str);
		this.time.set(bcdTime);
	}

	public String getUserId() {
		return BCDUtils.bcd2Str(this.userId.get());
	}

	public void setUserId(String userId) {
		byte[] bcdUid = BCDUtils.str2Bcd(userId);
		this.userId.set(bcdUid);
	}
}
