package com.example.android_supervisor.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class MyDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{16843284};
    public static final int HORIZONTAL_LIST = 0;
    public static final int VERTICAL_LIST = 1;
    private Drawable mDivider;
    private int mOrientation;

    public MyDividerItemDecoration(Context context, int orientation) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.mDivider = a.getDrawable(0);
        a.recycle();
        this.setOrientation(orientation);
    }
    public MyDividerItemDecoration(Context context, int orientation, boolean isHide) {

        if(isHide){
            this.mDivider = new ColorDrawable(Color.TRANSPARENT);
        }else{
            TypedArray a = context.obtainStyledAttributes(ATTRS);
            this.mDivider = a.getDrawable(0);
            a.recycle();
        }
        this.setOrientation(orientation);
    }
    public MyDividerItemDecoration(Context context, int orientation, int color) {
//       TypedArray a = context.obtainStyledAttributes(ATTRS);
//        this.mDivider = a.getDrawable(0);
//        a.recycle();
        this.mDivider = new ColorDrawable(color);
        this.setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if(orientation != 0 && orientation != 1) {
            throw new IllegalArgumentException("invalid orientation");
        } else {
            this.mOrientation = orientation;
        }
    }

    public void onDraw(Canvas c, RecyclerView parent) {
        if(this.mOrientation == 1) {
            this.drawVertical(c, parent);
        } else {
            this.drawHorizontal(c, parent);
       }

    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + this.mDivider.getIntrinsicHeight();
            this.mDivider.setBounds(left, top, right, bottom);
            this.mDivider.draw(c);
        }

    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();

        for(int i = 0; i < childCount; ++i) {
        View child = parent.getChildAt(i);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
        int left = child.getRight() + params.rightMargin;
        int right = left + this.mDivider.getIntrinsicHeight();
        this.mDivider.setBounds(left, top, right, bottom);
        this.mDivider.draw(c);
        }
    }

        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            if(this.mOrientation == 1) {
                outRect.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
            }
        }
    }