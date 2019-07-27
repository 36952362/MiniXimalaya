package com.jupiter.miniximalaya.base;

public interface IBasePresenter<T> {

    //注册回调类
    void registerCallback(T t);

    //取消注册
    void unRegisterCallback(T t);
}
