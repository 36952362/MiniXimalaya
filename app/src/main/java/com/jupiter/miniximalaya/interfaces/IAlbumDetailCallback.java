package com.jupiter.miniximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IAlbumDetailCallback {

    void onAlbumDetailLoaded(List<Track> tracks);

    void onAlbumDataLoaded(Album album);

    void onLoading();

    //获取数据错误的处理
    void onError(int errorCode, String desc);

    //没有获取到数据的处理
    void onEmpty();
}
