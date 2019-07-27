package com.jupiter.miniximalaya.interfaces;

import com.jupiter.miniximalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

public interface IPlayerPresenter extends IBasePresenter<IPlayerCallback> {

    void play();
    void pause();
    void stop();
    void playPre();
    void playNext();
    void setPlayMode(XmPlayListControl.PlayMode playMode);
    void getPlayList();
    void playByIndex(int index);
    void seekTo(int progress);
}
