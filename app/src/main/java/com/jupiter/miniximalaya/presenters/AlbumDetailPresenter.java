package com.jupiter.miniximalaya.presenters;

import com.jupiter.miniximalaya.interfaces.IAlbumDetailCallback;
import com.jupiter.miniximalaya.interfaces.IAlbumDetailPresenter;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private List<IAlbumDetailCallback> albumDetailCallbackList = new ArrayList<>();
    private Album album = null;

    private final String TAG = "AlbumDetailPresenter";

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
    public void getAlbumDetail(int albumId, int pageIndex) {

        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.ALBUM_ID, albumId + "");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, pageIndex + "");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                if(null != trackList) {
                    handleAlbumDetailData(trackList.getTracks());
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e(TAG, "errorCode:" + i  + ", errorMsg:" + s);
            }
        });

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

    private void handleAlbumDetailData(List<Track> tracks){
        for(IAlbumDetailCallback albumDetailCallback : albumDetailCallbackList){
            albumDetailCallback.onAlbumDetailLoaded(tracks);
        }
    }
}

