package com.jupiter.miniximalaya.presenters;

import com.jupiter.miniximalaya.interfaces.IAlbumDetailCallback;
import com.jupiter.miniximalaya.interfaces.IAlbumDetailPresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private List<IAlbumDetailCallback> albumDetailCallbackList = new ArrayList<>();
    private Album album = null;

    private  AlbumDetailPresenter(){}

    private static  AlbumDetailPresenter sInstance = null;

    public static AlbumDetailPresenter getsInstance(){
        if(null == sInstance){
            synchronized (AlbumDetailPresenter.class){
                if(null == sInstance){
                    sInstance = new AlbumDetailPresenter();
                }
            }
        }
        return  sInstance;
    }

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int albumId, int pager) {

    }

    @Override
    public void registerCallback(IAlbumDetailCallback albumDetailCallback){
        if(!albumDetailCallbackList.contains(albumDetailCallback)){
            albumDetailCallbackList.add(albumDetailCallback);
            albumDetailCallback.onAlbumDataLoaded(album);
        }
    }

    @Override
    public void unRegisterCallback(IAlbumDetailCallback albumDetailCallback){
        if(albumDetailCallbackList.contains(albumDetailCallback)){
            albumDetailCallbackList.remove(albumDetailCallback);
        }
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
