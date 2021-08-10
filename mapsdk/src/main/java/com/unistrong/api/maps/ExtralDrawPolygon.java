package com.unistrong.api.maps;

import android.graphics.Color;
import android.graphics.PointF;

import com.unistrong.api.mapcore.IMapDelegate;
import com.unistrong.api.maps.model.LatLng;

import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * 自定义图层绘制Polygone辅助类。
 */
public class ExtralDrawPolygon {
    SoftReference<IMapDelegate> mapDelegate =null;
    SoftReference<MapController> map =null;

    /**
     * 构造器。
     * @param mapView 地图实例。
     */
    public ExtralDrawPolygon(MapView mapView){
        try{
            mapDelegate = new SoftReference<IMapDelegate>(mapView.getMapFragmentDelegate().getMapDelegate());
            map = new SoftReference<MapController>(mapView.getMap());
        }catch (Exception ex){

        }
    }
    /**
     * 生成坐标缓存。
     * @param fs 坐标数组。
     * @return 线坐标缓存。
     */
    private FloatBuffer makeFloatBuffer(float [] fs) {
        ByteBuffer bb = ByteBuffer.allocateDirect(fs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(fs);
        fb.position(0);
        return fb;
    }

    /**
     * 计算坐标缓存
     * @param latLngPolygon 面对象的顶点坐标集合。
     * @return 坐标缓存
     */
    public FloatBuffer calMapFPoint(ArrayList<LatLng> latLngPolygon) {

        if(map!=null&&map.get()!=null) {
            ////////////poly坐标计算////////////
            PointF[] polyPoints = new PointF[latLngPolygon.size()];
            float[] lineVertexs = new float[3 * latLngPolygon.size()];
            int i = 0;
            for (LatLng xy : latLngPolygon) {
                polyPoints[i] = map.get().getProjection().toOpenGLLocation(xy);
                lineVertexs[i * 3] = polyPoints[i].x;
                lineVertexs[i * 3 + 1] = polyPoints[i].y;
                lineVertexs[i * 3 + 2] = 0.0f;
                i++;
            }
            return makeFloatBuffer(lineVertexs);
        }
        return null;
    }

    /**
     * 计算矩形坐标缓存
     * @param point1 顶点坐标
     * @param point2 顶点坐标
     * @return 坐标缓存
     */
    public FloatBuffer calRectPoint(LatLng point1,LatLng point2){
        if(point1==null || point2==null){
            return null;
        }
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        list.add(point1);
        list.add(new LatLng(point1.latitude,point2.longitude));
        list.add(point2);
        list.add(new LatLng(point2.latitude,point1.longitude));
        return calMapFPoint(list);
    }
    /**
     * 绘制面形。
     * @param gl 纹理ID。
     * @param strokeColor 边框颜色。
     * @param fillColor 面填充颜色
     * @param lineVertexBuffer 顶点坐标缓存。
     * @param lineWidth 线宽。
     * @param pointSize 顶点数量。
     */
    public void drawPolygon(GL10 gl, int strokeColor,int fillColor ,FloatBuffer lineVertexBuffer, float lineWidth, int pointSize){
        drawPolyline(gl,strokeColor,lineVertexBuffer,lineWidth,pointSize);
        drawPolygon(gl,fillColor,lineVertexBuffer,lineWidth,pointSize);
    }
    /**
     * 绘制面形。
     * @param gl 纹理ID。
     * @param color 面形颜色。
     * @param lineVertexBuffer 顶点坐标缓存。
     * @param lineWidth 线宽。
     * @param pointSize 顶点数量。
     */
    void drawPolygon(GL10 gl, int color,
                              FloatBuffer lineVertexBuffer, float lineWidth, int pointSize) {

        if (lineWidth == 0) {
            return;
        }
        gl.glPushMatrix();
        gl.glColor4f(1, 1, 1, 1);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        float colorA = Color.alpha(color) / 255f;
        float colorR = Color.red(color) / 255f;
        float colorG = Color.green(color) / 255f;
        float colorB = Color.blue(color) / 255f;

        // 绘线
        gl.glEnable(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVertexBuffer);
        gl.glColor4f(colorR, colorG, colorB, colorA);
        gl.glLineWidth(lineWidth);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, pointSize);
        gl.glDisable(GL10.GL_VERTEX_ARRAY);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glPopMatrix();
        gl.glFlush();

    }
    /**
     * 绘制边框。
     * @param gl GL10实例
     * @param color 线的颜色
     * @param lineVertexBuffer 线坐标buffer
     * @param lineWidth 线宽
     * @param pointSize 点数量
     */
    void drawPolyline(GL10 gl, int color,
                              FloatBuffer lineVertexBuffer, float lineWidth, int pointSize) {
        if (lineWidth == 0) {
            return;
        }
        gl.glPushMatrix();
        gl.glColor4f(1, 1, 1, 1);
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        float colorA = Color.alpha(color) / 255f;
        float colorR = Color.red(color) / 255f;
        float colorG = Color.green(color) / 255f;
        float colorB = Color.blue(color) / 255f;
        int lineType = GL10.GL_LINE_LOOP;

        gl.glEnable(lineType);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVertexBuffer);
        gl.glColor4f(colorR, colorG, colorB, colorA);
        gl.glLineWidth(lineWidth);

        gl.glDrawArrays(lineType, 0, pointSize);
        gl.glDisable(lineType);

        gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_NICEST);
        float size = lineWidth;
        if (size >= 10) {
            size = 6;
        } else if (size >= 5) {
            size -= 2;
        } else if (size >= 2) {
            size -= 1;
        }
        gl.glColor4f(colorR, colorG, colorB, colorA / 4);
        gl.glPointSize(size);
        gl.glDrawArrays(GL10.GL_POINTS, 1, pointSize - 1);
        gl.glDisable(GL10.GL_POINT_SMOOTH);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glPopMatrix();
        gl.glFlush();

    }
}
