package com.unistrong.api.mapcore;


import android.os.RemoteException;

import com.unistrong.api.maps.model.LatLng;

import javax.microedition.khronos.opengles.GL10;

/**
 * polyline、polygon绘制管理接口类
 */
public interface IGLOverlayLayer {
    public abstract IMapDelegate getOverlayMapDelegate();
    public abstract void clearOverlay(String id);
    public abstract void destroyOverlay();
    public abstract void add(IOverlayDelegateDecode overlay)
            throws RemoteException;
    public abstract boolean removeOverlay(String id)
            throws RemoteException;
    abstract void changeOverlayIndexs();
    public abstract void onDrawGL(GL10 paramGL10, boolean paramBoolean, int paramInt);
    public abstract void recycleOverlayId(Integer texsureId);
    public abstract void calMapFPoint();
    public abstract boolean checkInBounds();
    public abstract IOverlayDelegateDecode polylineClick(LatLng paramLatLng);

}
