package com.unistrong.api.services.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Address;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Looper;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * CoreUtil
 * 
 * <p>
 * Base class with code to manage the tool methods
 * 
 * @author
 * @version 1.0 2010.01.22<br>
 * 
 */
public class CoreUtil {
	public static final String EmptyString = "";
	private static final long NanaSeconds = 1000000000;
	public static final int E6Multiple = 1000000;
	private static String SMD5 = null;
	private static String scode = "";
	public static final String HtmlBlack = "#000000";
	public static final String HtmlGray = "#808080";
	public static final String HtmlStrong = "strong";
	private static String apiKey;
	public static boolean is900913 = true;
	private static String searchDefURL = "";
	private static String cloudDefURL = "";
	private static String truckSearchDefURL="";
	private static String searchTruckURL=null;
	private static String searchURL=null;
	private static String cloudURL = "";

	// 记录searchSDK的版本号
	private static final String SEARCHSDK_VERSION = "1.6.0";

	/**
	 * 获取终端身份信息
	 * 
	 * @param context
	 * @return 终端身份信息的md5
	 */
	protected static String getScode(Context context, String ts) {
		try {
			// scode一直在变,去除此处逻辑
//			if ((scode != null) && (!"".equals(scode))) {
//				return scode;
//			}
			PackageInfo localPackageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 64);

			byte[] arrayOfByte1 = localPackageInfo.signatures[0].toByteArray();
			MessageDigest localMessageDigest = MessageDigest
					.getInstance("SHA1");
			byte[] arrayOfByte2 = localMessageDigest.digest(arrayOfByte1);
			StringBuffer localStringBuffer = new StringBuffer();
			for (int i = 0; i < arrayOfByte2.length; i++) {
				String str = Integer.toHexString(0xFF & arrayOfByte2[i])
						.toUpperCase(Locale.US);

				if (str.length() == 1)
					localStringBuffer.append("0");
				localStringBuffer.append(str);
				localStringBuffer.append(":");
			}
			localStringBuffer.append(localPackageInfo.packageName);
			localStringBuffer.append(":" + ts);
			// 大写加密
//			scode = MD5(localStringBuffer.toString());
			// 小写加密
			scode = MD5.encryptString(localStringBuffer.toString());
			return scode;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {

		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {

		} catch (Throwable localThrowable) {

		}
		return scode;
	}

	/**
	 * 判断参数是否为空。
	 * 
	 * @param param
	 *            参数。
	 * @throws UnistrongException
	 *             异常。
	 */
	public static void checkParam(String param) throws UnistrongException {
		if (param == null || "".equals(param)) {
			throw new UnistrongException(UnistrongException.ERROR_INVALID_PARAMETER);
		}
	}

	/**
	 * 获取paikey。
	 * 
	 * @param context
	 * @return paikey
	 * @throws UnistrongException
	 *             Leador异常。
	 */
	public static String getApiKey(Context context) {
		try {
			if ((apiKey == null) || (apiKey.equals(""))) {
				ApplicationInfo aapinfo = context
						.getPackageManager().getApplicationInfo(
								context.getPackageName(), PackageManager.GET_META_DATA);
				if (aapinfo == null||aapinfo.metaData==null) {
					return apiKey;
				}
				apiKey = aapinfo.metaData.getString("com.leador.apikey");
			}
		} catch (PackageManager.NameNotFoundException e) {
			apiKey = "";
		}
		return apiKey;
	}
	
	public static String getSearchUrl(Context context) {
		try {
			if ((searchURL == null) || (searchURL.equals(""))) {
				ApplicationInfo aapinfo = context
						.getPackageManager().getApplicationInfo(
								context.getPackageName(), PackageManager.GET_META_DATA);
				if (aapinfo == null||aapinfo.metaData==null) {
					return searchURL;
				}
				searchURL = aapinfo.metaData.getString("com.leador.api.url");
                if(searchURL == null || searchURL.equals("")){
					searchURL = searchDefURL;
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			searchURL = searchDefURL;
		}
		return searchURL;
	}

	public static String getCloudUrl(Context context) {
		try {
			if ((cloudURL == null) || (cloudURL.equals(""))) {
				ApplicationInfo aapinfo = context
						.getPackageManager().getApplicationInfo(
								context.getPackageName(), PackageManager.GET_META_DATA);
				if (aapinfo == null||aapinfo.metaData==null) {
					return cloudDefURL;
				}
				cloudURL = aapinfo.metaData.getString("com.leador.cloud.url");
				if(cloudURL == null || cloudURL.equals("")){
					cloudURL = cloudDefURL;
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			cloudURL = cloudDefURL;
		}
		return cloudURL;
	}


	public static String getTruckSearchUrl(Context context) {
		try {
			if ((searchTruckURL == null) || (searchTruckURL.equals(""))) {
				ApplicationInfo aapinfo = context
						.getPackageManager().getApplicationInfo(
								context.getPackageName(), PackageManager.GET_META_DATA);
				if (aapinfo == null||aapinfo.metaData==null) {
					return truckSearchDefURL;
				}
				searchTruckURL = aapinfo.metaData.getString("com.leador.search.truck.url");
				if(searchTruckURL == null || searchTruckURL.equals("")){
					searchTruckURL = truckSearchDefURL;
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			searchTruckURL = truckSearchDefURL;
		}
		return searchTruckURL;
	}




	public static void setApiKey(String apiKey) {
		CoreUtil.apiKey = apiKey;
	}

	public static boolean IsEmptyOrNullString(String s) {
		return (s == null) || (s.trim().length() == 0);
	}

	public static long getCurrentTimeStamp() {
		long stamp = System.nanoTime() / NanaSeconds;
		return stamp;
	}

	public static long getCurrentTimeStampInMilly() {
		long stamp = System.nanoTime() * 1000 / NanaSeconds;
		return stamp;
	}

	public static int E6ToMeter(int e6Span) {
		// 111.7 KM
		return (int) (1117 * ((long) e6Span) / (E6Multiple / 100));
	}

	public static int MeterToE6(int meter) {
		return (int) ((long) meter * E6Multiple / (1117 * 100));
	}

	public static boolean isOverdue(long stamp, long cur,
			long over_bar_in_seconds) {
		return cur - stamp > over_bar_in_seconds;
	}

	public static Document getXml(InputStream in) {
		Document xml = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			xml = builder.parse(in);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return xml;
	}

	public static Document getXml(String xml) {
		return getXml(new ByteArrayInputStream(xml.getBytes()));
	}

	public static Looper makeLooper() {
		if (Looper.myLooper() == null) {
			Looper.prepare();
			Looper.loop();
		}

		return Looper.myLooper();
	}

	/**
	 * 获取字符串的md5值。
	 * 
	 * @param s
	 *            字符串。
	 * @return 字符串md5结果。
	 */
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String loadMd5(Context activity) {
		if (SMD5 == null) {
			final char[] Sigs = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
					'9', 'A', 'B', 'C', 'D', 'E', 'F' };
			String Md5 = EmptyString;
			try {
				Signature[] sigs = activity.getPackageManager().getPackageInfo(
						activity.getPackageName(),
						PackageManager.GET_SIGNATURES).signatures;
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(sigs[0].toByteArray());
				byte[] digest = md5.digest();
				for (int di = 0; di < digest.length; di++) {
					int tmp = digest[di] < 0 ? 256 + digest[di] : digest[di];
					Md5 += Sigs[tmp / 16];
					Md5 += Sigs[tmp % 16];
					if (di != digest.length - 1) {
						Md5 += ":";
					}
				}
				SMD5 = Md5;
			} catch (NoSuchAlgorithmException e) {
			} catch (NameNotFoundException e) {
			}
		}

		return SMD5;
	}

	@SuppressWarnings("deprecation")
	public static Proxy getProxy(Context cnt) {
		Proxy proxy = null;
		ConnectivityManager connMgr = (ConnectivityManager) cnt
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		if (null != info) {
			String proxyHost = null;
			int proxyPort = 0;
			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				// WIFI: global http proxy
				proxyHost = android.net.Proxy.getHost(cnt);
				proxyPort = android.net.Proxy.getPort(cnt);
			} else {
				// GPRS: APN http proxy
				proxyHost = android.net.Proxy.getDefaultHost();
				proxyPort = android.net.Proxy.getDefaultPort();
			}

			if (proxyHost != null) {
				proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP,
						new InetSocketAddress(proxyHost, proxyPort));
			}
		}
		return proxy;
	}

	public static long LatLonToE6(double pos) {

		return (long) (pos * E6Multiple);
	}

	public static double E6ToLatLon(long e6) {

		return ((double) e6) / E6Multiple;
	}

	public static boolean isNear(Rect rect, int enlargeDis, Point pt) {
		Rect rc = new Rect(rect.left - enlargeDis, rect.top - enlargeDis,
				rect.right + enlargeDis, rect.bottom + enlargeDis);

		return rc.contains(pt.x, pt.y);
	}

	public static void Log(String tag, String msg) {
		Log.e(tag, msg);
	}

	public static Address makeDefaultAddress() {
		Address addr = new Address(Locale.CHINA);
		addr.setCountryCode("CN");
		addr.setCountryName("\u4e2d\u56fd");// 中国 \u4e2d\u56fd

		return addr;
	}

	public static String addTag(String src, String tag) {
		String result = src;

		StringBuilder buf = new StringBuilder();
		buf.append("<").append(tag).append(">");
		buf.append(src);
		buf.append("</").append(tag).append(">");
		result = buf.toString();

		return result;
	}

	public static String makeHtmlSpace(int number) {
		final String space = "&nbsp;";
		// String result = "";
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < number; i++) {
			// result += Space;
			result.append(space);
		}
		return result.toString();
	}

	static float[] ScalesBuf = new float[9];

	public static float getMatrixValue(Matrix mx, int index) {

		mx.getValues(ScalesBuf);
		return ScalesBuf[index];
	}

	public static String makeHtmlNewLine() {
		return "<br />";
	}

	public static Spanned stringToSpan(String src) {
		return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
	}

	public static String colorFont(String src, String color) {
		StringBuffer strBuf = new StringBuffer();

		strBuf.append("<font color=").append(color).append(">").append(src)
				.append("</font>");
		return strBuf.toString();
	}

	public static int dipToPixel(Context context, int dipValue) {
		float pixelFloat = android.util.TypedValue.applyDimension(
				android.util.TypedValue.COMPLEX_UNIT_DIP, dipValue, context
						.getResources().getDisplayMetrics());
		return (int) pixelFloat;
	}

	public static int spToPixel(Context context, int spValue) {
		float pixelFloat = android.util.TypedValue.applyDimension(
				android.util.TypedValue.COMPLEX_UNIT_SP, spValue, context
						.getResources().getDisplayMetrics());
		return (int) pixelFloat;
	}

	// 检测网络环境
	public static boolean isNetworkOK(Context context) {
		if (context == null) {
			return false;
		}
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conn == null) {
			return false;
		}
		NetworkInfo netInfo = conn.getActiveNetworkInfo();
		if (netInfo == null) {
			return false;
		}
		State stateNet = netInfo.getState();
		if (stateNet == null || stateNet == NetworkInfo.State.DISCONNECTED
				|| stateNet == NetworkInfo.State.DISCONNECTING) {
			return false;
		}
		return true;
	}

	public static final String LAST_KNOW_LOCATION = "last_know_location";
	public static final String LAST_KNOW_LAT = "last_know_lat"; // 上次的纬度
	public static final String LAST_KNOW_LNG = "last_know_lng"; // 上次的经度

	// 得到存储的 上次定位位置
	public static Location getPrefsLocation(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				LAST_KNOW_LOCATION, Context.MODE_PRIVATE);
		String str = "";
		Location location = new Location(str);
		location.setProvider("lbs");
		double lat = Double.parseDouble(prefs.getString(LAST_KNOW_LAT, "0.0"));
		double lng = Double.parseDouble(prefs.getString(LAST_KNOW_LNG, "0.0"));
		location.setLatitude(lat);
		location.setLongitude(lng);
		return location;
	}

	// 存储上次定位的信息
	public static void setPrefsLocation(Context context, Location lastLocation) {
		SharedPreferences prefs = context.getSharedPreferences(
				LAST_KNOW_LOCATION, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(LAST_KNOW_LAT,
				String.valueOf(lastLocation.getLatitude()));
		editor.putString(LAST_KNOW_LNG,
				String.valueOf(lastLocation.getLongitude()));
		editor.commit();
	}

	public static boolean isNull(Object obj) {
		if (obj == null)
			return true;
		return false;
	}

	public static String getVersion()
	{
		return SEARCHSDK_VERSION;
	}
}
