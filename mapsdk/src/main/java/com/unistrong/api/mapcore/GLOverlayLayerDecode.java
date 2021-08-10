package com.unistrong.api.mapcore;

import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.leador.mapcore.IPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.microedition.khronos.opengles.GL10;

class GLOverlayLayerDecode implements IGLOverlayLayer{
  IMapDelegate mapDelegate;
  private static int index = 0;
  
  static String CreateId(String type) // a
  {
    index += 1;
    return type + index;
  }
  
  private CopyOnWriteArrayList<IOverlayDelegateDecode> overlayList = new CopyOnWriteArrayList(new ArrayList(500)); // d
  private CopyOnWriteArrayList<Integer> TexsureIdList = new CopyOnWriteArrayList<Integer>(); //e
  private Handler f = new Handler(); //f
  a comparator = new a(); //b

  private Runnable runnable = new Runnable(){ //g
	  public synchronized void run()
	    {
	      try
	      {
	    	  synchronized (GLOverlayLayerDecode.this)
	          {
	            ArrayList<IOverlayDelegateDecode> localArrayList = new ArrayList<IOverlayDelegateDecode>(overlayList);
	            Collections.sort(localArrayList, comparator);
	            overlayList = new CopyOnWriteArrayList<IOverlayDelegateDecode>(localArrayList);
	          }
	      }
	      catch (Throwable localThrowable)
	      {
	      }
	    }
  };
  
  public GLOverlayLayerDecode(IMapDelegate paramaa)
  {
    this.mapDelegate = paramaa;
  }

  @Override
  public IMapDelegate getOverlayMapDelegate() {
    return this.mapDelegate;
  }

