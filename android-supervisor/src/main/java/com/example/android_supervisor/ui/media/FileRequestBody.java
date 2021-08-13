package com.example.android_supervisor.ui.media;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 * @author wujie
 */
public class FileRequestBody extends RequestBody {
    final RequestBody delegate;
    final UploadServiceManager.Callback callback;

    public FileRequestBody(RequestBody delegate, UploadServiceManager.Callback callback) {
        this.delegate = delegate;
        this.callback = callback;
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return delegate.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink = Okio.buffer(new ForwardingSink(sink) {
            int bytesWritten = 0;
            int bytesLength = 0;

            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                bytesWritten += byteCount;
                if (bytesLength == 0) {
                    bytesLength = (int) contentLength();
                }
                callback.onProgress(bytesWritten, bytesLength);
            }
        });
        delegate.writeTo(sink);
        sink.flush();
    }
}
