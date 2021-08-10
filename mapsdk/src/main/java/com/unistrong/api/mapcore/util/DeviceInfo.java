package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.leador.mapcore.FileUtil;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

//import android.provider.Settings.System;
//import android.view.Display;

public class DeviceInfo //bq
{
  private static String utdid = "";
  private static boolean b = false;
  
  static String b(Context context)
  {
    String str = "";
    try
    {
      return getSubscriberId(context);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return str;
  }
  
  static int getNetworkType(Context context)
  {
    int i = -1;
    try
    {
      return getNetworkType1(context);
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
    return i;
  }
  
  static int getNetType(Context context)
  {
    int i = -1;
    try
    {
      return getNetType1(context);
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
    return i;
  }
  
  static String e(Context context)
  {
    try
    {
      return t(context);
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
    return "";
  }
  
  public static void a()
  {
    try
    {
      if (Build.VERSION.SDK_INT > 14)
      {
        Method localMethod = TrafficStats.class.getDeclaredMethod("setThreadStatsTag", new Class[] { Integer.TYPE });
        
        localMethod.invoke(null, new Object[] { Integer.valueOf(40964) });
      }
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      BasicLogHandler.a(localNoSuchMethodException, "DeviceInfo", "setTraficTag");
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      BasicLogHandler.a(localIllegalAccessException, "DeviceInfo", "setTraficTag");
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      BasicLogHandler.a(localIllegalArgumentException, "DeviceInfo", "setTraficTag");
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      BasicLogHandler.a(localInvocationTargetException, "DeviceInfo", "setTraficTag");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "DeviceInfo", "setTraficTag");
    }
  }
  
  static class a
    extends DefaultHandler
  {
    public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
      throws SAXException
    {
      if ((paramString2.equals("string")) && ("UTDID".equals(paramAttributes.getValue("name")))) {
        //bq.a(true);
    	 b = true; //上一句翻译
      }
    }
    
    public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
      throws SAXException
    {
      //if (bq.b()) {
      //  bq.a(new String(paramArrayOfChar, paramInt1, paramInt2));
      //}
    	if(b){ //上两句翻译
    		utdid = new String(paramArrayOfChar, paramInt1, paramInt2);
    	}
    }
    
    public void endElement(String paramString1, String paramString2, String paramString3)
      throws SAXException
    {
     // bq.a(false);
    	b = false; //上一句翻译
    }
  }
  
  public static String getUTDID(Context paramContext)
  {
    try
    {
      if ((utdid != null) && (!"".equals(utdid))) {
        return utdid;
      }
      if (paramContext.checkCallingOrSelfPermission("android.permission.WRITE_SETTINGS") == 0) {
        utdid = Settings.System.getString(paramContext.getContentResolver(), "mqBRboGZkQPcAkyk");
      }
      if ((utdid != null) && (!"".equals(utdid))) {
        return utdid;
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable1.printStackTrace();
    }
    try
    {
      if ("mounted".equals(Environment.getExternalStorageState()))
      {
        String str1 = Environment.getExternalStorageDirectory().getAbsolutePath();
        
        String str2 = str1 + "/.UTSystemConfig/Global/Alvin2.xml";
        File localFile = new File(str2);
        if (localFile.exists())
        {
          SAXParserFactory localSAXParserFactory = SAXParserFactory.newInstance();
          SAXParser localSAXParser = localSAXParserFactory.newSAXParser();
          a locala = new a();
          localSAXParser.parse(localFile, locala);
        }
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      localFileNotFoundException.printStackTrace();
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      localParserConfigurationException.printStackTrace();
    }
    catch (SAXException localSAXException)
    {
      localSAXException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    catch (Throwable localThrowable2)
    {
      localThrowable2.printStackTrace();
    }
    return utdid;
  }
  
  static String g(Context context)
  {
    String str = null;
    try
    {
      if ((context == null) || (context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") != 0)) {
        return str;
      }
      WifiManager localWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
      if (localWifiManager.isWifiEnabled())
      {
        WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
        str = localWifiInfo.getBSSID();
      }
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "DeviceInfo", "getWifiMacs");
    }
    return str;
  }
  
  static String getWifiMacs(Context context)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      if ((context == null) || (context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") != 0)) {
        return localStringBuilder.toString();
      }
      WifiManager localWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
      if (localWifiManager.isWifiEnabled())
      {
        List<ScanResult> localList = localWifiManager.getScanResults();
        if ((localList == null) || (localList.size() == 0)) {
          return localStringBuilder.toString();
        }
        localList = a(localList);
        int i = 1;
        for (int j = 0; (j < localList.size()) && (j < 7); j++)
        {
          ScanResult localScanResult = (ScanResult)localList.get(j);
          if (i != 0) {
            i = 0;
          } else {
            localStringBuilder.append(";");
          }
          localStringBuilder.append(localScanResult.BSSID);
        }
      }
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "DeviceInfo", "getWifiMacs");
    }
    return localStringBuilder.toString();
  }
  
  private static String c = "";
  
  static String getDeviceMac(Context paramContext)
  {
    try
    {
      if ((c != null) && (!"".equals(c))) {
        return c;
      }
      if (paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") != 0) {
        return c;
      }
      WifiManager localWifiManager = (WifiManager)paramContext.getSystemService(Context.WIFI_SERVICE);
      
      c = localWifiManager.getConnectionInfo().getMacAddress();
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "DeviceInfo", "getDeviceMac");
    }
    return c;
  }
  
  static String[] cellInfo(Context paramContext)
  {
    try
    {
      if ((paramContext.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") != 0) || (paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0)) {
        return new String[] { "", "" };
      }
      TelephonyManager localTelephonyManager = (TelephonyManager)paramContext.getSystemService(Context.TELEPHONY_SERVICE);
      
      CellLocation localCellLocation = localTelephonyManager.getCellLocation();
      Object localObject;
      int i;
      int j;
      if ((localCellLocation instanceof GsmCellLocation))
      {
        localObject = (GsmCellLocation)localCellLocation;
        i = ((GsmCellLocation)localObject).getCid();
        j = ((GsmCellLocation)localObject).getLac();
        return new String[] { j + "||" + i, "gsm" };
      }
      if ((localCellLocation instanceof CdmaCellLocation))
      {
        localObject = (CdmaCellLocation)localCellLocation;
        i = ((CdmaCellLocation)localObject).getSystemId();
        j = ((CdmaCellLocation)localObject).getNetworkId();
        int k = ((CdmaCellLocation)localObject).getBaseStationId();
        if ((i >= 0) && (j >= 0) && (k < 0)) {}
        return new String[] { i + "||" + j + "||" + k, "cdma" };
      }
    }
    catch (Throwable throwable)
    {
      BasicLogHandler.a(throwable, "DeviceInfo", "cellInfo");
    }
    return new String[] { "", "" };
  }
  
  static String getMNC(Context context)
  {
    String str1 = "";
    try
    {
      if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
        return str1;
      }
      TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
      
      String str2 = telephonyManager.getNetworkOperator();
      if ((TextUtils.isEmpty(str2)) && (str2.length() < 3)) {
        return str1;
      }
      if(str2.length()>3){
        str1 = str2.substring(3);
      }else{
        return str2;
      }
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
      BasicLogHandler.a(throwable, "DeviceInfo", "getMNC");
    }
    return str1;
  }
  
  public static int getNetWorkType(Context context)
  {
    int i = -1;
    try
    {
      return getNetworkType1(context);
    }
    catch (Throwable throwable)
    {
      BasicLogHandler.a(throwable, "DeviceInfo", "getNetWorkType");
    }
    return i;
  }
  
  public static int getActiveNetWorkType(Context context)
  {
    int i = -1;
    try
    {
      return getNetType1(context);
    }
    catch (Throwable throwable)
    {
      BasicLogHandler.a(throwable, "DeviceInfo", "getActiveNetWorkType");
    }
    return i;
  }
  
  public static NetworkInfo n(Context context)
  {
    NetworkInfo networkInfo = null;
    if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") != 0) {
      return networkInfo;
    }
    ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager == null) {
      return networkInfo;
    }
    networkInfo = connectivityManager.getActiveNetworkInfo();
    return networkInfo;
  }
  
  static String getNetworkExtraInfo(Context context)
  {
    String str = null;
    try
    {
      NetworkInfo localNetworkInfo = n(context);
      if (localNetworkInfo == null) {
        return str;
      }
      str = localNetworkInfo.getExtraInfo();
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "DeviceInfo", "getNetworkExtraInfo");
    }
    return str;
  }
  
