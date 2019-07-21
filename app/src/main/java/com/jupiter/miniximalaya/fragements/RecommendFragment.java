package com.jupiter.miniximalaya.fragements;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.RecommendPresenter;
import com.jupiter.miniximalaya.adapters.RecommendAdapter;
import com.jupiter.miniximalaya.base.BaseFragement;
import com.jupiter.miniximalaya.interfaces.IRecommendCallback;
import com.jupiter.miniximalaya.utils.Constants;
import com.jupiter.miniximalaya.utils.DPPXConverter;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendFragment extends BaseFragement implements IRecommendCallback {

    private static final  String TAG = "RecommendFragment";
    private View rootView;
    private RecyclerView recommendRecyclerView;
    private RecommendAdapter recommendAdapter;
    private RecommendPresenter recommendPresenter;

    @Override
    protected View onSubViewLoaded() {
        rootView = onLoadLayout(R.layout.fragement_recommend);
        recommendRecyclerView = rootView.findViewById(R.id.rv_recommend);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recommendRecyclerView.setLayoutManager(linearLayoutManager);

        recommendRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.left = DPPXConverter.dip2px(view.getContext(), 5);
                outRect.right = DPPXConverter.dip2px(view.getContext(), 5);
                outRect.top = DPPXConverter.dip2px(view.getContext(), 5);
                outRect.bottom = DPPXConverter.dip2px(view.getContext(), 5);
            }
        });

        //设置适配器
        recommendAdapter = new RecommendAdapter();
        recommendRecyclerView.setAdapter(recommendAdapter);

        //获取逻辑层对象
        recommendPresenter = RecommendPresenter.getsInstance();

        //注册回调
         recommendPresenter.registerCallback(this);
        recommendPresenter.getRecommendList();

        //getRecommendData();
        return rootView;
    }

    @Override
    public void onRecommendListLoaded(List<Album> result) {
        recommendAdapter.updateData(result);
    }

    @Override
    public void onLoadMore(List<Album> result) {

    }

    @Override
    public void onRefreshMore(List<Album> result) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //取消注册，避免内存泄露
        if(null != recommendPresenter) {
            recommendPresenter.unregisterCallback(this);
        }

    }
}
