package com.unistrong.api.maps;

import android.os.RemoteException;

import com.unistrong.api.maps.model.LatLng;
import com.leador.mapcore.DPoint;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapProjection;
import com.leador.mapcore.NativeLineRenderer;

import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;

/**
 * 绘制圆弧辅助类。
 */
public class ExtralDrawArc extends ExtralBaseDraw{

    private float[] glPointArray = null; //gl坐标数组
    private double q = 0.0D;
    private double r = 0.0D;
    private double s = 0.0D;
    ExtralDrawPolyline drawline = null;

    /**
     * 圆弧绘制辅助类。
     * @param mapView 地图实例。
     */
    public ExtralDrawArc(MapView mapView)
    {
        super(mapView);
        drawline = new ExtralDrawPolyline(mapView);
    }


    /**
     * 计算坐标。
     * @param start 起点。
     * @param passed 途经点。
     * @param end 终点。
     * @return 计算圆弧插值结果。
     */
    public int [] calMapFPoint(LatLng start, LatLng passed, LatLng end)
    {
        if ((start == null) || (passed == null) || (end == null)) {
            return null;
        }
        int pointArray[]=null;
        int pointArraySize = 0;
        try
        {
            MapProjection localMapProjection = this.mapDelegate.get().getMapProjection();
            if (!l(start,passed,end))
            {
                int i1 = 3;
                FPoint[] arrayOfFPoint = new FPoint[i1];
                this.glPointArray = new float[i1 * arrayOfFPoint.length];
                FPoint localObject = new FPoint();
                this.mapDelegate.get().getLatLng2Map(start.latitude, start.longitude, (FPoint) localObject);

                arrayOfFPoint[0] = localObject;
                FPoint localFPoint1 = new FPoint();
                this.mapDelegate.get().getLatLng2Map(passed.latitude,passed.longitude, localFPoint1);

                arrayOfFPoint[1] = localFPoint1;
                FPoint localFPoint2 = new FPoint();
                this.mapDelegate.get().getLatLng2Map(end.latitude,end.longitude, localFPoint2);

                arrayOfFPoint[2] = localFPoint2;
                for (int i2 = 0; i2 < i1; i2++)
                {
                    this.glPointArray[(i2 * 3)] = arrayOfFPoint[i2].x;
                    this.glPointArray[(i2 * 3 + 1)] = arrayOfFPoint[i2].y;
                    this.glPointArray[(i2 * 3 + 2)] = 0.0F;
                }

                //初始化20级像素坐标
                IPoint[] arrayOfIPoint = new IPoint[i1];
                pointArray = new int[2 * arrayOfIPoint.length];
                IPoint pointTemp = new IPoint();
                this.mapDelegate.get().latlon2Geo(start.latitude,start.longitude, pointTemp);
                arrayOfIPoint[0] = pointTemp;

                this.mapDelegate.get().latlon2Geo(passed.latitude,passed.longitude, pointTemp);
                arrayOfIPoint[1] = pointTemp;

                this.mapDelegate.get().latlon2Geo(end.latitude,end.longitude, pointTemp);
                arrayOfIPoint[2] = pointTemp;

                for (int i2 = 0; i2 < i1; i2++)
                {
                    this.glPointArray[(i2 * 2)] = arrayOfIPoint[i2].x;
                    this.glPointArray[(i2 * 2 + 1)] = arrayOfIPoint[i2].y;
                }

                pointArraySize = arrayOfFPoint.length;
            }
            else {

                Object localObject = m(start,passed,end);
                int i1 = (int) (Math.abs(this.s - this.r) * 180.0D / Math.PI);
                double d1 = (this.s - this.r) / i1;
                FPoint[] arrayOfFPoint = new FPoint[i1 + 1];
                this.glPointArray = new float[3 * arrayOfFPoint.length];
                pointArray = new int[2 * arrayOfFPoint.length];
                for (int i2 = 0; i2 <= i1; i2++) {
                    if (i2 == i1) {
                        FPoint fPoint = new FPoint();
                        this.mapDelegate.get().getLatLng2Map(end.latitude,end.longitude, fPoint);

                        arrayOfFPoint[i2] = fPoint;

                        IPoint pointTemp = new IPoint();
                        this.mapDelegate.get().latlon2Geo(end.latitude,end.longitude, pointTemp);
                        pointArray[i2 * 2] = pointTemp.x;
                        pointArray[i2 * 2 + 1] = pointTemp.y;

                        this.glPointArray[(i2 * 3)] = arrayOfFPoint[i2].x;
                        this.glPointArray[(i2 * 3 + 1)] = arrayOfFPoint[i2].y;
                        this.glPointArray[(i2 * 3 + 2)] = 0.0F;

                        continue;
                    } else {
                        arrayOfFPoint[i2] = a(localMapProjection, this.r + i2 * d1, ((DPoint) localObject).x, ((DPoint) localObject).y);
                    }


                    this.glPointArray[(i2 * 3)] = arrayOfFPoint[i2].x;
                    this.glPointArray[(i2 * 3 + 1)] = arrayOfFPoint[i2].y;
                    this.glPointArray[(i2 * 3 + 2)] = 0.0F;

                    IPoint value = aaaaa(this.r + i2 * d1, ((DPoint) localObject).x, ((DPoint) localObject).y);
                    pointArray[i2 * 2] = value.x;
                    pointArray[i2 * 2 + 1] = value.y;
                }

                pointArraySize = arrayOfFPoint.length;
            }

            //对最终的坐标点进行插值
            ArrayList<IPoint> desPoints = new ArrayList<IPoint>();
            desPoints.add(new IPoint(pointArray[0],pointArray[1]));
            for( int i = 1 ; i < pointArraySize ; i ++)
            {
                insert1(pointArray[i*2-2], pointArray[i*2-1],pointArray[i*2],pointArray[i*2+1], desPoints);
            }

            pointArray = new int[desPoints.size() * 2];
            for( int i = 0 ; i < desPoints.size() ; i ++ ){
                pointArray[i*2] = desPoints.get(i).x;
                pointArray[i*2+1] = desPoints.get(i).y;
            }
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return pointArray;
    }

    private FPoint a(MapProjection paramMapProjection, double paramDouble1, double paramDouble2, double paramDouble3)
    {
        double d1 = Math.cos(paramDouble1) * this.q;
        double d2 = -Math.sin(paramDouble1) * this.q;

        int i1 = (int)(paramDouble2 + d1);
        int i2 = (int)(paramDouble3 + d2);
        FPoint localFPoint = new FPoint();
        paramMapProjection.geo2Map(i1, i2, localFPoint);

        return localFPoint;
    }

    private IPoint aaaaa(double paramDouble1, double paramDouble2, double paramDouble3)
    {
        double d1 = Math.cos(paramDouble1) * this.q;
        double d2 = -Math.sin(paramDouble1) * this.q;

        int i1 = (int)(paramDouble2 + d1);
        int i2 = (int)(paramDouble3 + d2);
        IPoint localFPoint = new IPoint();
        localFPoint.x = i1;
        localFPoint.y = i2;

        return localFPoint;
    }

    private boolean l(LatLng start,LatLng passed,LatLng end)
    {
        double d1 = (start.latitude - passed.latitude) * (passed.longitude - end.longitude) - (start.longitude -passed.longitude) * (passed.latitude - end.latitude);
        if (Math.abs(d1) < 1.0E-6D) {
            return false;
        }
        return true;
    }

    private DPoint m(LatLng start,LatLng passed,LatLng end)
    {
        IPoint localIPoint1 = new IPoint();
        this.mapDelegate.get().latlon2Geo(start.latitude, start.longitude, localIPoint1);
        IPoint localIPoint2 = new IPoint();
        this.mapDelegate.get().latlon2Geo(passed.latitude, passed.longitude, localIPoint2);

        IPoint localIPoint3 = new IPoint();
        this.mapDelegate.get().latlon2Geo(end.latitude, end.longitude, localIPoint3);
        double startx = localIPoint1.x;
        double starty = localIPoint1.y;
        double passx = localIPoint2.x;
        double passy = localIPoint2.y;
        double endx = localIPoint3.x;
        double endy = localIPoint3.y;

        double d7 = ((endy - starty) * (passy * passy - starty * starty + passx * passx - startx * startx) + (passy - starty) * (starty * starty - endy * endy + startx * startx - endx * endx)) / (2.0D * (passx - startx) * (endy - starty) - 2.0D * (endx - startx) * (passy - starty));

        double d8 = ((endx - startx) * (passx * passx - startx * startx + passy * passy - starty * starty) + (passx - startx) * (startx * startx - endx * endx + starty * starty - endy * endy)) / (2.0D * (passy - starty) * (endx - startx) - 2.0D * (endy - starty) * (passx - startx));

        this.q = Math.sqrt((startx - d7) * (startx - d7) + (starty - d8) * (starty - d8));

        this.r = a(d7, d8, startx, starty);
        double d9 = a(d7, d8, passx, passy);
        this.s = a(d7, d8, endx, endy);
        if (this.r < this.s)
        {
            if ((d9 <= this.r) || (d9 >= this.s)) {
                this.s -= Math.PI * 2;
            }
        }
        else if ((d9 <= this.s) || (d9 >= this.r)) {
            this.s += Math.PI * 2;
        }
        return new DPoint(d7, d8);
    }

    private double a(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
    {
        double d1 = (paramDouble2 - paramDouble4) / this.q;
        d1 = Math.abs(d1) > 1.0D ? Math.signum(d1) : d1;
        double d2 = Math.asin(d1);
        if (d2 >= 0.0D)
        {
            if (paramDouble3 < paramDouble1) {
                d2 = Math.PI - Math.abs(d2);
            }
        }
        else if (paramDouble3 < paramDouble1) {
            d2 = Math.PI - d2;
        } else {
            d2 = Math.PI * 2 + d2;
        }
        return d2;
    }

    private void insert1(int flongitude, int flatitude, int llongitude, int llatitude, List<IPoint> desPoints){

        long  disSquare = getDisSquare(flongitude, flatitude, llongitude, llatitude);
        int  count = (int) (disSquare / 65000000000l);

        if(count > 0){
            count += 1;
            int dx = ( llongitude - flongitude )/count;
            int dy = ( llatitude - flatitude )/count;
            for( int i = 1; i < count; i++ ){
                IPoint insertPoint = new IPoint(flongitude + (i * dx),flatitude + (i * dy));
                desPoints.add(insertPoint);
            }
        }
        desPoints.add(new IPoint(llongitude, llatitude));
    }

    private long getDisSquare(long x1,long y1,long x2,long y2){
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }


    /**
     * 绘制圆弧。
     * @param gl GL10实例。
     * @param start 圆弧起点。
     * @param passed 圆弧途经点。
     * @param end 圆弧终点。
     * @param pointArray 插值计算结果。
     * @param width 圆弧线宽。
     * @param strokeColor 圆弧线颜色。
     * @throws RemoteException 异常。
     */
    public void drawArc(GL10 gl,LatLng start,LatLng passed,LatLng end,int pointArray [],int width,int strokeColor)
    {
        if ((start == null) || (passed == null) || (end == null)) {
            return;
        }
        if (pointArray == null) {
            calMapFPoint(start,passed,end);
        }
        if (pointArray != null)
        {

            long stateInstance = this.mapDelegate.get().getMapProjection().getInstanceHandle();
            //TODO 需要在创建纹理时初始化本信息
            int texOrgPixelLen = 32;
            float texLen = this.mapDelegate.get().getMapProjection().getMapLenWithWin(texOrgPixelLen); //系统默认的纹理像素宽高都是32像素
            int texid = this.mapDelegate.get().getLineTextureID(); //获得系统默认的纹理
            float lineWidth = this.mapDelegate.get().getMapProjection().getMapLenWithWin((int)width);

            //虚线的颜色
            boolean usecolor = true; //使用颜色替换纹理中的颜色
            boolean complexTex = false; //简单纹理


            NativeLineRenderer.nativeDrawLineByTextureID(pointArray, pointArray.length, stateInstance, texLen, lineWidth, texid, strokeColor, complexTex, usecolor);

        }
    }

}