  public synchronized void clearOverlay(String paramString)
  {
    try
    {
      if ((paramString == null) || (paramString.trim().length() == 0))
      {
        this.overlayList.clear();
        index = 0;
      }
      else
      {
        for (IOverlayDelegateDecode localai : this.overlayList) {
          if (!paramString.equals(localai.getId())) {
            this.overlayList.remove(localai);
          }
        }
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
      Log.d("leadorApi", "GLOverlayLayer clear erro" + localThrowable.getMessage());
    }
  }
  
  public synchronized void destroyOverlay() // a
  {
    try
    {
      for (IOverlayDelegateDecode localai : this.overlayList)
      {
        localai.destroy();
        localai = null;
      }
      clearOverlay(null);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
      Log.d("leadorApi", "GLOverlayLayer destroy erro" + localThrowable.getMessage());
    }
  }
  
  private synchronized IOverlayDelegateDecode getIOverlay(String paramString) // d
    throws RemoteException
  {
    for (IOverlayDelegateDecode localai : this.overlayList) {
      if ((localai != null) && (localai.getId().equals(paramString))) {
        return localai;
      }
    }
    return null;
  }
  
  public synchronized void add(IOverlayDelegateDecode overlay) // a
    throws RemoteException
  {
    synchronized (GLOverlayLayerDecode.this) {
      this.overlayList.add(overlay);
    }
    changeOverlayIndexs();
  }
  
  public synchronized boolean removeOverlay(String paramString) //c
    throws RemoteException
  {
    IOverlayDelegateDecode localai = getIOverlay(paramString);
    if (localai != null) {
      return this.overlayList.remove(localai);
    }
    return false;
  }
  
  public synchronized void changeOverlayIndexs() //b
  {
    this.f.removeCallbacks(this.runnable);
    this.f.postDelayed(this.runnable, 10L);
  }
  
  public void onDrawGL(GL10 paramGL10, boolean paramBoolean, int paramInt) // a
  {
//    long start = System.currentTimeMillis();
//    Log.e("@@@", "GLOverlayLayerDecode onDrawGL begin");
    for (Iterator<Integer> localIterator = this.TexsureIdList.iterator(); localIterator.hasNext();)
    {
      Integer localObject = (Integer)localIterator.next();
      paramGL10.glDeleteTextures(1, new int[] { ((Integer)localObject).intValue() }, 0);
      this.mapDelegate.deleteTexsureId(((Integer) localObject).intValue());
    }
    this.TexsureIdList.clear();
//    long delTime = System.currentTimeMillis();
//    Log.e("@@@", "GLOverlayLayerDecode onDrawGL del "+(delTime-start)+"ms");
    int overlaySize = this.overlayList.size();
    for (IOverlayDelegateDecode localai:overlayList)
    {
      try
      {
        if (localai.isVisible()) {
          if (overlaySize > 20)
          {
            if (localai.a()) {
              if (paramBoolean)
              {
                if (localai.getZIndex() <= paramInt) {
                  localai.draw(paramGL10);
                }
              }
              else if (localai.getZIndex() > paramInt) {
                localai.draw(paramGL10);
              }
            }
          }
          else if (paramBoolean)
          {
            if (localai.getZIndex() <= paramInt) {
              localai.draw(paramGL10);
            }
          }
          else if (localai.getZIndex() > paramInt) {
            localai.draw(paramGL10);
          }
        }
      }
      catch (RemoteException localRemoteException)
      {
        localRemoteException.printStackTrace();
      }
    }
//    Log.e("@@@", "GLOverlayLayerDecode onDrawGL draw "+(System.currentTimeMillis()-delTime)+"ms"
//            +", overlaySize = " + overlaySize);
  }
  
  public void recycleOverlayId(Integer texsureId) // a
  {
    if (texsureId.intValue() != 0) {
      this.TexsureIdList.add(texsureId);
    }
  }
  
  public synchronized void calMapFPoint() //c
  {
    for (IOverlayDelegateDecode localai : this.overlayList) {
      try
      {
        if (localai != null) {
          localai.calMapFPoint();
        }
      }
      catch (RemoteException localRemoteException)
      {

        localRemoteException.printStackTrace();
      }
    }
  }
  
  public boolean checkInBounds() // d
  {
    for (IOverlayDelegateDecode localai : this.overlayList) {
      if ((localai != null) && (!localai.checkInBounds())) {
        return false;
      }
    }
    return true;
  }
  
  public synchronized IOverlayDelegateDecode polylineClick(LatLng paramLatLng) // a
  {
    try {
      int min_dist = 100;
      IPoint motionP = new IPoint();
      mapDelegate.getLatLng2Pixel(paramLatLng.latitude, paramLatLng.longitude, motionP);
      PolylineDelegateImpDecode lineImpDecode = null;
      PolylineDelegateImpDecode lineImpDecodeTarget = null;
      for (IOverlayDelegateDecode localai : this.overlayList) {
        if ((localai != null) && (localai.checkInBounds()) && ((localai instanceof PolylineDelegateImpDecode))) {
          lineImpDecode = (PolylineDelegateImpDecode) localai;
        } else {
          continue;
        }
        if (lineImpDecode.isVisible() && lineImpDecode.getPoints() != null &&
                lineImpDecode.getPoints().size()>0){
          int len = lineImpDecode.getPoints().size();
          List<LatLng> points = lineImpDecode.getPoints();
          int min_dist_l = 2000;
          int lastx = 0, lasty = 0;

          int min_x = 999999999;
          int min_y = 999999999;
          int max_x = 0;
          int max_y = 0;
          int cur_x = 0;
          int cur_y = 0;
          IPoint iPoint = new IPoint();
          mapDelegate.getLatLng2Pixel(points.get(0).latitude, points.get(0).longitude, iPoint);
          lastx = iPoint.x;
          lasty = iPoint.y;
//          Log.e("@@@", "IPoint:"+iPoint.x+","+iPoint.y);
          cur_x = iPoint.x;
          cur_y = iPoint.y;
          if (cur_x < min_x) {
            min_x = cur_x;
          }

          if (cur_y < min_y) {
            min_y = cur_y;
          }

          if (cur_x > max_x) {
            max_x = cur_x;
          }

          if (cur_y > max_y) {
            max_y = cur_y;
          }

          for (int i = 1;i<len;i++){
            mapDelegate.getLatLng2Pixel(points.get(i).latitude, points.get(i).longitude, iPoint);
//            Log.e("@@@", "IPoint:"+iPoint.x+","+iPoint.y);

            if (iPoint.x != lastx && iPoint.y != lasty){
              int dist = pt2line_dist(iPoint.x, iPoint.y, lastx, lasty, motionP.x, motionP.y);
//              Log.e("@@@", "lastx = "+lastx+",lasty = "+lasty+",dist = "+dist);
              if (min_dist_l > dist){
                min_dist_l = dist;
              }
              lastx = iPoint.x;
              lasty = iPoint.y;
            } else {
              continue;
            }
            cur_x = iPoint.x;
            cur_y = iPoint.y;
            if (cur_x < min_x) {
              min_x = cur_x;
            }

            if (cur_y < min_y) {
              min_y = cur_y;
            }

            if (cur_x > max_x) {
              max_x = cur_x;
            }

            if (cur_y > max_y) {
              max_y = cur_y;
            }
          }

          // 如果该段不在屏幕范围内
          Rect rc = new Rect();
          rc.left = min_x;
          rc.top = min_y;
          rc.right = max_x;
          rc.bottom = max_y;

          LatLngBounds bounds = mapDelegate.getMapBounds();
          LatLng northeast = bounds.northeast;
          LatLng southwest = bounds.southwest;
          IPoint iPointN = new IPoint();
          IPoint iPointS = new IPoint();
          mapDelegate.getLatLng2Pixel(northeast.latitude, northeast.longitude, iPointN);
          mapDelegate.getLatLng2Pixel(southwest.latitude, southwest.longitude, iPointS);
          Rect bound = new Rect();
          bound.left = Math.min(iPointN.x, iPointS.x);
          bound.top = Math.min(iPointN.y, iPointS.y);
          bound.right = Math.max(iPointN.x, iPointS.x);
          bound.bottom = Math.max(iPointN.y, iPointS.y);
//          Log.e("@@@", "rc:"+rc.left+","+rc.top+","+rc.right+","+rc.bottom+";/n"+
//                  "bound:"+bound.left+","+bound.top+","+bound.right+","+bound.bottom);
          if (!IsScreenInRect(rc, bound)) {
//            Log.e("@@@", "IsScreenInRect is false");
            min_dist_l = 2000;
          }
//          Log.e("@@@", lineImpDecode.getId()+", min_dist_l = "+min_dist_l);
          // compare all
          if (min_dist > min_dist_l){
            min_dist = min_dist_l;
            if (min_dist < 30){
              lineImpDecodeTarget = lineImpDecode;
            }
          }

        }

      }
      return lineImpDecodeTarget;
    } catch (RemoteException e) {
    }
    return null;
  }

  static boolean IsScreenInRect(Rect drc, Rect bound) {
//    if (mapState == null)
//      return false;

//    Rect bound = new Rect();
//    mapState.getPixel20Bound(bound);

    if (drc.right < bound.left || drc.left > bound.right
            || drc.top > bound.bottom || drc.bottom < bound.top)
      return false;
    return true;

  }

  // 求点与线段的距离和垂足
  static int pt2line_dist(int x1, int y1, int x2, int y2, int ptx, int pty) {
    int dx = x2 - x1;
    int dy = y2 - y1;

    int dr = -(y1 - pty) * dy - (x1 - ptx) * dx;
    int dl;
    int fx, fy;
    if (dr <= 0) {
      fx = x1;
      fy = y1;
    } else if (dr >= (dl = dx * dx + dy * dy)) {
      fx = x2;
      fy = y2;
    } else {
      fx = x1 + dr * dx / dl;
      fy = y1 + dr * dy / dl;
    }
    dx = ptx - fx;
    dy = pty - fy;

    return (int) Math.sqrt(dx * dx + dy * dy);
  }

  static class a // a
          implements Comparator<Object>, Serializable
  {
    public int compare(Object paramObject1, Object paramObject2)
    {
      IOverlayDelegateDecode localai1 = (IOverlayDelegateDecode)paramObject1;
      IOverlayDelegateDecode localai2 = (IOverlayDelegateDecode)paramObject2;
      try
      {
        if ((localai1 != null) && (localai2 != null))
        {
          if (localai1.getZIndex() > localai2.getZIndex()) {
            return 1;
          }
          if (localai1.getZIndex() < localai2.getZIndex()) {
            return -1;
          }
        }
      }
      catch (Throwable localThrowable)
      {
        localThrowable.printStackTrace();
      }
      return 0;
    }
  }
}
