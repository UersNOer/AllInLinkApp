package com.unistrong.api.mapcore;

import android.content.Context;
import android.location.Location;
import android.os.RemoteException;

import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.maps.MapController;
import com.unistrong.api.maps.model.BitmapDescriptorFactory;
import com.unistrong.api.maps.model.CameraPosition;
import com.unistrong.api.maps.model.CircleOptions;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.Marker;
import com.unistrong.api.maps.model.MarkerOptions;
import com.unistrong.api.maps.model.MyLocationStyle;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapProjection;

class MyLocationOverlayDecode
{
    private IMapDelegate map; // a
    private Marker point; //b
    private ICircleDelegate c; //c
    private MyLocationStyle d; // d
    private LatLng e; //e
    private double f; //f
    private Context context; //g
    private SensorEventHelperDecode h; //h
    private int i = 1;  //i
    private boolean j = false; //j
    private final String k = "my_location_car.png"; //k
    private final String l = "my_location_car_3d.png"; //l
    private boolean m = false; //m

    MyLocationOverlayDecode(IMapDelegate paramaa, Context paramContext)
    {
        this.context = paramContext;
        this.map = paramaa;
        this.h = new SensorEventHelperDecode(this.context, paramaa);
    }

    public void setMyLocationStyle(MyLocationStyle paramMyLocationStyle)
    {
        try
        {
            this.d = paramMyLocationStyle;
            if ((this.point == null) && (this.c == null)) {
                return;
            }
            l();
            this.h.a(this.point);
            k();
        }
        catch (Throwable localThrowable)
        {
            SDKLogHandler.exception(localThrowable, "MyLocationOverlay", "setMyLocationStyle");
            localThrowable.printStackTrace();
        }
    }

    public void a(int paramInt)
    {
        this.i = paramInt;
        this.j = false;
        switch (this.i)
        {
            case 1:
                g();
                break;
            case 2:
                h();
                break;
            case 3:
                i();
                break;
        }
    }

    public void unRegister2DSensorListener(){
        if (this.i != 3){
            unRegisterSensorListener();
        }
    }

    public void unRegisterSensorListener()
    {
        if (this.h != null) {
            this.h.b();
        }
    }

