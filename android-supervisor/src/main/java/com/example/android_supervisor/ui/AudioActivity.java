package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.topevery.common.splite.Storage;
import com.topevery.common.splite.wigth.AudioView;
import com.example.android_supervisor.BuildConfig;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.AppContext;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.recorder.listener.RecordFftDataListener;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;
import com.zlw.main.recorderlib.recorder.listener.RecordSoundSizeListener;
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.btRecord)
    ImageView btRecord;
    @BindView(R.id.btStop)
    ImageView btStop;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvSoundSize)
    TextView tvSoundSize;
    @BindView(R.id.rgAudioFormat)
    RadioGroup rgAudioFormat;
    @BindView(R.id.rgSimpleRate)
    RadioGroup rgSimpleRate;
    @BindView(R.id.tbEncoding)
    RadioGroup tbEncoding;
    @BindView(R.id.audioView)
    AudioView audioView;
    @BindView(R.id.spUpStyle)
    Spinner spUpStyle;
    @BindView(R.id.spDownStyle)
    Spinner spDownStyle;

    private boolean isStart = false;
    private boolean isPause = false;
    final RecordManager recordManager = RecordManager.getInstance();
    private static final String[] STYLE_DATA = new String[]{"STYLE_ALL", "STYLE_NOTHING", "STYLE_WAVE", "STYLE_HOLLOW_LUMP"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_pager);
        ButterKnife.bind(this);
        initAudioView();
        initEvent();
        initRecord();

    }

    @Override
    protected void onResume() {
        super.onResume();
        doStop();
        initRecordEvent();
    }

    @Override
    protected void onStop() {
        super.onStop();
        doStop();
    }

    private void initAudioView() {
//        tvState.setVisibility(View.GONE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, STYLE_DATA);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUpStyle.setAdapter(adapter);
        spDownStyle.setAdapter(adapter);
        spUpStyle.setOnItemSelectedListener(this);
        spDownStyle.setOnItemSelectedListener(this);
    }

    private void initEvent() {
        rgAudioFormat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbPcm:
                        recordManager.changeFormat(RecordConfig.RecordFormat.PCM);
                        break;
                    case R.id.rbMp3:
                        recordManager.changeFormat(RecordConfig.RecordFormat.MP3);
                        break;
                    case R.id.rbWav:
                        recordManager.changeFormat(RecordConfig.RecordFormat.WAV);
                        break;
                    default:
                        break;
                }
            }
        });

        rgSimpleRate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb8K:
                        recordManager.changeRecordConfig(recordManager.getRecordConfig().setSampleRate(8000));
                        break;
                    case R.id.rb16K:
                        recordManager.changeRecordConfig(recordManager.getRecordConfig().setSampleRate(16000));
                        break;
                    case R.id.rb44K:
                        recordManager.changeRecordConfig(recordManager.getRecordConfig().setSampleRate(44100));
                        break;
                    default:
                        break;
                }
            }
        });

        tbEncoding.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb8Bit:
                        recordManager.changeRecordConfig(recordManager.getRecordConfig().setEncodingConfig(AudioFormat.ENCODING_PCM_8BIT));
                        break;
                    case R.id.rb16Bit:
                        recordManager.changeRecordConfig(recordManager.getRecordConfig().setEncodingConfig(AudioFormat.ENCODING_PCM_16BIT));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initRecord() {
        recordManager.init(AppContext.getInstance(), BuildConfig.DEBUG);
        recordManager.changeFormat(RecordConfig.RecordFormat.MP3);
//        String recordDir = String.format(Locale.getDefault(), "%s/Record/com.zlw.main/",
//                Environment.getExternalStorageDirectory().getAbsolutePath());
        String recordDir = Storage.getAudioDir(this);
        Logger.d("recordDir"+recordDir);
        recordManager.changeRecordDir(Storage.getAudioDir(this)+"/");
        initRecordEvent();
    }

    private void initRecordEvent() {
        recordManager.setRecordStateListener(new RecordStateListener() {
            @Override
            public void onStateChange(RecordHelper.RecordState state) {

                switch (state) {
                    case PAUSE:
                        tvState.setText("暂停中");
                        break;
                    case IDLE:
                        tvState.setText("空闲中");
                        break;
                    case RECORDING:
                        tvState.setText("录音中");
                        break;
                    case STOP:
                        tvState.setText("停止");
                        break;
                    case FINISH:
                        tvState.setText("录音结束");
                        tvSoundSize.setText("---");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onError(String error) {
                Logger.i(TAG, "onError %s", error);
            }
        });
        recordManager.setRecordSoundSizeListener(new RecordSoundSizeListener() {
            @Override
            public void onSoundSize(int soundSize) {
                tvSoundSize.setText(String.format(Locale.getDefault(), "声音大小：%s db", soundSize));
            }
        });
        recordManager.setRecordResultListener(new RecordResultListener() {
            @Override
            public void onResult(File result) {
                String saveAs = result.getAbsolutePath();
                //刷新媒体库
                Uri contentUri = Uri.fromFile(new File(saveAs));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,contentUri);
                sendBroadcast(mediaScanIntent);

                Intent intent  = new Intent();
                intent.setData(Uri.fromFile(new File(saveAs)));
                setResult(Activity.RESULT_OK,intent);
                Toast.makeText(AudioActivity.this, "录音文件已保存", Toast.LENGTH_SHORT).show();
//                Toast.makeText(AudioActivity.this, "录音文件： " + result.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        });
        recordManager.setRecordFftDataListener(new RecordFftDataListener() {
            @Override
            public void onFftData(byte[] data) {
                audioView.setWaveData(data);
            }
        });
    }

    @OnClick({R.id.btRecord, R.id.btStop, R.id.jumpTestActivity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btRecord:
                doPlay();
                break;
            case R.id.btStop:
                doStop();
                break;
            case R.id.jumpTestActivity:
//                startActivity(new Intent(this, TestHzActivity.class));
            default:
                break;
        }
    }

    private void doStop() {
        recordManager.stop();
//        btRecord.setText("开始");
        btRecord.setImageResource(R.drawable.play_button);
        isPause = false;
        isStart = false;
    }

    private void doPlay() {
        if (isStart) {
            recordManager.pause();
//            btRecord.setText("开始");
            btRecord.setImageResource(R.drawable.play_button);
            isPause = true;
            isStart = false;
        } else {
            if (isPause) {
                recordManager.resume();
            } else {
                recordManager.start();
            }
            btRecord.setImageResource(R.drawable.music_stop);
//            btRecord.setText("暂停");
            isStart = true;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spUpStyle:
                audioView.setStyle(AudioView.ShowStyle.getStyle(STYLE_DATA[position]), audioView.getDownStyle());
                break;
            case R.id.spDownStyle:
                audioView.setStyle(audioView.getUpStyle(), AudioView.ShowStyle.getStyle(STYLE_DATA[position]));
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //nothing
    }

    @Override
    public void finish() {
        super.finish();
    }
}
