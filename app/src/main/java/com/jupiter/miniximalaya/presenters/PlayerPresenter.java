package com.jupiter.miniximalaya.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.jupiter.miniximalaya.api.XimalayaApi;
import com.jupiter.miniximalaya.base.BaseApplication;
import com.jupiter.miniximalaya.interfaces.IPlayerCallback;
import com.jupiter.miniximalaya.interfaces.IPlayerPresenter;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;

public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private final String TAG = "PlayerPresenter";

    private static  PlayerPresenter sInstance = null;

    private XmPlayerManager xmPlayerManager;

    private boolean isPlayListSet = false;

    private List<IPlayerCallback> playerCallbacks = new ArrayList<>();

    private XmPlayListControl.PlayMode currentPlayMode = PLAY_MODEL_LIST;

    private static String PLAY_MODE_NAME = "PlayMode";
    private static String PLAY_MODE_KEY = "CurrentPlayMode";
    private final SharedPreferences sharedPreferences;

    private final int PLAY_MODE_LIST_INT = 0;
    private final int PLAY_MODE_LIST_LOOP_INT = 1;
    private final int PLAY_MODE_RANDOM_INT = 2;
    private final int PLAY_MODE_SINGLE_LOOP_INT = 3;

    private List<Track> tracks = new ArrayList<>();
    private int currentPlayIndex = 0;
    private boolean isAscending = true;
    @Getter
    private int currentProgress = 0;
    @Getter
    private int total = 0;


    private PlayerPresenter(){
        xmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        xmPlayerManager.addAdsStatusListener(this);
        xmPlayerManager.addPlayerStatusListener(this);
        xmPlayerManager.setPlayMode(currentPlayMode);
        sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(PLAY_MODE_NAME, Context.MODE_PRIVATE);
    }

    public static  PlayerPresenter getsInstance() {
        if(null == sInstance){
            synchronized (PlayerPresenter.class){}
            if(null == sInstance){
                sInstance = new PlayerPresenter();
            }
        }
        return sInstance;
    }


    public void setPlayList(List<Track> tracks, int currentIndex){
        this.tracks.clear();
        this.tracks.addAll(tracks);
        this.currentPlayIndex = currentIndex;
        if (xmPlayerManager != null && tracks.size() > 0) {
            xmPlayerManager.setPlayList(tracks,currentPlayIndex);
            isPlayListSet = true;
        }
    }

    public int getIntFromMode(XmPlayListControl.PlayMode playMode){
        switch (playMode){
            case PLAY_MODEL_LIST:
                return PLAY_MODE_LIST_INT;
            case PLAY_MODEL_LIST_LOOP:
                return PLAY_MODE_LIST_LOOP_INT;
            case PLAY_MODEL_RANDOM:
                return PLAY_MODE_RANDOM_INT;
            case PLAY_MODEL_SINGLE_LOOP:
                return PLAY_MODE_SINGLE_LOOP_INT;
        }
        return PLAY_MODE_LIST_INT;
    }

    public XmPlayListControl.PlayMode getModeByInt(int playModeInt){
        switch (playModeInt){
            case  PLAY_MODE_LIST_INT:
                return PLAY_MODEL_LIST;
            case PLAY_MODE_LIST_LOOP_INT:
                return PLAY_MODEL_LIST_LOOP;
            case PLAY_MODE_RANDOM_INT:
                return  PLAY_MODEL_RANDOM;
            case PLAY_MODE_SINGLE_LOOP_INT:
                return PLAY_MODEL_SINGLE_LOOP;
        }
        return PLAY_MODEL_LIST;
    }

    public boolean isPlaying(){
        if(xmPlayerManager != null){
            return xmPlayerManager.isPlaying();
        }else {
            return false;
        }
    }


    public boolean hasSetPlayList() {
        return  isPlayListSet;
    }

    public void play(int position) {
        if (xmPlayerManager != null) {
            xmPlayerManager.play(position);
        }
    }

    @Override
    public void play() {
        if(isPlayListSet){
            xmPlayerManager.play();
        }
    }

    @Override
    public void pause() {
        xmPlayerManager.pause();
    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {
        if (xmPlayerManager != null) {
            xmPlayerManager.playPre();
        }
    }

    @Override
    public void playNext() {
        if (xmPlayerManager != null) {
            xmPlayerManager.playNext();
        }
    }

    public void playByAlbumId(long albumId) {
        XimalayaApi ximalayaApi = XimalayaApi.getsInstance();
        ximalayaApi.getAlbumById((int)albumId, 1, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                setPlayList(trackList.getTracks(), 0);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(BaseApplication.getAppContext(), "请求数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setPlayMode(XmPlayListControl.PlayMode playMode) {
        if (xmPlayerManager != null) {
            currentPlayMode = playMode;
            xmPlayerManager.setPlayMode(playMode);

            for (IPlayerCallback playerCallback : playerCallbacks) {
                playerCallback.onPlayModeChanged(playMode);
            }

            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt(PLAY_MODE_KEY, getIntFromMode(playMode));
            edit.commit();
        }
    }

    @Override
    public void getPlayList() {
        if (xmPlayerManager != null) {
            List<Track> playList = xmPlayerManager.getPlayList();
            for (IPlayerCallback playerCallback : playerCallbacks) {
                playerCallback.onPlayList(playList);
            }
        }
    }

    @Override
    public void playByIndex(int index) {

    }

    @Override
    public void seekTo(int progress) {
        if (xmPlayerManager != null) {
            xmPlayerManager.seekTo(progress);
        }
    }

    @Override
    public void reversePlayList() {

        if (xmPlayerManager != null) {
            Collections.reverse(tracks);
            isAscending = !isAscending;
            currentPlayIndex = tracks.size() - currentPlayIndex - 1;
            xmPlayerManager.setPlayList(tracks, currentPlayIndex);
            PlayableModel currSound = xmPlayerManager.getCurrSound();

            for (IPlayerCallback playerCallback : playerCallbacks) {
                playerCallback.onPlayList(tracks);
                playerCallback.onSoundSwitch(currSound, currSound, currentPlayIndex);
                playerCallback.onPlaySortChange(isAscending);
            }
        }
    }

    @Override
    public void registerCallback(IPlayerCallback playerCallback) {

        if (!playerCallbacks.contains(playerCallback)) {
            playerCallbacks.add(playerCallback);
        }

        setPlayList(tracks, currentPlayIndex);

        int playModeInt = sharedPreferences.getInt(PLAY_MODE_KEY, PLAY_MODE_LIST_INT);

        XmPlayListControl.PlayMode playMode = getModeByInt(playModeInt);
        playerCallback.onPlayModeChanged(playMode);
        playerCallback.onPlayProgressChanged(getCurrentProgress(), getTotal());
    }

    @Override
    public void unRegisterCallback(IPlayerCallback playerCallback) {
        if (playerCallbacks.contains(playerCallback)) {
            playerCallbacks.remove(playerCallback);
        }
    }

    //Play callback start
    @Override
    public void onPlayStart() {
        for (IPlayerCallback playerCallback : playerCallbacks) {
            playerCallback.onPlayStart();
        }
    }

    @Override
    public void onPlayPause() {
        for (IPlayerCallback playerCallback : playerCallbacks) {
            playerCallback.onPlayPause();
        }
    }

    @Override
    public void onPlayStop() {

        for (IPlayerCallback playerCallback : playerCallbacks) {
            playerCallback.onPlayStop();
        }

    }

    @Override
    public void onSoundPlayComplete() {

    }

    @Override
    public void onSoundPrepared() {

        if (xmPlayerManager != null) {
            int playerStatus = xmPlayerManager.getPlayerStatus();
            LogUtil.d(TAG, "playerStatus: " + playerStatus);
            if(PlayerConstants.STATE_PREPARED  ==  xmPlayerManager.getPlayerStatus()){
                xmPlayerManager.play();
            }
        }

    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {

        int currentIndex = xmPlayerManager.getCurrentIndex();

        for (IPlayerCallback playerCallback : playerCallbacks) {
            playerCallback.onSoundSwitch(lastModel, curModel, currentIndex);
        }
    }

    @Override
    public void onBufferingStart() {

    }

    @Override
    public void onBufferingStop() {

    }

    @Override
    public void onBufferProgress(int i) {

    }

    @Override
    public void onPlayProgress(int currentProgress, int total) {
        this.currentProgress = currentProgress;
        this.total = total;
        //unit: ms
        for (IPlayerCallback playerCallback : playerCallbacks) {
            playerCallback.onPlayProgressChanged(currentProgress, total);
        }

    }

    @Override
    public boolean onError(XmPlayerException e) {
        return false;
    }
    //Play callback end


    //Ads callback start

    @Override
    public void onStartGetAdsInfo() {

    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {

    }

    @Override
    public void onAdsStartBuffering() {

    }

    @Override
    public void onAdsStopBuffering() {

    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {

    }

    @Override
    public void onCompletePlayAds() {

    }

    @Override
    public void onError(int i, int i1) {

    }
    //Ads callback end
}
