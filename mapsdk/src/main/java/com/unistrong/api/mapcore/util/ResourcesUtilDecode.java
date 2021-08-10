package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.lang.reflect.Method;

public class ResourcesUtilDecode //bi
{
  private static boolean resExist = false;
  
  static
  {
    File localFile = new File("/system/framework/leador.jar");
    resExist = localFile.exists();
  }
  
  public static AssetManager getSelfAssets(Context paramContext)
  {
    if (paramContext == null) {
      return null;
    }
    AssetManager localAssetManager = paramContext.getAssets();
    if (resExist) {
      try
      {
        Method localMethod = localAssetManager.getClass().getDeclaredMethod("addAssetPath", new Class[] { String.class });
        
        localMethod.invoke(localAssetManager, new Object[] { "/system/framework/leador.jar" });
      }
      catch (Throwable localThrowable)
      {
        SDKLogHandler.exception(localThrowable, "ResourcesUtil", "getSelfAssets");
      }
    }
    return localAssetManager;
  }
}
