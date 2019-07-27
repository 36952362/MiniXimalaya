package com.jupiter.miniximalaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jupiter.miniximalaya.adapters.TrackPlayPageAdapter;
import com.jupiter.miniximalaya.interfaces.IPlayerCallback;
import com.jupiter.miniximalaya.presenters.PlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
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
    private int currentProgress = 0;
    private boolean userTouched = false;
    private ImageView playPrevious;
    private ImageView playNext;
    private TextView tvPlayTitle;
    private String trackTitle;
    private ViewPager playCoverViewPager;
    private TrackPlayPageAdapter trackPlayPageAdapter;

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

        playerPresenter.getPlayList();
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

        if (progressBar != null) {
            progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int curProgress,  boolean isFromUser) {
                    if (isFromUser) {
                        currentProgress = curProgress;
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    userTouched = true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    userTouched = false;
                    playerPresenter.seekTo(currentProgress);
                }
            });
        }

        if (playPrevious != null) {
            playPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (playerPresenter != null) {
                        playerPresenter.playPre();
                    }
                }
            });
        }

        if (playNext != null) {
            playNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (playerPresenter != null) {
                        playerPresenter.playNext();
                    }
                }
            });
        }
    }

    private void initView() {
        playImageView = findViewById(R.id.iv_play);
        escapedTimeTextView = findViewById(R.id.tv_escaped_time);
        totalTimeTextView = findViewById(R.id.tv_total_time);
        progressBar = findViewById(R.id.sb_play_progress);
        playPrevious = findViewById(R.id.iv_previous);
        playNext = findViewById(R.id.iv_next);
        tvPlayTitle = findViewById(R.id.tv_track_title);
        if (!TextUtils.isEmpty(trackTitle)) {
            tvPlayTitle.setText(trackTitle);
        }

        playCoverViewPager = findViewById(R.id.vp_track_cover);
        trackPlayPageAdapter = new TrackPlayPageAdapter();
        playCoverViewPager.setAdapter(trackPlayPageAdapter);
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
        if (trackPlayPageAdapter != null) {
            trackPlayPageAdapter.setData(tracks);
        }
    }

    @Override
    public void onPlayModeChanged(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onPlayProgressChanged(int currProgress, int total) {
        String totalDuration;
        String escapedTime;
        progressBar.setMax(total);
        if (total >= 60 * 60 * 1000) {
            totalDuration = hourFormat.format(total);
            escapedTime = hourFormat.format(currProgress);
        }
        else{
            totalDuration = minFormat.format(total);
            escapedTime = minFormat.format(currProgress);
        }

        if (totalTimeTextView != null) {
            totalTimeTextView.setText(totalDuration);
        }

        if (escapedTimeTextView != null) {
            escapedTimeTextView.setText(escapedTime);
        }

        if (!userTouched) {
            if (progressBar != null) {
                progressBar.setProgress(currProgress);
            }
        }

    }

    @Override
    public void onPlayTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        if (curModel != null && curModel instanceof Track) {
            Track track = (Track)curModel;
            if (tvPlayTitle != null) {
                tvPlayTitle.setText(track.getTrackTitle());
            }
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
