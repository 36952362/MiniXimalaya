package com.jupiter.miniximalaya;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jupiter.miniximalaya.interfaces.IPlayerCallback;
import com.jupiter.miniximalaya.presenters.PlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.text.SimpleDateFormat;
import java.util.List;

public class TrackPlayerActivity extends AppCompatActivity implements View.OnClickListener, IPlayerCallback {

    private ImageView playImageView;
    private PlayerPresenter playerPresenter;

    private SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");
    private SimpleDateFormat minFormat = new SimpleDateFormat("mm:ss");
    private TextView escapedTimeTextView;
    private TextView totalTimeTextView;
    private SeekBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_player);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        playerPresenter = PlayerPresenter.getsInstance();
        playerPresenter.registerCallback(this);

        initView();
        initEvent();
        startPlay();
    }

    private void startPlay() {
        if (playerPresenter != null) {
            playerPresenter.play();
        }
    }

    private void pause(){
        if (playerPresenter != null) {
            playerPresenter.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playerPresenter!=null) {
            playerPresenter.unRegisterCallback(this);
            playerPresenter = null;
        }
    }

    private void initEvent() {
        if (playImageView != null) {
            playImageView.setOnClickListener(this);
        }
    }

    private void initView() {
        playImageView = findViewById(R.id.iv_play);
        escapedTimeTextView = findViewById(R.id.tv_escaped_time);
        totalTimeTextView = findViewById(R.id.tv_total_time);
        progressBar = findViewById(R.id.sb_play_progress);
    }

    @Override
    public void onClick(View view) {
        if(view == playImageView){
            if(playerPresenter.isPlaying()) {
                pause();
                return;
            }else{
                startPlay();
            }
        }
    }

    @Override
    public void onPlayStart() {
        if (playImageView!=null) {
            playImageView.setImageResource(R.mipmap.stop);
        }
    }

    @Override
    public void onPlayPause() {
        if (playImageView!=null) {
            playImageView.setImageResource(R.mipmap.play);
        }
    }

    @Override
    public void onPlayStop() {
        if (playImageView != null) {
            playImageView.setImageResource(R.mipmap.stop);
        }
    }

    @Override
    public void onPlayError(XmPlayerException exception) {

    }

    @Override
    public void onPlayPre() {

    }

    @Override
    public void onPlayNext() {

    }

    @Override
    public void onPlayList(List<Track> tracks) {

    }

    @Override
    public void onPlayModeChanged(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onPlayProgressChanged(long currentProgress, long total) {
        String totalDuration;
        String escapedTime;

        if (total >= 60 * 60 * 100) {
            totalDuration = hourFormat.format(total);
            escapedTime = hourFormat.format(currentProgress);
        }
        else{
            totalDuration = minFormat.format(total);
            escapedTime = minFormat.format(currentProgress);
        }

        if (totalTimeTextView != null) {
            totalTimeTextView.setText(totalDuration);
        }

        if (escapedTimeTextView != null) {

            escapedTimeTextView.setText(escapedTime);
        }
    }

    @Override
    public void onAdsStartBuffering() {

    }

    @Override
    public void onAdsStopBuffering() {

    }

    @Override
    public void onCompletePlayAds() {

    }
}
