package com.unistrong.api.maps;

import com.unistrong.api.maps.model.LatLng;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.NativeLineRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * 自定义图层绘制Polyline辅助类。
 */
public class ExtralDrawPolyline extends ExtralBaseDraw{

    public ExtralDrawPolyline(MapView mapView){
        super(mapView);
    }
    /**
     * 生成坐标缓存。
     * @param fs 坐标数组。
     * @return 线坐标缓存。
     */
    public FloatBuffer makeFloatBuffer(float [] fs) {
        ByteBuffer bb = ByteBuffer.allocateDirect(fs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(fs);
        fb.position(0);
        return fb;
    }

    /**
     * 绘制圆弧。
     * @param gl GL10实例。
     * @param pointList 点坐标集合
     * @param linePoint 点坐标集合。
     * @param width 线宽。
     * @param strokeColor 线颜色。
     * @param isDottedLine 是否虚线。
     */
    public void drawPolyline(GL10 gl, ArrayList<LatLng> pointList, int linePoint [], int width, int strokeColor, boolean isDottedLine)
    {

        if (linePoint == null && pointList.size()>1) {
            linePoint = calMapFPoint(pointList);
        }
        if (linePoint != null)
        {

            long stateInstance = this.mapDelegate.get().getMapProjection().getInstanceHandle();
            //TODO 需要在创建纹理时初始化本信息
            int texOrgPixelLen = 32;
            float texLen = this.mapDelegate.get().getMapProjection().getMapLenWithWin(texOrgPixelLen); //系统默认的纹理像素宽高都是32像素
            int texid = 0;
            if(isDottedLine){
                texid = this.mapDelegate.get().getDottedLineTextureID(); //获得系统默认的虚线纹理
            }else{
                texid = this.mapDelegate.get().getLineTextureID(); //获得系统默认的纹理
            }
            float lineWidth = this.mapDelegate.get().getMapProjection().getMapLenWithWin((int)width);

            boolean usecolor = true; //使用颜色替换纹理中的颜色
            boolean complexTex = false; //简单纹理


            NativeLineRenderer.nativeDrawLineByTextureID(linePoint, linePoint.length, stateInstance, texLen, lineWidth, texid, strokeColor, complexTex, usecolor);

        }
    }

    /**
     * 坐标计算转换。
     * @param pointList 线坐标。
     * @return 转换结果。
     */
    public int [] calMapFPoint(ArrayList<LatLng> pointList) {
        int size = pointList.size();
        int pointArray[] = new int[size];
        FPoint[] glFPoint = new FPoint[size];
        pointArray = new int[2 * glFPoint.length];
        for (int i2 = 0; i2 < size; i2++) {

            LatLng point  = pointList.get(i2);
            IPoint pointTemp = new IPoint();
            this.mapDelegate.get().latlon2Geo(point.latitude, point.longitude, pointTemp);
            pointArray[i2 * 2] = pointTemp.x;
            pointArray[i2 * 2 + 1] = pointTemp.y;
        }
        return pointArray;
    }

}
