package com.unistrong.api.maps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.unistrong.api.mapcore.ConfigableConstDecode;
import com.unistrong.api.mapcore.util.IMMapCoreException;
import com.unistrong.api.mapcore.util.AuthManager;
import com.unistrong.api.mapcore.util.SDKInfo;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.maps.model.LatLng;
import com.leador.mapcore.FileUtil;

import java.util.ArrayList;

/**
 * 定义了一个实现LeadorMap地图其他功能的类。
 */
public class MapUtils {
    private static final Double COEF = 0.00000896861111;//(32.287/3600000)的结果;
//    /**
//     * 速度优先 : 0
//     */
//  public static final int DRIVING_DEFAULT = 0;
//    /**
//     * 费用优先 : 1
//     */
//  public static final int DRIVING_SAVE_MONEY = 1;
//    /**
//     * 距离优先 : 2
//     */
//  public static final int DRIVING_SHORT_DISTANCE = 2;
//    /**
//     * 不走高速 : 3
//     */
//  public static final int DRIVING_NO_HIGHWAY = 3;
//    /**
//     * 避免拥堵 : 4
//     */
//  public static final int DRIVING_AVOID_CONGESTION = 4;
//    /**
//     * 不走高速且避免收费 : 5
//     */
//  public static final int DRIVING_NO_HIGHWAY_AVOID_SHORT_MONEY = 5;
//    /**
//     * 不走高速且躲避拥堵 : 6
//     */
//  public static final int DRIVING_NO_HIGHWAY_AVOID_CONGESTION = 6;
//    /**
//     * 躲避收费和拥堵 : 7
//     */
//  public static final int DRIVING_SAVE_MONEY_AVOID_CONGESTION = 7;
//    /**
//     * 不走高速躲避收费和拥堵 : 8
//     */
//  public static final int DRIVING_NO_HIGHWAY_SAVE_MONEY_AVOID_CONGESTION = 8;
//    /**
//     * 最快捷 : 0
//     */
//  public static final int BUS_TIME_FIRST = 0;
//    /**
//     * 费用优先 : 1
//     */
//  public static final int BUS_MONEY_LITTLE = 1;
//    /**
//     * 最少换乘 : 2
//     */
//  public static final int BUS_TRANSFER_LITTLE = 2;
//    /**
//     * 最少步行 : 3
//     */
//  public static final int BUS_WALK_LITTLE = 3;
//    /**
//     * 最舒适 : 4
//     */
//  public static final int BUS_COMFORT = 4;
//    /**
//     * 不乘地铁 : 5
//     */
//  public static final int BUS_NO_SUBWAY = 5;

    /**
     * 根据用户的起点和终点经纬度计算两点间距离，此距离为相对较短的距离，单位米。
     *
     * @param startLatlng - 起点的坐标。
     * @param endLatlng   - 终点的坐标。
     * @return 两点间相对较短的距离，单位米。
     */
    public static float calculateLineDistance(LatLng startLatlng, LatLng endLatlng) {
        double d1 = 0.01745329251994329D;
        double d2 = startLatlng.longitude;
        double d3 = startLatlng.latitude;
        double d4 = endLatlng.longitude;
        double d5 = endLatlng.latitude;
        d2 *= 0.01745329251994329D;
        d3 *= 0.01745329251994329D;
        d4 *= 0.01745329251994329D;
        d5 *= 0.01745329251994329D;
        double d6 = Math.sin(d2);
        double d7 = Math.sin(d3);
        double d8 = Math.cos(d2);
        double d9 = Math.cos(d3);
        double d10 = Math.sin(d4);
        double d11 = Math.sin(d5);
        double d12 = Math.cos(d4);
        double d13 = Math.cos(d5);
        double[] arrayOfDouble1 = new double[3];
        double[] arrayOfDouble2 = new double[3];
        arrayOfDouble1[0] = (d9 * d8);
        arrayOfDouble1[1] = (d9 * d6);
        arrayOfDouble1[2] = d7;
        arrayOfDouble2[0] = (d13 * d12);
        arrayOfDouble2[1] = (d13 * d10);
        arrayOfDouble2[2] = d11;
        double d14 = Math.sqrt((arrayOfDouble1[0] - arrayOfDouble2[0]) * (arrayOfDouble1[0] - arrayOfDouble2[0]) + (arrayOfDouble1[1] - arrayOfDouble2[1]) * (arrayOfDouble1[1] - arrayOfDouble2[1]) + (arrayOfDouble1[2] - arrayOfDouble2[2]) * (arrayOfDouble1[2] - arrayOfDouble2[2]));

        return (float) (Math.asin(d14 / 2.0D) * 1.27420015798544E7D);
    }

