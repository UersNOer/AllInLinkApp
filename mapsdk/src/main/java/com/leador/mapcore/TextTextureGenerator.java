package com.leador.mapcore;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.text.TextPaint;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

public class TextTextureGenerator
{
  static final int TEXT_FONTSIZE = 32;
  static final int TEXT_FONTSIZE_TRUE = 30;
  static final int AN_LABEL_MAXCHARINLINE = 7;
  static final int AN_LABEL_MULITYLINE_SPAN = 2;
  private static final int ALIGNCENTER = 51;
  private static final int ALIGNLEFT = 49;
  private static final int ALIGNRIGHT = 50;
  private float base_line = 0.0F;
  private float start_x = 0.0F;
  private Paint text_paint = null;
  
  public static int GetNearstSize2N(int paramInt)
  {
    int i = 1;
    for (;;)
    {
      if (paramInt <= i) {
        return i;
      }
      i *= 2;
    }
  }
  
  public TextTextureGenerator()
  {
    this.text_paint = newPaint(null, 30, 49);
    this.start_x = 0.0F;
    
    Paint.FontMetrics localFontMetrics = this.text_paint.getFontMetrics();
    
    float f = localFontMetrics.bottom - localFontMetrics.top;
    this.base_line = ((30.0F - f) / 2.0F - localFontMetrics.top);
  }
  
  public byte[] getTextPixelBuffer(int paramInt)
  {
    try
    {
      char[] arrayOfChar = new char[1];
      arrayOfChar[0] = ((char)paramInt);
      
      Bitmap localBitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ALPHA_8);
      
      Canvas localCanvas = new Canvas(localBitmap);
      
      byte[] arrayOfByte = new byte['Ѐ'];
      ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte);
      
      float f1 = this.text_paint.measureText(arrayOfChar[0] + "");
      
      Paint.Align localAlign = this.text_paint.getTextAlign();
      float f2 = f1 - 30.0F;
      if ((localAlign != Paint.Align.CENTER) && (f2 >= 4.0F))
      {
        this.text_paint.setTextAlign(Paint.Align.CENTER);
        this.text_paint.setTextSize(30.0F - f2);
        localCanvas.drawText(arrayOfChar, 0, 1, (30.0F - f2) / 2.0F, this.base_line, this.text_paint);
        
        this.text_paint.setTextAlign(localAlign);
      }
      else
      {
        localCanvas.drawText(arrayOfChar, 0, 1, this.start_x, this.base_line - 2.0F, this.text_paint);
      }
      localBitmap.copyPixelsToBuffer(localByteBuffer);
      
      localBitmap.recycle();
      localBitmap = null;
      localByteBuffer = null;
      return arrayOfByte;
    }
    catch (OutOfMemoryError localOutOfMemoryError) {}catch (Exception localException) {}
    return null;
  }
  
  public byte[] getCharsWidths(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    byte[] arrayOfByte = new byte[i];
    float[] arrayOfFloat = new float[1];
    for (int j = 0; j < i; j++)
    {
      arrayOfFloat[0] = this.text_paint.measureText((char)paramArrayOfInt[j] + "");
      
      arrayOfByte[j] = ((byte)(int)(arrayOfFloat[0] + 1.0F));
    }
    return arrayOfByte;
  }
  
  private static Paint newPaint(String paramString, int textSize, int align)
  {
    TextPaint textPaint = new TextPaint();
    textPaint.setColor(-1);
    textPaint.setTextSize(textSize);
    
    textPaint.setAntiAlias(true);
    textPaint.setFilterBitmap(true);
    
    textPaint.setTypeface(Typeface.DEFAULT);
    switch (align)
    {
    case 51: 
      textPaint.setTextAlign(Paint.Align.CENTER);
      break;
    case 49: 
      textPaint.setTextAlign(Paint.Align.LEFT);
      break;
    case 50: 
      textPaint.setTextAlign(Paint.Align.RIGHT);
      break;
    default: 
      textPaint.setTextAlign(Paint.Align.LEFT);
    }
    return textPaint;
  }
  
  public static float getFontlength(Paint paramPaint, String paramString)
  {
    return paramPaint.measureText(paramString);
  }
  
  public static float getFontHeight(Paint paramPaint)
  {
    Paint.FontMetrics localFontMetrics = paramPaint.getFontMetrics();
    return localFontMetrics.descent - localFontMetrics.ascent;
  }
  
  public static void generaAsccIITexturePng()
    throws Exception
  {
    if (!Environment.getExternalStorageState().equals("mounted")) {
      return;
    }
    File localFile1 = Environment.getExternalStorageDirectory();
    File localFile2 = new File(localFile1, "asccii.png");
    
    FileOutputStream localFileOutputStream = new FileOutputStream(localFile2);
    Paint localPaint = newPaint(null, 32, 49);
    Bitmap localBitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
    
    Paint.FontMetricsInt localFontMetricsInt = localPaint.getFontMetricsInt();
    Canvas localCanvas = new Canvas(localBitmap);
    localPaint = newPaint(null, 32, 49);
    float[] arrayOfFloat = new float[1];
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 16; j++)
      {
        char c = (char)(i * 16 + j);
        localCanvas.drawText(c + "", j * 16, i * 16 - localFontMetricsInt.ascent - 2, localPaint);
        
        localPaint.getTextWidths(c + "", arrayOfFloat);
      }
    }
    localBitmap.compress(Bitmap.CompressFormat.PNG, 100, localFileOutputStream);
    localBitmap.recycle();
    localBitmap = null;
  }
  
  public String getFontVersion()
  {
    int i = 39640;
    byte[] arrayOfByte = getTextPixelBuffer(i);
    return Md5Utility.getByteArrayMD5(arrayOfByte);
  }
}
