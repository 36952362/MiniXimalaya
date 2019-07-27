package com.jupiter.miniximalaya.interfaces;

import com.jupiter.miniximalaya.base.IBasePresenter;

public interface IRecommendPresenter extends IBasePresenter<IRecommendCallback> {

    //获取推荐内容
    void getRecommendList();

}
