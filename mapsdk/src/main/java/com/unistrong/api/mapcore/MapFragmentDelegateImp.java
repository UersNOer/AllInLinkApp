package com.unistrong.api.mapcore;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.unistrong.api.mapcore.util.LogManager;
import com.unistrong.api.maps.UnistrongException;
import com.unistrong.api.maps.MapOptions;
import com.unistrong.api.maps.model.CameraPosition;

public class MapFragmentDelegateImp
  implements IMapFragmentDelegate
{
  private IMapDelegate mapDelegate;
  public static volatile Context context;
  public int visibility = 0;
  private int viewType = 0;
  public static final int SURFACE_VIEW = 0;
  public static final int TEXTURE_VIEW = 1;
  private MapOptions mapOptions;
  public MapFragmentDelegateImp(int viewType)
  {
    this.viewType = (viewType > 0 ? 1 : 0);
  }

  public void setContext(Context context)
  {
    if (context != null) {
      MapFragmentDelegateImp.context = context.getApplicationContext();
    }
  }
  
  public void setMapOptions(MapOptions mapOptions)
  {
    this.mapOptions = mapOptions;
  }


  public IMapDelegate getMapDelegate()
          throws RemoteException, UnistrongException {
    if (this.mapDelegate == null)
    {
      if (context == null) {
        throw new NullPointerException("Context 为 null 请在地图调用之前 使用 MapsInitializer.initialize(Context paramContext) 来设置Context");
      }
      int dpi = context.getResources().getDisplayMetrics().densityDpi;
      if (dpi <= 120) {
        ConfigableConstDecode.Resolution = 0.5F;
      } else if (dpi <= 160) {
        ConfigableConstDecode.Resolution = 0.8F;
      } else if (dpi <= 240) {
        ConfigableConstDecode.Resolution = 0.87F;
      } else if (dpi <= 320) {
        ConfigableConstDecode.Resolution = 1.0F;
      } else if (dpi <= 480) {
        ConfigableConstDecode.Resolution = 1.5F;
      } else if (dpi <= 640) {
        ConfigableConstDecode.Resolution = 1.8F;
      } else {
        ConfigableConstDecode.Resolution = 0.9F;
      }
      if (this.viewType == SURFACE_VIEW) {
        this.mapDelegate = new MapGLSurfaceView(context).getMapDelegate();
      } else {
        this.mapDelegate = new MapGLTextureView(context).getMapDelegate();
      }
    }
    return this.mapDelegate;
  }
  
  public void onInflate(Activity paramActivity, MapOptions mapOptions, Bundle paramBundle)
    throws RemoteException
  {
    context = paramActivity.getApplicationContext();
    this.mapOptions = mapOptions;
  }
  
  public void onCreate(Bundle paramBundle)
    throws RemoteException
  {
    LogManager.writeLog("MapFragmentDelegateImp", "onCreate", 113);
  }
  
  public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle)
          throws RemoteException, UnistrongException {
    try{
      if ((this.mapOptions == null) && (bundle != null))
      {
        byte[] arrayOfByte = bundle.getByteArray("MapOptions");
        if (arrayOfByte != null)
        {
          Parcel localParcel = Parcel.obtain();
          localParcel.unmarshall(arrayOfByte, 0, arrayOfByte.length);
          localParcel.setDataPosition(0);
          this.mapOptions = MapOptions.CREATOR.createFromParcel(localParcel);
        }
      }
    }catch (Exception ex){
      ex.printStackTrace();
    }

    if (this.mapDelegate == null)
    {
      if ((context == null) && (inflater != null)) {
        context = inflater.getContext().getApplicationContext();
      }
      if (context == null) {
        throw new NullPointerException("Context 为 null 请在地图调用之前 使用 MapsInitializer.initialize(Context paramContext) 来设置Context");
      }
      int dpi = context.getResources().getDisplayMetrics().densityDpi;
      if (dpi <= 120) {
        ConfigableConstDecode.Resolution = 0.5F;
      } else if (dpi <= 160) {
        ConfigableConstDecode.Resolution = 0.6F;
      } else if (dpi <= 240) {
        ConfigableConstDecode.Resolution = 0.87F;
      } else if (dpi <= 320) {
        ConfigableConstDecode.Resolution = 1.0F;
      } else if (dpi <= 480) {
        ConfigableConstDecode.Resolution = 1.5F;
      } else if (dpi <= 640) {
        ConfigableConstDecode.Resolution = 1.8F;
      } else {
        ConfigableConstDecode.Resolution = 0.9F;
      }
      if (this.viewType == SURFACE_VIEW) {
        this.mapDelegate = new MapGLSurfaceView(context).getMapDelegate();
      } else {
        this.mapDelegate = new MapGLTextureView(context).getMapDelegate();
      }
      this.mapDelegate.setVisibility(this.visibility);
    }
    try
    {

      init(this.mapOptions);
      LogManager.writeLog("MapFragmentDelegateImp", "onCreateView", 113);
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
    return this.mapDelegate.getView();
  }
  
  void init(MapOptions mapOptions)
    throws RemoteException
  {
    if ((mapOptions != null) && (this.mapDelegate != null))
    {
      CameraPosition localCameraPosition = mapOptions.getCamera();
      if (localCameraPosition != null) {
        this.mapDelegate.moveCamera(CameraUpdateFactoryDelegate.a(localCameraPosition.target, localCameraPosition.zoom, localCameraPosition.bearing, localCameraPosition.tilt));
      }
      IUiSettingsDelegate uiSettings = this.mapDelegate.getUiSettings();
      uiSettings.setRotateGesturesEnabled(mapOptions.getRotateGesturesEnabled().booleanValue());
      uiSettings.setScrollGesturesEnabled(mapOptions.getScrollGesturesEnabled().booleanValue());
      uiSettings.setTiltGesturesEnabled(mapOptions.getTiltGesturesEnabled().booleanValue());
      uiSettings.setZoomControlsEnabled(mapOptions.getZoomControlsEnabled().booleanValue());
      uiSettings.setZoomGesturesEnabled(mapOptions.getZoomGesturesEnabled().booleanValue());
      uiSettings.setCompassEnabled(mapOptions.getCompassEnabled().booleanValue());
      uiSettings.setScaleControlsEnabled(mapOptions.getScaleControlsEnabled().booleanValue());

//      int[] logoPosition=mapOptions.viewPositionMap.get(MapOptions.POSITION_LOGO);
//      if(logoPosition==null||logoPosition.length>=2)
        uiSettings.setLogoPosition(mapOptions.getLogoPosition());

//      int[] compassPosition=mapOptions.viewPositionMap.get(MapOptions.POSITION_COMPASS);
//      if(compassPosition!=null&&compassPosition.length>=2)this.mapDelegate.setCompassViewPosition(compassPosition[0],compassPosition[1]);

//      int[] scalePosition=mapOptions.viewPositionMap.get(MapOptions.POSITION_SCALE);
//      if(scalePosition!=null&&scalePosition.length>=2)this.mapDelegate.setScaleViewPosition(scalePosition[0],scalePosition[1]);

      this.mapDelegate.setZOrderOnTop_(mapOptions.getZOrderOnTop().booleanValue());

      Bitmap[] logoBitmap=mapOptions.viewBitmapMap.get(0);
      if(logoBitmap!=null&&logoBitmap.length>=1)this.mapDelegate.setLogoBitmap(logoBitmap[0]);
      Bitmap[] compassBitmap=mapOptions.viewBitmapMap.get(1);
      if(compassBitmap!=null&&compassBitmap.length>=1)this.mapDelegate.setCompassViewBitmap(compassBitmap[0]);
      Bitmap[] myLocationBitmap=mapOptions.viewBitmapMap.get(2);
      if(myLocationBitmap!=null&&myLocationBitmap.length>=2)this.mapDelegate.setLocationViewBitmap(myLocationBitmap);

    }
  }
  
  public void onResume()
    throws RemoteException
  {
    if (this.mapDelegate != null) {
      this.mapDelegate.onResume();
    }
  }
  
  public void onPause()
    throws RemoteException
  {
    if (this.mapDelegate != null) {
      this.mapDelegate.onPause();
    }
  }
  
  public void onDestroyView()
    throws RemoteException
  {}
  
  public void onDestroy()
    throws RemoteException
  {
    if (this.mapDelegate != null)
    {
      this.mapDelegate.clear();
      this.mapDelegate.destroy();
      this.mapDelegate = null;
    }
  }
  
  public void onLowMemory()
    throws RemoteException
  {
//    Log.d("onLowMemory", "onLowMemory run");
  }
  
  public void onSaveInstanceState(Bundle bundle)
    throws RemoteException
  {
    if (this.mapDelegate != null)
    {
      if (this.mapOptions == null) {
        this.mapOptions = new MapOptions();
      }
      try
      {
        Parcel parcel = Parcel.obtain();
        this.mapOptions = this.mapOptions.camera(getMapDelegate().getCameraPositionPrj_(false));
        this.mapOptions.writeToParcel(parcel, 0);
        bundle.putByteArray("MapOptions", parcel.marshall());
      }
      catch (Throwable throwable) {}
    }
  }
  
  public void setVisible(int visible)
  {
    this.visibility = visible;
    if (this.mapDelegate != null) {
      this.mapDelegate.setVisibility(visible);
    }
  }
}
