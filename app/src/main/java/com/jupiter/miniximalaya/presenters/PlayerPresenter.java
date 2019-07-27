package com.jupiter.miniximalaya.presenters;

import com.jupiter.miniximalaya.base.BaseApplication;
import com.jupiter.miniximalaya.interfaces.IPlayerCallback;
import com.jupiter.miniximalaya.interfaces.IPlayerPresenter;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class PlayerPresenter implements IPlayerPresenter {

    private final String TAG = "PlayerPresenter";

    private static  PlayerPresenter sInstance = null;

    private XmPlayerManager xmPlayerManager;

    private boolean isPlayListSet = false;

    private PlayerPresenter(){
        xmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
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
        }else {
            LogUtil.e(TAG, "xmPlayerManager is null");
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

    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {

    }

    @Override
    public void playNext() {

    }

    @Override
    public void setPlayMode(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void getPlayList() {

    }

    @Override
    public void playByIndex(int index) {

    }

    @Override
    public void seekTo(int progress) {

    }

    @Override
    public void registerCallback(IPlayerCallback iPlayerCallback) {

    }

    @Override
    public void unRegisterCallback(IPlayerCallback iPlayerCallback) {

    }
}
