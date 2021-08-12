package com.example.android_supervisor.ui.media;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.android_supervisor.R;

import java.io.File;

/**
 * @author wujie
 */
@SuppressLint("ValidFragment")
public class AudioRecordFragment extends DialogFragment implements MediaRecorder.OnInfoListener {
    private MediaRecorder mRecorder;
    private ImageView ivMic;
    private File mOutputFile;
    private Handler mHandler = new Handler();
    private boolean isRecording;

    public AudioRecordFragment(File outputFile) {
        mOutputFile = outputFile;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_audio_record, null);
        ivMic = view.findViewById(R.id.iv_record_mic);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("正在录音")
                .setView(view)
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stop();
                    }
                })
                .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String filePath = mOutputFile.getAbsolutePath();
        if (start(filePath)) {
            updateMicStatus();
        }
    }

    public boolean start(String path) {
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setOnInfoListener(this);
            mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mRecorder.setOutputFile(path);
            mRecorder.setMaxDuration(10 * 1000); // 最长录音10s
            mRecorder.prepare();
            mRecorder.start();
            isRecording = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean stop() {
        if (mRecorder != null) {
            isRecording = false;
            try {
                mRecorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mRecorder.release();
                mRecorder = null;
            }
        }
        return false;
    }

    private void updateMicStatus() {
        if (isRecording) {
            int maxAmp = mRecorder.getMaxAmplitude();
            double db = maxAmp > 1 ? 20 * Math.log10(maxAmp) : 0;
            if (db < 50) {
                ivMic.setImageResource(R.drawable.mic_1);
            } else if (db < 60) {
                ivMic.setImageResource(R.drawable.mic_2);
            } else if (db < 70) {
                ivMic.setImageResource(R.drawable.mic_3);
            } else if (db < 80) {
                ivMic.setImageResource(R.drawable.mic_4);
            } else {
                ivMic.setImageResource(R.drawable.mic_5);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateMicStatus();
                }
            }, 200);
        }
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            Log.v("RecordActivity","Maximum duration reached");
            stop();
        }
    }
}
