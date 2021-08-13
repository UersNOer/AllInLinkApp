package com.example.android_supervisor.common;

import android.content.Context;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.android_supervisor.utils.JsonUtils;

import java.util.HashMap;

/**
 * @author wujie
 */
public class BaiduSpeechApi {
    private EventManager eventManager;

    private static BaiduSpeechApi baiduSpeechApi;

    public static BaiduSpeechApi getInstance(Context context) {
        if (baiduSpeechApi == null) {
            baiduSpeechApi = new BaiduSpeechApi(context.getApplicationContext());

        }
        return baiduSpeechApi;
    }

    private BaiduSpeechApi(Context context) {
        eventManager = EventManagerFactory.create(context, "asr");
    }

    public void registerListener(EventListener listener) {
        eventManager.registerListener(listener);
    }

    public void unregisterListener(EventListener listener) {
        eventManager.unregisterListener(listener);
    }

    public void start() {
        HashMap<String, Object> params = new HashMap<>();
        params.put(SpeechConstant.ACCEPT_AUDIO_DATA, true);
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_TOUCH);
        params.put(SpeechConstant.DISABLE_PUNCTUATION, false);
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        eventManager.send(SpeechConstant.ASR_START, JsonUtils.toJson(params), null, 0, 0);
    }

    public void stop() {
        eventManager.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
    }

    public void cancel() {
        eventManager.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
    }

}
