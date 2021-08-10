package com.unistrong.api.mapcore;

import com.unistrong.api.mapcore.util.SDKInfo;
import com.unistrong.api.maps.MapsInitializer;

public class ConfigableConstDecode
{
	public enum BuildType
	  {
	    PUBLIC, OTHER;
	  }

  public static float Resolution = 0.9F; // a
  public static String product = "3dmap"; //b
  public static String packageName = ""; //c
  public static final String userAgent = "MAPSDK Android Map " + MapsInitializer.getVersion().substring(0, MapsInitializer.getVersion().length()); // d
  public static final BuildType NowBuildType =  BuildType.PUBLIC; //e
  public static volatile SDKInfo sdkInfo; //f
  
}
