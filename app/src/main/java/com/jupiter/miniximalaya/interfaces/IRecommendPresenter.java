package com.jupiter.miniximalaya.interfaces;

public interface IRecommendPresenter {

    //获取推荐内容
    public void getRecommendList();

    //下拉刷新
    public void pullToRefresh();

    //上拉加载更多
    public void loadMore();

    //注册回调类
    public void registerCallback(IRecommendCallback recommendCallback);


    //取消注册
    public void unregisterCallback(IRecommendCallback recommendCallback);

}
