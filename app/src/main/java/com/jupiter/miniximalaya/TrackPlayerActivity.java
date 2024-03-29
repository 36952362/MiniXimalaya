package com.jupiter.miniximalaya;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.jupiter.miniximalaya.adapters.TrackPlayPageAdapter;
import com.jupiter.miniximalaya.interfaces.IPlayerCallback;
import com.jupiter.miniximalaya.presenters.PlayerPresenter;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.jupiter.miniximalaya.views.PlayListPopupWindow;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;

public class TrackPlayerActivity extends AppCompatActivity implements View.OnClickListener, IPlayerCallback {

    private static final String TAG = "TrackPlayerActivity";
    private ImageView playImageView;
    private PlayerPresenter playerPresenter;

    private SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");
    private SimpleDateFormat minFormat = new SimpleDateFormat("mm:ss");
    private TextView escapedTimeTextView;
    private TextView totalTimeTextView;
    private SeekBar progressBar;
    private int currentProgress = 0;
    private boolean userTouchedSeekBar = false;
    private boolean userTouchedViewPager = false;
    private ImageView playPrevious;
    private ImageView playNext;
    private TextView tvPlayTitle;
    private String trackTitle;
    private ViewPager playCoverViewPager;
    private TrackPlayPageAdapter trackPlayPageAdapter;
    private ImageView playMode;

    private XmPlayListControl.PlayMode currentPlayMode = PLAY_MODEL_LIST;

    private static Map<XmPlayListControl.PlayMode, XmPlayListControl.PlayMode> sPlayMode = new HashMap<>();

    static {
        sPlayMode.put(PLAY_MODEL_LIST, PLAY_MODEL_LIST_LOOP);
        sPlayMode.put(PLAY_MODEL_LIST_LOOP, PLAY_MODEL_RANDOM);
        sPlayMode.put(PLAY_MODEL_RANDOM, PLAY_MODEL_SINGLE_LOOP);
        sPlayMode.put(PLAY_MODEL_SINGLE_LOOP, PLAY_MODEL_LIST);
    }

    private ImageView playListView;
    private PlayListPopupWindow playListPopupWindow;
    private ValueAnimator enterAnimator;
    private ValueAnimator exitAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "currentProgress:" + currentProgress + ", this:" + this);
        setContentView(R.layout.activity_track_player);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        playerPresenter = PlayerPresenter.getsInstance();
        initView();
        playerPresenter.registerCallback(this);
        initAnimator();
        initEvent();

        playerPresenter.getPlayList();
    }

    private void initAnimator() {

        enterAnimator = ValueAnimator.ofFloat(1.0f, 0.7f);
        enterAnimator.setDuration(500);
        enterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                updateBgAlpha((float)valueAnimator.getAnimatedValue());
            }
        });

        exitAnimator = ValueAnimator.ofFloat(0.7f, 1.0f);
        exitAnimator.setDuration(500);
        exitAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                updateBgAlpha((float)valueAnimator.getAnimatedValue());
            }
        });


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
                    userTouchedSeekBar = true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    userTouchedSeekBar = false;
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

        if (playCoverViewPager != null) {
            playCoverViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (playerPresenter != null) {
                        playerPresenter.play(position);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            playCoverViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int action = motionEvent.getAction();
                    if(MotionEvent.ACTION_DOWN == action)
                        userTouchedViewPager = true;
                    return false;
                }
            });
        }

        if (playMode != null) {
            playMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchPlayMode();
                }
            });
        }


        if (playListView != null) {
            playListView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playListPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                    enterAnimator.start();
                }
            });
        }

        if (playListPopupWindow != null) {
            playListPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    exitAnimator.start();
                }
            });

            playListPopupWindow.setPlayItemClickListener(new PlayListPopupWindow.PlayItemClickListener() {
                @Override
                public void onClick(int position) {
                    if (playerPresenter != null) {
                        playerPresenter.play(position  );
                    }
                }
            });

            playListPopupWindow.setPlayListClickListener(new PlayListPopupWindow.PlayListClickListener() {
                @Override
                public void onPlayModeClick() {
                    switchPlayMode();
                }

                @Override
                public void onPlaySortClick() {
                    switchPlaySort();
                }
            });
        }

    }


    private void switchPlaySort(){

        if (playerPresenter != null) {
            playerPresenter.reversePlayList();
        }

    }


    private void updateBgAlpha(float alpha){
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = alpha;
        getWindow().setAttributes(attributes);
    }

    private void switchPlayMode() {

        XmPlayListControl.PlayMode nextPlayMode = sPlayMode.get(currentPlayMode);
        if (playerPresenter != null) {
            playerPresenter.setPlayMode(nextPlayMode);
        }
    }

    private void updatePlayModeImage() {
        int resId = R.drawable.selector_player_mode_list;
        switch (currentPlayMode){
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.selector_player_mode_list_loop;
                break;
            case PLAY_MODEL_LIST:
                resId = R.drawable.selector_player_mode_list;
                break;
            case PLAY_MODEL_RANDOM:
                resId = R.drawable.selector_player_mode_random;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resId = R.drawable.selector_player_mode_loop_one;
        }

        playMode.setImageResource(resId);

    }

    private void initView() {
        playImageView = findViewById(R.id.iv_play);

        if (playerPresenter.isPlaying()) {
            playImageView.setImageResource(R.drawable.selector_player_pause);
        }


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

        playMode = findViewById(R.id.iv_play_mode);

        playListView = findViewById(R.id.iv_player_icon_list);

        playListPopupWindow = new PlayListPopupWindow();
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
            playImageView.setImageResource(R.drawable.selector_player_pause);
        }
    }

    @Override
    public void onPlayPause() {
        if (playImageView!=null) {
            playImageView.setImageResource(R.drawable.selector_player_play);
        }
    }

    @Override
    public void onPlayStop() {
        if (playImageView != null) {
            playImageView.setImageResource(R.drawable.selector_player_pause);
        }
    }

    @Override
    public void onPlayList(List<Track> tracks) {
        if (trackPlayPageAdapter != null) {
            trackPlayPageAdapter.setData(tracks);
        }

        if (playListPopupWindow != null) {
            playListPopupWindow.setData(tracks);
        }
    }

    @Override
    public void onPlayModeChanged(XmPlayListControl.PlayMode playMode) {
        currentPlayMode = playMode;

        if (playListPopupWindow != null) {
            playListPopupWindow.updatePlayModeImage(playMode);
        }
        updatePlayModeImage();
    }

    @Override
    public void onPlayProgressChanged(int currentProgress, int total) {
        String totalDuration;
        String escapedTime;
        progressBar.setMax(total);
        if (total >= 60 * 60 * 1000) {
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

        if (!userTouchedSeekBar) {
            if (progressBar != null) {
                progressBar.setProgress(currentProgress);
            }
        }

    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel, int currentIndex) {
        if (curModel != null && curModel instanceof Track) {
            Track track = (Track)curModel;
            if (tvPlayTitle != null) {
                tvPlayTitle.setText(track.getTrackTitle());
            }
        }

        if (playCoverViewPager != null && userTouchedViewPager) {
            playCoverViewPager.setCurrentItem(currentIndex, true);
        }

        userTouchedViewPager = false;

        if (playListPopupWindow != null) {
            playListPopupWindow.setCurrentPlayIndex(currentIndex);
        }

    }

    @Override
    public void onPlaySortChange(boolean isAscending) {
        playListPopupWindow.setPlayListSort(!isAscending);
    }
}
