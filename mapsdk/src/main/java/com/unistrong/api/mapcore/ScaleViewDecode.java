package com.unistrong.api.mapcore;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

class ScaleViewDecode
  extends View
{
  private String mScaleText = ""; // a
  private int mScaleLength = 0; //b
  private MapDelegateImp mMapView; //c
  private Paint mPaint; // d
  private Paint mTextPanit; //e
  private Rect bounds; //f
  private boolean customPosition;
  private int y;
  private int x;
  private int margin=5;

  public void a() // a
  {
    this.mPaint = null;
    this.mTextPanit = null;
    this.bounds = null;
    this.mScaleText = null;
  }
  
  public ScaleViewDecode(Context paramContext)
  {
    super(paramContext);
  }
  
  public ScaleViewDecode(Context paramContext, MapDelegateImp mapDelegateImp)
  {
    super(paramContext);
    this.mMapView = mapDelegateImp;
    this.mPaint = new Paint();
    this.bounds = new Rect();
    this.mPaint.setAntiAlias(true);
    this.mPaint.setColor(-16777216);
    this.mPaint.setStrokeWidth(2.0F * ConfigableConstDecode.Resolution);
    this.mPaint.setStyle(Paint.Style.STROKE);
    this.mTextPanit = new Paint();
    this.mTextPanit.setAntiAlias(true);
    this.mTextPanit.setColor(-16777216);
    this.mTextPanit.setTextSize(20.0F * ConfigableConstDecode.Resolution);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    if ((this.mScaleText == null) || (this.mScaleText.equals("")) || (this.mScaleLength == 0)) {
      return;
    }
    Point localPoint = this.mMapView.I();//logo位置
    if (localPoint == null) {
      return;
    }
    int pointX=localPoint.x;
    int pointY=localPoint.y;
    this.mTextPanit.getTextBounds(this.mScaleText, 0, this.mScaleText.length(), this.bounds);
    if(customPosition){
      pointX=this.x;
      pointY=this.y;
    }
    int i = pointX;
    int j = pointY-this.bounds.height()-margin ;
    if(customPosition)
       j = pointY+this.bounds.height()+margin ;
    if( j>=getHeight()){
      j=getHeight()-this.bounds.height()-margin;
    }
    if(i+this.mScaleLength>=getWidth()-margin){
      i=getWidth()-this.mScaleLength-margin;
    }
    paramCanvas.drawText(this.mScaleText, i, j, this.mTextPanit);
    j += this.bounds.height() - margin;
    paramCanvas.drawLine(i, j - 2, i, j + 2, this.mPaint);
    paramCanvas.drawLine(i, j, i + this.mScaleLength, j, this.mPaint);
    paramCanvas.drawLine(i + this.mScaleLength, j - 2, i + this.mScaleLength, j + 2, this.mPaint);
  }
  
  public void setScaleViewPosition(int x,int y){
    this.x=x;
    this.y=y;
    this.customPosition=true;
  }
  public void setScaleText(String paramString) // a
  {
    this.mScaleText = paramString;
  }
  
  public void setScaleLength(int paramInt) // a
  {
    this.mScaleLength = paramInt;
  }
}
