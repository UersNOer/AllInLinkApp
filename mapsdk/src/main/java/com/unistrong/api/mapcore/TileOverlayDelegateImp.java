package com.unistrong.api.mapcore;

import android.graphics.Bitmap;

import com.unistrong.api.mapcore.util.AsyncTaskDecode;
import com.unistrong.api.mapcore.util.ImageCacheDecode;
import com.unistrong.api.mapcore.util.ImageFetcherDecode;
import com.unistrong.api.mapcore.util.ImageWorkerDecode;
import com.unistrong.api.mapcore.util.LogManager;
import com.unistrong.api.mapcore.util.RegionUtil;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.maps.model.TileOverlayOptions;
import com.unistrong.api.maps.model.TileProvider;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapProjection;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.microedition.khronos.opengles.GL10;

public class TileOverlayDelegateImp//bm
        implements ITileOverlayDelegate {
    private TileOverlayViewDecode mTileOverlayView; // a
    private TileProvider tileProvider;
    private Float mZIndex;
    private boolean mVisible;
    private boolean e = false; //e
    private IMapDelegate mMap; //f
    private static int index = 0; //g
    private int tileWidth = 256;
    private int tileHeight = 256;
    private int iLayerIndex = -1; //j
    private ImageFetcherDecode mImageFetcher; //k
    private CopyOnWriteArrayList<TileCoordinate> listTileDraw = new CopyOnWriteArrayList<TileCoordinate>(); //l
    private boolean isFlingState = false; //m
    private TileServer tileServer = null; //n

    private static String CreateId(String type) // a
    {
        index += 1;
        return type + index;
    }

    private String mId = null; //mId

    public TileOverlayDelegateImp(TileOverlayOptions tileOverlayOptions, TileOverlayViewDecode tileOverlayView) {
        this.mTileOverlayView = tileOverlayView;
        this.tileProvider = tileOverlayOptions.getTileProvider();
        this.tileWidth = this.tileProvider.getTileWidth();
        this.tileHeight = this.tileProvider.getTileHeight();
        int i1 = Util.pow2(this.tileWidth);
        int i2 = Util.pow2(this.tileHeight);
        float f1 = this.tileWidth / i1;
        float f2 = this.tileHeight / i2;
        this.coordBuffer = Util.makeFloatBuffer(new float[]{0.0F, f2, f1, f2, f1, 0.0F, 0.0F, 0.0F});

        this.mZIndex = Float.valueOf(tileOverlayOptions.getZIndex());
        this.mVisible = tileOverlayOptions.isVisible();
        this.mId = getId();
        this.mMap = this.mTileOverlayView.getMap();

        this.iLayerIndex = Integer.valueOf(this.mId.substring("TileOverlay".length())).intValue();

        ImageCacheDecode.ImageCacheParams cacheParams = new ImageCacheDecode.ImageCacheParams(this.mTileOverlayView.getContext(), this.mId);

        cacheParams.enableMemoryCache(tileOverlayOptions
                .getMemoryCacheEnabled());
        cacheParams.enableDiskCache(tileOverlayOptions.getDiskCacheEnabled());
        cacheParams.setMemCacheSize(tileOverlayOptions.getMemCacheSize());
        cacheParams.setDiskCacheSize(tileOverlayOptions.getDiskCacheSize());
        String cacheDir = tileOverlayOptions.getDiskCacheDir();
        if ((cacheDir != null) && (!cacheDir.equals(""))) {
            cacheParams.setDiskCacheDir(cacheDir);
        }
        this.mImageFetcher = new ImageFetcherDecode(this.mTileOverlayView.getContext(), this.tileWidth, this.tileHeight);

        this.mImageFetcher.setTileProvider(this.tileProvider);
        this.mImageFetcher.a(cacheParams);

        refresh(true);
    }

    public TileOverlayDelegateImp(TileOverlayOptions paramTileOverlayOptions, TileOverlayViewDecode parambn, boolean paramBoolean) {
        this(paramTileOverlayOptions, parambn);
    }

    public void a() // a --> remove
    {
        if ((this.tileServer != null) &&
                (this.tileServer.getStatus() == AsyncTaskDecode.Status.RUNNING)) {
            this.tileServer.cancel(true);
        }
        for (TileCoordinate tile : this.listTileDraw) {
            tile.destroy();
        }
        this.listTileDraw.clear();
        this.mImageFetcher.closeCache();
        this.mTileOverlayView.b(this);
        this.mMap.setRunLowFrame(false);
    }

    public void clearTileCache() //b
    {
        this.mImageFetcher.clearCache();
    }

    public String getId() //c
    {
        if (this.mId == null) {
            this.mId = CreateId("TileOverlay");
        }
        return this.mId;
    }

    public void setZIndex(float zIndex) // a
    {
        this.mZIndex = Float.valueOf(zIndex);
        this.mTileOverlayView.changeOverlayIndex();
    }

    public float getZIndex() // d
    {
        return this.mZIndex.floatValue();
    }

    public void setVisible(boolean visible) // a
    {
        this.mVisible = visible;
        this.mMap.setRunLowFrame(false);
        if (visible) {
            refresh(true);
        }
    }

    public boolean isVisible() //e
    {
        return this.mVisible;
    }

    public boolean equalsRemote(ITileOverlayDelegate paramao) {
        if ((equals(paramao)) ||
                (paramao.getId().equals(getId()))) {
            return true;
        }
        return false;
    }

    public int hashCodeRemote() //f
    {
        return super.hashCode();
    }

    private FloatBuffer coordBuffer = null; //p

    private boolean calFPoint(TileCoordinate tile) // a
    {
        MapProjection localMapProjection = this.mMap.getMapProjection();
        float zoom = tile.Zoom;
        int width = this.tileWidth;
        int height = this.tileHeight;

        int geox = tile.pointGeo.x;
        int geoy = tile.pointGeo.y + (1 << 20 - (int) zoom) * height;

        float[] vertices = new float[12];

        FPoint localFPoint1 = new FPoint();
        localMapProjection.geo2Map(geox, geoy, localFPoint1);

        FPoint localFPoint2 = new FPoint();
        localMapProjection.geo2Map(geox + (1 << 20 - (int) zoom) * width, geoy, localFPoint2);

        FPoint localFPoint3 = new FPoint();
        localMapProjection.geo2Map(geox + (1 << 20 - (int) zoom) * width, geoy - (1 << 20 - (int) zoom) * height, localFPoint3);

        FPoint localFPoint4 = new FPoint();
        localMapProjection.geo2Map(geox, geoy - (1 << 20 - (int) zoom) * height, localFPoint4);

        vertices[0] = localFPoint1.x;
        vertices[1] = localFPoint1.y;
        vertices[2] = 0.0F;

        vertices[3] = localFPoint2.x;
        vertices[4] = localFPoint2.y;
        vertices[5] = 0.0F;

        vertices[6] = localFPoint3.x;
        vertices[7] = localFPoint3.y;
        vertices[8] = 0.0F;

        vertices[9] = localFPoint4.x;
        vertices[10] = localFPoint4.y;
        vertices[11] = 0.0F;
        if (tile.verticesBuffer == null) {
            tile.verticesBuffer = Util.makeFloatBuffer(vertices);
        } else {
            tile.verticesBuffer = Util.makeFloatBuffer(vertices, tile.verticesBuffer);
        }
        vertices = null;
        return true;
    }

    private void displayTextureLabel(GL10 gl, int textureId, FloatBuffer pvertices, FloatBuffer coordBuffer) // a
    {
        if ((pvertices == null) || (coordBuffer == null)) {
            return;
        }
    gl.glEnable(3042);
    gl.glTexEnvf(8960, 8704, 8448.0F);

        gl.glBlendFunc(770, 771);
        gl.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        gl.glEnable(3553);
        gl.glEnableClientState(32884);
        gl.glEnableClientState(32888);
        gl.glBindTexture(3553, textureId);

        gl.glVertexPointer(3, 5126, 0, pvertices);
        gl.glTexCoordPointer(2, 5126, 0, coordBuffer);
        gl.glDrawArrays(6, 0, 4);

        gl.glDisableClientState(32884);
        gl.glDisableClientState(32888);
        gl.glDisable(3553);
        gl.glDisable(3042);
//    Log.e("mapLog","GL混合");
//        gl.glEnable(GL10.GL_BLEND);
//        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//
//        gl.glEnable(GL10.GL_TEXTURE_2D);
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
////
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, pvertices);
//        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
//        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glDisable(GL10.GL_TEXTURE_2D);
//        gl.glDisable(GL10.GL_BLEND);
    }

    public void drawTiles(GL10 gl) {
        if ((this.listTileDraw == null) || (this.listTileDraw.size() == 0)) {
            return;
        }
        for (TileCoordinate tileDraw : this.listTileDraw) {
            if (!tileDraw.isLoadTexture) {
                try {
                    IPoint pt = tileDraw.pointGeo;
                    if ((tileDraw.bitmap != null) && (!tileDraw.bitmap.isRecycled()) && (pt != null)) {
                        tileDraw.textureId = Util.loadTexture(gl, tileDraw.bitmap);
                        if (tileDraw.textureId != 0) {
                            tileDraw.isLoadTexture = true;
                        }
                        tileDraw.bitmap = null;
                    }
                } catch (Throwable localThrowable) {
                    SDKLogHandler.exception(localThrowable, "TileOverlayDelegateImp", "drawTiles");
                    LogManager.writeLog("TileOverlayDelegateImp", localThrowable.toString(), 112);
                }
            }
            if (tileDraw.isLoadTexture) {
                calFPoint(tileDraw);
                displayTextureLabel(gl, tileDraw.textureId, tileDraw.verticesBuffer, this.coordBuffer);
            }
        }
    }

    private ArrayList<TileCoordinate> getTilesInDomain(int iZoolLevel, int iMapWidth, int iMapHeight) // a
    {
        MapProjection prj = mMap.getMapProjection();
        int iCurZoomLevel = iZoolLevel;
        int iCenterTileX = 0;
        int iCenterTileY = 0;
        IPoint pointGeo = null;
        // geo bounds
        int minX = 0x7FFFFFFF;
        int maxX = 0;
        int minY = 0x7FFFFFFF;
        int maxY = 0;

        FPoint fpoint = new FPoint();
        IPoint ipoint = new IPoint();
        IPoint geoCenter = new IPoint();
        // top left
        prj.win2Map(0, 0, fpoint);
        prj.map2Geo(fpoint.x, fpoint.y, ipoint);
        minX = Math.min(minX, ipoint.x);
        maxX = Math.max(maxX, ipoint.x);
        minY = Math.min(minY, ipoint.y);
        maxY = Math.max(maxY, ipoint.y);
        // top right
        prj.win2Map(iMapWidth, 0, fpoint);
        prj.map2Geo(fpoint.x, fpoint.y, ipoint);
        minX = Math.min(minX, ipoint.x);
        maxX = Math.max(maxX, ipoint.x);
        minY = Math.min(minY, ipoint.y);
        maxY = Math.max(maxY, ipoint.y);
        // bottom left
        prj.win2Map(0, iMapHeight, fpoint);
        prj.map2Geo(fpoint.x, fpoint.y, ipoint);
        minX = Math.min(minX, ipoint.x);
        maxX = Math.max(maxX, ipoint.x);
        minY = Math.min(minY, ipoint.y);
        maxY = Math.max(maxY, ipoint.y);
        // bottom right
        prj.win2Map(iMapWidth, iMapHeight, fpoint);
        prj.map2Geo(fpoint.x, fpoint.y, ipoint);
        minX = Math.min(minX, ipoint.x);
        maxX = Math.max(maxX, ipoint.x);
        minY = Math.min(minY, ipoint.y);
        maxY = Math.max(maxY, ipoint.y);
        //当前级别下最小像素坐标计算再减掉一个网格
        minX = minX - (1 << (20 - iCurZoomLevel)) * this.tileWidth;//
        // maxX = maxX + (1 << (20 - iCurZoomLevel)) * this.tileWidth;
        minY = minY - (1 << (20 - iCurZoomLevel)) * this.tileHeight;
        // maxY = maxY + (1 << (20 - iCurZoomLevel)) * this.tileHeight;

        prj.getGeoCenter(geoCenter);
        //计算中心点所在图块编号，当前级别图块编号
        int xIndex = (geoCenter.x >> (20 - iCurZoomLevel)) / this.tileWidth;
        int yIndex = (geoCenter.y >> (20 - iCurZoomLevel)) / this.tileHeight;
        //左上角，20级像素坐标
        int x2 = (xIndex << (20 - iCurZoomLevel)) * this.tileWidth;
        int y2 = (yIndex << (20 - iCurZoomLevel)) * this.tileHeight;

        TileCoordinate tileCenter = new TileCoordinate(xIndex, yIndex,
                iCurZoomLevel, this.iLayerIndex);
//        Log.e("getTilesInDomain","tile_xyz:"+xIndex+"_"+yIndex+"_"+iCurZoomLevel);
        tileCenter.pointGeo = new IPoint(x2, y2);
        calFPoint(tileCenter);

        ArrayList<TileCoordinate> arrListTileCoord = new ArrayList<TileCoordinate>();
        arrListTileCoord.add(tileCenter);

        // 已经计算出了中心图块，然后再循环添加
        iCenterTileX = xIndex;
        iCenterTileY = yIndex;
        boolean boInsideScreen = false;
        int iAroundTileX = 0;
        int iAroundTileY = 0;
        TileCoordinate tileCoordFind = null;
        for (int i = 1; ; i++) {
            boInsideScreen = false;
            for (int iX = iCenterTileX - i; iX <= iCenterTileX + i; iX++) {
                iAroundTileX = iX;
                iAroundTileY = iCenterTileY + i;
                //图块左上角20级像素坐标
                pointGeo = new IPoint((iAroundTileX << (20 - iCurZoomLevel))
                        * this.tileWidth,
                        (iAroundTileY << (20 - iCurZoomLevel))
                                * this.tileHeight);
                if (pointGeo.x < maxX && pointGeo.x > minX && pointGeo.y < maxY
                        && pointGeo.y > minY) {//完全在屏幕内

                    if (boInsideScreen == false) {
                        boInsideScreen = true;
                    }
                    tileCoordFind = new TileCoordinate(iAroundTileX,
                            iAroundTileY, iCurZoomLevel, this.iLayerIndex);
                    tileCoordFind.pointGeo = pointGeo;
                    calFPoint(tileCoordFind);
                    arrListTileCoord.add(tileCoordFind);
                }

                iAroundTileY = iCenterTileY - i;

                pointGeo = new IPoint((iAroundTileX << (20 - iCurZoomLevel))
                        * this.tileWidth,
                        (iAroundTileY << (20 - iCurZoomLevel))
                                * this.tileHeight);
                if (pointGeo.x < maxX && pointGeo.x > minX && pointGeo.y < maxY
                        && pointGeo.y > minY) {
                    if (boInsideScreen == false) {
                        boInsideScreen = true;
                    }
                    tileCoordFind = new TileCoordinate(iAroundTileX,
                            iAroundTileY, iCurZoomLevel, this.iLayerIndex);
                    tileCoordFind.pointGeo = pointGeo;
                    calFPoint(tileCoordFind);
                    arrListTileCoord.add(tileCoordFind);
                }
            }

            for (int iY = iCenterTileY + i - 1; iY > iCenterTileY - i; iY--) {
                iAroundTileX = iCenterTileX + i;
                iAroundTileY = iY;

                pointGeo = new IPoint((iAroundTileX << (20 - iCurZoomLevel))
                        * this.tileWidth,
                        (iAroundTileY << (20 - iCurZoomLevel))
                                * this.tileHeight);
                if (pointGeo.x < maxX && pointGeo.x > minX && pointGeo.y < maxY
                        && pointGeo.y > minY) {
                    if (boInsideScreen == false) {
                        boInsideScreen = true;
                    }
                    tileCoordFind = new TileCoordinate(iAroundTileX,
                            iAroundTileY, iCurZoomLevel, this.iLayerIndex);
                    tileCoordFind.pointGeo = pointGeo;
                    calFPoint(tileCoordFind);
                    arrListTileCoord.add(tileCoordFind);
                }

                iAroundTileX = iCenterTileX - i;

                pointGeo = new IPoint((iAroundTileX << (20 - iCurZoomLevel))
                        * this.tileWidth,
                        (iAroundTileY << (20 - iCurZoomLevel))
                                * this.tileHeight);
                if (pointGeo.x < maxX && pointGeo.x > minX && pointGeo.y < maxY
                        && pointGeo.y > minY) {
                    if (boInsideScreen == false) {
                        boInsideScreen = true;
                    }
                    tileCoordFind = new TileCoordinate(iAroundTileX,
                            iAroundTileY, iCurZoomLevel, this.iLayerIndex);
                    tileCoordFind.pointGeo = pointGeo;
                    calFPoint(tileCoordFind);
                    arrListTileCoord.add(tileCoordFind);
                }
            }

            if (boInsideScreen == false) {
                break;
            }
        }

        return arrListTileCoord;
    }

    private boolean refreshLayerTiles(List<TileCoordinate> tileFound, int iCurrentZoomLevel, boolean needDowndload) // a
    {
        if (tileFound == null) {
            return false;
        }
        if (this.listTileDraw == null) {
            return false;
        }

        //是否有缓存
        for (TileCoordinate tile : this.listTileDraw) {
            boolean found = false;
            for (TileCoordinate tile2 : tileFound) {
                if (tile.equals(tile2) && tile.isLoadTexture) {
                    tile2.isLoadTexture = tile.isLoadTexture;
                    tile2.textureId = tile.textureId;
                    found = true;
                    break;
                }
            }
            if (!found) {
                tile.destroy();
            }
        }
        this.listTileDraw.clear();
        if ((iCurrentZoomLevel > (int) this.mMap.getMaxZoomLevel()) ||
                (iCurrentZoomLevel < (int) this.mMap.getMinZoomLevel())) {
            return false;
        }
        int iTileSize = tileFound.size();
        if (iTileSize <= 0) {
            return false;
        }
        TileCoordinate tileCoordInDomain = null;
        for (int i2 = 0; i2 < iTileSize; i2++) {
            tileCoordInDomain = (TileCoordinate) tileFound.get(i2);
            if (tileCoordInDomain != null) {
                if ((!this.e) || (
                        (tileCoordInDomain.Zoom >= 10) &&
                                (!RegionUtil.a(tileCoordInDomain.X, tileCoordInDomain.Y, tileCoordInDomain.Zoom))))
                {
                    this.listTileDraw.add(tileCoordInDomain);
                    if (!tileCoordInDomain.isLoadTexture) {
                        this.mImageFetcher.a(needDowndload, tileCoordInDomain);
                    }
                }
            }
        }
        return true;
    }

    public void refresh(boolean needDownload) //b
    {
        if (this.isFlingState) {
            return;
        }
        if ((this.tileServer != null) &&
                (this.tileServer.getStatus() == AsyncTaskDecode.Status.RUNNING)) {
            this.tileServer.cancel(true);
        }
        this.tileServer = new TileServer(needDownload);
        this.tileServer.execute(mMap);
    }

    public void onPause() //g
    {
        this.mImageFetcher.setPauseWork(false);
        this.mImageFetcher.setExitTasksEarly(true);
        this.mImageFetcher.flushCache();
    }

    public void onResume() //h
    {
        this.mImageFetcher.setExitTasksEarly(false);
    }

    public void onFling(boolean paramBoolean) //c
    {
        if (this.isFlingState != paramBoolean) {
            this.isFlingState = paramBoolean;
            this.mImageFetcher.setPauseWork(paramBoolean);
        }
    }

    private class TileServer //b
            extends AsyncTaskDecode<IMapDelegate, Void, List<TileOverlayDelegateImp.TileCoordinate>> {
        private int zoom; //e
        private boolean needDownload; //f

        public TileServer(boolean needDownload) {
            this.needDownload = needDownload;
        }

        //protected List<bm.a> a(aa... paramVarArgs)
        protected List<TileOverlayDelegateImp.TileCoordinate> doInBackground(IMapDelegate... map) {
            int width;
            int height;
            try {
                width = map[0].getMapWidth();
                height = map[0].getMapHeight();
                this.zoom = ((int) map[0].getZoomLevel());
            } catch (Throwable localThrowable) {
                width = 0;
                height = 0;
            }
            if ((width <= 0) || (height <= 0)) {
                return null;
            }
            //return bm.a(bm.this, this.e, i, j);
            return TileOverlayDelegateImp.this.getTilesInDomain(zoom, width, height);
        }

        protected void onPostExecute(List<TileOverlayDelegateImp.TileCoordinate> result) // a
        {
            if (result == null) {
                return;
            }
            int i = result.size();
            if (i <= 0) {
                return;
            }
            //bm.a(bm.this, paramList, this.e, this.f);
            TileOverlayDelegateImp.this.refreshLayerTiles(result, this.zoom, this.needDownload);
            result.clear();
            result = null;
        }
    }

    public class TileCoordinate // a
            implements Cloneable {
        public int X; // a
        public int Y; //b
        public int Zoom; //c
        public int d; // d
        public IPoint pointGeo; //e
        public int textureId = 0; //f
        public boolean isLoadTexture = false; //g
        public FloatBuffer verticesBuffer = null; //h
        public Bitmap bitmap = null; //i
        public ImageWorkerDecode.BitmapWorkerTask task = null; //j
        public int k = 0; //k

        public TileCoordinate(int tile_x, int tile_y, int tile_z, int paramInt4) {
            this.X = tile_x;
            this.Y = tile_y;
            this.Zoom = tile_z;
            this.d = paramInt4;
        }

        public TileCoordinate(TileCoordinate parama) {
            this.X = parama.X;
            this.Y = parama.Y;
            this.Zoom = parama.Zoom;
            this.d = parama.d;
            this.pointGeo = parama.pointGeo;
            this.verticesBuffer = parama.verticesBuffer;
        }

        public TileCoordinate a() // a
        {
            TileCoordinate locala = null;
            try {
                locala = (TileCoordinate) super.clone();
                locala.X = this.X;
                locala.Y = this.Y;
                locala.Zoom = this.Zoom;
                locala.d = this.d;
                locala.pointGeo = ((IPoint) this.pointGeo.clone());
                locala.verticesBuffer = this.verticesBuffer.asReadOnlyBuffer();
            } catch (CloneNotSupportedException localCloneNotSupportedException) {
                localCloneNotSupportedException.printStackTrace();
            }
            //return new a(bm.this, this); //??????????
            return locala;
        }

        public boolean equals(Object paramObject) {
            if (this == paramObject) {
                return true;
            }
            if (!(paramObject instanceof TileCoordinate)) {
                return false;
            }
            TileCoordinate locala = (TileCoordinate) paramObject;
            return (this.X == locala.X) && (this.Y == locala.Y) && (this.Zoom == locala.Zoom) && (this.d == locala.d);
        }

        public int hashCode() {
            return this.X * 7 + this.Y * 11 + this.Zoom * 13 + this.d;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.X);
            stringBuilder.append("-");
            stringBuilder.append(this.Y);
            stringBuilder.append("-");
            stringBuilder.append(this.Zoom);
            stringBuilder.append("-");
            stringBuilder.append(this.d);
            return stringBuilder.toString();
        }

        public void getBitmapFromMemCache(Bitmap paramBitmap) // a
        {
            if ((paramBitmap != null) && (!paramBitmap.isRecycled())) {
                try {
                    this.task = null;
                    int m = paramBitmap.getWidth();
                    int n = paramBitmap.getHeight();
                    this.bitmap = Util.a(paramBitmap, Util.pow2(m),
                            Util.pow2(n));
                    mMap.setRunLowFrame(false);
                } catch (Throwable localThrowable) {
                    SDKLogHandler.exception(localThrowable, "TileOverlayDelegateImp", "setBitmap");

                    localThrowable.printStackTrace();
                    if (this.k < 3) {
                        //bm.b(bm.this).a(true, this);
                        TileOverlayDelegateImp.this.mImageFetcher.a(true, this);
                        this.k += 1;
                    }
                }
            } else if (this.k < 3) {
                //bm.b(bm.this).a(true, this);
                TileOverlayDelegateImp.this.mImageFetcher.a(true, this);
                this.k += 1;
            }
            if ((paramBitmap != null) && (!paramBitmap.isRecycled())) {
                paramBitmap.recycle();
                paramBitmap = null;
            }
        }

        public void destroy() //b
        {
            ImageWorkerDecode.cancelWork(this);
            if (this.isLoadTexture) {
                //bm.c(bm.this).c.add(Integer.valueOf(this.f));
                mTileOverlayView.recycleTextureIds.add(Integer.valueOf(this.textureId));
            }
            this.isLoadTexture = false;
            this.textureId = 0;
            if ((this.bitmap != null) && (!this.bitmap.isRecycled())) {
                this.bitmap.recycle();
            }
            this.bitmap = null;
            if (this.verticesBuffer != null) {
                this.verticesBuffer.clear();
            }
            this.verticesBuffer = null;
            this.task = null;
        }


    }
}
