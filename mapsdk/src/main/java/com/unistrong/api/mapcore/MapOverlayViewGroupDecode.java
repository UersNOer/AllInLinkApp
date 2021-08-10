package com.unistrong.api.mapcore;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;

class MapOverlayViewGroupDecode
  extends ViewGroup
{
  private IMapDelegate map;
  public MapOverlayViewGroupDecode(Context context)
  {
    super(context);
  }
  
  public MapOverlayViewGroupDecode(Context context, IMapDelegate map)
  {
    super(context);
    this.map = map;
    setBackgroundColor(Color.WHITE);
  }
  
  public static class LayoutParamsExt
    extends ViewGroup.LayoutParams
  {
	  public static final int MODE_MAP = 0;
		public static final int MODE_VIEW = 1;
		public static final int LEFT = 3;
		public static final int RIGHT = 5;
		public static final int TOP = 48;
		public static final int BOTTOM = 80;
		public static final int CENTER_HORIZONTAL = 1;
		public static final int CENTER_VERTICAL = 16;
		public static final int CENTER = 17;
		public static final int TOP_LEFT = 51;
		public static final int BOTTOM_CENTER = 81;
		
    public FPoint point = null;
    public int mapx = 0;
    public int mapy = 0;
    public int alignment = TOP_LEFT;
    
    public LayoutParamsExt(int width, int height, FPoint point, int x, int y, int alignment)
    {
      super(width, height);
      this.point = point;
      this.mapx = x;
      this.mapy = y;
      this.alignment = alignment;
    }
    
    public LayoutParamsExt(Context context, AttributeSet attributeset)
    {
    	super(context, attributeset);
    }
    
    public LayoutParamsExt(ViewGroup.LayoutParams layoutparams)
    {
      //super();
    	super(layoutparams);
    }
  }
  
  @Override
  protected void onLayout(boolean change, int l, int t, int r, int b)
  {
    int iViewChildCount = getChildCount();
    for (int j = 0; j < iViewChildCount; j++)
    {
      View view = getChildAt(j);
      if (view != null) {
        if ((view.getLayoutParams() instanceof LayoutParamsExt))
        {
          LayoutParamsExt param = (LayoutParamsExt)view.getLayoutParams();
          layoutMap(view, param);
        }
        else
        {
          layoutView(view, view.getLayoutParams());
        }
      }
    }
  }
  
  private void layoutView(View view, ViewGroup.LayoutParams layoutParams)
  {
    int[] arrayOfInt = new int[2];
    doMeasure(view, layoutParams.width, layoutParams.height, arrayOfInt);
    //TODO WCB 屏蔽室内图
//    if ((paramView instanceof InndoorViewDecode)) {
//      doLayout(paramView, arrayOfInt[0], arrayOfInt[1], 20, this.map.I().y - 80 - arrayOfInt[1], 51);
//    } else {
      doLayout(view, arrayOfInt[0], arrayOfInt[1], 0, 0, 51);
//    }
  }
  
  private void layoutMap(View view, LayoutParamsExt layoutParamsExt)
  {
    int[] arrayOfInt = new int[2];
    doMeasure(view, layoutParamsExt.width, layoutParamsExt.height, arrayOfInt);
    if ((view instanceof ZoomControllerViewDecode))//绘制缩放按钮
    {
      doLayout(view, arrayOfInt[0], arrayOfInt[1], getWidth() - arrayOfInt[0],
        getHeight(), layoutParamsExt.alignment);
    }
    else if ((view instanceof LocationViewDecode))
    {
      doLayout(view, arrayOfInt[0], arrayOfInt[1], getWidth() - arrayOfInt[0], arrayOfInt[1], layoutParamsExt.alignment);
    }
    else if ((view instanceof CompassViewDecode))//绘制指南针
    {
      doLayout(view, arrayOfInt[0], arrayOfInt[1], layoutParamsExt.mapx,layoutParamsExt.mapy, layoutParamsExt.alignment);
    }
    else if (layoutParamsExt.point != null)
    {
      IPoint localIPoint = new IPoint();
      this.map.getMapProjection().map2Win(layoutParamsExt.point.x, layoutParamsExt.point.y, localIPoint);
      
      localIPoint.x += layoutParamsExt.mapx;
      localIPoint.y += layoutParamsExt.mapy;
      doLayout(view, arrayOfInt[0], arrayOfInt[1], localIPoint.x, localIPoint.y, layoutParamsExt.alignment);
      if (view.getVisibility() == View.VISIBLE) {
        a();
      }
    }
  }
  
  protected void a() {}
  
  private void doMeasure(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    if ((paramView instanceof ListView))
    {
      View localView = (View)paramView.getParent();
      if (localView != null)
      {
        paramArrayOfInt[0] = localView.getWidth();
        paramArrayOfInt[1] = localView.getHeight();
      }
    }
    if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
      paramView.measure(0, 0);
    }
    if (paramInt1 == -2) {
      paramArrayOfInt[0] = paramView.getMeasuredWidth();
    } else if (paramInt1 == -1) {
      paramArrayOfInt[0] = getMeasuredWidth();
    } else {
      paramArrayOfInt[0] = paramInt1;
    }
    if (paramInt2 == -2) {
      paramArrayOfInt[1] = paramView.getMeasuredHeight();
    } else if (paramInt2 == -1) {
      paramArrayOfInt[1] = getMeasuredHeight();
    } else {
      paramArrayOfInt[1] = paramInt2;
    }
  }
  
  private void doLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i = 7;
    int j = 112;
    int k = paramInt5 & 0x7;
    int m = paramInt5 & 0x70;
    if (k == LayoutParamsExt.RIGHT) {
      paramInt3 -= paramInt1;
    } else if (k == LayoutParamsExt.CENTER_HORIZONTAL) {
      paramInt3 -= paramInt1 / 2;
    }
    if (m == LayoutParamsExt.BOTTOM) {
      paramInt4 -= paramInt2;
    } else if (m == LayoutParamsExt.CENTER) {
      paramInt4 -= paramInt2 / 2;
    } else if (m == LayoutParamsExt.CENTER_VERTICAL) {
      paramInt4 = paramInt4 / 2 - paramInt2 / 2;
    }
    paramView.layout(paramInt3, paramInt4, paramInt3 + paramInt1, paramInt4 + paramInt2);
  }

}
