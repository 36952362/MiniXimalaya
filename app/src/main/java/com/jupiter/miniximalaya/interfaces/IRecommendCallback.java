package com.jupiter.miniximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface IRecommendCallback {


    //获取到数据后的回调函数
    void onRecommendListLoaded(List<Album> result);

    //获取数据错误的处理
    void onError(int errorCode, String desc);

    //没有获取到数据的处理
    void onEmpty();

    //获取中
    void onLoading();
}
