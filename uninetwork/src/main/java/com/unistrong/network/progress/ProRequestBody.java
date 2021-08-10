package com.unistrong.network.progress;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 * Description: ProRequestBody
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class ProRequestBody extends RequestBody {
    private RequestBody mDelegate;
    private BufferedSink mBufferedSink;
    private Set<WeakReference<ProgressListener>> mListeners;

    public ProRequestBody(RequestBody delegate, Set<WeakReference<ProgressListener>> listeners) {
        this.mDelegate = delegate;
        this.mListeners = listeners;
    }

    @Override
    public MediaType contentType() {
        return mDelegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return mDelegate.contentLength();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(new ProgressSink(sink));
        }
        try {
            mDelegate.writeTo(mBufferedSink);
            mBufferedSink.flush();
        } catch (IOException e) {
            e.printStackTrace();
            ProgressHelper.dispatchErrorEvent(mListeners, e);
            throw e;
        }
    }

    final class ProgressSink extends ForwardingSink {
        private long mSoFarBytes = 0;
        private long mTotalBytes = -1;

        public ProgressSink(okio.Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) {
            try {
                super.write(source, byteCount);
            } catch (Exception e) {
                ProgressHelper.dispatchErrorEvent(mListeners, e);
            }

            if (mTotalBytes < 0) {
                mTotalBytes = contentLength();
            }
            mSoFarBytes += byteCount;

            ProgressHelper.dispatchProgressEvent(mListeners, mSoFarBytes, mTotalBytes);
        }
    }
}
