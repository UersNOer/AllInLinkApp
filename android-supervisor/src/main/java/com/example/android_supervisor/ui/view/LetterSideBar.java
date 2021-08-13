package com.example.android_supervisor.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.android_supervisor.R;
import com.example.android_supervisor.utils.DensityUtils;

/**
 * @author wujie
 */
public class LetterSideBar extends View {
    private Paint paint;
    private final String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private final int letterSize = letters.length;

    private String currentLetter;
    private int currentPosition;
    boolean mCurrentIsTouch;
    private int itemHeight;

    private OnSideBarChangedListener onSideBarChangedListener;

    public LetterSideBar(Context context) {
        this(context, null);
    }


    public LetterSideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.darken));
        paint.setTextSize(DensityUtils.sp2px(context, 10));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        final int height = getMeasuredHeight();
        this.itemHeight = (height - paddingTop - paddingBottom) / letterSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //每一个字母的高度
        float singleHeight = (float) getHeight() / letterSize;
        //不断循环把绘制字母
        for (int i = 0; i < letterSize; i++) {
            String letter = letters[i];
            //获取字体的宽度
            Rect rect = new Rect();
            paint.getTextBounds(letter, 0, letter.length(), rect);
            float measureTextWidth = rect.width();
            //获取内容的宽度
            int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            float x = getPaddingLeft() + (contentWidth - measureTextWidth) / 2;
            //计算基线位置
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            float baseLine = singleHeight / 2 + (singleHeight * i) +
                    (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            //画字母，后面onTouch的时候需要处理高亮

            if (letters[i].equals(currentLetter) && mCurrentIsTouch) {
                paint.setTextSize(DensityUtils.sp2px(getContext(), 18));
                paint.setColor(Color.RED);
                canvas.drawText(letter, x, baseLine, paint);
            } else {
                paint.setTextSize(DensityUtils.sp2px(getContext(), 14));
                paint.setColor(Color.BLACK);

                canvas.drawText(letter, x, baseLine, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                 mCurrentIsTouch = true;
                break;

            case MotionEvent.ACTION_MOVE:
                float eventY = (int) event.getY();
                int position = (int) eventY / itemHeight;
                if (position < 0) {
                    position = 0;
                }
                if (position > letterSize - 1) {
                    position = letterSize - 1;
                }
                if (position != currentPosition) {
                    currentPosition = position;
                    currentLetter = letters[position];
                    if (onSideBarChangedListener != null) {
                        onSideBarChangedListener.onChanged(position, letters[position]);
                    }
                }
                break;
        }
        invalidate();

        return true;
    }

    public void setOnSideBarChangedListener(OnSideBarChangedListener listener) {
        onSideBarChangedListener = listener;
    }

    public interface OnSideBarChangedListener {
        void onChanged(int position, String letter);
    }
}