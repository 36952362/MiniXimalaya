package com.jupiter.miniximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IAlbumDetailCallback {

    void onAlbumDetailLoaded(List<Track> tracks);

    void onAlbumDataLoaded(Album album);
}
