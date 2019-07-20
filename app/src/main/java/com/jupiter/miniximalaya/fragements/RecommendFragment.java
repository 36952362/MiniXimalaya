package com.jupiter.miniximalaya.fragements;

import android.view.View;
import android.view.ViewGroup;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.base.BaseFragement;
import com.jupiter.miniximalaya.utils.Constants;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.Map;

public class RecommendFragment extends BaseFragement {

    private static final  String TAG = "RecommendFragment";

    @Override
    protected View onSubViewLoaded() {
        View view = onLoadLayout(R.layout.fragement_recommend);

        getRecommendData();
        return view;
    }

    private void getRecommendData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.i(TAG, "getGuessLikeAlbum success");
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e(TAG, "getGuessLikeAlbum failed");
                LogUtil.e(TAG, "errorCode: " + i + ", reason:" + s);
            }
        });
    }
}
