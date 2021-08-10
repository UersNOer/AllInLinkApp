package com.unistrong.api.services.core;

class ConfigableConstDecode
{
	public enum BuildType
	  {
	    PUBLIC, OTHER;
	  }

  public static float Resolution = 0.9F; // a
  public static String product = "searchSDK"; //b
  public static String packageName = ""; //c
  public static final String userAgent = "SEARCHSDK Android Map "
          + CoreUtil.getVersion().substring(0, CoreUtil.getVersion().length()); // d
  public static final BuildType NowBuildType =  BuildType.PUBLIC; //e
  public static volatile SDKInfo sdkInfo; //f
  
}
