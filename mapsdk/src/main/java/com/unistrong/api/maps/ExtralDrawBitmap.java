package com.unistrong.api.maps;

import android.graphics.PointF;
import android.graphics.Rect;

import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.LatLng;
import com.leador.mapcore.IPoint;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 自定义图层绘制图片辅助类。
 */
public class ExtralDrawBitmap extends ExtralBaseDraw {
    public ExtralDrawBitmap(MapView mapView){
        super(mapView);
    }

    /**
     * 绘制图片。
     * @param gl GL10实例。
     * @param bitmap 需要绘制的图片。
     * @param flag 是否贴地。
     * @param latlng 坐标。
     * @param textureId 纹理ID。
     * @param coordBuffer 纹理顶点坐标缓存。
     * @param rotate 图片旋转的角度，从正北开始，逆时针计算。
     * @param anchorU 锚点水平范围的比例。
     * @param anchorV 锚点垂直范围的比例。
     * @return 纹理ID.
     */
    public int drawBitmap(GL10 gl, BitmapDescriptor bitmap, boolean flag, LatLng latlng, int textureId, FloatBuffer coordBuffer, float rotate, float anchorU, float anchorV){
        if(textureId==0){
            textureId = getTexsureId(gl,textureId);
            bindTexture(gl,textureId,bitmap.getBitmap());
        }
        PointF pointT = null;
        try{
            pointT = map.get().getProjection().toOpenGLLocation(latlng);//转换为GL坐标
        }catch(Exception ex){

        }
        FloatBuffer pointBuffer = makeFloatBuffer(flag,pointT,bitmap, rotate,anchorU,anchorV);
        drawBitmap(textureId,pointBuffer,coordBuffer);
        return textureId;
    }

    /**
     * 生成纹理顶点坐标缓存。
     * @param bitmapDes 纹理。
     * @return 定点坐标缓存。
     */
    public static FloatBuffer makeBitmapFloatBuffer(BitmapDescriptor bitmapDes ){

        if (bitmapDes == null) {
            return null;
        }
        int i2 = bitmapDes.getWidth();
        int i3 = bitmapDes.getHeight();

        int i5 = bitmapDes.getBitmap().getHeight();
        int i6 = bitmapDes.getBitmap().getWidth();
        float f1 = ((float)i2) / ((float)i6);
        float f2 = ((float)i3) / ((float)i5);
        return Util.makeFloatBuffer(new float[] { 0.0F, f2, f1, f2, f1, 0.0F, 0.0F, 0.0F });
    }
    /**
     * 生成纹理映射坐标缓存。
     * @param flat 是否平贴地图。
     * @param glPoint 坐标。
     * @param bitmap 纹理。
     * @param anchorU 锚点水平范围的比例。
     * @param anchorV 锚点垂直范围的比例。
     * @return 坐标缓存结果。
     */
    public FloatBuffer makeFloatBuffer(boolean flat, PointF glPoint, BitmapDescriptor bitmap,float rotate,float anchorU, float anchorV) {
        float [] fs = this.makeFloatArray(flat,glPoint,bitmap, rotate,anchorU,anchorV);
        ByteBuffer bb = ByteBuffer.allocateDirect(fs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(fs);
        fb.position(0);
        return fb;
    }

    /**
     * 生成映射坐标缓存。
     * @param flat 是否平贴在地图上。
     * @param glPoint GL坐标
     * @param bitmap 纹理
     * @param buffer 已有映射坐标缓存。
     * @param anchorU 锚点水平范围的比例。
     * @param anchorV 锚点垂直范围的比例。
     * @return 映射坐标缓存
     */
    public FloatBuffer makeFloatBuffer(boolean flat, PointF glPoint, BitmapDescriptor bitmap,FloatBuffer buffer,float rotate,float anchorU, float anchorV)
    {
        try {
            float [] fs = this.makeFloatArray(flat,glPoint,bitmap, rotate,anchorU,anchorV);
            buffer.clear();
            buffer.put(fs);
            buffer.position(0);
            return buffer;
        } catch (Throwable throwable) {
            SDKLogHandler.exception(throwable, "Util", "makeFloatBuffer2");
            throwable.printStackTrace();
        }
        return null;
    }
    /**
     * 生成纹理映射坐标。
     * @param flat 是否平贴在地图上。
     * @param glPoint 坐标位置。
     * @param bitmap 纹理。
     * @param anchorU 锚点水平范围的比例。
     * @param anchorV 锚点垂直范围的比例。
     * @return
     */
    private float [] makeFloatArray(boolean flat, PointF glPoint, BitmapDescriptor bitmap,float rotate,float anchorU, float anchorV){
        try{
            if (mapDelegate.get()!=null){
                float rotateAngle = 0;
                if(rotate!=0){
                    rotateAngle = ((-rotate % 360.0F + 360.0F) % 360.0F);
                }
                return Util.makeFloatArray(mapDelegate.get(),flat ? 1 : 0,glPoint,rotateAngle,bitmap.getWidth(),bitmap.getHeight(),anchorU,anchorV);
            }
        }catch(Exception ex){
            return null;
        }
        return null;
    }

    /**
     * 点是否在屏幕内
     * @param lonlatPoint 点坐标
     * @return true 在屏幕内，false 不在屏幕内
     */
    public boolean isInScreen( LatLng lonlatPoint)
    {
        if (mapDelegate.get()==null){
            return false;
        }
        Rect localRect = mapDelegate.get().getRect();
        IPoint localIPoint = new IPoint();
        mapDelegate.get().getLatLng2Pixel(lonlatPoint.latitude,lonlatPoint.longitude, localIPoint);
        return localRect.contains(localIPoint.x, localIPoint.y);
    }
}
