package com.unistrong.api.mapcore;

import android.content.Context;
import android.view.View;
import com.unistrong.api.mapcore.util.LogManager;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.maps.model.TileOverlayOptions;
import com.unistrong.api.maps.model.UrlTileProvider;

import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.microedition.khronos.opengles.GL10;

public class TileOverlayViewDecode
  extends View
{
  private IMapDelegate mMap; //e
  CopyOnWriteArrayList<ITileOverlayDelegate> mTileOverlays = new CopyOnWriteArrayList<ITileOverlayDelegate>(); // a
  ListOverlayComparator b = new ListOverlayComparator();
  CopyOnWriteArrayList<Integer> recycleTextureIds = new CopyOnWriteArrayList<Integer>(); //c
  TileOverlayDelegateImp d = null;
  
  static class ListOverlayComparator // a
    implements Comparator<Object>, Serializable
  {
    public int compare(Object paramObject1, Object paramObject2)
    {
      ITileOverlayDelegate localao1 = (ITileOverlayDelegate)paramObject1;
      ITileOverlayDelegate localao2 = (ITileOverlayDelegate)paramObject2;
      try
      {
        if ((localao1 != null) && (localao2 != null))
        {
          if (localao1.getZIndex() > localao2.getZIndex()) {
            return 1;
          }
          if (localao1.getZIndex() < localao2.getZIndex()) {
            return -1;
          }
        }
      }
      catch (Throwable localThrowable)
      {
        SDKLogHandler.exception(localThrowable, "TileOverlayView", "compare");
        localThrowable.printStackTrace();
      }
      return 0;
    }
  }
  
  public TileOverlayViewDecode(Context paramContext)
  {
    super(paramContext);
  }
  
  public TileOverlayViewDecode(Context paramContext, IMapDelegate paramaa)
  {
    super(paramContext);
    this.mMap = paramaa;
    UrlTileProvider local1 = new UrlTileProvider(256, 256)
    {
      public URL getTileUrl(int x, int y, int zoom)
      {
        try
        {return null;
        }
        catch (Throwable localThrowable) {}
        return null;
      }
    };
    TileOverlayOptions localTileOverlayOptions = new TileOverlayOptions().tileProvider(local1);
    this.d = new TileOverlayDelegateImp(localTileOverlayOptions, this, true);
  }
  
  IMapDelegate getMap() // a
  {
    return this.mMap;
  }
  
  public void onDrawGL(GL10 gl10) // a
  {
    try
    {
      for (Iterator<Integer> localIterator = this.recycleTextureIds.iterator(); localIterator.hasNext();)
      {
        Integer localObject = (Integer)localIterator.next();
        Util.a(gl10, localObject.intValue());
      }
      this.recycleTextureIds.clear();
      this.d.drawTiles(gl10);
      for (Iterator<ITileOverlayDelegate> localIterator = this.mTileOverlays.iterator(); localIterator.hasNext();)
      {
        ITileOverlayDelegate localObject = (ITileOverlayDelegate)localIterator.next();
        if (localObject.isVisible()) {
          localObject.drawTiles(gl10);
        }
      }
    }
    catch (Throwable localThrowable)
    {
      LogManager.writeLog(LogManager.productInfo, hashCode() + " TileOverLayView draw exception :" + localThrowable.getMessage(), 115);
    }
  }
  
  public void clear() //b
  {
    for (ITileOverlayDelegate localao : this.mTileOverlays) {
      if (localao != null) {
        localao.a();
      }
    }
    this.mTileOverlays.clear();
  }
  
  void changeOverlayIndex() //c
  {
    Object[] arrayOfObject1 = this.mTileOverlays.toArray();
    Arrays.sort(arrayOfObject1, this.b);
    this.mTileOverlays.clear();
    for (Object localObject : arrayOfObject1) {
      this.mTileOverlays.add((ITileOverlayDelegate)localObject);
    }
  }
  
  public void addTileOverlay(ITileOverlayDelegate paramao) // a
  {
    b(paramao);
    this.mTileOverlays.add(paramao);
    changeOverlayIndex();
  }
  
  public boolean b(ITileOverlayDelegate paramao) //b
  {
    return this.mTileOverlays.remove(paramao);
  }
  
  public void refresh(boolean paramBoolean) // a
  {
    this.d.refresh(paramBoolean);
    for (ITileOverlayDelegate localao : this.mTileOverlays) {
      if ((localao != null) && (localao.isVisible())) {
        localao.refresh(paramBoolean);
      }
    }
  }
  
  public void d() // d->onPause
  {
    this.d.onPause();
    for (ITileOverlayDelegate localao : this.mTileOverlays) {
      if (localao != null) {
        localao.onPause();
      }
    }
  }
  
  public void e() //e->onResume
  {
    this.d.onResume();
    for (ITileOverlayDelegate localao : this.mTileOverlays) {
      if (localao != null) {
        localao.onResume();
      }
    }
  }
  
  public void b(boolean paramBoolean) //b
  {
    this.d.onFling(paramBoolean);
    for (ITileOverlayDelegate localao : this.mTileOverlays) {
      if (localao != null) {
        localao.onFling(paramBoolean);
      }
    }
  }
  
  public void f() //f
  {
    this.d.a();
    this.d = null;
  }
}
