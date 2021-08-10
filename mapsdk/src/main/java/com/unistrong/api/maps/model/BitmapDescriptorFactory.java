package com.unistrong.api.maps.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.FrameLayout;

import com.unistrong.api.mapcore.MapFragmentDelegateImp;
import com.unistrong.api.mapcore.IResourceProxyDecode;
import com.unistrong.api.mapcore.util.Util;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 根据传入的图片文件的资源ID，创建BitmapDescriptor 对象。
 */
public final class BitmapDescriptorFactory
{
    /**
     * 红色。
     */
  public static final int MARKER_COLOR_RED = 0;
    /**
     * 橙色。
     */
  public static final int MARKER_COLOR_ORANGE = 1;
    /**
     * 黄色。
     */
  public static final int MARKER_COLOR_YELLOW = 2;
    /**
     * 绿色。
     */
  public static final int MARKER_COLOR_GREEN = 3;
    /**
     * 青色。
     */
  public static final int MARKER_COLOR_CYAN = 4;
    /**
     * 天蓝色。
     */
  public static final int MARKER_COLOR_AZURE = 5;
    /**
     * 蓝色。
     */
  public static final int MARKER_COLOR_BLUE = 6;
    /**
     * 红色。
     */
  public static final int MARKER_COLOR_VIOLET = 7;
    /**
     * 紫色。
     */
  public static final int MARKER_COLOR_MAGENTA = 8;
    /**
     * 玫瑰红。
     */
  public static final int MARKER_COLOR_ROSE = 9;

    /**
     * 根据传入的图片文件的资源ID，创建BitmapDescriptor 对象。
     * @param resourceId - 图片文件的资源ID。
     * @return 根据传入的图片文件的资源ID，创建的BitmapDescriptor 对象，如文件不存在，则返回null。
     */
  public static BitmapDescriptor fromResource(int resourceId)
  {
    try
    {
      Context context = MapFragmentDelegateImp.context;
      if (context != null)
      {
        InputStream localInputStream = context.getResources().openRawResource(resourceId);
        
        Bitmap bitmap = BitmapFactory.decodeStream(localInputStream);
        BitmapDescriptor bitmapDescriptor = fromBitmap(bitmap);
        bitmap.recycle();
        return bitmapDescriptor;
      }
      return null;
    }
    catch (Throwable localThrowable) {}
    return null;
  }

    /**
     * 根据传入的view，创建BitmapDescriptor对象。
     * @param paramView  - 要显示的view。
     * @return 根据传入的view，创建的BitmapDescriptor对象，如view不存在，则返回null。
     */
  public static BitmapDescriptor fromView(View paramView)
  {
    try
    {
      Context localContext = MapFragmentDelegateImp.context;
      if (localContext != null)
      {
        FrameLayout localFrameLayout = new FrameLayout(localContext);
        localFrameLayout.addView(paramView);
        localFrameLayout.setDrawingCacheEnabled(true);
        Bitmap localBitmap = Util.getBitmapFromView(localFrameLayout);
        BitmapDescriptor localBitmapDescriptor = fromBitmap(localBitmap);
        localBitmap.recycle();
        return localBitmapDescriptor;
      }
      return null;
    }
    catch (Throwable localThrowable) {}
    return null;
  }

    /**
     *根据传入的图片文件名的绝对地址，创建的BitmapDescriptor 对象。
     * @param absolutePath  - 图片的绝对地址。
     * @return 根据传入的图片文件名的绝对地址，创建的BitmapDescriptor 对象，如文件不存在，则返回null。
     */
  public static BitmapDescriptor fromPath(String absolutePath)
  {
    try
    {
      Bitmap localBitmap = BitmapFactory.decodeFile(absolutePath);
      BitmapDescriptor localBitmapDescriptor = fromBitmap(localBitmap);
      localBitmap.recycle();
      return localBitmapDescriptor;
    }
    catch (Throwable localThrowable) {}
    return null;
  }

