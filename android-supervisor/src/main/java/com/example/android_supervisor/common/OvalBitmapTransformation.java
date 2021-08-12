package com.example.android_supervisor.common;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * @author wujie
 */
public class OvalBitmapTransformation implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Bitmap bitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        RectF rectF = new RectF(0, 0, source.getWidth(), source.getHeight());
        canvas.drawOval(rectF, paint);

        if (source != bitmap) {
            source.recycle();
        }
        return bitmap;
    }

    @Override
    public String key() {
        return "round-bitmap";
    }
}