    /**
     * 计算地图上矩形区域的面积，单位平方米。
     *
     * @param leftTopLatlng     - 矩形区域左上角点坐标。
     * @param rightBottomLatlng - 矩形区域右下角点坐标。
     * @return 地图上矩形区域的面积，单位平方米。
     */
    public static float calculateArea(ArrayList<LatLng> polygonPoints) {


        LatLng leftTopLatlng = polygonPoints.get(0);
        LatLng rightBottomLatlng = polygonPoints.get(3);
        double d1 = 6378137.0D;

        double d2 = Math.sin(leftTopLatlng.latitude * 3.141592653589793D / 180.0D) - Math.sin(rightBottomLatlng.latitude * 3.141592653589793D / 180.0D);
        double d3 = (rightBottomLatlng.longitude - leftTopLatlng.longitude) / 360.0D;
        if (d3 < 0.0D) {
            d3 += 1.0D;
        }
        return (float) (6.283185307179586D * d1 * d1 * d2 * d3);
    }

    /**
     * 求任意多边形的面积。
     *
     * @param latList 多边形的顶点集合.
     * @return 多边形的面积，单位：m * m
     */
    public static double calPolygonArea(ArrayList<LatLng> latList) {
        if (null == latList || latList.size() < 3) {
            return 0;
        }
        int size = latList.size();
        float polygonArea = 0;   //多边形面积
        double x1 = 0;
        double y1 = 0;
        double x2 = 0;
        double y2 = 0;//存放double坐标
        for (int i = 0; i < size; i++)   //根据顶点坐标，求多边形的面积
        {
            if (i < size - 1) {
                //将整型的点坐标转换为浮点型的经纬坐标，并参与计算面积
                x1 = (double) (latList.get(i).longitude / COEF);
                y1 = (double) (latList.get(i).latitude / COEF);
                x2 = (double) (latList.get(i + 1).longitude / COEF);
                y2 = (double) (latList.get(i + 1).latitude / COEF);
                polygonArea += (x1 * y2 - x2 * y1);
            } else {
                x1 = (double) (latList.get(i).longitude / COEF);
                y1 = (double) (latList.get(i).latitude / COEF);
                x2 = (double) (latList.get(0).longitude / COEF);
                y2 = (double) (latList.get(0).latitude / COEF);
                polygonArea += (x1 * y2 - x2 * y1);
                break;
            }
        }
        return Math.abs(polygonArea) / 2;
    }
//
//  /**
//   * 以midPoint为起点，startPoint为终点的直线为line1,以midPoint为起点，endPoint为终点的直线为line2；
//   * <p>以midPoint为中心，从line1顺时针旋转到line2的夹角<p/>
//   * @param startPoint 夹角端点。
//   * @param midPoint 夹角顶点。
//   * @param endPoint 夹角端点。
//     * @return 夹角，单位弧度。
//     */
//// public static double calculateAngle(LatLng startPoint ,LatLng midPoint , LatLng endPoint){
////   double angle1 = calculateAngle(midPoint.longitude,midPoint.latitude,startPoint.longitude,startPoint.latitude);
////   double angle2 = calculateAngle(midPoint.longitude,midPoint.latitude,endPoint.longitude,endPoint.latitude);
////   return Math.abs(angle1-angle2);
//// }