  private static String d = "";
  
  static String getReslution(Context context)
  {
    try
    {
      if ((d != null) && (!"".equals(d))) {
        return d;
      }
      DisplayMetrics localDisplayMetrics = new DisplayMetrics();
      WindowManager localWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
      
      localWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
      int i = localDisplayMetrics.widthPixels;
      int j = localDisplayMetrics.heightPixels;
      d = j + "*" + i;
    }
    catch (Throwable throwable)
    {
      BasicLogHandler.a(throwable, "DeviceInfo", "getReslution");
    }
    return d;
  }
  
  private static String e = "";
  
  public static String getDeviceID(Context context)
  {
    try
    {
      if ((e != null) && (!"".equals(e))) {
        return e;
      }
      if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
        return e;
      }
      TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
      
      e = telephonyManager.getDeviceId();
      if (e == null) {
        e = "";
      }
    }
    catch (Throwable throwable)
    {
      BasicLogHandler.a(throwable, "DeviceInfo", "getDeviceID");
    }
    return e;
  }
  
  public static String getSubscriberId1(Context context)
  {
    String str = "";
    try
    {
      return t(context);
    }
    catch (Throwable throwable)
    {
      BasicLogHandler.a(throwable, "DeviceInfo", "getSubscriberId");
    }
    return str;
  }
  
  static String getNetworkOperatorName(Context context)
  {
    try
    {
      return getNetworkOperatorName1(context);
    }
    catch (Throwable throwable)
    {
      BasicLogHandler.a(throwable, "DeviceInfo", "getNetworkOperatorName");
    }
    return "";
  }
  
  private static String f = "";
  
  private static String t(Context context)
  {
    if ((f != null) && (!"".equals(f))) {
      return f;
    }
    if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
      return f;
    }
    TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    
    f = telephonyManager.getSubscriberId();
    if (f == null) {
      f = "";
    }
    return f;
  }
  
  private static String getNetworkOperatorName1(Context context)
  {
    String str = null;
    if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
      return str;
    }
    TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    
    str = telephonyManager.getSimOperatorName();
    if (TextUtils.isEmpty(str)) {
      str = telephonyManager.getNetworkOperatorName();
    }
    return str;
  }
  
  private static int getNetType1(Context context)
  {
    int i = -1;
    if ((context == null) || (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") != 0)) {
      return i;
    }
    ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager == null) {
      return i;
    }
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    if (networkInfo == null) {
      return i;
    }
    i = networkInfo.getType();
    
    return i;
  }
  
  private static String getSubscriberId(Context context)
  {
    String str1 = "";
    String str2 = getSubscriberId1(context);
    if ((str2 == null) || (str2.length() < 5)) {
      return str1;
    }
    str1 = str2.substring(3, 5);
    
    return str1;
  }
  
  private static int getNetworkType1(Context context)
  {
    int i = -1;
    if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
      return i;
    }
    TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    
    i = telephonyManager.getNetworkType();
    
    return i;
  }
  
  private static List<ScanResult> a(List<ScanResult> list)
  {
    int i = list.size();
    for (int j = 0; j < i - 1; j++) {
      for (int k = 1; k < i - j; k++) {
        if (((ScanResult)list.get(k - 1)).level > ((ScanResult)list.get(k)).level)
        {
          ScanResult scanResult = (ScanResult)list.get(k - 1);
          list.set(k - 1, list.get(k));
          list.set(k, scanResult);
        }
      }
    }
    return list;
  }

  public static String getAndroidID(Context context){
    String androidID=Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
    return androidID;
  }

  // 获取CPU最大频率（单位KHZ）
  // "/system/bin/cat" 命令行
  // "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
  public static String getMaxCpuFreq() {
    String result = "0";
    ProcessBuilder cmd;
    try {
      String[] args = { "/system/bin/cat",
              "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
      cmd = new ProcessBuilder(args);
      Process process = cmd.start();
      InputStream in = process.getInputStream();
      byte[] re = new byte[24];
      while (in.read(re) != -1) {
        result = result + new String(re);
      }
      in.close();
    } catch (IOException ex) {
      ex.printStackTrace();
      result = "N/A";
    }
    return result.trim();
  }

  //getRAM
  public static String getTotalMemory() {
    String str1 = "/proc/meminfo";
    String memory="";
    try {
      FileReader fr = new FileReader(str1);
      BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
      String  str2 = localBufferedReader.readLine();
      String[] arrayOfString = str2.split(":");
//       initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
      memory=arrayOfString[1].substring(0,arrayOfString[1].length()-2).trim();
      int size=Integer.valueOf(memory)*1024;
      memory=size+"";
      localBufferedReader.close();
    } catch (IOException e) {
    }
    return  memory;
  }

  public static long[] getRomMemroy() {
    long[] romInfo = new long[2];
    //Total rom memory
    romInfo[0] = getTotalInternalMemorySize();

    //Available rom memory
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = stat.getBlockSize();
    long availableBlocks = stat.getAvailableBlocks();
    romInfo[1] = blockSize * availableBlocks;
    return romInfo;
  }

  //getROM
  public static long getTotalInternalMemorySize() {
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = stat.getBlockSize();
    long totalBlocks = stat.getBlockCount();
    return totalBlocks * blockSize;
  }

  public static String getCpuAndRom(){
    return "cpuFreq:"+getMaxCpuFreq()+"_Ram:"+getTotalMemory()+"_Rom:"+getTotalInternalMemorySize();
  }

  //getUUID
  public static String getMyUUID(Context context){
    String uuidPath= FileUtil.getExternalStroragePath(context)+"/leador/data/uuid.data";
    String uniqueId=getSPUUID(context);
    if(!TextUtils.isEmpty(uniqueId))return uniqueId;

    uniqueId=getFileUUID(context);
    if(!TextUtils.isEmpty(uniqueId))return uniqueId;

    UUID uuid = UUID.randomUUID();
    uniqueId = uuid.toString();
    saveUUID(context,uniqueId);
    FileUtil.writeTextFile(new File(uuidPath),uniqueId);
    return uniqueId;
  }

  private static String getSPUUID(Context context){
    if(context!=null)
      return context.getSharedPreferences("UUID_config",context.MODE_PRIVATE).getString("uuid","");
    return "";
  }

  private static void saveUUID(Context context,String uuidStr){
    if(context!=null)
      context.getSharedPreferences("UUID_config",context.MODE_PRIVATE).edit().putString("uuid",uuidStr).commit();
  }

  public static String getFileUUID(Context context) {
    String uuidPath=FileUtil.getExternalStroragePath(context)+"/leador/data/uuid.data";
    if(FileUtil.isFileExists(uuidPath))
      try {
        return FileUtil.getFileContents(uuidPath);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    return null;
  }
}
