package com.unistrong.api.maps.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.util.LongSparseArray;


import com.unistrong.api.mapcore.util.Bounds;
import com.unistrong.api.maps.UnistrongException;
import com.leador.mapcore.DPoint;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 实现TileProvider类，热力图Provider。
 */
public class HeatmapTileProvider implements TileProvider {
    /**
     * 热力图层默认的点的半径
     */
    public static final int DEFAULT_RADIUS = 12;
    /**
     * 热力图层默认透明度
     */
    public static final double DEFAULT_OPACITY = 0.6D;
    private static final int[] colors = new int[]{Color.rgb(102, 225, 0), Color.rgb(255, 0, 0)};
    private static final float[] startPoints = new float[]{0.2F, 1.0F};
    /**
     * 热力图默认渐变
     */
    public static final Gradient DEFAULT_GRADIENT;
    private c c;
    private Collection<WeightedLatLng> weightedLatLngs;
    private Bounds e;
    private int pointRadius;
    private Gradient gradient;
    private int[] h;
    private double[] i;
    private double opacity;
    private double[] k;

    private HeatmapTileProvider(HeatmapTileProvider.Builder builder) {
        this.weightedLatLngs = builder.weightedLatLngs;
        this.pointRadius = builder.pointRadius;
        this.gradient = builder.gradient;
        if(this.gradient == null || !this.gradient.isAvailable()) {
            this.gradient = DEFAULT_GRADIENT;
        }

        this.opacity = builder.opacity;
        this.i = a(this.pointRadius, (double)this.pointRadius / 3.0D);
        this.a(this.gradient);
        this.c(this.weightedLatLngs);
    }

    private void c(Collection<WeightedLatLng> var1) {
        ArrayList var2 = new ArrayList();
        Iterator var3 = var1.iterator();

        WeightedLatLng var4;
        while(var3.hasNext()) {
            var4 = (WeightedLatLng)var3.next();
            if(var4.latLng.latitude < 85.0D && var4.latLng.latitude > -85.0D) {
                var2.add(var4);
            }
        }

        this.weightedLatLngs = var2;
        this.e = a(this.weightedLatLngs);
        this.c = new c(this.e);
        var3 = this.weightedLatLngs.iterator();

        while(var3.hasNext()) {
            var4 = (WeightedLatLng)var3.next();
            this.c.a(var4);
        }

        this.k = this.a(this.pointRadius);
    }

    private static Collection<WeightedLatLng> d(Collection<LatLng> var0) {
        ArrayList var1 = new ArrayList();
        Iterator var2 = var0.iterator();

        while(var2.hasNext()) {
            LatLng var3 = (LatLng)var2.next();
            var1.add(new WeightedLatLng(var3));
        }

        return var1;
    }

