package com.unistrong.api.maps;

import com.unistrong.api.mapcore.GLESUtility;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.maps.model.LatLng;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapProjection;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 圆形绘制辅助类。
 */
public class ExtralDrawCircle extends ExtralBaseDraw{
    private static final int INCISION_PRECISION = 360;

    /**
     * 辅助类构造器。
     * @param mapView 地图实例
     */
    public ExtralDrawCircle(MapView mapView){
        super(mapView);
    }

    /**
     * 绘制圆。
     * @param gl 纹理ID.
     * @param floatBuffer 位置缓存。
     * @param center 中心点坐标，作用：当floatBuffer为null时，生成floatBuffer。
     * @param radius 半径，作用：当floatBuffer为null时，生成floatBuffer。
     * @param fillColor 填充颜色。
     * @param strokeColor 圆形边框颜色。
     * @param strokeWidth 线宽度。
     */
    public void drawCircle(GL10 gl, FloatBuffer floatBuffer, LatLng center, double radius, int fillColor, int strokeColor, float strokeWidth)
    {
        if ((center == null) || (radius <= 0.0D)) {
            return;
        }
        if (floatBuffer == null) {
            floatBuffer = calMapFPoint(center,radius);
        }
        if (floatBuffer != null) {
            GLESUtility.drawCircle(gl,fillColor, strokeColor,floatBuffer,strokeWidth,INCISION_PRECISION);
        }

    }


    /**
     * 计算位置，差值后生成坐标缓存。
     * @param center 圆形中心点坐标。
     * @param radius 圆形半径。
     */
    public FloatBuffer calMapFPoint(LatLng center, double radius)
    {
        if (center != null&&mapDelegate.get()!=null)
        {
            FPoint[] linePoints = new FPoint[INCISION_PRECISION];

            float[] lineVertexs = new float[3 * linePoints.length];
            double hypotenuse = MMapPointsPerMeter(center.latitude) * radius;

            IPoint centerMapPoint = new IPoint();
            MapProjection localMapProjection = mapDelegate.get().getMapProjection();
            MapProjection.lonlat2Geo(center.longitude, center.latitude, centerMapPoint);
            for (int i = 0; i < INCISION_PRECISION; i++)
            {
                double radian = i * Math.PI / 180.0D;
                double xOffset = Math.sin(radian) * hypotenuse;
                double yOffset = Math.cos(radian) * hypotenuse;

                int x = (int)(centerMapPoint.x + xOffset);
                int y = (int)(centerMapPoint.y + yOffset);
                FPoint resultPnt = new FPoint();
                localMapProjection.geo2Map(x, y, resultPnt);
                linePoints[i] = resultPnt;

                lineVertexs[(i * 3)] = linePoints[i].x;
                lineVertexs[(i * 3 + 1)] = linePoints[i].y;
                lineVertexs[(i * 3 + 2)] = 0.0F;
            }
            return Util.makeFloatBuffer(lineVertexs);
        }
        return null;
    }

    private double MMapPointsPerMeter(double paramDouble)
    {
        return 1.0D / b(paramDouble);
    }
    private static float m = 4.0075016E7F;
    private static int n = 256;
    private static int o = 20;
    private float b(double paramDouble)
    {
        return (float)(Math.cos(paramDouble * 3.141592653589793D / 180.0D) * m / (n << o));
    }
}
