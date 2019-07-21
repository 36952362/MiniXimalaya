package com.jupiter.miniximalaya;

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

        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.i(TAG, "getGuessLikeAlbum success");
                if(null != gussLikeAlbumList){
                    handleRecommendList(gussLikeAlbumList.getAlbumList());
                    //recommendAdapter.updateData(gussLikeAlbumList.getAlbumList());
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e(TAG, "getGuessLikeAlbum failed");
                LogUtil.e(TAG, "errorCode: " + i + ", reason:" + s);
            }
        });

    }

    private void handleRecommendList(List<Album> albumList) {
        //通知UI更新
        for(IRecommendCallback recommendCallback : recommendCallbackList){
            recommendCallback.onRecommendListLoaded(albumList);
        }
    }

    @Override
    public void pullToRefresh() {

    }

    @Override
    public void loadMore() {

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
