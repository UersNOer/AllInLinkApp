package com.unistrong.api.mapcore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.unistrong.api.mapcore.util.ResourcesUtilDecode;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.mapcore.util.SDKLogHandler;

import java.io.InputStream;

/**
 * LOGO资源管理
 */
class WaterMarkerViewDecode
        extends View {
    private Bitmap waterBitmap_white_zoom;
    private Bitmap waterBitmap_black_zoom;
    private Bitmap waterBitmap_white;
    private Bitmap waterBitmap_black;
    private Bitmap customBitmap;
    private Paint mPaint = new Paint();
    private boolean isBlack = false;
    private MapDelegateImp mMapView;
    private int logoPosition = 0;  //0左下，1居中，2右下，-1代表用户自定义位置
    private int topleftx;
    private int toplefty;
    private int imgMargin = 15; //留出5个像素位置，防止LOGO贴边

    public void destroy() {
        try {
            if (this.waterBitmap_white_zoom != null) {
                this.waterBitmap_white_zoom.recycle();
            }
            if (this.waterBitmap_black_zoom != null) {
                this.waterBitmap_black_zoom.recycle();
            }
            this.waterBitmap_white_zoom = null;
            this.waterBitmap_black_zoom = null;
            if (this.waterBitmap_white != null) {
                this.waterBitmap_white.recycle();
                this.waterBitmap_white = null;
            }
            if (this.waterBitmap_black != null) {
                this.waterBitmap_black.recycle();
                this.waterBitmap_black = null;
            }
            this.mPaint = null;
        } catch (Throwable localThrowable) {
            SDKLogHandler.exception(localThrowable, "WaterMarkerView", "destory");
            localThrowable.printStackTrace();
        }
    }

    public WaterMarkerViewDecode(Context paramContext) {
        super(paramContext);
    }

    public WaterMarkerViewDecode(Context paramContext, MapDelegateImp map) {
        super(paramContext);
        this.mMapView = map;
        InputStream localInputStream1 = null;
        InputStream localInputStream2 = null;
        try {
            localInputStream1 = ResourcesUtilDecode.getSelfAssets(paramContext).open("logo.png");
            this.waterBitmap_white = BitmapFactory.decodeStream(localInputStream1);
            this.waterBitmap_white_zoom = Util.zoomBitmap(this.waterBitmap_white, ConfigableConstDecode.Resolution);

            localInputStream2 = ResourcesUtilDecode.getSelfAssets(paramContext).open("logo.png");
            this.waterBitmap_black = BitmapFactory.decodeStream(localInputStream2);
            this.waterBitmap_black_zoom = Util.zoomBitmap(this.waterBitmap_black, ConfigableConstDecode.Resolution);
        } catch (Throwable localThrowable) {
            SDKLogHandler.exception(localThrowable, "WaterMarkerView", "create");
            localThrowable.printStackTrace();
        } finally {
            try {
                if (localInputStream1 != null) {
                    localInputStream1.close();
                }
                if (localInputStream2 != null) {
                    localInputStream2.close();
                }
            } catch (Exception ex) {

            }
        }
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.BLACK); //-16777216);
        this.mPaint.setStyle(Paint.Style.STROKE);
    }

    public Bitmap getBitmap() {
        if (this.customBitmap != null)
            return this.customBitmap;

        if (this.isBlack) {
            return this.waterBitmap_black_zoom;
        }

        return this.waterBitmap_white_zoom;
    }

    /**
     * 根据白天黑夜模式设置LOGO图片
     *
     * @param isBlack 是否是黑夜模式
     */
    public void changeBitmap(boolean isBlack) {
        this.isBlack = isBlack;
        if (isBlack) {
            this.mPaint.setColor(Color.WHITE);
        } else {
            this.mPaint.setColor(Color.BLACK);
        }
        invalidate();
    }

    public Point getPosition() {
        //获取绘制的图片及宽高
        Bitmap bitmap = getBitmap();
        if (null == bitmap)
            return new Point(0, 0);

        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();
        int maxY = getHeight() - imgHeight - imgMargin;
        int maxX = getWidth() - imgWidth - imgMargin;
        //确定最终绘制位置
        int imgtopleftx = 0;
        int imgtoplefty = 0;

        switch (logoPosition) {
            case 0: //居左
                imgtopleftx = imgMargin;
                imgtoplefty = this.getHeight() - imgHeight - imgMargin;
                break;
            case 1: //居中
                imgtopleftx = (this.mMapView.getWidth() - imgWidth) / 2;
                imgtoplefty = this.getHeight() - imgHeight - imgMargin;
                break;
            case 2: //局右
                imgtopleftx = this.mMapView.getWidth() - imgWidth - imgMargin;
                imgtoplefty = this.getHeight() - imgHeight - imgMargin;
                break;
            case -1: //用户自定义
                imgtopleftx = topleftx >= maxX ? maxX : topleftx;
                imgtoplefty = toplefty >= maxY ? maxY : toplefty;
                break;
            default:
                break;
        }
        return new Point(imgtopleftx, imgtoplefty);
    }

    public void setLogoPosition(int position) {
        this.logoPosition = position;
    }

    @Override
    public void onDraw(Canvas paramCanvas) {
        try {
            //获取绘制的图片及宽高
            Bitmap bitmap = getBitmap();
            if (null == bitmap)
                return;

            //确定最终绘制位置
            Point position = getPosition();

            //绘制
            paramCanvas.drawBitmap(bitmap, position.x, position.y, this.mPaint);
        } catch (Throwable localThrowable) {
            SDKLogHandler.exception(localThrowable, "WaterMarkerView", "onDraw");
            localThrowable.printStackTrace();
        }
    }

    public void setBitMap(Bitmap bitMap) {
        if (bitMap != null)
            this.customBitmap = Util.zoomBitmap(bitMap, ConfigableConstDecode.Resolution);
        else this.customBitmap = null;
        invalidate();
    }

    public void setLogoPositionByPix(int x, int y) {
        this.topleftx = x;
        this.toplefty = y;
        this.logoPosition = -1;
    }
}
