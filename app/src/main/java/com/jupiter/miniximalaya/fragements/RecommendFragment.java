package com.jupiter.miniximalaya.fragements;

import android.content.Intent;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.AlbumDetailActivity;
import com.jupiter.miniximalaya.presenters.AlbumDetailPresenter;
import com.jupiter.miniximalaya.presenters.RecommendPresenter;
import com.jupiter.miniximalaya.adapters.AlbumAdapter;
import com.jupiter.miniximalaya.base.BaseFragment;
import com.jupiter.miniximalaya.interfaces.IRecommendCallback;
import com.jupiter.miniximalaya.utils.DPPXConverter;
import com.jupiter.miniximalaya.views.UILoader;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public class RecommendFragment extends BaseFragment implements IRecommendCallback, UILoader.OnRetryClickListener, AlbumAdapter.OnRecommendItemClickListener {

    private static final  String TAG = "RecommendFragment";
    private View successView;
    private RecyclerView recommendRecyclerView;
    private AlbumAdapter recommendAdapter;
    private RecommendPresenter recommendPresenter;
    private UILoader uiLoader;

    @Override
    protected View onCreateSubView(ViewGroup container) {

        uiLoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                View successView = createSuccessView(container);
                return successView;
            }
        };

        getRecommendDataAndDisplay();

        //如果已经绑定了，先进行解绑
        if(uiLoader.getParent() instanceof ViewGroup){
            ((ViewGroup) uiLoader.getParent()).removeView(uiLoader);
        }

        uiLoader.setRetryClickListener(this);
        return uiLoader;
    }

    private void getRecommendDataAndDisplay() {

        //获取逻辑层对象
        recommendPresenter = RecommendPresenter.getsInstance();

        //注册回调
        recommendPresenter.registerCallback(this);
        recommendPresenter.getRecommendList();
    }

    private View createSuccessView(ViewGroup container) {

        successView = onLoadLayout(R.layout.fragment_recommend, container);
        recommendRecyclerView = successView.findViewById(R.id.rv_recommend);

        TwinklingRefreshLayout refreshLayout = successView.findViewById(R.id.over_scroll_view);
        refreshLayout.setPureScrollModeOn();

        //设置适配器
        recommendAdapter = new AlbumAdapter();
        recommendRecyclerView.setAdapter(recommendAdapter);

        recommendAdapter.setOnRecommendItemClickListener(this);

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
        return successView;
    }

    @Override
    public void onRecommendListLoaded(List<Album> result) {
        uiLoader.updateUIStatus(UILoader.UIStatus.SUCCESS);
        recommendAdapter.updateData(result);
    }

    @Override
    public void onError(int errorCode, String desc) {
        uiLoader.updateUIStatus(UILoader.UIStatus.ERROR);
    }

    @Override
    public void onEmpty() {
        uiLoader.updateUIStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        uiLoader.updateUIStatus(UILoader.UIStatus.LOADING);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //取消注册，避免内存泄露
        if(null != recommendPresenter) {
            recommendPresenter.unRegisterCallback(this);
        }

    }

    @Override
    public void onRetry() {
        if(null != recommendPresenter){
            recommendPresenter.getRecommendList();
        }
    }

    @Override
    public void onItemClick(int position, Album album) {
        Intent intent = new Intent(getContext(), AlbumDetailActivity.class);
        AlbumDetailPresenter.getsInstance().setAlbum(album);
        startActivity(intent);
    }
}
