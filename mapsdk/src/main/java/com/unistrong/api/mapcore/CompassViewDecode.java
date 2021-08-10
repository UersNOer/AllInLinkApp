package com.unistrong.api.mapcore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.maps.model.CameraPosition;
import com.leador.mapcore.MapProjection;

/**
 * 指南针
 */
class CompassViewDecode
  extends LinearLayout
{
  Bitmap mCompass;
  Bitmap mCompass_pressed;
  Bitmap c;
  public ImageView mCompassView;
  IMapDelegate mMap;
  Bitmap mCustomBitmap;
  public void a()
  {
    try
    {
      if (this.mCompass != null) {
        this.mCompass.recycle();
      }
      if (this.mCompass_pressed != null) {
        this.mCompass_pressed.recycle();
      }
      if (this.c != null) {
        this.c.recycle();
      }
      this.c = null;
      this.mCompass = null;
      this.mCompass_pressed = null;
      this.mCompassView=null;
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "CompassView", "destroy");
      localThrowable.printStackTrace();
    }
  }
  
  public CompassViewDecode(Context paramContext)
  {
    super(paramContext);
  }
  
  public CompassViewDecode(Context context, MapMessageQueueDecode msg, IMapDelegate map)
  {
    super(context);
    this.mMap = map;
    try
    {
      this.c = Util.fromAsset(context, "map_compass.png");
      this.mCompass_pressed = Util.zoomBitmap(this.c, ConfigableConstDecode.Resolution * 0.8F);
      
      this.c = Util.zoomBitmap(this.c, ConfigableConstDecode.Resolution * 0.7F);
      this.mCompass = Bitmap.createBitmap(this.mCompass_pressed.getWidth(), this.mCompass_pressed
        .getHeight(), Bitmap.Config.ARGB_8888);
      
      Canvas localCanvas = new Canvas(this.mCompass);
      Paint p = new Paint();
      p.setAntiAlias(true);
      p.setFilterBitmap(true);
      localCanvas.drawBitmap(this.c, 
        (this.mCompass_pressed.getWidth() - this.c.getWidth()) / 2.0F, 
        (this.mCompass_pressed.getHeight() - this.c.getHeight()) / 2.0F, p);
      this.mCompassView = new ImageView(context);
      this.mCompassView.setScaleType(ImageView.ScaleType.MATRIX);
      this.mCompassView.setImageBitmap(this.mCompass);
      this.mCompassView.setClickable(true);
      invalidateAngle();
      this.mCompassView.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
        {
          try
          {
            if (!CompassViewDecode.this.mMap.R()) {
              return false;
            }
            if (paramAnonymousMotionEvent.getAction() == MotionEvent.ACTION_DOWN)
            {
              if(mCustomBitmap!=null)mCompassView.setImageBitmap(mCustomBitmap);
              else
              mCompassView.setImageBitmap(mCompass_pressed);
            }
            else if (paramAnonymousMotionEvent.getAction() == MotionEvent.ACTION_UP)
            {
              if(mCustomBitmap!=null)mCompassView.setImageBitmap(mCustomBitmap);
              else
              mCompassView.setImageBitmap(mCompass);
              
              CameraPosition localCameraPosition = CompassViewDecode.this.mMap.getCameraPosition();
              mMap.animateCamera(CameraUpdateFactoryDelegate.newCameraPosition(new CameraPosition(localCameraPosition.target, localCameraPosition.zoom, 0.0F, 0.0F)));
            }
          }
          catch (Throwable throwable)
          {
            SDKLogHandler.exception(throwable, "CompassView", "onTouch");
            throwable.printStackTrace();
          }
          return false;
        }
      });
      addView(this.mCompassView);
    }
    catch (Throwable throwable)
    {
      SDKLogHandler.exception(throwable, "CompassView", "create");
      throwable.printStackTrace();
    }
  }
  public void setmCompassViewBitmap(Bitmap bitmap){
    this.mCustomBitmap=bitmap;
    if(bitmap!=null)
    this.mCompassView.setImageBitmap(bitmap);
    else
      this.mCompassView.setImageBitmap(this.mCompass);
    invalidateAngle();
  }
  
  public void invalidateAngle() //b
  {
    try
    {
      MapProjection localMapProjection = this.mMap.getMapProjection();
      float f1 = localMapProjection.getMapAngle();
      float f2 = localMapProjection.getCameraHeaderAngle();
      Matrix localMatrix = new Matrix();
      localMatrix.postRotate(-f1, this.mCompassView.getDrawable().getBounds()
        .width() / 2.0F, this.mCompassView.getDrawable().getBounds()
        .height() / 2.0F);
      localMatrix.postScale(1.0F, (float)Math.cos(f2 * Math.PI / 180.0D), this.mCompassView
        .getDrawable().getBounds().width() / 2.0F, this.mCompassView
        .getDrawable().getBounds().height() / 2.0F);
      this.mCompassView.setImageMatrix(localMatrix);
    }
    catch (Throwable throwable)
    {
      SDKLogHandler.exception(throwable, "CompassView", "invalidateAngle");
      throwable.printStackTrace();
    }
  }
}
