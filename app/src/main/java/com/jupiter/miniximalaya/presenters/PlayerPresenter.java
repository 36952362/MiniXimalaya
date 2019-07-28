package com.jupiter.miniximalaya.presenters;

import com.jupiter.miniximalaya.base.BaseApplication;
import com.jupiter.miniximalaya.interfaces.IPlayerCallback;
import com.jupiter.miniximalaya.interfaces.IPlayerPresenter;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private final String TAG = "PlayerPresenter";

    private static  PlayerPresenter sInstance = null;

    private XmPlayerManager xmPlayerManager;

    private boolean isPlayListSet = false;

    private List<IPlayerCallback> playerCallbacks = new ArrayList<>();
    private String trackTitle;

    private PlayerPresenter(){
        xmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        xmPlayerManager.addAdsStatusListener(this);
        xmPlayerManager.addPlayerStatusListener(this);
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

        if (xmPlayerManager != null) {
            xmPlayerManager.setPlayList(tracks,currentIndex);
            isPlayListSet = true;
            Track track = tracks.get(currentIndex);
            trackTitle = track.getTrackTitle();
        }else {
            LogUtil.e(TAG, "xmPlayerManager is null");
        }
    }


    public boolean isPlaying(){
        if(xmPlayerManager != null){
            return xmPlayerManager.isPlaying();
        }else {
            return false;
        }
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

    @Override
    public void setPlayMode(XmPlayListControl.PlayMode playMode) {

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
    public void registerCallback(IPlayerCallback playerCallback) {
        playerCallback.onPlayTitle(trackTitle);
        if (!playerCallbacks.contains(playerCallback)) {
            playerCallbacks.add(playerCallback);
        }

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