    /**
     * 计算两条直接的夹角，射线line1顺时针旋转到射线line2的夹角。
     *
     * @param line1Start 射线line1的起点。
     * @param line1end   射线line1的终点。
     * @param line2Start 射线line2的起点。
     * @param line2End   射线line2的终点。
     * @return 夹角，单位弧度。
     */
    public static double calculateAngle(LatLng line1Start, LatLng line1end, LatLng line2Start, LatLng line2End) {
        double angle1 = calculateAngle(line1Start.longitude, line1Start.latitude, line1end.longitude, line1end.latitude);
        double angle2 = calculateAngle(line2Start.longitude, line2Start.latitude, line2End.longitude, line2End.latitude);
        double angle = angle2 - angle1;
        angle = Math.toDegrees(angle);
        if (angle < 0) {
            angle = 360 - angle;
        }
        angle = Math.toRadians(angle);
        return Math.abs(angle);
    }

    /**
     * 角度计算，相对与正北的顺时针夹角，单位弧度。
     *
     * @param startLon 起点经度。
     * @param startLat 起点纬度。
     * @param endLon   终点经度。
     * @param endLat   终点纬度。
     * @return 夹角，单位弧度。
     */
    public static double calculateAngle(double startLon, double startLat, double endLon, double endLat) {

        double dAngle;
        if (endLon != startLon) {
            double s = Math.cos((endLat + startLat) * 0.008726646);
            double dAtan = (endLat - startLat) / ((endLon - startLon) * s);
            dAngle = Math.atan(dAtan);
            if (endLon - startLon < 0) {
                dAngle += Math.PI;
            } else {
                if (dAngle < 0) {
                    dAngle += 2 * Math.PI;
                }
            }
        } else {
            if (endLat > startLat) {
                dAngle = Math.PI / 2;
            } else {
                dAngle = Math.PI / 2 * 3;
            }
        }

        dAngle = Math.PI * 5 / 2 - dAngle;
        if (dAngle > Math.PI * 2) {
            dAngle = dAngle - Math.PI * 2;
        }
        return dAngle;
    }

    //计算两点之间距离 单位 米
    public static String getDistance(LatLng lcation, LatLng lcation1) {
        return Util.getDistance(lcation, lcation1) + "";
    }

    //计算两点之间距离 单位 千米
    public static String getDistanceK(LatLng lcation, LatLng lcation1) {
        return Util.getDistanceK(lcation, lcation1) + "";
    }

