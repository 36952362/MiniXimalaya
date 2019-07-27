package com.jupiter.miniximalaya.interfaces;

import com.jupiter.miniximalaya.base.IBasePresenter;

public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetailCallback> {

    void pull2RefreshMore();

    void loadMore();

    void getAlbumDetail(int albumId, int pager);
}
