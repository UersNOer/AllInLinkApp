//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.android_supervisor.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.example.android_supervisor.R;
import com.github.chengang.library.R.styleable;

public class TickView2 extends View {
    private Context mContext;
    private Paint mPaintCircle;
    private Paint mPaintRing;
    private Paint mPaintTick;
    private RectF mRectF;
    private float[] mPoints;
    private int centerX;
    private int centerY;
    private int circleRadius;
    private int ringProgress;
    private boolean isChecked;
    private boolean clickable;
    private boolean isAnimationRunning;
    private int unCheckBaseColor;
    private int checkBaseColor;
    private int checkTickColor;
    private int radius;
    private float tickRadius;
    private float tickRadiusOffset;
    private static final int SCALE_TIMES = 6;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private TickAnimatorListener mTickAnimatorListener;
    private AnimatorSet mFinalAnimatorSet;
    private int mRingAnimatorDuration;
    private int mCircleAnimatorDuration;
    private int mScaleAnimatorDuration;

    public TickView2(Context context) {
        this(context, (AttributeSet)null);
    }

    public TickView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mRectF = new RectF();
        this.mPoints = new float[8];
        this.circleRadius = -1;
        this.ringProgress = 0;
        this.isChecked = false;
        this.clickable = true;
        this.isAnimationRunning = false;
        this.mContext = context;
        this.initAttrs(attrs);
        this.initPaint();
        this.initAnimatorCounter();
        this.setUpEvent();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = this.mContext.obtainStyledAttributes(attrs, styleable.TickView);
        this.unCheckBaseColor = typedArray.getColor(styleable.TickView_uncheck_base_color, this.getResources().getColor(R.color.divider_color));
        this.checkBaseColor = typedArray.getColor(styleable.TickView_check_base_color, this.getResources().getColor(R.color.tick_yellow));
        this.checkTickColor = typedArray.getColor(styleable.TickView_check_tick_color, this.getResources().getColor(R.color.white));
        this.radius = typedArray.getDimensionPixelOffset(styleable.TickView_radius, dp2px(this.mContext, 30.0F));
        this.clickable = typedArray.getBoolean(styleable.TickView_clickable, true);
        int rateMode = typedArray.getInt(styleable.TickView_rate, 1);
        this.mRingAnimatorDuration = 500;
        this.mCircleAnimatorDuration = 300;
        this.mScaleAnimatorDuration = 450;
        typedArray.recycle();
        this.tickRadius = (float)dp2px(this.mContext, 24/*12.0F*/);
        this.tickRadiusOffset = (float)dp2px(this.mContext, 8/*4.0F*/);
    }

    private void initPaint() {
        if(this.mPaintRing == null) {
            this.mPaintRing = new Paint(1);
        }

        this.mPaintRing.setStyle(Style.STROKE);
        this.mPaintRing.setColor(this.isChecked?this.checkBaseColor:this.unCheckBaseColor);
        this.mPaintRing.setStrokeCap(Cap.ROUND);
        this.mPaintRing.setStrokeWidth((float)dp2px(this.mContext, 10/*2.5F*/));
        if(this.mPaintCircle == null) {
            this.mPaintCircle = new Paint(1);
        }

        this.mPaintCircle.setColor(this.checkBaseColor);
        if(this.mPaintTick == null) {
            this.mPaintTick = new Paint(1);
        }

        this.mPaintTick.setColor(this.isChecked?this.checkTickColor:this.unCheckBaseColor);
        this.mPaintTick.setAlpha(this.isChecked?0:255);
        this.mPaintTick.setStyle(Style.STROKE);
        this.mPaintTick.setStrokeCap(Cap.ROUND);
        this.mPaintTick.setStrokeWidth((float)dp2px(this.mContext, 5/*2.5F*/));
    }

    private void initAnimatorCounter() {
        ObjectAnimator mRingAnimator = ObjectAnimator.ofInt(this, "ringProgress", new int[]{0, 360});
        mRingAnimator.setDuration((long)this.mRingAnimatorDuration);
        mRingAnimator.setInterpolator((TimeInterpolator)null);
        ObjectAnimator mCircleAnimator = ObjectAnimator.ofInt(this, "circleRadius", new int[]{this.radius - 5, 0});
        mCircleAnimator.setInterpolator(new DecelerateInterpolator());
        mCircleAnimator.setDuration((long)this.mCircleAnimatorDuration);
        ObjectAnimator mAlphaAnimator = ObjectAnimator.ofInt(this, "tickAlpha", new int[]{0, 255});
        mAlphaAnimator.setDuration(200L);
        ObjectAnimator mScaleAnimator = ObjectAnimator.ofFloat(this, "ringStrokeWidth", new float[]{this.mPaintRing.getStrokeWidth(), this.mPaintRing.getStrokeWidth() + dp2px(getContext(), 10), this.mPaintRing.getStrokeWidth()});
        mScaleAnimator.setInterpolator((TimeInterpolator)null);
        mScaleAnimator.setDuration((long)this.mScaleAnimatorDuration);
        AnimatorSet mAlphaScaleAnimatorSet = new AnimatorSet();
        mAlphaScaleAnimatorSet.playTogether(new Animator[]{mAlphaAnimator, mScaleAnimator});
        this.mFinalAnimatorSet = new AnimatorSet();
        this.mFinalAnimatorSet.playSequentially(new Animator[]{mRingAnimator, mCircleAnimator, mAlphaScaleAnimatorSet});
        this.mFinalAnimatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(TickView2.this.mTickAnimatorListener != null) {
                    TickView2.this.mTickAnimatorListener.onAnimationEnd(TickView2.this);
                }

            }

            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if(TickView2.this.mTickAnimatorListener != null) {
                    TickView2.this.mTickAnimatorListener.onAnimationStart(TickView2.this);
                }

            }
        });
    }

    private void setUpEvent() {
        if(this.clickable) {
            this.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    isChecked = !isChecked;
                    reset();
                    if(mOnCheckedChangeListener != null) {
                        mOnCheckedChangeListener.onCheckedChanged((TickView2) view, isChecked);
                    }

                }
            });
        }

    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch(mode) {
            case -2147483648:
            case 0:
                mySize = defaultSize;
                break;
            case 1073741824:
                mySize = size;
        }

        return mySize;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = this.getMySize((this.radius + dp2px(this.mContext, 10F)) * 2, widthMeasureSpec);
        int height = this.getMySize((this.radius + dp2px(this.mContext, 10F)) * 2, heightMeasureSpec);
        height = width = Math.max(width, height);
        this.setMeasuredDimension(width, height);
        this.centerX = this.getMeasuredWidth() / 2;
        this.centerY = this.getMeasuredHeight() / 2;
        this.mRectF.set((float)(this.centerX - this.radius), (float)(this.centerY - this.radius), (float)(this.centerX + this.radius), (float)(this.centerY + this.radius));
        this.mPoints[0] = (float)this.centerX - this.tickRadius + this.tickRadiusOffset;
        this.mPoints[1] = (float)this.centerY;
        this.mPoints[2] = (float)this.centerX - this.tickRadius / 2.0F + this.tickRadiusOffset;
        this.mPoints[3] = (float)this.centerY + this.tickRadius / 2.0F;
        this.mPoints[4] = (float)this.centerX - this.tickRadius / 2.0F + this.tickRadiusOffset;
        this.mPoints[5] = (float)this.centerY + this.tickRadius / 2.0F;
        this.mPoints[6] = (float)this.centerX + this.tickRadius * 2.0F / 4.0F + this.tickRadiusOffset;
        this.mPoints[7] = (float)this.centerY - this.tickRadius * 2.0F / 4.0F;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!this.isChecked) {
            canvas.drawArc(this.mRectF, 90.0F, 360.0F, false, this.mPaintRing);
//            canvas.drawLines(this.mPoints, this.mPaintTick);
        } else {
            canvas.drawArc(this.mRectF, 90.0F, (float)this.ringProgress, false, this.mPaintRing);
            this.mPaintCircle.setColor(this.checkBaseColor);
            canvas.drawCircle((float)this.centerX, (float)this.centerY, this.ringProgress == 360?(float)this.radius:0.0F, this.mPaintCircle);
            if(this.ringProgress == 360) {
                this.mPaintCircle.setColor(this.checkTickColor);
                canvas.drawCircle((float)this.centerX, (float)this.centerY, (float)this.circleRadius, this.mPaintCircle);
            }

            if(this.circleRadius == 0) {
                canvas.drawLines(this.mPoints, this.mPaintTick);
                canvas.drawArc(this.mRectF, 0.0F, 360.0F, false, this.mPaintRing);
            }

            if(!this.isAnimationRunning) {
                this.isAnimationRunning = true;
                this.mFinalAnimatorSet.start();
            }

        }
    }

    private int getRingProgress() {
        return this.ringProgress;
    }

    private void setRingProgress(int ringProgress) {
        this.ringProgress = ringProgress;
        this.postInvalidate();
    }

    private int getCircleRadius() {
        return this.circleRadius;
    }

    private void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
        this.postInvalidate();
    }

    private int getTickAlpha() {
        return 0;
    }

    private void setTickAlpha(int tickAlpha) {
        this.mPaintTick.setAlpha(tickAlpha);
        this.postInvalidate();
    }

    private float getRingStrokeWidth() {
        return this.mPaintRing.getStrokeWidth();
    }

    private void setRingStrokeWidth(float strokeWidth) {
        this.mPaintRing.setStrokeWidth(strokeWidth);
        this.postInvalidate();
    }

    public void setChecked(boolean checked) {
        if(this.isChecked != checked) {
            this.isChecked = checked;
            this.reset();
        }

    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void toggle() {
        this.setChecked(!this.isChecked);
    }

    private void reset() {
        this.initPaint();
        this.mFinalAnimatorSet.cancel();
        this.ringProgress = 0;
        this.circleRadius = -1;
        this.isAnimationRunning = false;
        this.mRectF.set((float)(this.centerX - this.radius), (float)(this.centerY - this.radius), (float)(this.centerX + this.radius), (float)(this.centerY + this.radius));
        this.invalidate();
    }

    private static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.mOnCheckedChangeListener = listener;
    }

    public void addAnimatorListener(TickAnimatorListener tickAnimatorListener) {
        this.mTickAnimatorListener = tickAnimatorListener;
    }

    public abstract static class TickAnimatorListenerAdapter implements TickAnimatorListener {
        public TickAnimatorListenerAdapter() {
        }

        public void onAnimationStart(TickView2 tickView) {
        }

        public void onAnimationEnd(TickView2 tickView) {
        }
    }

    public interface TickAnimatorListener {
        void onAnimationStart(TickView2 var1);

        void onAnimationEnd(TickView2 var1);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(TickView2 var1, boolean var2);
    }
}
