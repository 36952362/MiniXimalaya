package com.jupiter.miniximalaya.interfaces;

import com.jupiter.miniximalaya.base.IBasePresenter;

public interface ISearchPresenter extends IBasePresenter<ISearchCallback> {

    void doSearch(String keyword);

    void reSearch();

    void loadMore(String keyword);

    void getHotword();

    void getSuggestWords(String keyword);
}
