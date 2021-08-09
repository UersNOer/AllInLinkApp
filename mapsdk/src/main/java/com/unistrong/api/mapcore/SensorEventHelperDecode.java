package com.unistrong.api.mapcore;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.RemoteException;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import com.unistrong.api.maps.model.Marker;

public class SensorEventHelperDecode
        implements SensorEventListener
{
    private SensorManager sensorManager;
    private Sensor sensor;
    private long c = 0L;
    private final int d = 100;
    private float e;
    private Context context;
    private IMapDelegate map;
    private Marker marker;
    private float lastAngle;

    public SensorEventHelperDecode(Context context, IMapDelegate mapDelegate)
    {
        this.context = context;
        this.map = mapDelegate;

        this.sensorManager = ((SensorManager)context.getSystemService(Context.SENSOR_SERVICE));
        this.sensor = this.sensorManager.getDefaultSensor(3);
    }

    public void a()
    {
        this.sensorManager.registerListener(this, this.sensor, 3);
    }

    public void b()
    {
        this.sensorManager.unregisterListener(this, this.sensor);
    }

    public void a(Marker paramMarker)
    {
        this.marker = paramMarker;
    }

    public void onAccuracyChanged(Sensor paramSensor, int paramInt) {}

    public void onSensorChanged(SensorEvent paramSensorEvent)
    {
        if (System.currentTimeMillis() - this.c < 100L) {
            return;
        }
        if (!this.map.S().isFinished()) {
            return;
        }
        switch (paramSensorEvent.sensor.getType())
        {
            case 3:
                float f1 = paramSensorEvent.values[0];
                f1 += a(this.context);
                f1 %= 360.0F;
                if (f1 > 180.0F) {
                    f1 -= 360.0F;
                } else if (f1 < -180.0F) {
                    f1 += 360.0F;
                }
                if (Math.abs(this.e - f1) >= 3.0F)
                {
                    this.e = (Float.isNaN(f1) ? 0.0F : f1);
                    this.lastAngle = this.e;
                    if (this.marker != null) {
                        try
                        {
                            int locType = this.map.getMyLocationType();
                            if (locType == 3){
                                this.map.moveCamera(
                                        CameraUpdateFactoryDelegate.changeBearing(this.e));
                                this.marker.setRotateAngle(-this.e);
                            } else {
                                this.marker.setRotateAngle(-this.e);
                            }
                        }
                        catch (RemoteException localRemoteException)
                        {
                            localRemoteException.printStackTrace();
                        }
                    }
                    this.c = System.currentTimeMillis();
                }
                break;
        }
    }

    public static int a(Context context)
    {
        Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        switch (display.getRotation())
        {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return -90;
        }
        return 0;
    }

    public float getLastAngle() {
        return this.lastAngle;
    }
}
