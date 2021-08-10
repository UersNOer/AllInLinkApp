package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.unistrong.api.mapcore.TileOverlayDelegateImp;
import com.unistrong.api.maps.model.Tile;
import com.unistrong.api.maps.model.TileProvider;

public class ImageFetcherDecode
  extends ImageResizerDecode
{
  private TileProvider tileProvider = null;
  
  public ImageFetcherDecode(Context context, int imageWidth, int imageHeight)
  {
    super(context, imageWidth, imageHeight);
    init(context);
  }
  
  private void init(Context context) // a
  {
    checkConnection(context);
  }
  
  public void setTileProvider(TileProvider provider) // a
  {
    this.tileProvider = provider;
  }
  
  private void checkConnection(Context context) //b
  {
    ConnectivityManager localConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
    if ((localNetworkInfo == null) || (!localNetworkInfo.isConnectedOrConnecting())) {
      LogManager.writeLog("ImageFetcher", "checkConnection - no connection found", 112);
    }
  }
  
  private Bitmap processBitmap(TileOverlayDelegateImp.TileCoordinate tile) //c
  {
    LogManager.writeLog("ImageFetcher", "processBitmap - " + tile, 111);
    
    Bitmap localBitmap = null;
    try
    {
      Tile localTile = this.tileProvider.getTile(tile.X, tile.Y, tile.Zoom);
      if ((localTile != null) && (localTile != TileProvider.NO_TILE)) {
        localBitmap = BitmapFactory.decodeByteArray(localTile.data, 0, localTile.data.length);
      }
      localTile = null;
    }
    catch (Throwable localThrowable) {}
    return localBitmap;
  }
  
  protected Bitmap processBitmap(Object paramObject) 
  {
    return processBitmap((TileOverlayDelegateImp.TileCoordinate)paramObject);
  }
}
