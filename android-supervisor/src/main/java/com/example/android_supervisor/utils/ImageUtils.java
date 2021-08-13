package com.example.android_supervisor.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.text.TextPaint;
import android.util.Log;

import com.j256.ormlite.misc.IOUtils;
import com.orhanobut.logger.Logger;
import com.example.android_supervisor.entities.WaterMarkSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author wujie
 */
public class ImageUtils {

    public static File compress(File imageFile, int maxWidth, int maxHeight, int quality, String destDir) throws Exception {
        if (maxWidth <= 0 || maxHeight <= 0) {
            throw new IllegalArgumentException("maxWidth or maxHeight must large than 0");
        }

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        if (bitmap == null) {
            throw new NullPointerException("decoded bitmap is null");
        }
        Bitmap scaledBitmap = compress(bitmap, maxWidth, maxHeight);
        if (scaledBitmap == null) {
            throw new NullPointerException("scaled bitmap is null");
        }

        String imageName = imageFile.getName();
        File destFile = new File(destDir, imageName);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(destFile);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, output);
            copyExifData(imageFile, destFile);
            return destFile;
        } finally {
            IOUtils.closeQuietly(output);
            bitmap.recycle();
            scaledBitmap.recycle();
        }
    }

    public static Bitmap compress(Bitmap bitmap, int maxWidth, int maxHeight) {
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        boolean shouldScaleWidth = maxWidth < originWidth;
        boolean shouldScaleHeight = maxHeight < originHeight;
        boolean shouldScale = shouldScaleWidth || shouldScaleHeight;

        int width = Math.min(originWidth, maxWidth);
        int height = Math.min(originHeight, maxHeight);

        if (shouldScale) {
            int scaledWidth = height * originWidth / originHeight;
            int scaledHeight = width * originHeight / originWidth;

            if (width < height) {
                height = scaledHeight;
            } else if (height < width) {
                width = scaledWidth;
            } else {
                if (originWidth < originHeight) {
                    width = scaledWidth;
                } else if (originHeight < originWidth) {
                    height = scaledHeight;
                }
            }
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    private static void copyExifData(File sourceFile, File destFile) {
        try {
            ExifInterface sourceExif = new ExifInterface(sourceFile.getAbsolutePath());
            ExifInterface destExif = new ExifInterface(destFile.getAbsolutePath());

            for (String attribute : EXIF_ATTRS) {
                if (sourceExif.getAttribute(attribute) != null) {
                    destExif.setAttribute(attribute, sourceExif.getAttribute(attribute));
                }
            }
            destExif.saveAttributes();
        } catch (Exception ex) {
            Logger.e("Error preserving Exif data on selected image: " + ex);
        }
    }
    public static Bitmap netPicToBmp(String src) {
        try {
            Log.d("FileUtil", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            //设置固定大小
            //需要的大小
            float newWidth = 200f;
            float newHeigth = 200f;

            //图片大小
            int width = myBitmap.getWidth();
            int height = myBitmap.getHeight();

            //缩放比例
            float scaleWidth = newWidth / width;
            float scaleHeigth = newHeigth / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeigth);

            Bitmap bitmap = Bitmap.createBitmap(myBitmap, 0, 0, width, height, matrix, true);
            return bitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    private static String[] EXIF_ATTRS = {
            "FNumber",
            "ExposureTime",
            "ISOSpeedRatings",
            "GPSAltitude",
            "GPSAltitudeRef",
            "FocalLength",
            "GPSDateStamp",
            "WhiteBalance",
            "GPSProcessingMethod",
            "GPSTimeStamp",
            "DateTime",
            "Flash",
            "GPSLatitude",
            "GPSLatitudeRef",
            "GPSLongitude",
            "GPSLongitudeRef",
            "Make",
            "Model",
            "Orientation"
    };


    // 时间戳，白色背景
    public static Bitmap watermarkBitmap(Bitmap photo, String text) {
        int width = photo.getWidth();
        int height = photo.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 建立一个空的BItMap

        Canvas canvas = new Canvas(bitmap);// 初始化画布绘制的图像到icon上

        Paint photoPaint = new Paint(); // 建立画笔
        photoPaint.setDither(true); // 获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);// 过滤一些

        Rect src = new Rect(0, 0, width, height);// 创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, height);// 创建一个指定的新矩形的坐标
        canvas.drawBitmap(photo, src, dst, photoPaint);// 将photo 缩放或则扩大到
        // dst使用的填充区photoPaint

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
        textPaint.setTextSize(18);
        textPaint.setTypeface(Typeface.DEFAULT);

        Rect bounds = new Rect();
        // float textWidth = textPaint.measureText(text);// 文字宽
        textPaint.getTextBounds(text, 0, text.length(), bounds);// 计算文字的宽高
        int textWidth = bounds.width();
        int texthHeight = bounds.height();

        float textX = width - textWidth;
        float textY = 0 + texthHeight;

        float left = textX;
        float top = 2;
        float right = width;
        float bottom = top + texthHeight;
        textPaint.setColor(Color.WHITE);
        textPaint.setAlpha(222);
        canvas.drawRect(left, top, right, bottom, textPaint);

        textPaint.setAlpha(255);
        textPaint.setColor(Color.RED);
        canvas.drawText(text, textX, textY, textPaint);
        canvas.save();
        canvas.restore();
        return bitmap;
    }


    public static Bitmap watermarkBitmap(Bitmap photo, String[] texts) {
        if (texts==null){
            return photo;
        }
        int width = photo.getWidth();
        int height = photo.getHeight();


        Paint photoPaint = new Paint(); // 建立画笔
        photoPaint.setDither(true); // 获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);// 过滤一些


        Bitmap bitmap =photo;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 建立一个空的BItMap
        Canvas canvas = new Canvas(bitmap);// 初始化画布绘制的图像到icon上
        Rect src = new Rect(0, 0, width, height);// 创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, height);// 创建一个指定的新矩形的坐标

        for(int i = 0;i < texts.length;i++){

            // dst使用的填充区photoPaint
            canvas.drawBitmap(photo, src, dst, photoPaint);// 将photo 缩放或则扩大到

            TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
            textPaint.setTextSize(18);
            textPaint.setTypeface(Typeface.DEFAULT);

            String text  = texts[i];
            Rect bounds = new Rect();
            // float textWidth = textPaint.measureText(text);// 文字宽
            textPaint.getTextBounds(text, 0, text.length(), bounds);// 计算文字的宽高
            int textWidth = bounds.width();
            int texthHeight = bounds.height();

            float textX = width - textWidth;
            float textY = 0 + texthHeight;

            float left = textX;
            float top = 2 + 2*i ;
            float right = width;
            float bottom = top + texthHeight;
            textPaint.setColor(Color.WHITE);
            textPaint.setAlpha(222);
//            canvas.drawRect(left, top, right, bottom, textPaint);

            textPaint.setAlpha(255);
            textPaint.setColor(Color.RED);

//            canvas.drawText(texts[i], textX, textY+i*textY, textPaint);

            canvas.drawText(texts[0], textX, textY+i*textY, textPaint);
            canvas.drawText(texts[1], textX, textY+2*i*textY, textPaint);
            canvas.drawText(texts[2], textX, textY+3*i*textY, textPaint);

            canvas.save();
            canvas.restore();

        }


        return bitmap;
    }
    public static Bitmap watermarkBitmap(Bitmap photo, String[] texts, WaterMarkSet mWaterSet) {
        if (texts==null){
            return photo;
        }
        int width = photo.getWidth();
        int height = photo.getHeight();


        Paint photoPaint = new Paint(); // 建立画笔
        photoPaint.setDither(true); // 获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);// 过滤一些


        Bitmap bitmap =photo;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 建立一个空的BItMap
        Canvas canvas = new Canvas(bitmap);// 初始化画布绘制的图像到icon上
        Rect src = new Rect(0, 0, width, height);// 创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, height);// 创建一个指定的新矩形的坐标

        for(int i = 0;i < texts.length;i++){

            // dst使用的填充区photoPaint
            canvas.drawBitmap(photo, src, dst, photoPaint);// 将photo 缩放或则扩大到

            TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
            textPaint.setTextSize(mWaterSet.getSize()<1?1:mWaterSet.getSize());
            textPaint.setTypeface(Typeface.DEFAULT);

            String text  = texts[i];
            Rect bounds = new Rect();
            // float textWidth = textPaint.measureText(text);// 文字宽
            textPaint.getTextBounds(text, 0, text.length(), bounds);// 计算文字的宽高
            int textWidth = bounds.width();
            int texthHeight = bounds.height();

            float textX = width - textWidth;
            float textY = 0 + texthHeight;

            float left = textX;
            float top = 2 + 2*i ;
            float right = width;
            float bottom = top + texthHeight;
            textPaint.setColor(Color.WHITE);
            textPaint.setAlpha(222);
//            canvas.drawRect(left, top, right, bottom, textPaint);

            textPaint.setAlpha(255);
            try {
                String[] split = mWaterSet.getColor().split("\\(")[1].split("\\)")[0].split(",");
                String color = toHex(Integer.valueOf(split[0].trim()), Integer.valueOf(split[1].trim()), Integer.valueOf(split[2].trim()));
                textPaint.setColor(Color.parseColor("#"+color));
            }catch (Exception e){
                textPaint.setColor(Color.RED);
            }


//            canvas.drawText(texts[i], textX, textY+i*textY, textPaint);
            if ("2".equals(mWaterSet.getWmWord())){
                float[] textLoca = getTextLoca(mWaterSet.getImgPosition(), width, height, 1, textWidth, texthHeight);
                canvas.drawText(texts[0], textLoca[0], textLoca[1], textPaint);
            }else {
                float[] textLoca = getTextLoca(mWaterSet.getImgPosition(), width, height, 2, textWidth, texthHeight);
                canvas.drawText(texts[0], textLoca[0], textLoca[1], textPaint);
                canvas.drawText(texts[1], textLoca[0], (float) (textLoca[1] + 1.5*textY), textPaint);
            }


            canvas.save();
            canvas.restore();

        }


        return bitmap;
    }
    public static float[] getTextLoca(String loca,int width,int height,int number,float textW,float textH){
        float wUnit = width/3f;
        float hUnit = height/3f;
        float[] xy = new float[2];
        switch (loca){
            case "左上":
                if (number == 1){
                    xy[1] = (hUnit - textH)/2f;
                }else {
                    xy[1] = (hUnit - 3*textH)/2f;
                }
                xy[0] = 2;
                break;
            case "中上":

                xy[0] = (Math.abs(wUnit - textW)/2f) + wUnit;
                if (number == 1){
                    xy[1] = (hUnit - textH)/2f;
                }else {
                    xy[1] = (hUnit - 3*textH)/2f;
                }
                break;
            case "右上":
                if (wUnit > textW){
                    xy[0] = (Math.abs(wUnit - textW)/2f) + 2*wUnit;
                }else {
                    xy[0] = width - textW -2;
                }
                if (number == 1){
                    xy[1] = (hUnit - textH)/2f;
                }else {
                    xy[1] = (hUnit - 3*textH)/2f;
                }
                break;
            case "中左":
                if (number == 1){
                    xy[1] = (hUnit - textH)/2f + hUnit;
                }else {
                    xy[1] = (hUnit - 3*textH)/2f + hUnit;
                }
                xy[0] = 2;
                break;
            case "正中":
                xy[0] = (Math.abs(wUnit - textW)/2f) + wUnit;
                if (number == 1){
                    xy[1] = (hUnit - textH)/2f + hUnit;
                }else {
                    xy[1] = (hUnit - 3*textH)/2f + hUnit;
                }
                break;
            case "中右":
                if (wUnit > textW){
                    xy[0] = (Math.abs(wUnit - textW)/2f) + 2*wUnit;
                }else {
                    xy[0] = width - textW -2;
                }
                if (number == 1){
                    xy[1] = (hUnit - textH)/2f + hUnit;
                }else {
                    xy[1] = (hUnit - 3*textH)/2f + hUnit;
                }
                break;
            case "左下":
                if (number == 1){
                    xy[1] = (hUnit - textH)/2f + 2*hUnit;
                }else {
                    xy[1] = (hUnit - 3*textH)/2f + 2*hUnit;
                }
                xy[0] = 2;
                break;
            case "中下":
                xy[0] = (Math.abs(wUnit - textW)/2f) + wUnit;
                if (number == 1){
                    xy[1] = (hUnit - textH)/2f + 2*hUnit;
                }else {
                    xy[1] = (hUnit - 3*textH)/2f + 2*hUnit;
                }
                break;
            case "右下":
                if (wUnit > textW){
                    xy[0] = (Math.abs(wUnit - textW)/2f) + 2*wUnit;
                }else {
                    xy[0] = width - textW -2;
                }
                if (number == 1){
                    xy[1] = (hUnit - textH)/2f + 2*hUnit;
                }else {
                    xy[1] = (hUnit - 3*textH)/2f + 2*hUnit;
                }
                break;
        }
        return xy;
    }
    public static String toHex(int r, int g, int b){
        String hr = Integer.toHexString(r);
        String hg = Integer.toHexString(g);
        String hb = Integer.toHexString(b);
        hr = hr.length() < 2? "0"+hr:hr;
        hg = hg.length() < 2? "0"+hg:hg;
        hb = hb.length() < 2? "0"+hb:hb;
        return (hr + hg + hb);
    }
}
