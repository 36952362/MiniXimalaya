package com.jupiter.miniximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface IRecommendCallback {


    //获取到数据后的回调函数
    public void onRecommendListLoaded(List<Album> result);

    //加载更多
    public void onLoadMore(List<Album> result);


    //下拉刷新更多的数据
    public void onRefreshMore(List<Album> result);
}
