package com.example.android_supervisor.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;
import com.example.android_supervisor.utils.DensityUtils;

/**
 * @author wujie
 */
public class DefaultWeekView extends WeekView {
    private int mRadius;

    /**
     * 当前日期背景颜色画笔
     */
    private Paint mCurDayPaint = new Paint();

    public DefaultWeekView(Context context) {
        super(context);
        mRadius = DensityUtils.dip2px(getContext(), 16);

        mCurDayPaint.setAntiAlias(true);
        mCurDayPaint.setStyle(Paint.Style.FILL);
        mCurDayPaint.setStrokeWidth(2);
        mCurDayPaint.setColor(0x20000000);
    }

    /**
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return true 则绘制onDrawScheme，因为这里背景色不是是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        final int cx = x + mItemWidth / 2;
        final int cy = mItemHeight / 2;

        if (isSelected) {
            canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine, mSelectTextPaint);
        } else {
            if (calendar.isCurrentDay()) {
                canvas.drawCircle(cx, cy, mRadius, mCurDayPaint);
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine, mCurDayTextPaint);
            } else if (calendar.isCurrentMonth()) {
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine, mCurMonthTextPaint);
            } else {
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine, mOtherMonthTextPaint);
            }
        }
    }
}
