package com.jupiter.miniximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.List;

public interface ISearchCallback {

    void onSearchResult(List<Album> albums);

    void onLoadMoreResult(List<Album> albums, boolean hasMore);

    void onHotWordResult(List<HotWord> hotWordList);

    void onSuggestResult(List<QueryResult> keyWordList);

    void onError(int errorCode, String errorMsg);
}
