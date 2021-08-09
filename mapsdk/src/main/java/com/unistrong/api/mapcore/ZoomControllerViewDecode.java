package com.unistrong.api.mapcore;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.mapcore.util.SDKLogHandler;

/**
 * 原类名:br
 */
class ZoomControllerViewDecode
  extends LinearLayout
{
  private Bitmap zoomin_selected_final;
  private Bitmap zoomin_unselected_final;
  private Bitmap zoomout_selected_final;
  private Bitmap zoomout_unselected_final;
  private Bitmap zoomin_pressed_final;
  private Bitmap zoomout_pressed_final;
  private Bitmap zoomin_selected;
  private Bitmap zoomin_unselected;
  private Bitmap zoomout_selected;
  private Bitmap zoomout_unselected;
  private Bitmap zoomin_pressed;
  private Bitmap zoomout_pressed;
  private ImageView zoomin;
  private ImageView zoomout;
  private IMapDelegate mMap;
  
  public void a() // a
  {
    try
    {
      this.zoomin_selected_final.recycle();
      this.zoomin_unselected_final.recycle();
      this.zoomout_selected_final.recycle();
      this.zoomout_unselected_final.recycle();
      this.zoomin_pressed_final.recycle();
      this.zoomout_pressed_final.recycle();
      
      this.zoomin_selected_final = null;
      this.zoomin_unselected_final = null;
      this.zoomout_selected_final = null;
      this.zoomout_unselected_final = null;
      this.zoomin_pressed_final = null;
      this.zoomout_pressed_final = null;
      if (this.zoomin_selected != null)
      {
        this.zoomin_selected.recycle();
        this.zoomin_selected = null;
      }
      if (this.zoomin_unselected != null)
      {
        this.zoomin_unselected.recycle();
        this.zoomin_unselected = null;
      }
      if (this.zoomout_selected != null)
      {
        this.zoomout_selected.recycle();
        this.zoomout_selected = null;
      }
      if (this.zoomout_unselected != null)
      {
        this.zoomout_unselected.recycle();
        this.zoomin_selected = null;
      }
      if (this.zoomin_pressed != null)
      {
        this.zoomin_pressed.recycle();
        this.zoomin_pressed = null;
      }
      if (this.zoomout_pressed != null)
      {
        this.zoomout_pressed.recycle();
        this.zoomout_pressed = null;
      }
      removeAllViews();
      this.zoomin = null;
      this.zoomout = null;
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "ZoomControllerView", "destory");
      localThrowable.printStackTrace();
    }
  }
  
  public ZoomControllerViewDecode(Context paramContext)
  {
    super(paramContext);
  }
  
  public ZoomControllerViewDecode(Context paramContext, IMapDelegate mapDelegate)
  {
    super(paramContext);
    
    this.mMap = mapDelegate;
    try
    {
      this.zoomin_selected = Util.fromAsset(paramContext, "map_zoomin_button_selected.png");
      this.zoomin_selected_final = Util.zoomBitmap(this.zoomin_selected, ConfigableConstDecode.Resolution);

      this.zoomin_unselected = Util.fromAsset(paramContext, "map_zoomin_button_unselected.png");
      this.zoomin_unselected_final = Util.zoomBitmap(this.zoomin_unselected, ConfigableConstDecode.Resolution);

      this.zoomout_selected = Util.fromAsset(paramContext, "map_zoomout_button_selected.png");
      this.zoomout_selected_final = Util.zoomBitmap(this.zoomout_selected, ConfigableConstDecode.Resolution);
      
      this.zoomout_unselected = Util.fromAsset(paramContext, "map_zoomout_button_unselected.png");
      this.zoomout_unselected_final = Util.zoomBitmap(this.zoomout_unselected, ConfigableConstDecode.Resolution);
      
      this.zoomin_pressed = Util.fromAsset(paramContext, "map_zoomin_button_pressed.png");
      this.zoomin_pressed_final = Util.zoomBitmap(this.zoomin_pressed, ConfigableConstDecode.Resolution);

      this.zoomout_pressed = Util.fromAsset(paramContext, "map_zoomout_button_pressed.png");
      this.zoomout_pressed_final = Util.zoomBitmap(this.zoomout_pressed, ConfigableConstDecode.Resolution);
      
      this.zoomin = new ImageView(paramContext);
      this.zoomin.setImageBitmap(this.zoomin_selected_final);
      this.zoomin.setClickable(true);
      
      this.zoomout = new ImageView(paramContext);
      this.zoomout.setImageBitmap(this.zoomout_selected_final);
      this.zoomout.setClickable(true);
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "ZoomControllerView", "create");
      localThrowable.printStackTrace();
    }
    this.zoomin.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View view, MotionEvent event)
      {
//        if ((br.doInBackground(br.this).E() >= br.doInBackground(br.this).r()) || (!br.doInBackground(br.this).R())) {
//          return false;
//        }
    	  
    	  if (mMap.getZoomLevel() >= mMap.getMaxZoomLevel() || (!mMap.R())) {
				return false;
          }

        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
          //br.c(br.this).setImageBitmap(br.b(br.this));
        	zoomin.setImageBitmap(zoomin_pressed_final);
        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
//          br.c(br.this).setImageBitmap(br.a(br.this));
        	zoomin.setImageBitmap(zoomin_selected_final);
          try
          {
            //br.doInBackground(br.this).b(o.b());
            mMap.animateCamera(CameraUpdateFactoryDelegate.zoomIn());
          }
          catch (RemoteException v0)
          {
            SDKLogHandler.exception(v0, "ZoomControllerView", "zoomin ontouch");
            v0.printStackTrace();
          }
        }
        return false;
      }
    });
    this.zoomout.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View view, MotionEvent even)
      {
//        if ((br.doInBackground(br.this).E() <= br.doInBackground(br.this).s()) || (!br.doInBackground(br.this).R())) {
//          return false;
//        }
    	  
    	  if (mMap.getZoomLevel() <= mMap.getMinZoomLevel() || (!mMap.R())) {
				return false;
          }
        if (even.getAction() == MotionEvent.ACTION_DOWN)
        {
          //br.f(br.this).setImageBitmap(br.e(br.this));
        	zoomout.setImageBitmap(zoomout_pressed_final);
        }
        else if (even.getAction() == 1)
        {
          //br.f(br.this).setImageBitmap(br.g(br.this));
        	zoomout.setImageBitmap(zoomout_selected_final);
          try
          {
            //br.doInBackground(br.this).b(o.c());
            mMap.animateCamera(CameraUpdateFactoryDelegate.zoomOut());
          }
          catch (RemoteException v0)
          {
            SDKLogHandler.exception(v0, "ZoomControllerView", "zoomout ontouch");
            v0.printStackTrace();
          }
        }
        return false;
      }
    });
    this.zoomin.setPadding(0, 0, 20, -2);
    this.zoomout.setPadding(0, 0, 20, 20);
    setOrientation(VERTICAL);
    addView(this.zoomin);
    addView(this.zoomout);
  }
  
  public void setZoomBitmap(float zoomlevel)
  {
    if ((zoomlevel < this.mMap.getMaxZoomLevel()) && 
      (zoomlevel > this.mMap.getMinZoomLevel()))
    {
      this.zoomin.setImageBitmap(this.zoomin_selected_final);
      this.zoomout.setImageBitmap(this.zoomout_selected_final);
    }
    else if (zoomlevel == this.mMap.getMinZoomLevel())
    {
      this.zoomout.setImageBitmap(this.zoomout_unselected_final);
      this.zoomin.setImageBitmap(this.zoomin_selected_final);
    }
    else if (zoomlevel == this.mMap.getMaxZoomLevel())
    {
      this.zoomin.setImageBitmap(this.zoomin_unselected_final);
      this.zoomout.setImageBitmap(this.zoomout_selected_final);
    }
  }
}
