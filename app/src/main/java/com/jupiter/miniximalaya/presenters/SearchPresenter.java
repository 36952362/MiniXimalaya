package com.jupiter.miniximalaya.presenters;

import com.jupiter.miniximalaya.api.XimalayaApi;
import com.jupiter.miniximalaya.interfaces.ISearchCallback;
import com.jupiter.miniximalaya.interfaces.ISearchPresenter;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements ISearchPresenter {

    private static final String TAG = "SearchPresenter";
    private List<ISearchCallback> callbacks = new ArrayList<>();
    private final XimalayaApi ximalayaApi;

    private final static int DEFAULT_PAGE = 1;
    private final int currentPage = DEFAULT_PAGE;
    private String searchKeyword = "";

    private SearchPresenter(){
        ximalayaApi = XimalayaApi.getsInstance();
    }

    private static SearchPresenter sInstance = null;

    public static SearchPresenter getsInstance(){
        if (sInstance == null) {
            synchronized (SearchPresenter.class){
                if (sInstance == null) {
                    sInstance = new SearchPresenter();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void doSearch(String keyword) {
        this.searchKeyword = keyword;
        search(keyword);

    }

    private void search(String keyword) {
        ximalayaApi.searchByKeyword(keyword, currentPage, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(SearchAlbumList searchAlbumList) {
                if (searchAlbumList != null) {
                    List<Album> albumList = searchAlbumList.getAlbums();
                    for (ISearchCallback callback : callbacks) {
                        callback.onSearchResult(albumList);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "search, errorCode:" + errorCode);
                LogUtil.d(TAG, "search, errorMsg:" + errorMsg);
                for (ISearchCallback callback : callbacks) {
                    callback.onError(errorCode,errorMsg);
                }
            }
        });
    }

    @Override
    public void reSearch() {
        search(searchKeyword);
    }

    @Override
    public void loadMore(String keyword) {

    }

    @Override
    public void getHotWords() {
        ximalayaApi.getHotWords(new IDataCallBack<HotWordList>() {
            @Override
            public void onSuccess(HotWordList hotWordList) {
                if (hotWordList != null) {
                    List<HotWord> hotWordResultList = hotWordList.getHotWordList();
                    for (ISearchCallback callback : callbacks) {
                        callback.onHotWordResult(hotWordResultList);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "getHotWords, errorCode:" + errorCode);
                LogUtil.d(TAG, "getHotWords, errorMsg:" + errorMsg);
            }
        });
    }

    @Override
    public void getSuggestWords(String keyword) {
        ximalayaApi.getSuggestWord(keyword, new IDataCallBack<SuggestWords>() {
            @Override
            public void onSuccess(SuggestWords suggestWords) {
                if (suggestWords != null) {
                    List<QueryResult> suggestWordsList = suggestWords.getKeyWordList();
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "getSuggestWords, errorCode:" + errorCode);
                LogUtil.d(TAG, "getSuggestWords, errorMsg:" + errorMsg);
            }
        });

    }

    @Override
    public void registerCallback(ISearchCallback iSearchCallback) {
        if (!callbacks.contains(iSearchCallback)) {
            callbacks.add(iSearchCallback);
        }
    }

    @Override
    public void unRegisterCallback(ISearchCallback iSearchCallback) {
        callbacks.remove(iSearchCallback);
    }
}
