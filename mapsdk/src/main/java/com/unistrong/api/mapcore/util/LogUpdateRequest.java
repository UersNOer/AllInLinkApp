package com.unistrong.api.mapcore.util;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class LogUpdateRequest//ck
  extends Request
{
  private byte[] postData;
  private String scode = "";
  private String ts = "";
  private String ak="";
  public LogUpdateRequest(Context context, byte[] postData)
  {
    this.postData = ((byte[])postData.clone());
    ts = ClientInfo.getTS();
    scode = ClientInfo.Scode(context,ts,"");
    ak = AppInfo.getKey(context);
  }
  
  public Map<String, String> getHeadMaps()
  {
    HashMap<String,String> localHashMap = new HashMap<String,String>();
    
    localHashMap.put("Content-Type", "text\\html");
    localHashMap.put("Content-Length", String.valueOf(this.postData.length));
    
    return localHashMap;
  }
  
  public Map<String, String> getRequestParam()
  {
    return null;
  }
  
  public String getUrl()
  {
    return String.format(WrapperConstConfig.b, new Object[] { "1", "1", "0",ts,scode,ak });
  }
  
  public byte[] a_()
  {
    return this.postData;
  }
}
