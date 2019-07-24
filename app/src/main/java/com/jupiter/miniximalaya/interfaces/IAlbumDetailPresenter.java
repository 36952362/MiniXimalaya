package com.jupiter.miniximalaya.interfaces;

public interface IAlbumDetailPresenter {

    void pull2RefreshMore();

    void loadMore();

    void getAlbumDetail(int albumId, int pager);

    void registerCallback(IAlbumDetailCallback albumDetailCallback);

    void unRegisterCallback(IAlbumDetailCallback albumDetailCallback);
}
