package com.example.android_supervisor.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.example.android_supervisor.utils.DensityUtils;

/**
 * @author wujie
 */
public class DefaultMonthView extends MonthView {
    private int mRadius;

    /**
     * 当前日期背景颜色画笔
     */
    private Paint mCurDayPaint = new Paint();

    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private float mSchemeBaseLine;


    public DefaultMonthView(Context context) {
        super(context);
        mRadius = DensityUtils.dip2px(getContext(), 16);

        mCurDayPaint.setAntiAlias(true);
        mCurDayPaint.setStyle(Paint.Style.FILL);
        mCurDayPaint.setStrokeWidth(2);
        mCurDayPaint.setColor(0x20000000);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mRadio = dipToPx(getContext(), 7);
        mPadding = dipToPx(getContext(), 4);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

    }

    /**
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return true 则绘制onDrawScheme，因为这里背景色不是是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        mSchemeBasicPaint.setColor(calendar.getSchemeColor());

        canvas.drawCircle(x + mItemWidth - mPadding - mRadio / 2, y + mPadding + mRadio, mRadio, mSchemeBasicPaint);

        canvas.drawText(calendar.getScheme(), x + mItemWidth - mPadding - mRadio, y + mPadding + mSchemeBaseLine, mCurDayPaint);

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        final int cx = x + mItemWidth / 2;
        final int cy = y + mItemHeight / 2;

        if (isSelected) {
            canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + y, mSelectTextPaint);
        } else {
            if (calendar.isCurrentDay()) {
                canvas.drawCircle(cx, cy, mRadius, mCurDayPaint);
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + y, mCurDayTextPaint);
            } else if (calendar.isCurrentMonth()) {
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + y, mCurMonthTextPaint);
            } else {
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + y, mOtherMonthTextPaint);
            }
        }
    }
    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
