package com.example.android_supervisor.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.core.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.speech.EventListener;
import com.baidu.speech.asr.SpeechConstant;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.BaiduSpeechApi;
import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.utils.JsonUtils;

/**
 * @author yj
 */
@SuppressLint("ValidFragment")
public class VoiceRecognizeDialog extends DialogFragment implements EventListener {
    private BaiduSpeechApi speechApi;
    private ResultCallback<String> callback;
    private int soureTag;

    public VoiceRecognizeDialog(Context context) {
        speechApi = BaiduSpeechApi.getInstance(context);
    }

    public void setCallback(ResultCallback<String> callback) {
        this.callback = callback;
    }

    public void register() {
        speechApi.registerListener(this);
    }

    public void unregister() {
        speechApi.unregisterListener(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.speech,null);
        ImageView img=(ImageView)view.findViewById(R.id.iv_speech);
        TextView tv_speech=(TextView)view.findViewById(R.id.tv_speech);
        tv_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Animation animation= AnimationUtils.loadAnimation(getContext(), R.anim.speech);
        img.startAnimation(animation);

        AlertDialog dialog = new AlertDialog.Builder(getContext(),R.style.speech_dialog)
                .setView(view)
                .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        speechApi.start();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        speechApi.stop();
    }

    @Override
    public  void onEvent(String name, String params, byte[] data, int offset, int length) {
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            Speech speech = JsonUtils.fromJson(params, Speech.class);
            if (speech != null && "final_result".equals(speech.result_type)) {
                if (speech.results_recognition != null && speech.results_recognition.length > 0) {
                    String text = speech.results_recognition[0];
                    if (callback != null) {
                        callback.onResult(text,soureTag);
                    }
                }
            }
        }
    }

    public void setTag(int tag) {
        this.soureTag = tag;
    }

    public class Speech {
        public int error;
        public String best_result;
        public String result_type;
        public String[] results_recognition;
        public OriginResult origin_result;
    }

    public static class OriginResult {
        public String[] uncertain_word;
        public String[] word;
    }
}
