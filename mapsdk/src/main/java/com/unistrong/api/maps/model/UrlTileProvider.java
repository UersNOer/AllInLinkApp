package com.unistrong.api.maps.model;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 部分实现TileProvider类，只需要一个URL指向的图像。 注意：使用这个类，要求所有的图像都具有相同的尺寸。
 */
public abstract class UrlTileProvider
  implements TileProvider
{
  private final int width;
  private final int height;

    /**
     * 构造一个UrlTileProvider对象。
     * @param width -用于Tile的图片宽度。
     * @param height - 用于Tile的图片高度。
     */
  public UrlTileProvider(int width, int height)
  {
    this.width = width;
    this.height = height;
  }

    /**
     * 返回指定tile坐标对应图片的URL。
     * @param x - tile的横坐标，范围为[0, 2的zoom次方 - 1]。
     * @param y - tile的纵坐标，范围为[0, 2的zoom次方 - 1]。
     * @param zoom - tile的缩放级别，范围通过类MapController的getMinZoomLevel()和getMaxZoomLevel()获得。
     * @return 指定tile坐标对应图片的URL。
     */
  public abstract URL getTileUrl(int x, int y, int zoom);

    /**
     * 返回指定tile坐标的tile对象。
     * @param x - tile的横坐标，范围为[0, 2的zoom次方 - 1]。
     * @param y - tile的纵坐标，范围为[0, 2的zoom次方 - 1]。
     * @param zoom  - tile的缩放级别，范围通过类MapController的getMinZoomLevel()和getMaxZoomLevel()获得。
     * @return 指定tile坐标的tile对象。
     */
  public final Tile getTile(int x, int y, int zoom)
  {
    URL localURL = getTileUrl(x, y, zoom);
    if (localURL == null) {
      return NO_TILE;
    }
    Tile tile;

    try
    {
      tile = new Tile(this.width, this.height, a(localURL.openStream()));
    }
    catch (IOException localIOException)
    {
      tile = NO_TILE;
    }
    return tile;
  }

  private static byte[] a(InputStream inputStream)
    throws IOException
  {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    a(inputStream, outputStream);
    return outputStream.toByteArray();
  }

  private static long a(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
//    byte[] arrayOfByte = new byte['က'];
//    int i;
//    for (long l = 0L;; l += i)
//    {
//      i = paramInputStream.read(arrayOfByte);
//      if (i == -1) {
//        break;
//      }
//      paramOutputStream.write(arrayOfByte, 0, i);
//    }
//    return l;
	  byte[] arrayOfByte = new byte[4096];

	    long l = 0L;
	    while (true) { int i = paramInputStream.read(arrayOfByte);
	      if (i == -1)
	        break;
	      paramOutputStream.write(arrayOfByte, 0, i);

	      l += i;
	    }

	    return l;
  }

    /**
     * 返回指定tile的图片宽度。
     * @return 指定tile的图片宽度，单位像素pixel。
     */
  public int getTileWidth()
  {
    return this.width;
  }
    /**
     * 返回指定tile的图片高度。
     * @return 指定tile的图片高度，单位像素pixel。
     */
  public int getTileHeight()
  {
    return this.height;
  }
}
