package com.unistrong.api.mapcore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLDebugHelper;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

@SuppressLint({"NewApi"})
public class GLTextureView extends TextureView implements TextureView.SurfaceTextureListener {
  public GLTextureView(Context paramContext) {
    super(paramContext);
    setSurfaceTextureListener();
  }
  
  public GLTextureView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setSurfaceTextureListener();
  }
  
  protected void finalize() throws Throwable {
    try {
      if(this.glThread != null) {
        this.glThread.f();
      }
    } finally {
      super.finalize();
    }
  }
  
  private void setSurfaceTextureListener() {
    setSurfaceTextureListener(this);
  }
  
  public void setRenderer(GLSurfaceView.Renderer paramRenderer)
  {
    checkGLThread();
    if (ieglConfig == null) {
      ieglConfig = new EGLConfigImp(true);
    }
    if (ieglContext == null) {
      ieglContext = new IEGLContextImp();
    }
    if (iglSurfaceView == null) {
      iglSurfaceView = new GLSurfaceViewImp();
    }
    this.render = paramRenderer;
    this.glThread = new GLThread(weakReference);
    this.glThread.start();
  }
  
  public void setRenderMode(int mode) {
    this.glThread.renderMode(mode);
  }
  
  public void requestRender() {
    this.glThread.requestRender();
  }
  
  public void queueEvent(Runnable paramRunnable)
  {
    this.glThread.a(paramRunnable);
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (separateWindow && render != null) {
      int m = 1;
      if (this.glThread != null) {
        m = this.glThread.b();
      }
      this.glThread = new GLThread(weakReference);
      if (m != 1) {
        this.glThread.renderMode(m);
      }
      this.glThread.start();
    }
    separateWindow = false;
  }
  
  protected void onDetachedFromWindow() {
    if (this.glThread != null) {
      this.glThread.f();
    }
    separateWindow= true;
    super.onDetachedFromWindow();
  }
  
  public static abstract interface k {
    public abstract GL a(GL paramGL);
  }
  
  public interface IEGLContext {
    EGLContext getEGLContext(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig);
    void setEGLContext(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLContext paramEGLContext);
  }
  
  private class IEGLContextImp implements IEGLContext {
    private int b = 12440;

    public EGLContext getEGLContext(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig) {
      //int[] arrayOfInt = { this.b, x.glThreadManager(x.this), 12344 };
      int[] arrayOfInt = { this.b, k, 12344 }; //上一句翻译
      
      //return paramEGL10.eglCreateContext(paramEGLDisplay, paramEGLConfig, EGL10.EGL_NO_CONTEXT,
      //  x.glThreadManager(x.this) != 0 ? arrayOfInt : null);
      return paramEGL10.eglCreateContext(paramEGLDisplay, paramEGLConfig, EGL10.EGL_NO_CONTEXT, 
    	        k != 0 ? arrayOfInt : null); //上一句翻译
    }
    
    public void setEGLContext(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLContext paramEGLContext) {
      if (!paramEGL10.eglDestroyContext(paramEGLDisplay, paramEGLContext)) {
        Log.e("DefaultContextFactory", "display:" + paramEGLDisplay + " context: " + paramEGLContext);
        
        //x.h.glThreadManager("eglDestroyContex", paramEGL10.eglGetError());
        EGLHelper.thorwError("eglDestroyContex", paramEGL10.eglGetError()); //上一句翻译
      }
    }
  }
  
  public  interface IGLSurfaceView {
      EGLSurface createEGLSurface(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, Object paramObject);
      void destrorySurface(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface);
  }
  
  private static class GLSurfaceViewImp implements GLTextureView.IGLSurfaceView {
    public EGLSurface createEGLSurface(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, Object paramObject) {
      EGLSurface localEGLSurface = null;
      try {
        localEGLSurface = paramEGL10.eglCreateWindowSurface(paramEGLDisplay, paramEGLConfig, paramObject, null);
      } catch (IllegalArgumentException localIllegalArgumentException) {
        Log.e("GLSurfaceView", "eglCreateWindowSurface", localIllegalArgumentException);
      }
      return localEGLSurface;
    }
    
    public void destrorySurface(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface) {
      paramEGL10.eglDestroySurface(paramEGLDisplay, paramEGLSurface);
    }
  }
  
  public  interface IEGLConfig {
    EGLConfig getEGLConfig(EGL10 paramEGL10, EGLDisplay paramEGLDisplay);
  }
  
  private abstract class IEGLConfigImg implements GLTextureView.IEGLConfig
  {
    protected int[] a;
    
    public IEGLConfigImg(int[] paramArrayOfInt) {
      this.a = a(paramArrayOfInt);
    }
    
    public EGLConfig getEGLConfig(EGL10 paramEGL10, EGLDisplay paramEGLDisplay) {
      int[] arrayOfInt = new int[1];
      if (!paramEGL10.eglChooseConfig(paramEGLDisplay, this.a, null, 0, arrayOfInt)) {
        throw new IllegalArgumentException("eglChooseConfig failed");
      }
      int i = arrayOfInt[0];
      if (i <= 0) {
        throw new IllegalArgumentException("No configs match configSpec");
      }
      EGLConfig[] arrayOfEGLConfig = new EGLConfig[i];
      if (!paramEGL10.eglChooseConfig(paramEGLDisplay, this.a, arrayOfEGLConfig, i, arrayOfInt)) {
        throw new IllegalArgumentException("eglChooseConfig#2 failed");
      }
      EGLConfig localEGLConfig = getEGLConfig(paramEGL10, paramEGLDisplay, arrayOfEGLConfig);
      if (localEGLConfig == null) {
        throw new IllegalArgumentException("No config chosen");
      }
      return localEGLConfig;
    }
    
    abstract EGLConfig getEGLConfig(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig[] paramArrayOfEGLConfig);
    
    private int[] a(int[] paramArrayOfInt) {
      //if ((x.glThreadManager(x.this) != 2) && (x.glThreadManager(x.this) != 3)) {
      if ((k != 2) && (k != 3)) { //上一句翻译
        return paramArrayOfInt;
      }
      int i = paramArrayOfInt.length;
      int[] arrayOfInt = new int[i + 2];
      System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, i - 1);
      arrayOfInt[(i - 1)] = 12352;
      //if (x.glThreadManager(x.this) == 2) {
      if (k == 2) { //上一句翻译
        arrayOfInt[i] = 4;
      } else {
        arrayOfInt[i] = 64;
      }
      arrayOfInt[(i + 1)] = 12344;
      return arrayOfInt;
    }
  }
  
  private class EGLConfigManager extends GLTextureView.IEGLConfigImg {
    private int[] value;
    protected int red;
    protected int green;
    protected int blue;
    protected int alpha;
    protected int depth;
    protected int stencil;
    
    public EGLConfigManager(int red, int green, int blue, int alpha, int depth, int stencil) {
      super(new int[] { 12324, red, 12323, green, 12322, blue, 12321, alpha, 12325, depth, 12326, stencil, 12344 });
      this.value = new int[1];
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.alpha = alpha;
      this.depth = depth;
      this.stencil = stencil;
    }
    
    public EGLConfig getEGLConfig(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig[] paramArrayOfEGLConfig) {
      for (EGLConfig localEGLConfig : paramArrayOfEGLConfig)
      {
        int depth = initAttribs(paramEGL10, paramEGLDisplay, localEGLConfig, EGL10.EGL_DEPTH_SIZE, 0);
        
        int stencil = initAttribs(paramEGL10, paramEGLDisplay, localEGLConfig, EGL10.EGL_STENCIL_SIZE, 0);
        if (depth >= this.depth && (stencil >= this.stencil)) {
          int red = initAttribs(paramEGL10, paramEGLDisplay, localEGLConfig, EGL10.EGL_RED_SIZE, 0);
          
          int green = initAttribs(paramEGL10, paramEGLDisplay, localEGLConfig, EGL10.EGL_GREEN_SIZE, 0);
          
          int blue = initAttribs(paramEGL10, paramEGLDisplay, localEGLConfig,EGL10.EGL_BLUE_SIZE, 0);
          
          int alpha = initAttribs(paramEGL10, paramEGLDisplay, localEGLConfig, EGL10.EGL_ALPHA_SIZE, 0);
          if ((red == this.red) && (green == this.green) && (blue == this.blue) && (alpha == this.alpha)) {
            return localEGLConfig;
          }
        }
      }
      return null;
    }
    
    private int initAttribs(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, int paramInt1, int paramInt2) {
      if (paramEGL10.eglGetConfigAttrib(paramEGLDisplay, paramEGLConfig, paramInt1, this.value)) {
        return this.value[0];
      }
      return paramInt2;
    }
  }
  
  private class EGLConfigImp extends EGLConfigManager {
    public EGLConfigImp(boolean isDepth) {
      super(8, 8, 8, 0, isDepth ? 16 : 0, 0);
    }
  }
  
  private static class EGLHelper {
    private WeakReference<GLTextureView> glTextureViewWeakReference;
    EGL10 egl10;
    EGLDisplay eglDisplay;
    EGLSurface eglSurface;
    EGLConfig eglConfig;
    EGLContext eglContext;
    
    public EGLHelper(WeakReference<GLTextureView> glTextureViewWeakReference) {
      this.glTextureViewWeakReference = glTextureViewWeakReference;
    }
    
    public void init() {
      egl10 = ((EGL10)EGLContext.getEGL());
      eglDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
      if (eglDisplay == EGL10.EGL_NO_DISPLAY) {
        throw new RuntimeException("eglGetDisplay failed");
      }
      int[] arrayOfInt = new int[2];
      if (!egl10.eglInitialize(eglDisplay, arrayOfInt)) {
        throw new RuntimeException("eglInitialize failed");
      }
      GLTextureView localx = glTextureViewWeakReference.get();
      if (localx == null) {
        eglConfig = null;
        eglContext = null;
      } else {
        //this.changeBearing = x.b(localx).glThreadManager(this.glThreadManager, this.b);
        //this.e = x.c(localx).glThreadManager(this.glThreadManager, this.b, this.changeBearing);
        eglConfig = localx.ieglConfig.getEGLConfig(egl10, eglDisplay); //上两句翻译
        eglContext = localx.ieglContext.getEGLContext(egl10, eglDisplay, eglConfig);
      }
      if (eglContext== null || eglContext == EGL10.EGL_NO_CONTEXT) {
        eglContext = null;
        thorwError("createContext");
      }
      eglSurface = null;
    }
    
    public boolean initHelper() {
      if (egl10 == null) {
        throw new RuntimeException("egl not initialized");
      }
      if (eglDisplay == null) {
        throw new RuntimeException("eglDisplay not initialized");
      }
      if (eglConfig == null) {
        throw new RuntimeException("mEglConfig not initialized");
      }
      destrorySurface();
      GLTextureView glTextureView = glTextureViewWeakReference.get();
      if (glTextureView != null) {
        //this.c = x.changeBearing(localx).glThreadManager(this.glThreadManager, this.b, this.changeBearing, localx.getSurfaceTexture());
        eglSurface = glTextureView.iglSurfaceView.createEGLSurface(egl10, eglDisplay, eglConfig, glTextureView.getSurfaceTexture()); //上一句翻译
      } else {
        eglSurface= null;
      }
      if ((eglSurface== null) || (eglSurface == EGL10.EGL_NO_SURFACE))
      {
        int i = this.egl10.eglGetError();
        if (i == 12299) {
          Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
        }
        return false;
      }
      if (!this.egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext))
      {
        errorInfo("EGLHelper", "eglMakeCurrent", egl10
          .eglGetError());
        return false;
      }
      return true;
    }
    
    public GL getGL() {
      GL gl = eglContext.getGL();
      GLTextureView glTextureView = glTextureViewWeakReference.get();
      if (glTextureView != null)
      {
        //if (x.e(localx) != null) {
        //  localGL = x.e(localx).glThreadManager(localGL);
        //}
        if (glTextureView.i != null) { //上两句翻译
            gl = glTextureView.i.a(gl);
        }
        //if ((x.f(localx) & 0x3) != 0)
        if ((glTextureView.j & 0x3) != 0)//上一句翻译
        {
          int i = 0;
          WriterLog writerLog = null;
          //if ((x.f(localx) & 0x1) != 0) {
          if ((glTextureView.j & 0x1) != 0) {//上一句翻译
            i |= 0x1;
          }
          //if ((x.f(localx) & 0x2) != 0) {
          if ((glTextureView.j & 0x2) != 0) {//上一句翻译
            writerLog = new WriterLog();
          }
          gl = GLDebugHelper.wrap(gl, i, writerLog);
        }
      }
      return gl;
    }
    
    public int d() {
      if (!egl10.eglSwapBuffers(eglDisplay, eglSurface)) {
        return egl10.eglGetError();
      }
      return 12288;
    }
    
    public void destory() {
      destrorySurface();
    }
    
    private void destrorySurface() {
      if ((eglSurface != null) && (eglSurface != EGL10.EGL_NO_SURFACE)) {
        egl10.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        GLTextureView glTextureView = glTextureViewWeakReference.get();
        if (glTextureView != null) {
          glTextureView.iglSurfaceView.destrorySurface(egl10, eglDisplay, eglSurface); //上一句翻译
        }
        eglSurface = null;
      }
    }
    
    public void f() {
      if (eglContext != null) {
        GLTextureView localx = (GLTextureView)this.glTextureViewWeakReference.get();
        if (localx != null) {
          //x.c(localx).glThreadManager(this.glThreadManager, this.b, this.e);
          localx.ieglContext.setEGLContext(egl10, eglDisplay, eglContext);
        }
        eglContext = null;
      }
      if (eglDisplay!= null) {
        egl10.eglTerminate(eglDisplay);
        eglDisplay = null;
      }
    }
    
    private void thorwError(String paramString) {
      thorwError(paramString, egl10.eglGetError());
    }
    
    public static void thorwError(String paramString, int paramInt) {
      String str = errorInfo(paramString, paramInt);
      throw new RuntimeException(str);
    }
    
    public static void errorInfo(String paramString1, String paramString2, int paramInt) {
      Log.w(paramString1, errorInfo(paramString2, paramInt));
    }
    
    public static String errorInfo(String paramString, int paramInt) {
      return paramString + " failed: " + paramInt;
    }
  }
  
  static class GLThread extends Thread {
    private boolean stopRender;
    private boolean lock;
    private boolean c;
    private boolean d;
    private boolean e;
    private boolean f;
    private boolean g;
    private boolean addGLThread;
    private boolean removeGLThead;
    private boolean j;
    private boolean k;
    private int width;
    private int height;
    private int n;
    private boolean o;
    private boolean p;

    GLThread(WeakReference<GLTextureView> paramWeakReference) {
      this.width = 0;
      this.height = 0;
      this.o = true;
      this.n = 1;
      this.t = paramWeakReference;
    }
    
    public void run() {
    	this.setName("GLThread " + this.getId());
        try {
          drawFrame();
        } catch(InterruptedException v0_1) { //同以下一场捕获颠倒了顺序
            //x.b().lock(this);
        	GLTextureView.glThreadManager.lock(this); //上一句翻译
            return;
        } catch(Throwable v0) {
            //x.b().lock(this);
            GLTextureView.glThreadManager.lock(this); //上一句翻译
            //throw v0; //报错，屏蔽掉了
        }
        //x.b().lock(this);
        GLTextureView.glThreadManager.lock(this); //上一句翻译
    }
    
    private void release() {
      if (removeGLThead) {
        removeGLThead = false;
        eglHelper.destory();
      }
    }
    
    private void addGLThread()
    {
      if (addGLThread) {
        eglHelper.f();
        addGLThread = false;
        //x.b().c(this);
        GLTextureView.glThreadManager.c(this); //上一句翻译
      }
    }

    private void drawFrame() throws InterruptedException {
      eglHelper = new EGLHelper(this.t);
      addGLThread = false;
      removeGLThead = false;
      boolean var32 = false;
      try {
        var32 = true;
        GL10 gl10 = null;
        boolean isCreated = false;
        boolean initHelper = false;
        boolean var4 = false;
        boolean var5 = false;
        boolean isChanged = false;
        boolean var7 = false;
        boolean var8 = false;
        boolean var9 = false;
        int width = 0;
        int height = 0;
        Runnable runnable = null;
        label427:
        while(true) {
          while(true) {
            synchronized(GLTextureView.glThreadManager) {
              while(true) {
                if(this.stopRender) {
                  var32 = false;
                  break label427;
                }

                if(!this.runnableArrayList.isEmpty()) {
                  runnable = this.runnableArrayList.remove(0);
                  break;
                }

                boolean var14 = false;
                if(this.d != this.c) {
                  var14 = this.c;
                  this.d = this.c;
                  GLTextureView.glThreadManager.notifyAll();
                }

                if(this.k) {
                  this.release();
                  this.addGLThread();
                  this.k = false;
                  var9 = true;
                }

                if(var5) {
                  this.release();
                  this.addGLThread();
                  var5 = false;
                }

                if(var14 && removeGLThead) {
                  this.release();
                }

                if(var14 && addGLThread) {
                  GLTextureView var15 = this.t.get();
                  boolean var16 = var15 == null?false:var15.l;
                  if(!var16 || GLTextureView.glThreadManager.a()) {
                    this.addGLThread();
                  }
                }

                if(var14 && GLTextureView.glThreadManager.b()) {
                  eglHelper.f();
                }

                if(!this.e && !this.g) {
                  if(removeGLThead) {
                    this.release();
                  }

                  this.g = true;
                  this.f = false;
                  GLTextureView.glThreadManager.notifyAll();
                }

                if(this.e && this.g) {
                  this.g = false;
                  GLTextureView.glThreadManager.notifyAll();
                }

                if(var8) {
                  var7 = false;
                  var8 = false;
                  this.p = true;
                  GLTextureView.glThreadManager.notifyAll();
                }

                if(this.k()) {
                  if(!addGLThread) {
                    if(var9) {
                      var9 = false;
                    } else if(GLTextureView.glThreadManager.checkGLThread(this)) {
                      try {
                        eglHelper.init();
                      } catch (RuntimeException var38) {
                        GLTextureView.glThreadManager.c(this);
                        throw var38;
                      }
                      addGLThread = true;
                      isCreated = true;
                      GLTextureView.glThreadManager.notifyAll();
                    }
                  }

                  if(addGLThread && !removeGLThead) {
                    removeGLThead = true;
                    initHelper = true;
                    var4 = true;
                    isChanged = true;
                  }

                  if(removeGLThead) {
                    if(this.r) {
                      isChanged = true;
                      width = this.width;
                      height = this.height;
                      var7 = true;
                      initHelper = true;
                      this.r = false;
                    }

                    this.o = false;
                    GLTextureView.glThreadManager.notifyAll();
                    break;
                  }
                }

                GLTextureView.glThreadManager.wait();
              }
            }

            if(runnable != null) {
              runnable.run();
              runnable = null;
            } else {
              if(initHelper) {
                if(!eglHelper.initHelper()) {
                  synchronized(GLTextureView.glThreadManager) {
                    this.j = true;
                    this.f = true;
                    GLTextureView.glThreadManager.notifyAll();
                    continue;
                  }
                }
                synchronized(GLTextureView.glThreadManager) {
                  this.j = true;
                  GLTextureView.glThreadManager.notifyAll();
                }
                initHelper = false;
              }

              if(var4) {
                gl10 = (GL10)eglHelper.getGL();
                GLTextureView.glThreadManager.supportHardwareSpeed(gl10);
                var4 = false;
              }

              GLTextureView glTextureView;
              if(isCreated) {
                glTextureView = this.t.get();
                if(glTextureView != null) {
                  glTextureView.render.onSurfaceCreated(gl10, eglHelper.eglConfig);
                }
                isCreated = false;
              }

              if(isChanged) {
                glTextureView = this.t.get();
                if(glTextureView != null) {
                  glTextureView.render.onSurfaceChanged(gl10, width, height);
                }
                isChanged = false;
              }

              glTextureView = t.get();
              if(glTextureView != null) {
                glTextureView.render.onDrawFrame(gl10);
              }

              int var41 = eglHelper.d();
              switch(var41) {
                case 12288:
                  break;
                case 12302:
                  var5 = true;
                  break;
                default:
                  GLTextureView.EGLHelper.errorInfo("GLThread", "eglSwapBuffers", var41);
                  synchronized(GLTextureView.glThreadManager) {
                    this.f = true;
                    GLTextureView.glThreadManager.notifyAll();
                  }
              }

              if(var7) {
                var8 = true;
              }
            }
          }
        }
      } finally {
        if(var32) {
          synchronized(GLTextureView.glThreadManager) {
            this.release();
            this.addGLThread();
          }
        }
      }

      synchronized(GLTextureView.glThreadManager) {
        this.release();
        this.addGLThread();
      }
    }
    
    public boolean a() {
      return (addGLThread) && (removeGLThead) && (k());
    }
    
    private boolean k() {
      return (!this.d) && (this.e) && (!this.f) && (this.width > 0) && (this.height > 0) && ((this.o) || (this.n == 1));
    }
    
    public void renderMode(int paramInt) {
      if ((0 > paramInt) || (paramInt > 1)) {
        throw new IllegalArgumentException("renderMode");
      }
      //synchronized (x.b())
      synchronized (GLTextureView.glThreadManager) //上一句翻译
      {
        this.n = paramInt;
        //x.b().notifyAll();
        GLTextureView.glThreadManager.notifyAll(); //上一句翻译
      }
    }
    
    public int b() {
    	//如下是 jeb 原始摘录内容
//    	j v1 = x.b();
//        __monitor_enter(v1);
//        try {
//            __monitor_exit(v1);
//            return this.n;
//        label_6:
//            __monitor_exit(v1);
//        }
//        catch(Throwable v0) {
//            goto label_6;
//        }
    	//如下摘录并翻译自jeb -----------
    	synchronized (GLTextureView.glThreadManager) {
    		return this.n;
        }
    	//-----------------
    }
    
    public void requestRender() {
      synchronized (GLTextureView.glThreadManager) {
        this.o = true;
        //x.b().notifyAll();
        GLTextureView.glThreadManager.notifyAll(); //上一句翻译
      }
    }
    
    public void onSurfaceTextureAvailable() {
      synchronized (GLTextureView.glThreadManager) {
        this.e = true;
        this.j = false;
        //x.b().notifyAll();
        GLTextureView.glThreadManager.notifyAll(); //上一句翻译
        while ((this.g) && (!this.j) && (!lock)) {
          try {
            //x.b().wait();
        	  GLTextureView.glThreadManager.wait(); //上一句翻译
          }
          catch (InterruptedException localInterruptedException)
          {
            Thread.currentThread().interrupt();
          }
        }
      }
    }
    
    public void onSurfaceTextureDestroyed() {
      //synchronized ()
      synchronized (GLTextureView.glThreadManager) //上一句翻译
      {
        this.e = false;
        //x.b().notifyAll();
        GLTextureView.glThreadManager.notifyAll();//上一句翻译
        while ((!this.g) && (!lock)) {
          try
          {
            //x.b().wait();
        	  GLTextureView.glThreadManager.wait(); //上一句翻译
          }
          catch (InterruptedException localInterruptedException)
          {
            Thread.currentThread().interrupt();
          }
        }
      }
    }
    
    public void onSurfaceTextureSizeChanged(int width, int height) {
      //synchronized ()
      synchronized (GLTextureView.glThreadManager) //上一句翻译
      {
        this.width = width;
        this.height = height;
        this.r = true;
        this.o = true;
        this.p = false;
        //x.b().notifyAll();
        GLTextureView.glThreadManager.notifyAll();//上一句翻译
        while ((!lock) && (!this.d) && (!this.p) && (a())) {
          try {
            //x.b().wait();
            GLTextureView.glThreadManager.wait(); //上一句翻译
          } catch (InterruptedException localInterruptedException) {
            Thread.currentThread().interrupt();
          }
        }
      }
    }
    
    public void f() {
      //synchronized ()
      synchronized (GLTextureView.glThreadManager) //上一句翻译
      {
        this.stopRender = true;
        //x.b().notifyAll();
        GLTextureView.glThreadManager.notifyAll();//上一句翻译
        while (!lock) {
          try {
            //x.b().wait();
            GLTextureView.glThreadManager.wait(); //上一句翻译
          }
          catch (InterruptedException localInterruptedException)
          {
            Thread.currentThread().interrupt();
          }
        }
      }
    }
    
    public void g() {
      this.k = true;
      //x.b().notifyAll();
      GLTextureView.glThreadManager.notifyAll();//上一句翻译
    }
    
    public void a(Runnable r) {
      if (r == null) {
        throw new IllegalArgumentException("r must not be null");
      }
      //synchronized (x.b())
      synchronized (GLTextureView.glThreadManager)//上一句翻译
      {
        this.runnableArrayList.add(r);
        //x.b().notifyAll();
        GLTextureView.glThreadManager.notifyAll();//上一句翻译
      }
    }
    
    private ArrayList<Runnable> runnableArrayList = new ArrayList<Runnable>();
    private boolean r = true;
    private EGLHelper eglHelper;
    private WeakReference<GLTextureView> t;
  }
  
  static class WriterLog extends Writer {
    private StringBuilder sb = new StringBuilder();
    public void close() {
      removeStr();
    }
    
    public void flush() {
      removeStr();
    }
    
    public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
      for (int i = 0; i < paramInt2; i++)
      {
        char c = paramArrayOfChar[(paramInt1 + i)];
        if (c == '\n') {
          removeStr();
        } else {
          sb.append(c);
        }
      }
    }
    
    private void removeStr() {
      if (sb.length() > 0) {
        sb.delete(0, sb.length());
      }
    }
  }
  
  private void checkGLThread() {
    if (this.glThread != null) {
      throw new IllegalStateException("setRenderer has already been called for this instance.");
    }
  }
  
  private static class GLThreadManager {
    private boolean b;
    private int c;
    private boolean d;
    private boolean e;
    private boolean f;
    private GLThread glThread;
    
    public synchronized void lock(GLThread glThread) {
      glThread.lock = true; //上一句翻译
      if (this.glThread== glThread) {
        this.glThread= null;
      }
      notifyAll();
    }
    
    public boolean checkGLThread(GLThread glThread) {
      if (this.glThread== glThread || this.glThread== null) {
        this.glThread = glThread;
        notifyAll();
        return true;
      }
      c();
      if (this.e) {
        return true;
      }
      if (glThread != null) {
        glThread.g();
      }
      return false;
    }
    
    public void c(GLTextureView.GLThread parami) {
      if (glThread == parami) {
        glThread = null;
      }
      notifyAll();
    }
    
    public synchronized boolean a() {
      return this.f;
    }
    
    public synchronized boolean b() {
      c();
      return !this.e;
    }

    //是否支持OpenGL 硬件加速
    public synchronized void supportHardwareSpeed(GL10 paramGL10) {
      if (!this.d) {
        c();
        String str = paramGL10.glGetString(7937);
        if (this.c < 131072)
        {
          this.e = (!str.startsWith("Q3Dimension MSM7500 "));
          notifyAll();
        }
        this.f = (!this.e);
        this.d = true;
      }
    }
    
    private void c() {
      if (!this.b) {
        this.c = 131072;
        if (this.c >= 131072) {
          this.e = true;
        }
        this.b = true;
      }
    }
  }
  
  //private static final j glThreadManager = new j(null);
  private static final GLThreadManager glThreadManager = new GLThreadManager(); //上一句翻译
  private final WeakReference<GLTextureView> weakReference = new WeakReference(this);
  private GLThread glThread;
  private GLSurfaceView.Renderer render; // d;
  private boolean separateWindow;
  private IEGLConfig ieglConfig;
  private IEGLContext ieglContext;
  private IGLSurfaceView iglSurfaceView;
  private k i;
  private int j;
  private int k;
  private boolean l;
  
  public void onSurfaceTextureAvailable(SurfaceTexture paramSurfaceTexture, int width, int height) {
    this.glThread.onSurfaceTextureAvailable();
  }
  
  public boolean onSurfaceTextureDestroyed(SurfaceTexture paramSurfaceTexture) {
    this.glThread.onSurfaceTextureDestroyed();
    return true;
  }
  
  public void onSurfaceTextureSizeChanged(SurfaceTexture paramSurfaceTexture, int width, int height) {
    this.glThread.onSurfaceTextureSizeChanged(width, height);
  }
  
  public void onSurfaceTextureUpdated(SurfaceTexture surface) {}
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    onSurfaceTextureSizeChanged(getSurfaceTexture(), paramInt3 - paramInt1, paramInt4 - paramInt2);
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }
}
