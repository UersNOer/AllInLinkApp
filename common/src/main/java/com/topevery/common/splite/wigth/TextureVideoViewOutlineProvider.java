package com.topevery.common.splite.wigth;

import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;

//https://www.cnblogs.com/fuyaozhishang/p/10044836.html
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TextureVideoViewOutlineProvider extends ViewOutlineProvider {
    private float mRadius;

    public TextureVideoViewOutlineProvider(float radius) {
        this.mRadius = radius;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int leftMargin = 0;
        int topMargin = 0;
        Rect selfRect = new Rect(leftMargin, topMargin,
                rect.right - rect.left - leftMargin, rect.bottom - rect.top - topMargin);
        outline.setRoundRect(selfRect, mRadius);
    }


}
