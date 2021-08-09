package com.unistrong.network.progress;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Description: ProResponseBody
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class ProResponseBody extends ResponseBody {
    private ResponseBody mDelegate;
    private BufferedSource mBufferedSource;
    private Set<WeakReference<ProgressListener>> mListeners;

    public ProResponseBody(ResponseBody delegate, Set<WeakReference<ProgressListener>> listeners) {
        this.mDelegate = delegate;
        this.mListeners = listeners;
    }

    @Override
    public MediaType contentType() {
        return mDelegate.contentType();
    }

    @Override
    public long contentLength() {
        return mDelegate.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(new ProgressSource(mDelegate.source()));
        }
        return mBufferedSource;
    }


    final class ProgressSource extends ForwardingSource {
        private long mSoFarBytes = 0;
        private long mTotalBytes = -1;

        public ProgressSource(Source delegate) {
            super(delegate);
        }

        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            long bytesRead = 0L;
            try {
                bytesRead = super.read(sink, byteCount);

                if (mTotalBytes < 0) {
                    mTotalBytes = contentLength();
                }
                mSoFarBytes += (bytesRead != -1 ? bytesRead : 0);

                ProgressHelper.dispatchProgressEvent(mListeners, mSoFarBytes, mTotalBytes);
            } catch (IOException e) {
                ProgressHelper.dispatchErrorEvent(mListeners, e);
                throw e;
            }

            return bytesRead;
        }
    }
}
