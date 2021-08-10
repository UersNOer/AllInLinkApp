package com.unistrong.api.mapcore;

import android.content.Context;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
/**
 * 该类只负责计算动画过程中的属性值
 *
 */
public class CameraAnimatorDecode//n
{
  private int mMode;// 动画模式 FLING_MODE(加速) &　CHANGE_CAMERA_MODE(正常移动)
  private int mStartX;//b
  private int mStartY;//c
  private float mStartZ;// d
  private float mStartBearing;//e
  private float mStartTilt;//f
  private int mFinalX;//g
  private int mFinalY;//h
  private float mFinalZ;//i
  private float mFinalBearing;//j
  private float mFinalTilt;//k
  private int mMinX;//l
  private int mMaxX;//m
  private int mMinY;//n
  private int mMaxY;//o
  private int mCurrX;//p
  private int mCurrY;//q
  private float mCurrZ;//r
  private float mCurrBearing;//s
  private float mCurrTilt;//t
  private long mStartTime;//u
  private long mDuration;//v
  private float mDurationReciprocal;//w
  private float mDeltaX;//x
  private float mDeltaY;//y
  private float mDeltaZ;//z
  private float mDeltaBearing;//A
  private float mDeltaTilt;//B
  private boolean mFinished;//C
  private Interpolator mInterpolator;//D
  private boolean mFlywheel;//E
  private float mVelocity;//F
  private float mCurrVelocity;//G
  private int mDistance;//H
  private float mFlingFriction = ViewConfiguration.getScrollFriction();
  private static float DECELERATION_RATE = (float)(Math.log(0.78) / Math.log(0.9));
  private static final float INFLEXION = 0.35f; // Tension lines cross at
  // (INFLEXION, 1)
  private static final float START_TENSION = 0.5f;
  private static final float END_TENSION = 1.0f;
  private static final float P1 = START_TENSION * INFLEXION;
  private static final float P2 = 1.0f - END_TENSION * (1.0f - INFLEXION);