    /**
     * 根据瓦片列号行号和地图zoom级别获取瓦片对象
     * @param x 行号
     * @param y 列号
     * @param zoom 地图zoom级别
     * @return 瓦片对象
     */
    public Tile getTile(int x, int y, int zoom) {
        double var4 = 1.0D / Math.pow(2.0D, (double)zoom);//2的zoom次方
        double var6 = var4 * (double)this.pointRadius / 256.0D;
        double var8 = var4 + 2.0D * var6;
        double var10 = var8 / (double)(256 + this.pointRadius * 2);
        double left = (double)x * var4 - var6;
        double right = (double)(x + 1) * var4 + var6;
        double top = (double)y * var4 - var6;
        double bottom = (double)(y + 1) * var4 + var6;
        double var20 = 0.0D;
        Collection<WeightedLatLng> weightedLatLngs = new ArrayList();
        Bounds bounds;
        if(left < 0.0D) {
            bounds = new Bounds(left + 1.0D, 1.0D, top, bottom);
            var20 = -1.0D;
            weightedLatLngs = this.c.a(bounds);
        } else if(right > 1.0D) {
            bounds = new Bounds(0.0D, right - 1.0D, top, bottom);
            var20 = 1.0D;
            weightedLatLngs = this.c.a(bounds);
        }

        bounds = new Bounds(left, right, top, bottom);
        Bounds var24 = new Bounds(this.e.left - var6, this.e.right + var6, this.e.top - var6, this.e.bottom + var6);
        if(!bounds.a(var24)) {
            return TileProvider.NO_TILE;
        } else {
            Collection var25 = this.c.a(bounds);
            if(var25.isEmpty()) {
                return TileProvider.NO_TILE;
            } else {
                double[][] var26 = new double[256 + this.pointRadius * 2][256 + this.pointRadius * 2];

                Iterator var27;
                DPoint var29;
                WeightedLatLng var28;
                int var31;
                int var30;
                for(var27 = var25.iterator(); var27.hasNext(); var26[var30][var31] += var28.intensity) {
                    var28 = (WeightedLatLng)var27.next();
                    var29 = var28.getPoint();
                    var30 = (int)((var29.x - left) / var10);
                    var31 = (int)((var29.y - top) / var10);
                }

                for(var27 = ((Collection)weightedLatLngs).iterator(); var27.hasNext(); var26[var30][var31] += var28.intensity) {
                    var28 = (WeightedLatLng)var27.next();
                    var29 = var28.getPoint();
                    var30 = (int)((var29.x + var20 - left) / var10);
                    var31 = (int)((var29.y - top) / var10);
                }

                double[][] var32 = a(var26, this.i);
                Bitmap var33 = a(var32, this.h, this.k[zoom]);

                return a(var33, x,  y,  zoom);
            }
        }
    }
    //    public static void writeDatasToFile(String fileName, byte[] data) {
//
//
//        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
//        ReentrantReadWriteLock.WriteLock writelock = lock.writeLock();
//        writelock.lock();
//        try {
//            java.io.File fExternalStorageDirectory = Environment
//                    .getExternalStorageDirectory();
//            java.io.File dir = new java.io.File(
//                    fExternalStorageDirectory, "leadormap");
//            boolean result = false;
//            if (!dir.exists()) {
//                result = dir.mkdir();
//            }
//            String path = dir.toString()+"/"+fileName+".png";
//            if (data == null || data.length == 0)
//                return;
//            File file = new File(path);
//            if (file.exists()) { // 判断当前文件是否存在
//                file.delete(); // 存在就删除
//            }
//            file.createNewFile();
//            OutputStream os = new FileOutputStream(file);
//            os.write(data);
//            os.flush();
//            os.close();
//        }
//        catch (Exception ex) {
//        } finally {
//            writelock.unlock();
//        }
//    }
    private void a(Gradient gradient) {
        this.gradient = gradient;
        this.h = gradient.generateColorMap(this.opacity);
    }

    private double[] a(int var1) {
        double[] var2 = new double[21];

        int var3;
        for(var3 = 5; var3 < 11; ++var3) {
            var2[var3] = a(this.weightedLatLngs, this.e, var1, (int)(1280.0D * Math.pow(2.0D, (double)var3)));
            if(var3 == 5) {
                for(int var4 = 0; var4 < var3; ++var4) {
                    var2[var4] = var2[var3];
                }
            }
        }

        for(var3 = 11; var3 < 21; ++var3) {
            var2[var3] = var2[10];
        }

        return var2;
    }
    private static Tile a(Bitmap var0,int x, int y, int z) {
        ByteArrayOutputStream var1 = new ByteArrayOutputStream();
        var0.compress(Bitmap.CompressFormat.PNG, 100, var1);
        byte[] var2 = var1.toByteArray();
        return new Tile(256, 256, var2);
    }

