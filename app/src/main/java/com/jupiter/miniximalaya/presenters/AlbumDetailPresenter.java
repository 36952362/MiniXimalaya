package com.jupiter.miniximalaya.presenters;

import com.jupiter.miniximalaya.api.XimalayaApi;
import com.jupiter.miniximalaya.interfaces.IAlbumDetailCallback;
import com.jupiter.miniximalaya.interfaces.IAlbumDetailPresenter;
import com.jupiter.miniximalaya.interfaces.IRecommendCallback;
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

    //当前专辑ID
    private int albumId = -1;

    //当前专辑加载页
    private int currentPageIndex = 0;

    private List<Track> currentTracks = new ArrayList<>();

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


    private void updateLoading(){
        for (IAlbumDetailCallback albumDetailCallback : albumDetailCallbackList) {
            albumDetailCallback.onLoading();
        }
    }

    @Override
    public void loadMore() {
        currentPageIndex++;
        loadData(true);
    }

    private void loadData(final boolean isLoadMore){

        XimalayaApi ximalayaApi = XimalayaApi.getsInstance();
        ximalayaApi.getAlbumById(albumId, currentPageIndex, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                if(null != trackList) {
                    if(isLoadMore){
                        //加载更多，追加到列表后面
                        currentTracks.addAll(trackList.getTracks());
                        handleLoadFinishResult(trackList.getTracks().size());
                    }
                    else{
                        //刷新，加载到列表前面
                        currentTracks.addAll(0, trackList.getTracks());
                    }
                    handleAlbumDetailData(currentTracks);
                }
            }

            @Override
            public void onError(int i, String s) {
                if(isLoadMore){
                    currentPageIndex--;
                }
                LogUtil.e(TAG, "errorCode:" + i  + ", errorMsg:" + s);
                handleAlbumDetailError(i, s);
            }
        });
    }

    private void handleLoadFinishResult(int size) {
        for (IAlbumDetailCallback albumDetailCallback : albumDetailCallbackList) {
            albumDetailCallback.onLoadMoreFinish(size);
        }
    }

    @Override
    public void getAlbumDetail(int albumId, int pageIndex) {
        updateLoading();
        this.albumId = albumId;
        this.currentPageIndex = pageIndex;
        this.currentTracks.clear();
        loadData(false);
    }

    private void handleAlbumDetailError(int i, String s) {
        for (IAlbumDetailCallback albumDetailCallback : albumDetailCallbackList) {
            albumDetailCallback.onError(i, s);
        }
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
        if(null == tracks || tracks.size() == 0){
            for(IAlbumDetailCallback albumDetailCallback : albumDetailCallbackList){
                albumDetailCallback.onEmpty();
            }
        }else{
            for(IAlbumDetailCallback albumDetailCallback : albumDetailCallbackList){
                albumDetailCallback.onAlbumDetailLoaded(tracks);
            }
        }
    }
}