  private static final int NB_SAMPLES = 100;
  private static final float[] SPLINE_POSITION = new float[NB_SAMPLES+1];//K
  private static final float[] SPLINE_TIME = new float[NB_SAMPLES+1];
  private float mDeceleration;//M
  private final float N;
  private float O;
  private boolean P;
  private boolean isLocModeChanged; // 是否过渡动画
  private static float sViscousFluidScale;//Q
  private static float sViscousFluidNormalize ;//R
  static final int FLING_MODE = 1;//新增
  static final int CHANGE_CAMERA_MODE = 2;//新增
  static
  {
    float x_min = 0.0f;//f1
    float y_min = 0.0f;//f2
    for (int i = 0; i < NB_SAMPLES; i++)
    {
//      float f3 = i1 / 100.0F;
      final float alpha = (float) i / NB_SAMPLES;//上句翻译

      float x_max = 1.0F;
      float x;//f5
      float coef;//f7
      while (true)
      {
        x = x_min + (x_max - x_min) / 2.0F;
        coef = 3.0F * x * (1.0F - x);
        float tx = coef * ((1.0F - x) * P1 + x * P2) + x * x * x;//tx--f6
        if (Math.abs(tx - alpha) < 1E-5) {
          break;
        }
        if (tx > alpha) {
          x_max = x;
        } else {
          x_min = x;
        }
      }
      SPLINE_POSITION[i] = (coef * ((1.0F - x) * START_TENSION + x) + x * x * x);
      
      float y_max = 1.0F;//f8
      float y;//f9
      while (true)//上句翻译
      {
        y = y_min + (y_max - y_min) / 2.0F;
        coef = 3.0F * y * (1.0F - y);
        float dy = coef * ((1.0F - y) * START_TENSION + y) + y * y * y;
        if (Math.abs(dy - alpha) < 1E-5) {
          break;
        }
        if (dy > alpha) {
          y_max = y;
        } else {
          y_min = y;
        }
      }
      SPLINE_TIME[i] = (coef * ((1.0F - y) * P1 + y * P2) + y * y * y);
    }
    SPLINE_POSITION[NB_SAMPLES] = (SPLINE_TIME[NB_SAMPLES] = 1.0F);

    sViscousFluidScale = 8.0f;
    sViscousFluidNormalize = 1.0F;
    sViscousFluidNormalize = 1.0F / viscousFluid(1.0F);
  }
  

  
  public CameraAnimatorDecode(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public CameraAnimatorDecode(Context paramContext, Interpolator paramInterpolator)
  {
    this(paramContext, paramInterpolator, 
    
      paramContext.getApplicationInfo().targetSdkVersion >= 11);
  }
  
  public CameraAnimatorDecode(Context context, Interpolator paramInterpolator, boolean flywheel)
  {
    this.mFinished = true;
    this.mInterpolator = paramInterpolator;
    this.N = (context.getResources().getDisplayMetrics().density * 160.0F);
    this.mDeceleration = b(
      ViewConfiguration.getScrollFriction());
    this.mFlywheel = flywheel;
    
    this.O = b(0.84F);
  }
  
  public void setInterpolator(Interpolator interpolator)
  {
    this.mInterpolator = interpolator;
  }
  
  private float b(float paramFloat)
  {
    return 386.0878F * this.N * paramFloat;
  }
  /**
   *
   * Returns whether the scroller has finished scrolling.
   *
   * @return True if the scroller has finished scrolling, false otherwise.
   */
  public final boolean isFinished()
  {

    return this.mFinished;
  }
  /**
   * Returns how long the scroll event will take, in milliseconds.
   *
   * @return The duration of the scroll in milliseconds.
   */
  public final void forceFinished(boolean paramBoolean)
  {

    this.mFinished = paramBoolean;
  }
  
  public final int getCurrX()//b
  {
    return this.mCurrX;
  }
  
  public final int getCurrY()
  {
    return this.mCurrY;
  }
  
  public final float getCurrZ()
  {
    return this.mCurrZ;
  }
  
  public final float getCurrBearing()
  {
    return this.mCurrBearing;
  }
  
  public final float getCurrTilt()
  {
    return this.mCurrTilt;
  }
  /**
   * Returns the current velocity.
   *
   * @return The original velocity less the deceleration. Result may be
   *         negative.
   */
  public float getCurrVelocity()
  {
    return this.mMode == FLING_MODE ? this.mCurrVelocity : this.mVelocity - this.mDeceleration * timePassed() / 2000.0F;
  }
  public boolean computeScrollOffset()//h
  {
    if (this.mFinished) {
      return false;
    }
    int timePassed = (int)(AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);//i1
    if (timePassed < this.mDuration)
    {

      switch (this.mMode)
      {
      case FLING_MODE:
        final float t = timePassed / (float)this.mDuration;
        final int index = (int)(NB_SAMPLES * t);
        float distanceCoef = 1.0F;
        float velocityCoef = 0.0F;
        if (index < NB_SAMPLES)
        {
          final float t_inf = (float) index / NB_SAMPLES;
          final float t_sup = (float) (index + 1) / NB_SAMPLES;
          final float d_inf = SPLINE_POSITION[index];
          final float d_sup = SPLINE_POSITION[index + 1];
          velocityCoef = (d_sup - d_inf) / (t_sup - t_inf);
          distanceCoef = d_inf + (t - t_inf) * velocityCoef;
        }
        this.mCurrVelocity = (velocityCoef * this.mDistance / (float)this.mDuration * 1000.0F);
        
        this.mCurrX = (this.mStartX + Math.round(distanceCoef * (this.mFinalX - this.mStartX)));
        
        this.mCurrX = Math.min(this.mCurrX, this.mMaxX);
        this.mCurrX = Math.max(this.mCurrX, this.mMinX);
        
        this.mCurrY = (this.mStartY + Math.round(distanceCoef * (this.mFinalY - this.mStartY)));
        
        this.mCurrY = Math.min(this.mCurrY, this.mMaxY);
        this.mCurrY = Math.max(this.mCurrY, this.mMinY);
        if ((this.mCurrX == this.mFinalX) && (this.mCurrY == this.mFinalY)) {
          this.mFinished = true;
        }
        break;
      case CHANGE_CAMERA_MODE:
        float x = timePassed * mDurationReciprocal;//上句翻译
        if (this.mInterpolator == null) {
          x = viscousFluid(x);
        } else {
          x = mInterpolator.getInterpolation(x);
        }
        this.mCurrX = (this.mStartX + Math.round(x * this.mDeltaX));
        this.mCurrY = (this.mStartY + Math.round(x * this.mDeltaY));
        this.mCurrZ = (this.mStartZ + x * this.mDeltaZ);
        this.mCurrBearing = (this.mStartBearing + x * this.mDeltaBearing);
        this.mCurrTilt = (this.mStartTilt + x * this.mDeltaTilt);
        break;
      }
    }
    else
    {
      this.mCurrX = this.mFinalX;
      this.mCurrY = this.mFinalY;
      this.mCurrZ = this.mFinalZ;
      this.mCurrBearing = this.mFinalBearing;
      this.mCurrTilt = this.mFinalTilt;
      this.mFinished = true;
    }
    return true;
  }

  /**
   * 正常移动
   * @param startX
   * @param startY
   * @param startZ
   * @param startBearing
   * @param startTilt
   * @param dx
   * @param dy
   * @param dz
   * @param dbearing
   * @param dtilt
   * @param duration
   */
  public void startChangeCamera(int startX, int startY, float startZ, float startBearing, float startTilt, int dx,
                                int dy, float dz, float dbearing, float dtilt, long duration)
  {
    this.mMode = CHANGE_CAMERA_MODE;
    this.mFinished = false;
    this.mDuration = duration;
    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
    this.mStartX = startX;
    this.mStartY = startY;
    this.mStartZ = startZ;
    this.mStartBearing = startBearing;
    this.mStartTilt = startTilt;
    this.mFinalX = startX + dx;
    this.mFinalY = startY + dy;
    this.mFinalZ = startZ + dz;
    this.mFinalBearing = startBearing + dbearing;
    this.mFinalTilt = startTilt + dtilt;
    this.mDeltaX = dx;
    this.mDeltaY = dy;
    this.mDeltaZ = dz;
    this.mDeltaBearing = dbearing;
    this.mDeltaTilt = dtilt;
    this.mDurationReciprocal = (1.0F / (float)this.mDuration);
  }

  /**
   * Start scrolling based on a fling gesture. The distance travelled will
   * depend on the initial velocity of the fling.
   *
   * @param startX
   *            Starting point of the scroll (X)
   * @param startY
   *            Starting point of the scroll (Y)
   * @param velocityX
   *            Initial velocity of the fling (X) measured in pixels per
   *            second.
   * @param velocityY
   *            Initial velocity of the fling (Y) measured in pixels per
   *            second
   * @param minX
   *            Minimum X value. The scroller will not scroll past this point.
   * @param maxX
   *            Maximum X value. The scroller will not scroll past this point.
   * @param minY
   *            Minimum Y value. The scroller will not scroll past this point.
   * @param maxY
   *            Maximum Y value. The scroller will not scroll past this point.
   */
  public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY)
  {
    if ((this.mFlywheel) && (!this.mFinished))
    {
      float oldVel = getCurrVelocity();
      float dx = this.mFinalX - this.mStartX;
      float dy = this.mFinalY - this.mStartY;
      float hyp = (float)Math.sqrt(dx * dx + dy * dy);

      float ndx = dx / hyp;
      float ndy = dy / hyp;

      float oldVelocityX = ndx * oldVel;
      float oldVelocityY = ndy * oldVel;
      if ((Math.signum(velocityX) == Math.signum(oldVelocityX)) &&
        (Math.signum(velocityY) == Math.signum(oldVelocityY)))
      {
        velocityX = (int)(velocityX + oldVelocityX);
        velocityY = (int)(velocityY + oldVelocityY);
      }
    }
    this.mMode = FLING_MODE;
    this.mFinished = false;
    
    float velocity = (float)Math.sqrt(velocityX * velocityX + velocityY * velocityY);

    this.mVelocity = velocity;
    this.mDuration = getSplineFlingDuration(velocity);
    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();

    this.mStartX = startX;
    this.mStartY = startY;
    
    float coeffX = velocity == 0.0F ? 1.0F : velocityX / velocity;
    float coeffY = velocity == 0.0F ? 1.0F : velocityY / velocity;
    
    double totalDistance = getSplineFlingDistance(velocity);
    this.mDistance = ((int)(totalDistance * Math.signum(velocity)));
    
    this.mMinX = minX;
    this.mMaxX = maxX;
    this.mMinY = minY;
    this.mMaxY = maxY;

    this.mFinalX = (startX + (int)Math.round(totalDistance * coeffX));
    
    this.mFinalX = Math.min(this.mFinalX, this.mMaxX);
    this.mFinalX = Math.max(this.mFinalX, this.mMinX);
    
    this.mFinalY = (startY + (int)Math.round(totalDistance * coeffY));
    
    this.mFinalY = Math.min(this.mFinalY, this.mMaxY);
    this.mFinalY = Math.max(this.mFinalY, this.mMinY);
  }
  
