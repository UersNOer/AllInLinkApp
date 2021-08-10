package com.unistrong.api.mapcore;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.Marker;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.microedition.khronos.opengles.GL10;

class MapOverlayImageViewDecode
        implements IMapOverlayImageView
{
  IMapDelegate map;
  private CopyOnWriteArrayList<IMarkerDelegate> markers = new CopyOnWriteArrayList<IMarkerDelegate>(new ArrayList(500));
  private CopyOnWriteArrayList<OverlayTextureItem> overlayitem = new CopyOnWriteArrayList<OverlayTextureItem>();
  private CopyOnWriteArrayList<Integer> recycleTextureIds = new CopyOnWriteArrayList<Integer>();
  Comparetor comparetor = new Comparetor();
  private IPoint mHitMarkPoint;
  private IMarkerDelegate mHitMarker;
  private Handler handler;
  private Runnable runnable = new Runnable(){ //原始逆向出来的类是aw,此处将其省略
	  public synchronized void run()
	    {
		  //av.a(this.a);
	      changeOverlayIndex(); //上一句翻译
	    }
  };

  public MapOverlayImageViewDecode(Context context)
  {

  }

  public MapOverlayImageViewDecode(Context context, AttributeSet attrs, IMapDelegate map)
  {

    this.map = map;
    this.handler = ((MapDelegateImp)this.map).getAu();
  }

  public synchronized int getMarkersSize()
  {
    return this.markers.size();
  }

  // 清理当前所有的marker,如果id不为空则保留此id的marker
  public synchronized void clear(String id)
  {
    try
    {
      int m = (id == null) || (id.trim().length() == 0) ? 1 : 0;
      this.mHitMarker = null;
      this.mHitMarkPoint = null;
      if (m != 0)
      {
        for (Iterator<IMarkerDelegate> localIterator = this.markers.iterator(); localIterator.hasNext();)
        {
          IMarkerDelegate localag = (IMarkerDelegate)localIterator.next();
          localag.remove();
        }
        this.markers.clear();
      }
      else
      {
        for (Iterator<IMarkerDelegate> localIterator = this.markers.iterator(); localIterator.hasNext();)
        {
          IMarkerDelegate localag = (IMarkerDelegate)localIterator.next();
          if (!id.equals(localag.getId())) {
            localag.remove();
          }
        }
      }
    }
    catch (RemoteException localRemoteException)
    {
      //Iterator localIterator;
      //ag localag;
      SDKLogHandler.exception(localRemoteException, "MapOverlayImageView", "clear");
      localRemoteException.printStackTrace();
    }
  }

  public synchronized void addMarker(IMarkerDelegate paramag)
  {
    this.markers.add(paramag);
    changeIndexs();
  }

  @Override
  public IMapDelegate getMapDelegate() {
    return this.map;
  }

  boolean isRemove = false;

  // IMarkerDelegate的remove()调用
  public synchronized boolean removeMarker(final IMarkerDelegate marker)
  {

    this.handler.post(new Runnable() {
      @Override
      public void run() {
        hideInfoWindow(marker);
        isRemove = MapOverlayImageViewDecode.this.markers.remove(marker);
      }
    });

    return isRemove;
  }

  public synchronized void set2Top(IMarkerDelegate marker)
  {
    try
    {
      if (this.markers.remove(marker))
      {
        changeOverlayIndex();
        this.markers.add(marker);
      }
    }
    catch (Throwable throwable)
    {
      SDKLogHandler.exception(throwable, "MapOverlayImageView", "set2Top");
    }
  }

  public void showInfoWindow(final IMarkerDelegate marker)
  {
      this.handler.post(new Runnable() {
        @Override
        public void run() {
          try {
            if (MapOverlayImageViewDecode.this.mHitMarkPoint == null) {
              MapOverlayImageViewDecode.this.mHitMarkPoint = new IPoint();
            }
            Rect rect = marker.getRect();
            MapOverlayImageViewDecode.this.mHitMarkPoint = new IPoint(rect.left + rect.width() / 2, rect.top);
            MapOverlayImageViewDecode.this.mHitMarker = marker;
            MapOverlayImageViewDecode.this.map.showInfoWindow(MapOverlayImageViewDecode.this.mHitMarker);
          }
          catch (Throwable throwable)
          {
            SDKLogHandler.exception(throwable, "MapOverlayImageView", "showInfoWindow");

            throwable.printStackTrace();
          }
        }
      });


  }

  public void hideInfoWindow(IMarkerDelegate marker)
  {
    try
    {
      if (marker.isInfoWindowShow())
      {
        this.map.hiddenInfoWindowShown();
        this.mHitMarker = null;
      }
      else if ((this.mHitMarker != null) && (this.mHitMarker.getId() == marker.getId()))
      {
        this.mHitMarker = null;
      }
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
  }

  private final Handler j = new Handler();
  private final Runnable reDrawRunnable = new Runnable(){ //原始逆向出来的类是ax,此处将其省略
	  public void run()
	  {
	    try
	    {
	      //this.a.a.p();
	    	map.redrawInfoWindow(); //上一句翻译
	    }
	    catch (Throwable throwable)
	    {
	      SDKLogHandler.exception(throwable, "MapOverlayImageView", "redrawInfoWindow post");

	      throwable.printStackTrace();
	    }
	  }
  };

  public synchronized void calFPoint()
  {
    for (IMarkerDelegate marker : this.markers) {
      try
      {
        if (marker.isVisible()) {
          marker.calFPoint();
        }
      }
      catch (Throwable throwable)
      {
        SDKLogHandler.exception(throwable, "MapOverlayImageView", "calFPoint");

        throwable.printStackTrace();
      }
    }
  }

  private void changeOverlayIndex() //j
  {
    try
    {
      ArrayList<IMarkerDelegate> localArrayList = new ArrayList<IMarkerDelegate>(this.markers);

      Collections.sort(localArrayList, this.comparetor);
      this.markers = new CopyOnWriteArrayList<IMarkerDelegate>(localArrayList);
    }
    catch (Throwable throwable)
    {
      SDKLogHandler.exception(throwable, "MapOverlayImageView", "changeOverlayIndex");
    }
  }

  public void onDrawGL(GL10 gl)
  {
	  for (Integer id : recycleTextureIds) {
      gl.glDeleteTextures(1, new int[]{id}, 0);
      this.map.deleteTexsureId(id.intValue());
    }

    this.recycleTextureIds.clear();
    if ((this.mHitMarker != null) && (!this.mHitMarker.F())) {
      postDraw();
    }

    for (IMarkerDelegate marker : markers) {
      if ((marker.isInScreen()) || (marker.isInfoWindowShow())) {
        marker.drawMarker(gl, this.map);
      }
    }
  }

  public synchronized boolean isAnimator()
  {
    for (IMarkerDelegate localag : this.markers) {
      if (!localag.isAnimator()) {
        return false;
      }
    }
    return true;
  }

  public IMarkerDelegate getHitMarker()
  {
    return this.mHitMarker;
  }

  public IMarkerDelegate getLongPressHitMarker(MotionEvent paramMotionEvent)
  {
    for (IMarkerDelegate marker : this.markers) {
      if ((marker instanceof MarkerDelegateImp))
      {
        Rect localRect = marker.getRect();
        boolean retvalue = hitTest(localRect, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
        if (retvalue)
        {
          this.mHitMarker = marker;
          return this.mHitMarker;
        }
      }
    }
    return null;
  }

  public synchronized void addTexture(OverlayTextureItem overlayTextureItem)
  {
    if (overlayTextureItem == null) {
      return;
    }
    if (overlayTextureItem.getTextureId() == 0) {
      return;
    }
    this.overlayitem.add(overlayTextureItem);
  }

  // IMarkerDelegate的destroy()调用,参数是纹理id
  public synchronized void removeMarker(int paramInt)
  {
    for (OverlayTextureItem localbd : this.overlayitem) {
      if (localbd.getTextureId() == paramInt) {
        this.overlayitem.remove(localbd);
      }
    }
  }

  public void recycleId(Integer paramInteger)
  {
    if (paramInteger.intValue() != 0) {
      this.recycleTextureIds.add(paramInteger);
    }
  }

  public synchronized int getTextureID(BitmapDescriptor paramBitmapDescriptor)
  {
    if ((paramBitmapDescriptor == null) || (paramBitmapDescriptor.getBitmap() == null) ||
      (paramBitmapDescriptor.getBitmap().isRecycled())) {
      return 0;
    }
    OverlayTextureItem overlayTextureItem = null;
    for (int m = 0; m < this.overlayitem.size(); m++)
    {
      overlayTextureItem = (OverlayTextureItem)this.overlayitem.get(m);
      if (overlayTextureItem.getBitmapDes().equals(paramBitmapDescriptor)) {
        return overlayTextureItem.getTextureId();
      }
    }
    return 0;
  }

  public synchronized void destroy() //e
  {
    try
    {
    	for (IMarkerDelegate mk : markers) {
        if (mk != null)
        {
          mk.destroy();
          mk = null;
        }
      }
      // a(null);
      clear(null); //上一句翻译
      for (Iterator<OverlayTextureItem> localIterator = this.overlayitem.iterator(); localIterator.hasNext();)
      {
        OverlayTextureItem localObject = (OverlayTextureItem)localIterator.next();
        ((OverlayTextureItem)localObject).getBitmapDes().recycle();
      }
      this.overlayitem.clear();
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "MapOverlayImageView", "destroy");
      localThrowable.printStackTrace();
      Log.d("leadorApi", "MapOverlayImageView clear erro" + localThrowable.getMessage());
    }
  }
  
  public boolean onSingleTap(MotionEvent motionEvent)
    throws RemoteException
  {
    boolean bool1 = false;
    for (IMarkerDelegate marker : this.markers) {
      if (((marker instanceof MarkerDelegateImp)) && (marker.isVisible()))
      {
        Rect localRect = marker.getRect();
        boolean bool2 = hitTest(localRect, (int)motionEvent.getX(), (int)motionEvent.getY());
        if (bool2)
        {
          bool1 = bool2;
          
          this.mHitMarkPoint = new IPoint(localRect.left + localRect.width() / 2, localRect.top);
          this.mHitMarker = marker;
          break;
        }
      }
    }
    return bool1;
  }
  
  public boolean hitTest(Rect rect, int x, int y)
  {
    return rect.contains(x, y);
  }
  
  public synchronized List<Marker> getMapScreenMarkers()
  {
    ArrayList<Marker> localArrayList = new ArrayList<Marker>();
    try
    {
      Rect localRect = new Rect(0, 0, this.map.getMapWidth(), this.map.getMapHeight());
      FPoint fPoint = null;
      IPoint iPoint = new IPoint();
      for (IMarkerDelegate localag : this.markers) {
        if (!(localag instanceof TextDelegateImp))
        {
          fPoint = localag.anchorUVoff();
          if (fPoint != null)
          {
            this.map.getMapProjection().map2Win(fPoint.x, fPoint.y, iPoint);
            if (hitTest(localRect, iPoint.x, iPoint.y)) {
              localArrayList.add(new Marker(localag));
            }
          }
        }
      }
    }
    catch (Throwable localThrowable)
    {
//      Rect localRect;
//      FPoint localFPoint;
//      IPoint localIPoint;
      SDKLogHandler.exception(localThrowable, "MapOverlayImageView", "getMapScreenMarkers");
      
      localThrowable.printStackTrace();
    }
    return localArrayList;
  }
  
  public synchronized void realdestroy()
  {
    for (IMarkerDelegate localag : this.markers) {
      if (localag.x()) {
        localag.realdestroy();
      }
    }
  }
  
  public synchronized void changeIndexs()
  {
    this.handler.removeCallbacks(this.runnable);
    this.handler.postDelayed(this.runnable, 10L);
  }
  
  static class Comparetor
    implements Comparator<Object>, Serializable
  {
    public int compare(Object paramObject1, Object paramObject2)
    {
      IMarkerDelegate localag1 = (IMarkerDelegate)paramObject1;
      IMarkerDelegate localag2 = (IMarkerDelegate)paramObject2;
      try
      {
        if ((localag1 != null) && (localag2 != null))
        {
          if (localag1.getZIndex() > localag2.getZIndex()) {
            return 1;
          }
          if (localag1.getZIndex() < localag2.getZIndex()) {
            return -1;
          }
        }
      }
      catch (Throwable localThrowable)
      {
        SDKLogHandler.exception(localThrowable, "MapOverlayImageView", "compare");
        
        localThrowable.printStackTrace();
      }
      return 0;
    }
  }
  
  public void postDraw()
  {
    this.j.post(this.reDrawRunnable);
  }
}
