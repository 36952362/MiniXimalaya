package com.jupiter.miniximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.List;

public interface IPlayerCallback {

    void onPlayStart();
    void onPlayPause();
    void onPlayStop();
    void onPlayList(List<Track> tracks);
    void onPlayModeChanged(XmPlayListControl.PlayMode playMode);
    void onPlayProgressChanged(int currentProgress, int total);
    void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel, int currentIndex);
    void onPlaySortChange(boolean isAscending);
}