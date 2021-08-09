package com.unistrong.api.maps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.maps.model.LatLng;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 自定义图层绘制Text辅助类。
 */
public class ExtralDrawText extends ExtralBaseDraw {
    public ExtralDrawText(MapView mapView){
        super(mapView);
    }

    /**
     * 文字转换成图片。
     * @param text 文字。
     * @param paint 画笔。
     * @param backgroundColor 背景颜色。
     * @return 图片。
     */
    public Bitmap createTextBitmap(String text, Paint paint, int backgroundColor)
    {
        if ((text == null) || (text.trim().length() <= 0)) {
            return null;
        }
        try
        {
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            int height = (int)(fontMetrics.descent - fontMetrics.ascent);
            int width = (int)((height - fontMetrics.bottom - fontMetrics.top) / 2.0F);
            Rect rect = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect);
            Bitmap bitmap = Bitmap.createBitmap(rect.width() + 6, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(backgroundColor);
            canvas.drawText(text, rect.centerX() + 3, width, paint);
            return bitmap;
        }
        catch (Throwable throwable)
        {
            SDKLogHandler.exception(throwable, "TextDelegateImp", "initBitmap");
        }
        return null;
    }

    /**
     * 绘制图片到GL上。
     * @param gl GL10实例。
     * @param bitmap 需要绘制的图片。
     * @param flag 是否平贴地皮
     * @param latlng 坐标。
     * @param textureId 纹理ID。
     * @param coordBuffer 纹理映射坐标缓存。
     * @param rotate - Marker图片旋转的角度，从正北开始，逆时针计算。
     * @param anchorU 锚点水平范围的比例。
     * @param anchorV 锚点垂直范围的比例。
     * @return 纹理ID。
     */
    public int drawBitmap(GL10 gl, Bitmap bitmap, boolean flag, LatLng latlng , int textureId, FloatBuffer coordBuffer, float rotate, float anchorU, float anchorV){
        if(textureId == 0){
            textureId = getTexsureId(gl,textureId);
            bindTexture(gl,textureId,bitmap);
        }
        PointF pointT = null;
        try{
            pointT = map.get().getProjection().toOpenGLLocation(latlng);//转换为GL坐标
        }catch(Exception ex){

        }
        FloatBuffer pointBuffer = makeFloatBuffer(flag,pointT,bitmap,rotate,anchorU,anchorV);
        drawBitmap(textureId,pointBuffer,coordBuffer);
        return textureId;
    }

    /**
     * 生成文本顶点坐标缓存。
     * @return 坐标缓存。
     */
    public FloatBuffer getTextFloatBuffer(){
        return Util.makeFloatBuffer(new float[] { 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F });
    }
    /**
     * 生成float缓存。
     * @param flat 是否平贴地图。
     * @param glPoint 坐标。
     * @param bitmap 纹理。
     * @param rotate - Marker图片旋转的角度，从正北开始，逆时针计算。
     * @param anchorU 锚点水平范围的比例。
     * @param anchorV 锚点垂直范围的比例。
     * @return 缓存。
     */
     FloatBuffer makeFloatBuffer(boolean flat, PointF glPoint, Bitmap bitmap,float rotate,float anchorU, float anchorV) {
        float [] fs = this.makeFloatArray(flat,glPoint,bitmap,rotate,anchorU,anchorV);
        ByteBuffer bb = ByteBuffer.allocateDirect(fs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(fs);
        fb.position(0);
        return fb;
    }

    /**
     * 获取纹理映射坐标。
     * @param flat 是否平贴在地图上。
     * @param glPoint 坐标位置。
     * @param bitmap 纹理。
     * @param rotate - 图片旋转的角度，从正北开始，逆时针计算。
     * @param anchorU 锚点水平范围的比例。
     * @param anchorV 锚点垂直范围的比例。
     * @return 纹理坐标缓存。
     */
    private float [] makeFloatArray(boolean flat, PointF glPoint, Bitmap bitmap ,float rotate,float anchorU, float anchorV){
        try{
            if (mapDelegate.get()!=null) {
                float rotateAngle = 0;
                if(rotate!=0){
                    rotateAngle = ((-rotate % 360.0F + 360.0F) % 360.0F);
                }
                return Util.makeFloatArray(mapDelegate.get(), flat ? 1 : 0, glPoint,rotateAngle, bitmap.getWidth(), bitmap.getHeight(),anchorU,anchorV);
            }
        }catch(Exception ex){
            return null;
        }
        return null;
    }
}
