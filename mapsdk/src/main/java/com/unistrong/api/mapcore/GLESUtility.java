package com.unistrong.api.mapcore;

import android.graphics.Color;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class GLESUtility
{
  private static void c(GL10 gl, int paramInt1, int color, FloatBuffer pointBuffer, float paramFloat, int pointSize)
  {
    if (paramFloat == 0.0F) {
      return;
    }
    gl.glPushMatrix();
    gl.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    gl.glEnable(3042);
    gl.glDisable(2929);
    gl.glBlendFunc(770, 771);
    gl.glDisable(3553);
    
    gl.glEnableClientState(32884);
    
    float f1 = Color.alpha(color) / 255.0F;
    float f2 = Color.red(color) / 255.0F;
    float f3 = Color.green(color) / 255.0F;
    float f4 = Color.blue(color) / 255.0F;
    
    gl.glEnable(32925);
    
    gl.glLineWidth(paramFloat);
    gl.glVertexPointer(3, 5126, 0, pointBuffer);
    gl.glColor4f(f2, f3, f4, f1);
    gl.glDrawArrays(paramInt1, 0, pointSize);
    
    gl.glDisable(32925);
    
    gl.glEnable(2832);
    gl.glHint(3153, 4354);
    float f5 = paramFloat;
    if (f5 >= 10.0F) {
      f5 = 6.0F;
    } else if (f5 >= 5.0F) {
      f5 -= 2.0F;
    } else if (f5 >= 2.0F) {
      f5 -= 1.0F;
    }
    gl.glColor4f(f2, f3, f4, f1 / 4.0F);
    gl.glPointSize(f5);
    gl.glDrawArrays(0, 1, pointSize - 2);
    gl.glDisable(2832);
    
    gl.glDisableClientState(32884);
    gl.glDisableClientState(32888);
    gl.glDisable(3042);
    gl.glPopMatrix();
  }
  
  public static void a(GL10 gl, int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer, float paramFloat, int paramInt3)
  {
    c(gl, 3, paramInt2, paramFloatBuffer, paramFloat, paramInt3);
  }
  
  public static void a(GL10 gl, int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer1, float paramFloat, FloatBuffer paramFloatBuffer2, int paramInt3, int paramInt4)
  {
    c(gl, 4, paramInt1, paramFloatBuffer2, 1.0F, paramInt4);
    
    c(gl, 2, paramInt2, paramFloatBuffer1, paramFloat, paramInt3);
  }
  
  public static void drawCircle(GL10 gl, int fillColor, int strokeColor, FloatBuffer pointBuffer, float strokeWidth, int pointSize)
  {
    c(gl, 6, fillColor, pointBuffer, 1.0F, pointSize);
    
    c(gl, 2, strokeColor, pointBuffer, strokeWidth, pointSize);
  }
}
