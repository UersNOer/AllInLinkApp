package com.example.android_supervisor.http.request;

import android.util.Log;


import com.example.android_supervisor.utils.JsonUtils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;

/**
 * @author wujie
 */
public class JsonBody extends RequestBody {
    final byte[] bytes;

    public JsonBody(Object object) {
        String jsonChars = JsonUtils.toJson(object);
        bytes = jsonChars.getBytes(Util.UTF_8);
        Log.d("JsonBody",jsonChars);
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("application/json");
    }

    @Override
    public long contentLength() throws IOException {
        return bytes.length;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink.write(bytes);
    }
}
