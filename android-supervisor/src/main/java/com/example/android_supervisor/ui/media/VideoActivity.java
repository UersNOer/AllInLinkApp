package com.example.android_supervisor.ui.media;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.example.android_supervisor.R;
import com.example.android_supervisor.http.OkHttpClientFactory;

/**
 * @author wujie
 */
public class VideoActivity extends Activity {
    private SimpleExoPlayer mPlayer;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(0x00000000);
        }
        
        setContentView(R.layout.activity_video);
        mProgressBar = findViewById(R.id.progressBar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TrackSelector trackSelector = new DefaultTrackSelector();
        mPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        PlayerView playerView = findViewById(R.id.player_view);
        playerView.requestFocus();
        playerView.setPlayer(mPlayer);

        Uri uri = getIntent().getData();
        if (uri == null) {
            throw new RuntimeException();
        }

        String url = uri.toString();
        DataSource.Factory sourceFactory;
        if (URLUtil.isHttpUrl(url)) {
            sourceFactory = new DefaultHttpDataSourceFactory(
                    "ExoPlayer",
                    null,
                    DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                    true);
        } else if (URLUtil.isHttpsUrl(url)) {
            sourceFactory = new OkHttpDataSourceFactory(
                    OkHttpClientFactory.createSSLHttpClient(), "ExoPlayer", null);
        } else {
            sourceFactory = new DefaultDataSourceFactory(this, "ExoPlayer");
        }

        MediaSource mediaSource = buildMediaSource(uri, sourceFactory);
        if (mediaSource!=null){
            mPlayer.addListener(eventListener);
            mPlayer.setPlayWhenReady(true);
            mPlayer.prepare(mediaSource);
        }



    }

    private MediaSource buildMediaSource(Uri uri, DataSource.Factory sourceFactory) {
        try{
            int type = Util.inferContentType(uri.getLastPathSegment());
            switch (type) {
                case C.TYPE_SS:
                    return new SsMediaSource(
                            uri, null, new DefaultSsChunkSource.Factory(sourceFactory), null, null);
                case C.TYPE_DASH:
                    return new DashMediaSource(
                            uri, null, new DefaultDashChunkSource.Factory(sourceFactory), null, null);
                case C.TYPE_HLS:
                    return new HlsMediaSource(uri, sourceFactory, null, null);
                case C.TYPE_OTHER:
                    return new ExtractorMediaSource(
                            uri, sourceFactory, new DefaultExtractorsFactory(), null, null);
                default: {
                    throw new IllegalStateException("Unsupported type: " + type);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public void onBackPressed() {
        mPlayer.stop();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mPlayer.release();
        super.onDestroy();
    }

    Player.EventListener eventListener = new Player.EventListener() {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState){
                case Player.STATE_ENDED:
                    break;
                case Player.STATE_READY:
                    mProgressBar.setVisibility(View.GONE);
                    break;
                case Player.STATE_BUFFERING:
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case Player.STATE_IDLE:

                    break;
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    };
}
