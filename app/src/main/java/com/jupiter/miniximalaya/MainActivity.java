package com.jupiter.miniximalaya;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.jupiter.miniximalaya.adapters.MainContentAdapter;
import com.jupiter.miniximalaya.adapters.MainIndicatorAdapter;
import com.jupiter.miniximalaya.api.XimalayaApi;
import com.jupiter.miniximalaya.interfaces.IPlayerCallback;
import com.jupiter.miniximalaya.presenters.PlayerPresenter;
import com.jupiter.miniximalaya.presenters.RecommendPresenter;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.List;

public class MainActivity extends FragmentActivity implements IPlayerCallback {

    private static final String TAG = "MainActivity";
    private MagicIndicator mainIndicator;
    private ViewPager mainViewPager;
    private ImageView mainTrackIcon;
    private TextView mainPlayTitle;
    private TextView mainAlbumAuthor;
    private ImageView mainPlayControl;
    private PlayerPresenter playerPresenter;
    private View playPanelContain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initIndicate();
        initEvent();

        playerPresenter = PlayerPresenter.getsInstance();
        playerPresenter.registerCallback(this);
    }

    private void initEvent() {

        if (mainPlayControl != null) {
            mainPlayControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (playerPresenter != null) {
                        if (playerPresenter.hasSetPlayList()) {
                            if (playerPresenter.isPlaying()) {
                                playerPresenter.pause();
                            }
                            else{
                                playerPresenter.play();
                            }
                        }
                        else{
                            playFirstAlbum();
                        }
                    }
                }
            });
        }

        if (playPanelContain != null) {
            playPanelContain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (playerPresenter != null && playerPresenter.hasSetPlayList()) {
                        Intent intent = new Intent(MainActivity.this, TrackPlayerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void playFirstAlbum() {
        RecommendPresenter recommendPresenter = RecommendPresenter.getsInstance();
        List<Album> currentRecoomandList = recommendPresenter.getCurrentRecommendList();
        if (currentRecoomandList != null && currentRecoomandList.size() > 0) {
            Album album = currentRecoomandList.get(0);
            playerPresenter.playByAlbumId(album.getId());
        }
    }

    private void initView() {
        mainIndicator = findViewById(R.id.main_indicator);
        mainViewPager = findViewById(R.id.main_viewpager);
        mainTrackIcon = findViewById(R.id.iv_track_icon);
        mainPlayTitle = findViewById(R.id.tv_main_play_title);
        mainPlayTitle.setSelected(true);
        mainAlbumAuthor = findViewById(R.id.tv_album_author);
        mainPlayControl = findViewById(R.id.iv_main_play_icon);
        playPanelContain = findViewById(R.id.playPanelContain);
    }

    private void initIndicate() {

        //对导航栏的设置
        CommonNavigator commonNavigator = new CommonNavigator(this);
        //条目位置平均放置
        commonNavigator.setAdjustMode(true);
        MainIndicatorAdapter mainIndicatorAdapter = new MainIndicatorAdapter(this, mainViewPager);
        commonNavigator.setAdapter(mainIndicatorAdapter);
        mainIndicator.setNavigator(commonNavigator);

        //创建内容适配器
        FragmentManager fragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(fragmentManager);
        mainViewPager.setAdapter(mainContentAdapter);
        
        ViewPagerHelper.bind(mainIndicator, mainViewPager);
    }

    @Override
    protected void onDestroy() {
        //销毁时取消注册，避免内存泄露
        if (playerPresenter != null) {
            playerPresenter.unRegisterCallback(this);
        }
        super.onDestroy();
    }

    @Override
    public void onPlayStart() {
        checkAndUpdatePlayStatus(true);
    }

    @Override
    public void onPlayPause() {
        checkAndUpdatePlayStatus(false);
    }

    @Override
    public void onPlayStop() {
        checkAndUpdatePlayStatus(false);
    }

    private void checkAndUpdatePlayStatus(boolean isPlaying){
        if (mainPlayControl != null ) {
            if(isPlaying){
                mainPlayControl.setImageResource(R.drawable.selector_player_pause);
            }
            else{
                mainPlayControl.setImageResource(R.drawable.selector_player_play);
            }
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
    public void onPlayProgressChanged(int currentProgress, int total) {

    }

    @Override
    public void onPlayTitle(String trackTitle) {

    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel, int currentIndex) {
        if (curModel != null && curModel instanceof Track) {
            Track track = (Track)curModel;

            if (mainTrackIcon != null && !TextUtils.isEmpty(track.getCoverUrlMiddle())) {
                Picasso.with(this).load(track.getCoverUrlMiddle()).into(mainTrackIcon);
            }

            if (mainPlayTitle != null) {
                mainPlayTitle.setText(track.getTrackTitle());
            }

            if (mainAlbumAuthor != null) {
                mainAlbumAuthor.setText(track.getAnnouncer().getNickname());
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

    @Override
    public void onPlaySortChange(boolean isAscending) {

    }
}