    public void registerSensorListener(){
        try {
            if ((this.i == 3 || isLocRotateEnabled()) && (this.h != null)) {
                this.h.a();
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    private boolean isLocRotateEnabled() throws RemoteException {
        boolean isLocRotateEnabled = true;
        if ((this.i == 1 || this.i == 2) && this.map.isLocationRotateEnabled()){
            isLocRotateEnabled = true;
        } else {
            isLocRotateEnabled = false;
        }
        return isLocRotateEnabled;
    }

    private void g()
    {
        if (this.point != null)
        {
            try {
//      c(0.0F);
                if (this.map.isLocationRotateEnabled()){
                    registerSensorListener();
                } else {
                    unRegisterSensorListener();
                }
                if (!this.m) {
                    this.point.setIcon(BitmapDescriptorFactory.fromAsset("my_location_car.png"));
                }
                this.point.setFlat(false);
//      b(0.0F);
                CameraPosition position = this.map.getCameraPosition();
                CameraPosition position1 = new CameraPosition(position.target, 15.0F, 0.0F, 0.0F);
                CameraUpdateFactoryDelegate update = CameraUpdateFactoryDelegate.newCameraPosition(position1);
                this.map.animateCameraWithDurationAndCallback(update, 400L, new MapController.CancelableCallback() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void h()
    {
        if (this.point != null)
        {
            try {
//      c(0.0F);
                if (this.map.isLocationRotateEnabled()){
                    registerSensorListener();
                } else {
                    unRegisterSensorListener();
                }
                if (!this.m) {
                    this.point.setIcon(BitmapDescriptorFactory.fromAsset("my_location_car.png"));
                }
                this.point.setFlat(false);
//      b(0.0F);
                CameraPosition position = this.map.getCameraPosition();
                CameraPosition position1 = new CameraPosition(position.target, 15.0F, 0.0F, 0.0F);
                CameraUpdateFactoryDelegate update = CameraUpdateFactoryDelegate.newCameraPosition(position1);
                this.map.animateCameraWithDurationAndCallback(update, 400L, new MapController.CancelableCallback() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void i()
    {
        if (this.point != null)
        {
            if (this.h != null){
                this.point.setRotateAngle(-this.h.getLastAngle());
            }
            try
            {
//      this.h.a();
                float lastAngle = 0.0F;
                if (!this.map.isLocationRotateEnabled()){
                    registerSensorListener();
                    lastAngle = 0.0F;
                } else {
                    lastAngle = this.h.getLastAngle();
                }
                if (!this.m) {
                    this.point.setIcon(BitmapDescriptorFactory.fromAsset("my_location_car_3d.png"));
                }
                this.point.setFlat(true);
//        this.map.moveCamera(CameraUpdateFactoryDelegate.zoomTo(17.0F));
//        b(45.0F);
                // animate
                CameraPosition position = this.map.getCameraPosition();
                CameraPosition position1 = new CameraPosition(position.target, 17.0F, 45.0F, lastAngle);
                CameraUpdateFactoryDelegate update = CameraUpdateFactoryDelegate.newCameraPosition(position1);
                this.map.animateCameraWithDurationAndCallback(update, 400L, new MapController.CancelableCallback() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
            catch (RemoteException localRemoteException)
            {
                localRemoteException.printStackTrace();
            }
        }
    }

    private void b(float paramFloat)
    {
        if (this.map == null) {
            return;
        }
        try
        {
            this.map.moveCamera(CameraUpdateFactoryDelegate.changeTilt(paramFloat));
        }
        catch (RemoteException localRemoteException)
        {
            localRemoteException.printStackTrace();
        }
    }

    private void c(float paramFloat)
    {
        if (this.map == null) {
            return;
        }
        try
        {
            this.map.moveCamera(CameraUpdateFactoryDelegate.changeBearing(paramFloat));
        }
        catch (RemoteException localRemoteException)
        {
            localRemoteException.printStackTrace();
        }
    }

    public void setCentAndRadius(Location paramLocation)
    {
        if (paramLocation == null) {
            return;
        }
        this.e = new LatLng(paramLocation.getLatitude(), paramLocation.getLongitude());

        this.f = paramLocation.getAccuracy();
        if ((this.point == null) && (this.c == null)) {
            k();
        }
        if (this.point != null) {
            this.point.setPosition(this.e);
        }
        if (this.c != null)
        {
            try
            {
                this.c.setCenter(this.e);
                if (this.f != -1.0D) {
                    this.c.setRadius(this.f);
                }
            }
            catch (RemoteException localRemoteException)
            {
                SDKLogHandler.exception(localRemoteException, "MyLocationOverlay", "setCentAndRadius");
                localRemoteException.printStackTrace();
            }
            j();
            if (this.i != 3) {
//        b(paramLocation);
            }
            this.j = true;
        }
    }

    private void b(Location paramLocation)
    {
        float f1 = paramLocation.getBearing();

        f1 %= 360.0F;
        if (f1 > 180.0F) {
            f1 -= 360.0F;
        } else if (f1 < -180.0F) {
            f1 += 360.0F;
        }
        if (this.point != null) {
            this.point.setRotateAngle(-f1);
        }
    }

    private void j()
    {
        if ((this.i == 1) && (this.j)) {
            return;
        }
        try
        {
            IPoint localIPoint = new IPoint();
            MapProjection.lonlat2Geo(this.e.longitude, this.e.latitude, localIPoint);
            this.map.animateCamera(CameraUpdateFactoryDelegate.changeGeoCenter(localIPoint));
        }
        catch (RemoteException localRemoteException)
        {
            SDKLogHandler.exception(localRemoteException, "MyLocationOverlay", "locaitonFollow");
            localRemoteException.printStackTrace();
        }
    }

    private void k()
    {
        if (this.d == null)
        {
            this.d = new MyLocationStyle();
            this.d.myLocationIcon(BitmapDescriptorFactory.fromAsset("my_location_car.png"));
            m();
        }
        else
        {
            this.m = true;
            m();
        }
    }

    public void remove()  //c
            throws RemoteException
    {
        l();
        if (this.h != null)
        {
            this.h.b();
            this.h = null;
        }
    }

    private void l()
    {
        if (this.c != null)
        {
            try
            {
                this.map.removeGLOverlay(this.c.getId());
            }
            catch (RemoteException localRemoteException)
            {
                SDKLogHandler.exception(localRemoteException, "MyLocationOverlay", "locationIconRemove");
                localRemoteException.printStackTrace();
            }
            this.c = null;
        }
        if (this.point != null)
        {
            this.point.remove();
            this.point.destroy();
            this.point = null;
            //this.h.a(null);
            this.h.a((Marker)null);
        }
    }

    private void m()
    {
        try
        {
            this.c = this.map.addCircle(new CircleOptions()
                    .strokeWidth(this.d.getStrokeWidth())
                    .fillColor(this.d.getRadiusFillColor())
                    .strokeColor(this.d.getStrokeColor())
                    .center(new LatLng(0.0D, 0.0D)).zIndex(1.0F));
            if (this.e != null) {
                this.c.setCenter(this.e);
            }
            this.c.setRadius(this.f);
            this.point = this.map.addMarker(new MarkerOptions().visible(false)
                    .anchor(this.d.getAnchorU(), this.d.getAnchorV())
                    .icon(this.d.getMyLocationIcon())
                    .position(new LatLng(0.0D, 0.0D)));
            a(this.i);
            if (this.e != null)
            {
                this.point.setPosition(this.e);
                this.point.setVisible(true);
            }
            this.h.a(this.point);
        }
        catch (RemoteException localRemoteException)
        {
            SDKLogHandler.exception(localRemoteException, "MyLocationOverlay", "myLocStyle");
            localRemoteException.printStackTrace();
        }
    }

    public void setRotateAngle(float paramFloat)
    {
        if (this.point != null) {
            this.point.setRotateAngle(paramFloat);
        }
    }

    public String getId()
    {
        if (this.point != null) {
            return this.point.getId();
        }
        return null;
    }

    public String e()
            throws RemoteException
    {
        if (this.c != null) {
            return this.c.getId();
        }
        return null;
    }

    public void f()
    {
        this.c = null;
        this.point = null;
    }
}
