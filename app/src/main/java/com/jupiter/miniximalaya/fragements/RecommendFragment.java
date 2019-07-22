package com.jupiter.miniximalaya.fragements;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.RecommendPresenter;
import com.jupiter.miniximalaya.adapters.RecommendAdapter;
import com.jupiter.miniximalaya.base.BaseFragment;
import com.jupiter.miniximalaya.interfaces.IRecommendCallback;
import com.jupiter.miniximalaya.utils.DPPXConverter;
import com.jupiter.miniximalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public class RecommendFragment extends BaseFragment implements IRecommendCallback {

    private static final  String TAG = "RecommendFragment";
    private View successView;
    private RecyclerView recommendRecyclerView;
    private RecommendAdapter recommendAdapter;
    private RecommendPresenter recommendPresenter;
    private UILoader uiLoader;

    @Override
    protected View onCreateSubView() {

        uiLoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView() {
                View successView = createSuccessView();
                return successView;
            }
        };

        getRecommendDataAndDisplay();

        //如果已经绑定了，先进行解绑
        if(uiLoader.getParent() instanceof ViewGroup){
            ((ViewGroup) uiLoader.getParent()).removeView(uiLoader);
        }

        return uiLoader;
    }

    private void getRecommendDataAndDisplay() {

        //获取逻辑层对象
        recommendPresenter = RecommendPresenter.getsInstance();

        //注册回调
        recommendPresenter.registerCallback(this);
        recommendPresenter.getRecommendList();
    }

    private View createSuccessView() {

        successView = onLoadLayout(R.layout.fragement_recommend);
        recommendRecyclerView = successView.findViewById(R.id.rv_recommend);

        //设置适配器
        recommendAdapter = new RecommendAdapter();
        recommendRecyclerView.setAdapter(recommendAdapter);

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
            recommendPresenter.unregisterCallback(this);
        }

    }
}
