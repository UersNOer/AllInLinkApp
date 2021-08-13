package com.example.android_supervisor.ui.view;//package com.example.android_supervisor.ui.view;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.animation.ValueAnimator;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.PathMeasure;
//import androidx.annotation.Nullable;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//import android.view.animation.DecelerateInterpolator;
//
//import com.example.android_supervisor.R;
//import com.example.android_supervisor.utils.DensityUtils;
//
///**
// * @author wujie
// */
//public class CheckView extends View {
//    private Paint mInnerCirclePaint;
//    private Paint mOuterCirclePaint;
//
//    //打钩的画笔
//    private Paint mCheckPaint;
//
//    //计数器
//    private int circleRadius = -1;
//    private int ringProgress = 0;
//
//    //是否被点亮
//    private boolean isChecked = false;
//
//    //是否处于动画中
//    private boolean isAnimationRunning = false;
//
//    //最后扩大缩小动画中，画笔的宽度的最大倍数
//    private static final int SCALE_TIMES = 6;
//
//    private AnimatorSet mFinalAnimatorSet;
//
//    private int mOuterCircleRadius;
//    private int mInnerCircleRadius;
//
//    private int mOuterCircleColor;
//    private int mInnerCircleColor;
//
//    private int mCheckRadius;
//    private int mCheckRadiusOffset;
//
//    public CheckView(Context context) {
//        this(context, null);
//    }
//
//    public CheckView(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public CheckView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        loadAttributeSet(context, attrs);
//        initAnimatorCounter();
//    }
//
//    private void loadAttributeSet(Context context, AttributeSet attrs) {
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckView);
//        mOuterCircleRadius = a.getDimensionPixelOffset(R.styleable.CheckView_outer_circle_radius, DensityUtils.dip2px(context, 45));
//        mInnerCircleRadius = a.getDimensionPixelOffset(R.styleable.CheckView_inner_circle_radius, DensityUtils.dip2px(context, 40));
//
//        mOuterCircleColor = a.getColor(R.styleable.CheckView_outer_circle_color, 0xff3d8bff);
//        mInnerCircleColor = a.getColor(R.styleable.CheckView_inner_circle_color, 0xffffffff);
//
//        mCheckRadius = DensityUtils.dip2px(context, 12);
//        mCheckRadiusOffset = DensityUtils.dip2px(context, 4);
//        a.recycle();
//
//        mOuterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mOuterCirclePaint.setStyle(Paint.Style.FILL);
//        mOuterCirclePaint.setColor(mOuterCircleColor);
//
//        mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mInnerCirclePaint.setStyle(Paint.Style.FILL);
//        mInnerCirclePaint.setColor(mInnerCircleColor);
//
//        mCheckPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggle();
//            }
//        });
//
//        initAnimatorCounter();
//    }
//
//    /**
//     * 用ObjectAnimator初始化一些计数器
//     */
//    private void initAnimatorCounter() {
//        //圆环进度
//        ObjectAnimator mRingAnimator = ObjectAnimator.ofInt(this, "ringProgress", 0, 360);
//        mRingAnimator.setDuration(500);
//        mRingAnimator.setInterpolator(null);
//
//        //收缩动画
//        ObjectAnimator mCircleAnimator = ObjectAnimator.ofInt(this, "circleRadius", mOuterCircleRadius - 5, 0);
//        mCircleAnimator.setInterpolator(new DecelerateInterpolator());
//        mCircleAnimator.setDuration(300);
//        Animator mTickAnima;
//
//        //勾勾采取动态画出
//        mTickAnima = ValueAnimator.ofFloat(0.0f, 1.0f);
//        mTickAnima.setDuration(400);
//        mTickAnima.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//                setTickProgress(0);
//                mPathMeasure.nextContour();
//                mPathMeasure.setPath(mTickPath, false);
//                mTickPathMeasureDst.reset();
//            }
//        });
//        ((ValueAnimator) mTickAnima).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                setTickProgress((Float) animation.getAnimatedValue());
//            }
//        });
//        mTickAnima.setInterpolator(new DecelerateInterpolator());
//
//        //最后的放大再回弹的动画，改变画笔的宽度来实现
//        ObjectAnimator mScaleAnimator = ObjectAnimator.ofFloat(this, "ringStrokeWidth", mOuterCirclePaint.getStrokeWidth(), mOuterCirclePaint.getStrokeWidth() * SCALE_TIMES, mOuterCirclePaint.getStrokeWidth() / SCALE_TIMES);
//        mScaleAnimator.setInterpolator(null);
//        mScaleAnimator.setDuration(450);
//
//        //打钩和放大回弹的动画一起执行
//        AnimatorSet mAlphaScaleAnimatorSet = new AnimatorSet();
//        mAlphaScaleAnimatorSet.playTogether(mTickAnima, mScaleAnimator);
//
//        mFinalAnimatorSet = new AnimatorSet();
//        mFinalAnimatorSet.playSequentially(mRingAnimator, mCircleAnimator, mAlphaScaleAnimatorSet);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        final int measureWidth;
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        if (widthMode == MeasureSpec.EXACTLY) {
//            measureWidth = widthSize;
//        } else {
//            measureWidth = (mOuterCircleRadius + (mOuterCircleRadius - mInnerCircleRadius)) * 2;
//        }
//
//        final int measureHeight;
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        if (heightMode == MeasureSpec.EXACTLY) {
//            measureHeight = heightSize;
//        } else {
//            measureHeight = (mOuterCircleRadius + (mOuterCircleRadius - mInnerCircleRadius)) * 2;
//        }
//
//        setMeasuredDimension(measureWidth, measureHeight);
//
////        int checkRadius = mInnerCircleRadius / 2;
//
//
//
////        //设置打钩的几个点坐标
////        final float startX = centerX - mCheckRadius + mCheckRadiusOffset;
////        final float startY = (float) centerY;
////
////        final float middleX = centerX - mCheckRadius / 2 + mCheckRadiusOffset;
////        final float middleY = centerY + mCheckRadius / 2;
////
////        final float endX = centerX + mCheckRadius * 2 / 4 + mCheckRadiusOffset;
////        final float endY = centerY - mCheckRadius * 2 / 4;
//
//        mTickPath.reset();
//        mTickPath.moveTo(startX, startY);
//        mTickPath.lineTo(middleX, middleY);
//        mTickPath.lineTo(endX, endY);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (!isChecked) {
//            mCheckPaint.setColor(0xffe0e0e0);
//            mOuterCirclePaint.setColor(0xffe0e0e0);
//            canvas.drawArc(mRectF, 90, 360, true, mOuterCirclePaint);
//            canvas.drawPath(mTickPath, mCheckPaint);
//            return;
//        }
//
//        mCheckPaint.setColor(0xffffffff);
//        mOuterCirclePaint.setColor(0xff3d8bff);
//
//        //画圆弧进度
//        canvas.drawArc(mRectF, 90, ringProgress, true, mOuterCirclePaint);
//
//        //画黄色的背景
//        mInnerCirclePaint.setColor(0xff3d8bff);
//        canvas.drawCircle(centerX, centerY, ringProgress == 360 ? mOuterCircleRadius : 0, mInnerCirclePaint);
//
//        //画收缩的白色圆
//        if (ringProgress == 360) {
//            mInnerCirclePaint.setColor(0xffffffff);
//            canvas.drawCircle(centerX, centerY, circleRadius, mInnerCirclePaint);
//        }
//        //画勾,以及放大收缩的动画
//        if (circleRadius == 0) {
//            mCheckPaint.setAlpha((int) (255 * tickProgress));
//            mPathMeasure.getSegment(0, tickProgress * mPathMeasure.getLength(), mTickPathMeasureDst, true);
//            canvas.drawPath(mTickPathMeasureDst, mCheckPaint);
//            canvas.drawArc(mRectF, 0, 360, false, mOuterCirclePaint);
//        }
//        //ObjectAnimator动画替换计数器
//        if (!isAnimationRunning) {
//            isAnimationRunning = true;
//            mFinalAnimatorSet.start();
//        }
//    }
//
//    /*--------------属性动画---getter/setter begin---------------*/
//
//    private int getRingProgress() {
//        return ringProgress;
//    }
//
//    private void setRingProgress(int ringProgress) {
//        this.ringProgress = ringProgress;
//        postInvalidate();
//    }
//
//    private int getCircleRadius() {
//        return circleRadius;
//    }
//
//    private void setCircleRadius(int circleRadius) {
//        this.circleRadius = circleRadius;
//        postInvalidate();
//    }
//
//    private float tickProgress = 0.0f;
//
//    private float getTickProgress() {
//        return tickProgress;
//    }
//
//    private void setTickProgress(float tickProgress) {
//        this.tickProgress = tickProgress;
//        Log.i("progress", "setTickProgress: " + tickProgress);
//        invalidate();
//    }
//
//    private void setTickAlpha(int tickAlpha) {
//        mCheckPaint.setAlpha(tickAlpha);
//        postInvalidate();
//    }
//
//    private float getRingStrokeWidth() {
//        return mOuterCirclePaint.getStrokeWidth();
//    }
//
//    private void setRingStrokeWidth(float strokeWidth) {
//        mOuterCirclePaint.setStrokeWidth(strokeWidth);
//        postInvalidate();
//    }
//
//     /*--------------属性动画---getter/setter end---------------*/
//
//    /**
//     * 改变状态
//     *
//     * @param checked 选中还是未选中
//     */
//    public void setChecked(boolean checked) {
//        if (this.isChecked != checked) {
//            isChecked = checked;
//            reset();
//        }
//    }
//
//    /**
//     * @return 当前状态是否选中
//     */
//    public boolean isChecked() {
//        return isChecked;
//    }
//
//    /**
//     * 改变当前的状态
//     */
//    public void toggle() {
//        setChecked(!isChecked);
//    }
//
//    /**
//     * 重置
//     */
//    private void reset() {
//        mFinalAnimatorSet.cancel();
//        ringProgress = 0;
//        circleRadius = -1;
//        isAnimationRunning = false;
//        final int radius = mOuterCircleRadius;
//        mRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
//        invalidate();
//    }
//}