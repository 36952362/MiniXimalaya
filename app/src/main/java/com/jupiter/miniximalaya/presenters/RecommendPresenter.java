package com.jupiter.miniximalaya.presenters;

import com.jupiter.miniximalaya.interfaces.IRecommendCallback;
import com.jupiter.miniximalaya.interfaces.IRecommendPresenter;
import com.jupiter.miniximalaya.utils.Constants;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendPresenter implements IRecommendPresenter {

    private static final String TAG = "RecommendPresenter";

    private List<IRecommendCallback> recommendCallbackList = new ArrayList<>();

    private RecommendPresenter(){}

    private static RecommendPresenter sInstance = null;

    public static RecommendPresenter getsInstance(){
        if(null == sInstance){
            synchronized (RecommendPresenter.class){
                if(null == sInstance){
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getRecommendList() {

        updateLoading();

        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.COUNT_RECOMMEND + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.i(TAG, "getGuessLikeAlbum success");
                if(null != gussLikeAlbumList){
                    handleRecommendList(gussLikeAlbumList.getAlbumList());
                }
            }

            @Override
            public void onError(int i, String s) {
                handleRecommendError(i, s);
            }
        });

    }

    private void updateLoading(){
        for (IRecommendCallback recommendCallback : recommendCallbackList) {
            recommendCallback.onLoading();
        }
    }

    private void handleRecommendError(int i, String s) {
        for (IRecommendCallback recommendCallback : recommendCallbackList) {
            recommendCallback.onError(i, s);
        }
    }

    private void handleRecommendList(List<Album> albumList) {
        if(albumList.isEmpty()){
            for (IRecommendCallback recommendCallback : recommendCallbackList) {
                recommendCallback.onEmpty();
            }
            return;
        }

        //通知UI更新
        for(IRecommendCallback recommendCallback : recommendCallbackList){
            recommendCallback.onRecommendListLoaded(albumList);
        }
    }


    @Override
    public void registerCallback(IRecommendCallback recommendCallback) {

        if(!recommendCallbackList.contains(recommendCallback)){
            recommendCallbackList.add(recommendCallback);
        }

    }

    @Override
    public void unregisterCallback(IRecommendCallback recommendCallback) {

        if(recommendCallbackList.contains(recommendCallback)){
            recommendCallbackList.remove(recommendCallback);
        }

    }
}