    /**
     *根据传入的asset 目录内图片文件名称创建图片的BitmapDescriptor 对象。
     * @param assetName - asset 文件内的一个图片文件的文件名称。
     * @return 根据传入的asset 目录内图片文件名称创建图片的BitmapDescriptor 对象，如果图片不存在则返回null。
     */
  public static BitmapDescriptor fromAsset(String assetName)
  {
    InputStream inputStream = null;
    try
    {
      inputStream = BitmapDescriptorFactory.class.getResourceAsStream("/assets/" + assetName);
      Bitmap localBitmap = BitmapFactory.decodeStream(inputStream);
      inputStream.close();
      BitmapDescriptor localBitmapDescriptor = fromBitmap(localBitmap);
      localBitmap.recycle();
      return localBitmapDescriptor;
    }
    catch (Throwable localThrowable) {}finally {
      try{
        if(inputStream!=null){
          inputStream.close();
        }
      }catch (Exception ex){

      }
    }
    return null;
  }

    /**
     * 根据传入的在手机存储器里的图片文件名，创建的BitmapDescriptor 对象。文件名应该包括路径。
     * @param fileName - 手机存储器里的图片文件名，应该包括路径。
     * @return 根据传入的在手机存储器里的图片文件名，创建的BitmapDescriptor 对象，如文件不存在，则返回null。
     */
  public static BitmapDescriptor fromFile(String fileName)
  {
    try
    {
      Context localContext = MapFragmentDelegateImp.context;
      if (localContext != null)
      {
        FileInputStream localFileInputStream = localContext.openFileInput(fileName);
        Bitmap localBitmap = BitmapFactory.decodeStream(localFileInputStream);
        localFileInputStream.close();
        BitmapDescriptor localBitmapDescriptor = fromBitmap(localBitmap);
        localBitmap.recycle();
        return localBitmapDescriptor;
      }
      return null;
    }
    catch (Throwable localThrowable) {}
    return null;
  }

    /**
     * 创建默认的marker 图标BitmapDescriptor 对象。
     * @return 默认的marker 图标BitmapDescriptor 对象。
     */
  public static BitmapDescriptor defaultMarker()
  {
    try
    {
      return fromAsset(IResourceProxyDecode.bitmap.marker_color_default.name() + ".png");
    }
    catch (Throwable localThrowable) {}
    return null;
  }

    /**
     *API 提供了10 个颜色的Marker 图标，用户可以通过此方法传入值来调用。请参见本类的常量。
     * @param hue  - 请参见本类的常量。
     * @return API 提供的彩色marker 图标BitmapDescriptor 对象。
     */
  public static BitmapDescriptor defaultMarker(int hue)
  {
    try
    {
        if(hue<0||hue>10)hue=0;
      String str = "";
      if (hue == 0) {
        str = "marker_color_red";
      } else if (hue == 1) {
        str = "marker_color_orange";
      } else if (hue == 2) {
        str = "marker_color_yellow";
      } else if (hue == 3) {
        str = "marker_color_green";
      } else if (hue == 4) {
        str = "marker_color_cyan";
      } else if (hue == 5) {
        str = "marker_color_azuer";
      } else if (hue == 6) {
        str = "marker_color_blue";
      } else if (hue == 7) {
        str = "marker_color_violet";
      } else if (hue == 8) {
        str = "marker_color_magenta";
      } else if (hue == 9) {
        str = "marker_color_rose";
      }
      return fromAsset(str + ".png");
    }
    catch (Throwable localThrowable) {}
    return null;
  }

    /**
     *根据传入的Bitmap 对象创建BitmapDescriptor 对象。 传入的bitmap对象不会被自动回收，需要开发者自行回收处理。
     * @param image  - 图片的Bitmap 对象。
     * @return 根据传入的Bitmap 对象创建的BitmapDescriptor 对象，如果传入null，则返回的null。
     */
  public static BitmapDescriptor fromBitmap(Bitmap image)
  {
    if (image == null) {
      return null;
    }
    BitmapDescriptor localBitmapDescriptor = null;
    try
    {
      return new BitmapDescriptor(image);
    }
    catch (Throwable localThrowable) {}
    return localBitmapDescriptor;
  }
}
