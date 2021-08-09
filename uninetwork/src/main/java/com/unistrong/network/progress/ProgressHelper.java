package com.unistrong.network.progress;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description: ProgressHelper
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class ProgressHelper {
    private final Map<String, Set<WeakReference<ProgressListener>>> mRequestListenerMap = new WeakHashMap<>();
    private final Map<String, Set<WeakReference<ProgressListener>>> mResponseListenerMap = new WeakHashMap<>();

    private static Handler mMainHandler = new Handler(Looper.getMainLooper());

    private Interceptor mInterceptor;

    private ProgressHelper() {
        mInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return wrapResponseBody(chain.proceed(wrapRequestBody(chain.request())));
            }
        };
    }

    public static ProgressHelper get() {
        return Holder.instance;
    }

    public Interceptor getInterceptor() {
        return mInterceptor;
    }


    public void addRequestListener(String url, ProgressListener listener) {
        if (TextUtils.isEmpty(url) || listener == null) {
            return;
        }

        Set<WeakReference<ProgressListener>> listeners = null;
        synchronized (ProgressHelper.class) {
            listeners = mRequestListenerMap.get(url);
            if (listeners == null) {
                listeners = new HashSet<>();
                mRequestListenerMap.put(url, listeners);
            }
            listeners.add(new WeakReference<>(listener));
        }
    }

    public void addResponseListener(String url, ProgressListener listener) {
        if (TextUtils.isEmpty(url) || listener == null) {
            return;
        }

        Set<WeakReference<ProgressListener>> listeners = null;
        synchronized (ProgressHelper.class) {
            listeners = mResponseListenerMap.get(url);
            if (listeners == null) {
                listeners = new HashSet<>();
                mResponseListenerMap.put(url, listeners);
            }
            listeners.add(new WeakReference<>(listener));
        }
    }

    public void delRequestListener(String url, ProgressListener listener) {
        if ((mResponseListenerMap == null || mResponseListenerMap.isEmpty())) {
            return;
        }

        if (TextUtils.isEmpty(url)) {

            if (listener != null) {
                for (String key : mRequestListenerMap.keySet()) {
                    delReference(mRequestListenerMap.get(key), listener);
                }
            }
        } else {
            if (listener != null) {
                delReference(mRequestListenerMap.get(url), listener);
            } else {
                mRequestListenerMap.remove(url);
            }
        }
    }

    public void delResponseListener(String url, ProgressListener listener) {
        if ((mResponseListenerMap == null || mResponseListenerMap.isEmpty())) {
            return;
        }

        if (TextUtils.isEmpty(url)) {

            if (listener != null) {
                for (String key : mResponseListenerMap.keySet()) {
                    delReference(mResponseListenerMap.get(key), listener);
                }
            }
        } else {
            if (listener != null) {
                delReference(mResponseListenerMap.get(url), listener);
            } else {
                mResponseListenerMap.remove(url);
            }
        }
    }

    public void clearAll() {
        mRequestListenerMap.clear();
        mResponseListenerMap.clear();
    }


    public static void dispatchProgressEvent(Set<WeakReference<ProgressListener>> listeners,
                                             final long soFarBytes, final long totalBytes) {
        if (!(listeners == null || listeners.isEmpty())) {
            for (final WeakReference<ProgressListener> reference : listeners) {
                if (reference.get() != null) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            reference.get().onProgress(soFarBytes, totalBytes);
                        }
                    });
                }
            }
        }
    }

    public static void dispatchErrorEvent(Set<WeakReference<ProgressListener>> listeners,
                                          final Throwable throwable) {
        if (!(listeners == null || listeners.isEmpty())) {
            for (final WeakReference<ProgressListener> reference : listeners) {
                if (reference.get() != null) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            reference.get().onError(throwable);
                        }
                    });
                }
            }
        }
    }

    private Request wrapRequestBody(Request request) {
        if (request == null || request.body() == null) {
            return request;
        }

        String key = request.url().toString();
        if (mRequestListenerMap.containsKey(key)) {
            Set<WeakReference<ProgressListener>> listeners = mRequestListenerMap.get(key);
            return request.newBuilder()
                    .method(request.method(), new ProRequestBody(request.body(), listeners))
                    .build();
        }
        return request;
    }

    private Response wrapResponseBody(Response response) {
        if (response == null || response.body() == null) {
            return response;
        }

        String key = response.request().url().toString();
        if (mResponseListenerMap.containsKey(key)) {
            Set<WeakReference<ProgressListener>> listeners = mResponseListenerMap.get(key);
            return response.newBuilder()
                    .body(new ProResponseBody(response.body(), listeners))
                    .build();
        }
        return response;
    }


    private void delReference(Set<WeakReference<ProgressListener>> references,
                              ProgressListener listener) {
        if (!(references == null || references.isEmpty())) {
            for (WeakReference<ProgressListener> reference : references) {
                if (reference.get() != null && reference.get() == listener) {
                    references.remove(reference);
                }
            }
        }
    }


    private static class Holder {
        private static ProgressHelper instance = new ProgressHelper();
    }
}