  private double c(float paramFloat)
  {
    return Math.log(0.35F * Math.abs(paramFloat) / (this.mFlingFriction * this.O));
  }
  
  private int getSplineFlingDuration(float paramFloat)
  {
    double d1 = c(paramFloat);
    double d2 = DECELERATION_RATE - 1.0D;
    return (int)(1000.0D * Math.exp(d1 / d2));
  }
  
  private double getSplineFlingDistance(float paramFloat)
  {
    double d1 = c(paramFloat);
    double d2 = DECELERATION_RATE - 1.0D;
    
    return this.mFlingFriction * this.O * Math.exp(DECELERATION_RATE / d2 * d1);
  }
  
  static float viscousFluid(float x)
  {
    x *= sViscousFluidScale;
    if (x < 1.0F)
    {
      x -= 1.0F - (float)Math.exp(-x);
    }
    else
    {
      float start = 0.36787945F;
      x = 1.0F - (float)Math.exp(1.0F - x);
      x = start + x * (1.0F - start);
    }
    x *= sViscousFluidNormalize;
    return x;
  }

  public int timePassed()
  {
    return (int)(AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
  }
  
  public final int getMode()//j
  {
    return this.mMode;
  }
  
  public void b(boolean paramBoolean)
  {
    this.P = paramBoolean;
  }
  
  public boolean k()
  {
    return this.P;
  }

  public boolean isLocModeChanged() {
    return isLocModeChanged;
  }

  public void setLocModeChanged(boolean locModeChanged) {
    isLocModeChanged = locModeChanged;
  }
}
