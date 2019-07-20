package com.jupiter.miniximalaya.fragements;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.adapters.RecommendAdapter;
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
    private View rootView;
    private RecyclerView recommendRecyclerView;
    private RecommendAdapter recommendAdapter;

    @Override
    protected View onSubViewLoaded() {
        rootView = onLoadLayout(R.layout.fragement_recommend);
        recommendRecyclerView = rootView.findViewById(R.id.rv_recommend);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recommendRecyclerView.setLayoutManager(linearLayoutManager);

        //设置适配器
        recommendAdapter = new RecommendAdapter();
        recommendRecyclerView.setAdapter(recommendAdapter);

        getRecommendData();
        return rootView;
    }

    private void getRecommendData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.i(TAG, "getGuessLikeAlbum success");
                if(null != gussLikeAlbumList){
                    recommendAdapter.updateData(gussLikeAlbumList.getAlbumList());
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e(TAG, "getGuessLikeAlbum failed");
                LogUtil.e(TAG, "errorCode: " + i + ", reason:" + s);
            }
        });
    }
}