    /**
     * 使用默认浏览器跳转到地图app的下载页面
     *
     * @param context - 上下文。
     */
    public static void getLatestMapApp(Context context) {
        try {
            String str = "http://www.leador.com.cn/";
            Intent localIntent = new Intent("android.intent.action.VIEW");
            localIntent.addFlags(276824064);

            localIntent.addCategory("android.intent.category.DEFAULT");
            localIntent.setData(Uri.parse(str));
            a locala = new a("glaa", context);
            locala.start();
            context.startActivity(localIntent);
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
    }

//    /**
//     * 唤起地图进行导航。
//     * @param para  - 导航需要的参数。
//     * @param context - 上下文
//     * @throws UnistrongException  UnistrongException异常。
//     */
//  public static void openAMapNavi(NaviPara para, Context context)
//    throws UnistrongException
//  {
//    if (a(context))
//    {
//      if (para.getTargetPoint() != null) {
//        try
//        {
//          Intent localIntent = new Intent("android.intent.action.VIEW");
//          localIntent.addFlags(276824064);
//
//          localIntent.addCategory("android.intent.category.DEFAULT");
//          localIntent.setData(Uri.parse(a(para, context)));
//
//          localIntent.setPackage("com.autonavi.minimap");
//          a locala = new a("oan", context);
//          locala.start();
//            context.startActivity(localIntent);
//        }
//        catch (Throwable localThrowable)
//        {
//          throw new UnistrongException(UnistrongException.LMAP_NOT_SUPPORT);
//        }
//      } else {
//        throw new UnistrongException(UnistrongException.ILLEGAL_LMAP_ARGUMENT);
//      }
//    }
//    else {
//      throw new UnistrongException(UnistrongException.LMAP_NOT_SUPPORT);
//    }
//  }
//
//    /**
//     *调起地图poi周边检索页面。
//     * @param para - poi周边检索需要的参数。
//     * @param context - 上下文。
//     * @throws UnistrongException UnistrongException异常。
//     */
//  public static void openAMapPoiNearbySearch(PoiPara para, Context context)
//    throws UnistrongException
//  {
//    if (a(context))
//    {
//      if ((para.getKeywords() != null) &&
//        (para.getKeywords().trim().length() > 0)) {
//        try
//        {
//          Intent localIntent = new Intent("android.intent.action.VIEW");
//          localIntent.addFlags(276824064);
//
//          localIntent.addCategory("android.intent.category.DEFAULT");
//          localIntent.setData(Uri.parse(a(para, context)));
//
//          localIntent.setPackage("com.autonavi.minimap");
//          a locala = new a("oan", context);
//          locala.start();
//            context.startActivity(localIntent);
//        }
//        catch (Throwable localThrowable)
//        {
//          throw new UnistrongException(UnistrongException.LMAP_NOT_SUPPORT);
//        }
//      } else {
//        throw new UnistrongException(UnistrongException.ILLEGAL_LMAP_ARGUMENT);
//      }
//    }
//    else {
//      throw new UnistrongException(UnistrongException.LMAP_NOT_SUPPORT);
//    }
//  }
//
//    /**
//     * 调起地图驾车路线检索页面。
//     * @param para  - 驾车路线检索需要的参数。
//     * @param context - 上下文。
//     * @throws UnistrongException UnistrongException异常。
//     */
//  public static void openAMapDrivingRoute(RoutePara para, Context context)
//    throws UnistrongException
//  {
//    a(para, context, 2);
//  }
//    /**
//     * 打开地图公交路线规划。
//     * @param para  - 公交路线检索需要的参数。
//     * @param context - 上下文。
//     * @throws UnistrongException UnistrongException异常。
//     */
//  public static void openAMapTransitRoute(RoutePara para, Context context)
//    throws UnistrongException
//  {
//    a(para, context, 1);
//  }
//    /**
//     * 打开地图步行路线规划
//     * @param para  -规划参数。
//     * @param context - 上下文。
//     * @throws UnistrongException UnistrongException异常。
//     */
//  public static void openAMapWalkingRoute(RoutePara para, Context context)
//    throws UnistrongException
//  {
//    a(para, context, 4);
//  }

//  private static void a(RoutePara paramRoutePara, Context paramContext, int paramInt)
//    throws UnistrongException
//  {
//    if (a(paramContext))
//    {
//      if (a(paramRoutePara)) {
//        try
//        {
//          Intent localIntent = new Intent("android.intent.action.VIEW");
//          localIntent.addFlags(276824064);
//
//          localIntent.addCategory("android.intent.category.DEFAULT");
//          localIntent.setData(Uri.parse(b(paramRoutePara, paramContext, paramInt)));
//
//          localIntent.setPackage("com.leador.minimap");
//          a locala = new a("oan", paramContext);
//          locala.start();
//          paramContext.startActivity(localIntent);
//        }
//        catch (Throwable localThrowable)
//        {
//          throw new UnistrongException(UnistrongException.LMAP_NOT_SUPPORT);
//        }
//      } else {
//        throw new UnistrongException(UnistrongException.ILLEGAL_LMAP_ARGUMENT);
//      }
//    }
//    else {
//      throw new UnistrongException(UnistrongException.LMAP_NOT_SUPPORT);
//    }
//  }

//  private static boolean a(RoutePara paramRoutePara)
//  {
//    if ((paramRoutePara.getStartPoint() == null) || (paramRoutePara.getEndPoint() == null) ||
//      (paramRoutePara.getStartName() == null) ||
//      (paramRoutePara.getStartName().trim().length() <= 0) ||
//      (paramRoutePara.getEndName() == null) ||
//      (paramRoutePara.getEndName().trim().length() <= 0)) {
//      return false;
//    }
//    return true;
//  }

    static class a
            extends Thread {
        String a = "";
        Context b;

        public a(String paramString, Context paramContext) {
            this.a = paramString;
            if (paramContext != null) {
                this.b = paramContext.getApplicationContext();
            }
        }

        public void run() {
            if (this.b != null) {
                try {
                    SDKInfo localbv = new SDKInfo.createSDKInfo(this.a, MapsInitializer.getVersion(), ConfigableConstDecode.userAgent, "").setPackageName(new String[]{"com.leador.api.maps"}).a();
                    AuthManager.getKeyAuth(this.b, localbv);
                    interrupt();
                } catch (IMMapCoreException localbl) {
                    localbl.printStackTrace();
                }
            }
        }
    }

//  private static String a(NaviPara paramNaviPara, Context paramContext)
//  {
//    return String.format("androidamap://navi?sourceApplication=%s&lat=%f&lon=%f&dev=0&style=%d", new Object[] { AppInfo.getApplicationName(paramContext),
//      Double.valueOf(paramNaviPara.getTargetPoint().latitude),
//      Double.valueOf(paramNaviPara.getTargetPoint().longitude), Integer.valueOf(paramNaviPara.getNaviStyle()) });
//  }

//  private static String b(RoutePara paramRoutePara, Context paramContext, int paramInt)
//  {
//    String str = String.format("androidamap://route?sourceApplication=%s&slat=%f&slon=%f&sname=%s&dlat=%f&dlon=%f&dname=%s&dev=0&t=%d", new Object[] {
//      AppInfo.getApplicationName(paramContext),
//      Double.valueOf(paramRoutePara.getStartPoint().latitude), Double.valueOf(paramRoutePara.getStartPoint().longitude), paramRoutePara
//      .getStartName(), Double.valueOf(paramRoutePara.getEndPoint().latitude),
//      Double.valueOf(paramRoutePara.getEndPoint().longitude), paramRoutePara.getEndName(), Integer.valueOf(paramInt) });
//    if (paramInt == 1) {
//      str = str + "&m=" + paramRoutePara.getTransitRouteStyle();
//    } else if (paramInt == 2) {
//      str = str + "&m=" + paramRoutePara.getDrivingRouteStyle();
//    }
//    return str;
//  }

//  private static String a(PoiPara paramPoiPara, Context paramContext)
//  {
//    String str = String.format("androidamap://arroundpoi?sourceApplication=%s&keywords=%s&dev=0", new Object[] {
//      AppInfo.getApplicationName(paramContext), paramPoiPara.getKeywords() });
//    if (paramPoiPara.getCenter() != null) {
//      str = str + "&lat=" + paramPoiPara.getCenter().latitude + "&lon=" + paramPoiPara.getCenter().longitude;
//    }
//    return str;
//  }
//
//  private static boolean a(Context paramContext)
//  {
//    PackageInfo localPackageInfo = null;
//    try
//    {
//      localPackageInfo = paramContext.getPackageManager().getPackageInfo("com.leador.minimap", 0);
//    }
//    catch (PackageManager.NameNotFoundException localNameNotFoundException)
//    {
//      localPackageInfo = null;
//      return false;
//    }
//    if (localPackageInfo != null) {
//      return true;
//    }
//    return false;
//  }

    /**
     * 返回设备的存储目录根路径。
     *
     * @param context 对应的context。
     * @return 设备的存储目录根路径。
     * @since 1.10.0
     */
    public static String getExternalStroragePath(Context context) {
        return FileUtil.getExternalStroragePath(context);
    }
}