    static Bounds a(Collection<WeightedLatLng> var0) {
        Iterator var1 = var0.iterator();
        WeightedLatLng var2 = (WeightedLatLng)var1.next();
        double var3 = var2.getPoint().x;
        double var5 = var2.getPoint().x;
        double var7 = var2.getPoint().y;
        double var9 = var2.getPoint().y;

        while(var1.hasNext()) {
            WeightedLatLng var11 = (WeightedLatLng)var1.next();
            double var12 = var11.getPoint().x;
            double var14 = var11.getPoint().y;
            if(var12 < var3) {
                var3 = var12;
            }

            if(var12 > var5) {
                var5 = var12;
            }

            if(var14 < var7) {
                var7 = var14;
            }

            if(var14 > var9) {
                var9 = var14;
            }
        }

        return new Bounds(var3, var5, var7, var9);
    }

    static double[] a(int pointRadius, double var1) {
        double[] var3 = new double[pointRadius * 2 + 1];

        for(int var4 = -pointRadius; var4 <= pointRadius; ++var4) {
            var3[var4 + pointRadius] = Math.exp((double)(-var4 * var4) / (2.0D * var1 * var1));
        }

        return var3;
    }

    static double[][] a(double[][] var0, double[] var1) {
        int var2 = (int)Math.floor((double)var1.length / 2.0D);
        int var3 = var0.length;
        int var4 = var3 - 2 * var2;
        int var5 = var2;
        int var6 = var2 + var4 - 1;
        double[][] var7 = new double[var3][var3];

        int var8;
        int var9;
        int var12;
        double var13;
        for(var8 = 0; var8 < var3; ++var8) {
            for(var9 = 0; var9 < var3; ++var9) {
                var13 = var0[var8][var9];
                if(var13 != 0.0D) {
                    int var11 = (var6 < var8 + var2?var6:var8 + var2) + 1;
                    var12 = var5 > var8 - var2?var5:var8 - var2;

                    for(int var10 = var12; var10 < var11; ++var10) {
                        var7[var10][var9] += var13 * var1[var10 - (var8 - var2)];
                    }
                }
            }
        }

        double[][] var15 = new double[var4][var4];

        for(var8 = var5; var8 < var6 + 1; ++var8) {
            for(var9 = 0; var9 < var3; ++var9) {
                var13 = var7[var8][var9];
                if(var13 != 0.0D) {
                    int var17 = (var6 < var9 + var2?var6:var9 + var2) + 1;
                    var12 = var5 > var9 - var2?var5:var9 - var2;

                    for(int var16 = var12; var16 < var17; ++var16) {
                        var15[var8 - var2][var16 - var2] += var13 * var1[var16 - (var9 - var2)];
                    }
                }
            }
        }

        return var15;
    }

    static Bitmap a(double[][] var0, int[] var1, double var2) {
        int var4 = var1[var1.length - 1];
        double var5 = (double)(var1.length - 1) / var2;
        int var7 = var0.length;
        int[] var14 = new int[var7 * var7];

        for(int var8 = 0; var8 < var7; ++var8) {
            for(int var9 = 0; var9 < var7; ++var9) {
                double var12 = var0[var9][var8];
                int var10 = var8 * var7 + var9;
                int var11 = (int)(var12 * var5);
                if(var12 != 0.0D) {
                    if(var11 < var1.length) {
                        var14[var10] = var1[var11];
                    } else {
                        var14[var10] = var4;
                    }
                } else {
                    var14[var10] = 0;
                }
            }
        }

        Bitmap var15 = Bitmap.createBitmap(var7, var7, Bitmap.Config.ARGB_8888);

        var15.setPixels(var14, 0, var7, 0, 0, var7, var7);
        return var15;
    }

