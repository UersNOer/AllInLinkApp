package com.unistrong.api.mapcore;

import android.graphics.Rect;
import android.os.RemoteException;
import android.view.MotionEvent;

import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.Marker;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * marker绘制管理接口类
 */
public interface IMapOverlayImageView {
    public abstract IMapDelegate getMapDelegate();
    public abstract boolean removeMarker(IMarkerDelegate marker);
    public abstract void removeMarker(int id);
    public abstract void hideInfoWindow(IMarkerDelegate marker);
    public abstract void showInfoWindow(IMarkerDelegate marker);
    public abstract int getTextureID(BitmapDescriptor paramBitmapDescriptor);
    public abstract void addTexture(OverlayTextureItem overlayTextureItem);
    public abstract void postDraw();
    public abstract void set2Top(IMarkerDelegate marker);
     abstract void changeIndexs();
    public abstract void recycleId(Integer paramInteger);
    public abstract void destroy();
    public abstract void onDrawGL(GL10 gl);
    public abstract void addMarker(IMarkerDelegate marker);
    public abstract void clear(String id);
    public abstract IMarkerDelegate getLongPressHitMarker(MotionEvent motionEvent);
    public abstract boolean hitTest(Rect rect, int x, int y);
    public abstract IMarkerDelegate getHitMarker();
    public abstract boolean onSingleTap(MotionEvent motionEvent)throws RemoteException;
    abstract  int getMarkersSize();
    public abstract  List<Marker> getMapScreenMarkers();
    public void realdestroy();
    public void calFPoint();
    public boolean isAnimator();
}
