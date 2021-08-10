package com.unistrong.api.mapcore.util;

import java.util.HashMap;
import java.util.Map;

class AuthRequest//bx
  extends Request
{
  private Map<String, String> sdkInfo = new HashMap();
  private String url;
  private Map<String, String> devInfo = new HashMap();
  
  void setSDKInfo(Map<String, String> paramMap)
  {
    this.sdkInfo.clear();
    this.sdkInfo.putAll(paramMap);
  }
  
  void setUrl(String url)
  {
    this.url = url;
  }
  
  void setDevInfo(Map<String, String> paramMap)
  {
    this.devInfo.clear();
    this.devInfo.putAll(paramMap);
  }
  
  public String getUrl()
  {
    return this.url;
  }
  
  public Map<String, String> getHeadMaps()
  {
    return this.sdkInfo;
  }
  
  public Map<String, String> getRequestParam()
  {
    return this.devInfo;
  }
}