    static double a(Collection<WeightedLatLng> var0, Bounds var1, int var2, int var3) {
        double var4 = var1.left;
        double var6 = var1.right;
        double var8 = var1.top;
        double var10 = var1.bottom;
        double var12 = var6 - var4 > var10 - var8?var6 - var4:var10 - var8;
        int var14 = (int)((double)(var3 / (2 * var2)) + 0.5D);
        double var15 = (double)var14 / var12;
        LongSparseArray var17 = new LongSparseArray();
        double var22 = 0.0D;
        Iterator var24 = var0.iterator();

        while(var24.hasNext()) {
            WeightedLatLng var25 = (WeightedLatLng)var24.next();
            double var18 = var25.getPoint().x;
            double var20 = var25.getPoint().y;
            int var26 = (int)((var18 - var4) * var15);
            int var27 = (int)((var20 - var8) * var15);
            LongSparseArray var28 = (LongSparseArray)var17.get((long)var26);
            if(var28 == null) {
                var28 = new LongSparseArray();
                var17.put((long)var26, var28);
            }

            Double var29 = (Double)var28.get((long)var27);
            if(var29 == null) {
                var29 = Double.valueOf(0.0D);
            }

            var29 = Double.valueOf(var29.doubleValue() + var25.intensity);
            var28.put((long)var27, var29);
            if(var29.doubleValue() > var22) {
                var22 = var29.doubleValue();
            }
        }

        return var22;
    }

    /**
     * 获取瓦片高度
     * @return 瓦片高度
     */
    public int getTileHeight() {
        return 256;
    }

    /**
     * 获取瓦片宽度
     * @return 瓦片宽度
     */
    public int getTileWidth() {
        return 256;
    }

    static {
        DEFAULT_GRADIENT = new Gradient(colors, startPoints);
    }

    /**
     * 热力图构造器
     */
    public static class Builder {
        private Collection<WeightedLatLng> weightedLatLngs;
        private int pointRadius = DEFAULT_RADIUS;
        private Gradient gradient;
        private double opacity;//透明度

        public Builder() {
            this.gradient = HeatmapTileProvider.DEFAULT_GRADIENT;
            this.opacity = DEFAULT_OPACITY;
        }

        /**
         * 设置热力图绘制的数据
         * @param collection 热力图绘制数据,不能为null
         * @return 更新后的热力图瓦片提供者构造器引用
         */
        public HeatmapTileProvider.Builder data(Collection<LatLng> collection) {
            return this.weightedData(HeatmapTileProvider.d(collection));
        }

        /**
         * 设置热力图绘制的数据 带权值的位置点集合
         * @param collection 热力图绘制数据,不能为null
         * @return 更新后的热力图瓦片提供者构造器引用
         */
        public HeatmapTileProvider.Builder weightedData(Collection<WeightedLatLng> collection) {
            this.weightedLatLngs = collection;
            return this;
        }

        /**
         * 设置热力图点的半径,默认为12sp,可不设置该接口
         * @param var1 热力图点半径
         * @return 更新后的热力图瓦片提供者构造器引用
         */
        public HeatmapTileProvider.Builder radius(int var1) {
            this.pointRadius = Math.max(10, Math.min(var1, 50));
            return this;
        }
        /**
         * 设置热力图渐变,有默认值DEFAULT_GRADIENT,可不设置该接口
         * @param var1 热力图渐变
         * @return 更新后的热力图瓦片提供者构造器引用
         */

        public HeatmapTileProvider.Builder gradient(Gradient var1) {
            this.gradient = var1;
            return this;
        }
        /**
         * 设置热力图层的透明度,默认0.6,可不设置该接口
         * @param var1 热力图绘制数据,不能为null，不能为0
         * @return 更新后的热力图瓦片提供者构造器引用
         */

        public HeatmapTileProvider.Builder transparency(double var1) {
            this.opacity = Math.max(0.0D, Math.min(var1, 1.0D));
            return this;
        }

        /**
         * 构造热力图,调用该方法之前必须先通过data或者weightedData方法设置该热力图所要渲染的数据
         * @return 构造好的热力图瓦片提供者
         */
        public HeatmapTileProvider build() {
            if(this.weightedLatLngs != null && this.weightedLatLngs.size() != 0) {
                return new HeatmapTileProvider(this);
            } else {
                try {
                    throw new UnistrongException("No input points.");
                } catch (UnistrongException var2) {
                    Log.e("leadormap", var2.getErrorMessage());
                    var2.printStackTrace();
                    return null;
                }
            }
        }
    }
}