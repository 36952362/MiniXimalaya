package com.jupiter.miniximalaya;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jupiter.miniximalaya.adapters.AlbumDetailAdapter;
import com.jupiter.miniximalaya.base.BaseActivity;
import com.jupiter.miniximalaya.base.BaseApplication;
import com.jupiter.miniximalaya.interfaces.IAlbumDetailCallback;
import com.jupiter.miniximalaya.interfaces.IPlayerCallback;
import com.jupiter.miniximalaya.presenters.AlbumDetailPresenter;
import com.jupiter.miniximalaya.presenters.PlayerPresenter;
import com.jupiter.miniximalaya.utils.DPPXConverter;
import com.jupiter.miniximalaya.utils.GaussianBlur;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.jupiter.miniximalaya.views.RoundRectImageView;
import com.jupiter.miniximalaya.views.UILoader;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailActivity extends BaseActivity implements IAlbumDetailCallback, UILoader.OnRetryClickListener, AlbumDetailAdapter.OnItemClickListener, IPlayerCallback {

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
    private PlayerPresenter playerPresenter;
    private ImageView playStatusImageView;
    private TextView playStatusTextView;
    private List<Track> tracks = new ArrayList<>();
    private TwinklingRefreshLayout albumDetailRefreshLayout;
    private String trackTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();

        albumDetailPresenter = AlbumDetailPresenter.getsInstance();
        albumDetailPresenter.registerCallback(this);


        playerPresenter = PlayerPresenter.getsInstance();
        playerPresenter.registerCallback(this);

        checkAndUpdatePlayStatus(playerPresenter.isPlaying());

        initEvent();
    }

    private void initEvent() {

        playStatusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndUpdatePlayStatus();
            }
        });

        playStatusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndUpdatePlayStatus();
            }
        });
    }


    private void checkAndUpdatePlayStatus(){
        if (playerPresenter != null) {
            if(playerPresenter.hasSetPlayList()) {
                updatePlayStatus();
            }
            else{
                setPlayList();
            }
        }
    }

    private void setPlayList() {
        if(playerPresenter != null){
            playerPresenter.setPlayList(tracks, 0);
        }
        updatePlayStatus();
    }

    private void updatePlayStatus() {
        if (playerPresenter.isPlaying()) {
            playerPresenter.pause();
        }
        else{
            playerPresenter.play();
        }
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

        playStatusImageView = findViewById(R.id.iv_album_item_player);

        playStatusTextView = findViewById(R.id.id_album_player_status);
        playStatusTextView.setSelected(true);
    }

    private View createSuccessView(ViewGroup container) {

        View successView = LayoutInflater.from(this).inflate(R.layout.item_album_detail_list, container, false);

        albumDetailRefreshLayout = successView.findViewById(R.id.albumDetailRefreshLayout);


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


        BezierLayout headView = new BezierLayout(this);
        albumDetailRefreshLayout.setHeaderView(headView);
        albumDetailRefreshLayout.setMaxHeadHeight(140);

        albumDetailRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                BaseApplication.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        albumDetailRefreshLayout.finishRefreshing();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                isLoadingMore = true;
                albumDetailPresenter.loadMore();
            }
        });

        return successView;
    }

    private boolean isLoadingMore = false;

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
        if (isLoadingMore) {
            //完成加载更多
            albumDetailRefreshLayout.finishLoadmore();
            isLoadingMore = false;
        }
        this.tracks.clear();
        this.tracks.addAll(tracks);
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
    public void onItemClick(List<Track> tracks, int position) {
        PlayerPresenter.getsInstance().setPlayList(tracks, position);
        Intent intent = new Intent(this, TrackPlayerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPlayStart() {
        checkAndUpdatePlayStatus(true);
    }

    @Override
    public void onPlayPause() {
        checkAndUpdatePlayStatus(false);
    }

    @Override
    public void onPlayStop() {
        checkAndUpdatePlayStatus(false);
    }


    private void checkAndUpdatePlayStatus(boolean isPlaying){
        if (playStatusImageView != null && playStatusTextView != null) {
            if(isPlaying){
                playStatusImageView.setImageResource(R.drawable.selector_playstatus_pause);
                playStatusTextView.setText(R.string.album_player_playing);
                if (!TextUtils.isEmpty(trackTitle)) {
                    playStatusTextView.setText(trackTitle);
                }
            }
            else{
                playStatusImageView.setImageResource(R.drawable.selector_playstatus_playing);
                playStatusTextView.setText(R.string.album_player_pause);
            }
        }
    }

    @Override
    public void onPlayError(XmPlayerException exception) {

    }

    @Override
    public void onPlayPre() {

    }

    @Override
    public void onPlayNext() {

    }

    @Override
    public void onPlayList(List<Track> tracks) {

    }

    @Override
    public void onPlayModeChanged(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onPlayProgressChanged(int currentProgress, int total) {

    }

    @Override
    public void onPlayTitle(String trackTitle) {

    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel, int currentIndex) {

        trackTitle = ((Track)curModel).getTrackTitle();
        if (!TextUtils.isEmpty(trackTitle)) {
            playStatusTextView.setText(trackTitle);
        }
    }

    @Override
    public void onAdsStartBuffering() {

    }

    @Override
    public void onAdsStopBuffering() {

    }

    @Override
    public void onCompletePlayAds() {

    }

    @Override
    public void onPlaySortChange(boolean isAscending) {

    }
}
