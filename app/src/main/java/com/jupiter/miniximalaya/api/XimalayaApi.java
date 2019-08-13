package com.jupiter.miniximalaya.api;

import com.jupiter.miniximalaya.utils.Constants;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.HashMap;
import java.util.Map;

public class XimalayaApi {

    private XimalayaApi(){};

    private static XimalayaApi sInstance = null;

    public static XimalayaApi getsInstance(){
        if (sInstance == null) {
            synchronized (XimalayaApi.class){
                if (sInstance == null) {
                    sInstance = new XimalayaApi();
                }
            }
        }
        return sInstance;
    }


    public void getRecommendList(IDataCallBack<GussLikeAlbumList> callBack){
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.COUNT_RECOMMEND + "");
        CommonRequest.getGuessLikeAlbum(map, callBack);
    }

    public void getAlbumById(int albumId, int pageIndex, IDataCallBack<TrackList> callBack){
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.ALBUM_ID, albumId + "");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, pageIndex + "");
        CommonRequest.getTracks(map, callBack);
    }


    public void searchByKeyword(String keyword, int currentPage, IDataCallBack<SearchAlbumList> callback) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.SEARCH_KEY, keyword);
        map.put(DTransferConstants.CATEGORY_ID, "");
        map.put(DTransferConstants.PAGE, currentPage + "");
        map.put(DTransferConstants.PAGE_SIZE, Constants.COUNT_RECOMMEND + "");
        CommonRequest.getSearchedAlbums(map, callback);
    }

    public void getHotWords(IDataCallBack<HotWordList> callback){
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.TOP, Constants.COUNT_RECOMMEND + "");
        CommonRequest.getHotWords(map, callback);
    }

    public void getSuggestWord(String keyword, IDataCallBack<SuggestWords> callback){
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.SEARCH_KEY, keyword);
        CommonRequest.getSuggestWord(map, callback);
    }
}
