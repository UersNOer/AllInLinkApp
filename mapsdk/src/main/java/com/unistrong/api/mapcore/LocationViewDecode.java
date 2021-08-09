package com.unistrong.api.mapcore;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.maps.model.LatLng;

class LocationViewDecode
  extends LinearLayout
{
  Bitmap a;
  Bitmap b;
  Bitmap c;
  Bitmap d;
  Bitmap e;
  Bitmap f;
  ImageView g;
  IMapDelegate h;
  
  public void a()
  {
    try
    {
      if (this.a != null) {
        this.a.recycle();
      }
      if (this.b != null) {
        this.b.recycle();
      }
      if (this.b != null) {
        this.c.recycle();
      }
      this.a = null;
      this.b = null;
      this.c = null;
      if (this.d != null)
      {
        this.d.recycle();
        this.d = null;
      }
      if (this.e != null)
      {
        this.e.recycle();
        this.e = null;
      }
      if (this.f != null)
      {
        this.f.recycle();
        this.f = null;
      }
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "LocationView", "destroy");
      localThrowable.printStackTrace();
    }
  }
  
  public LocationViewDecode(Context paramContext)
  {
    super(paramContext);
  }
  
  public LocationViewDecode(Context paramContext, MapMessageQueueDecode paramau, IMapDelegate paramaa)
  {
    super(paramContext);
    
    this.h = paramaa;
    initBitmap();
    this.g = new ImageView(paramContext);
    this.g.setImageBitmap(this.a);
    this.g.setClickable(true);
    this.g.setPadding(0, 20, 20, 0);
    this.g.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        if (!LocationViewDecode.this.i) {
          return false;
        }
        if (paramAnonymousMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
          LocationViewDecode.this.g.setImageBitmap(LocationViewDecode.this.b);
        } else if (paramAnonymousMotionEvent.getAction() == MotionEvent.ACTION_UP) {
          try
          {
            LocationViewDecode.this.g.setImageBitmap(LocationViewDecode.this.a);
            Location localLocation = LocationViewDecode.this.h.getMyLocation();
            if (localLocation == null) {
              return false;
            }
            LatLng localLatLng = new LatLng(localLocation.getLatitude(), localLocation.getLongitude());
            LocationViewDecode.this.h.showMyLocationOverlay(localLocation);
            LocationViewDecode.this.h.moveCamera(
              CameraUpdateFactoryDelegate.a(localLatLng, LocationViewDecode.this.h.getZoomLevel()));
          }
          catch (Throwable localThrowable)
          {
            SDKLogHandler.exception(localThrowable, "LocationView", "onTouch");
            localThrowable.printStackTrace();
          }
        }
        return false;
      }
    });
    addView(this.g);
  }
  
  boolean i = false;
  private void initBitmap(){
    try
    {
      this.d = Util.fromAsset(getContext(), "map_location_button_selected.png");
      this.a = Util.zoomBitmap(this.d, ConfigableConstDecode.Resolution);

      this.e = Util.fromAsset(getContext(), "map_location_button_pressed.png");
      this.b = Util.zoomBitmap(this.e, ConfigableConstDecode.Resolution);

      this.f = Util.fromAsset(getContext(), "map_location_button_unselected.png");
      this.c = Util.zoomBitmap(this.f, ConfigableConstDecode.Resolution);
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "LocationView", "create");
      localThrowable.printStackTrace();
    }
  }
  public void setLocstionViewBitmap(Bitmap selectedBitmap,Bitmap pressedBitmap){
    if(selectedBitmap!=null&&pressedBitmap!=null){
      this.a=selectedBitmap;
      this.b=pressedBitmap;
    }else{
      initBitmap();
    }
    this.g.setImageBitmap(this.a);
  }
  public void showSelect(boolean paramBoolean) // a
  {
    this.i = paramBoolean;
    if (paramBoolean) {
      this.g.setImageBitmap(this.a);
    } else {
      this.g.setImageBitmap(this.c);
    }
    this.g.invalidate();
  }
}
