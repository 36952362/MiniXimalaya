package com.jupiter.miniximalaya;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jupiter.miniximalaya.adapters.AlbumDetailAdapter;
import com.jupiter.miniximalaya.base.BaseActivity;
import com.jupiter.miniximalaya.interfaces.IAlbumDetailCallback;
import com.jupiter.miniximalaya.presenters.AlbumDetailPresenter;
import com.jupiter.miniximalaya.utils.DPPXConverter;
import com.jupiter.miniximalaya.utils.GaussianBlur;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.jupiter.miniximalaya.views.RoundRectImageView;
import com.jupiter.miniximalaya.views.UILoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class AlbumDetailActivity extends BaseActivity implements IAlbumDetailCallback, UILoader.OnRetryClickListener, AlbumDetailAdapter.OnItemClickListener {

    private ImageView albumLargeCover;
    private RoundRectImageView albumSmallCover;
    private TextView albumTitle;
    private TextView albumAuthor;
    private AlbumDetailPresenter albumDetailPresenter;
    private String TAG = "AlbumDetailActivity";

    private int albumPageIndex = 1;
    private RecyclerView albumDetailInfo;
    private AlbumDetailAdapter albumDetailAdapter;
    private UILoader uiLoader = null;
    private FrameLayout albumDetailContainer;

    private long albumId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();

        albumDetailPresenter = AlbumDetailPresenter.getsInstance();
        albumDetailPresenter.registerCallback(this);
    }

    private void initView() {
        albumLargeCover = findViewById(R.id.iv_album_large_cover);
        albumSmallCover = findViewById(R.id.iv_album_small_cover);
        albumTitle = findViewById(R.id.tv_album_item_title);
        albumAuthor = findViewById(R.id.tv_album_item_author);

        albumDetailContainer = findViewById(R.id.fl_album_detail_info);

        if(null == uiLoader) {
            uiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
        }
        uiLoader.setRetryClickListener(this);
        albumDetailContainer.removeAllViews();
        albumDetailContainer.addView(uiLoader);
    }

    private View createSuccessView(ViewGroup container) {

        View successView = LayoutInflater.from(this).inflate(R.layout.item_album_detail_list, container, false);

        albumDetailInfo = successView.findViewById(R.id.rv_album_detail_info);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        albumDetailInfo.setLayoutManager(linearLayoutManager);

        albumDetailAdapter = new AlbumDetailAdapter();
        albumDetailAdapter.setOnItemClickListener(this);
        albumDetailInfo.setAdapter(albumDetailAdapter);
        albumDetailInfo.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.left = DPPXConverter.dip2px(view.getContext(), 2);
                outRect.right = DPPXConverter.dip2px(view.getContext(), 2);
                outRect.top = DPPXConverter.dip2px(view.getContext(), 2);
                outRect.bottom = DPPXConverter.dip2px(view.getContext(), 2);
            }
        });

        return successView;
    }


    @Override
    protected void onDestroy() {
        albumDetailPresenter.unRegisterCallback(this);
        super.onDestroy();
    }

    @Override
    public void onAlbumDetailLoaded(List<Track> tracks) {
        if(null == tracks){
            return;
        }
        uiLoader.updateUIStatus(UILoader.UIStatus.SUCCESS);
        albumDetailAdapter.setAlbumDetailData(tracks);
    }

    @Override
    public void onAlbumDataLoaded(Album album) {

        if(null == album){
            return;
        }

        Picasso.with(this).load(album.getCoverUrlLarge()).into(albumLargeCover, new Callback() {
            @Override
            public void onSuccess() {
                Drawable drawable = albumLargeCover.getDrawable();
                if(null != drawable){
                    GaussianBlur.makeBlur(albumLargeCover, AlbumDetailActivity.this);
                }
            }

            @Override
            public void onError() {
                LogUtil.e(TAG, "load picture failed");

            }
        });

        Picasso.with(this).load(album.getCoverUrlSmall()).into(albumSmallCover);
        albumTitle.setText(album.getAlbumTitle());
        albumAuthor.setText(album.getAnnouncer().getNickname());

        albumId = album.getId();
        //获取专辑列表数据
        albumDetailPresenter.getAlbumDetail((int)albumId, albumPageIndex);
    }

    @Override
    public void onLoading() {
        uiLoader.updateUIStatus(UILoader.UIStatus.LOADING);
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
    public void onRetry() {
        //获取专辑列表数据
        albumDetailPresenter.getAlbumDetail((int)albumId, albumPageIndex);
    }

    @Override
    public void onItemClick() {
        Intent intent = new Intent(this, TrackPlayerActivity.class);
        startActivity(intent);
    }
}
