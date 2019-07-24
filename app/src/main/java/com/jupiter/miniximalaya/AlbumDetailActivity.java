package com.jupiter.miniximalaya;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jupiter.miniximalaya.interfaces.IAlbumDetailCallback;
import com.jupiter.miniximalaya.presenters.AlbumDetailPresenter;
import com.jupiter.miniximalaya.utils.GaussianBlur;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.jupiter.miniximalaya.views.RoundRectImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class AlbumDetailActivity extends AppCompatActivity implements IAlbumDetailCallback {

    private ImageView albumLargeCover;
    private RoundRectImageView albumSmallCover;
    private TextView albumTitle;
    private TextView albumAuthor;
    private AlbumDetailPresenter albumDetailPresenter;
    private String TAG = "AlbumDetailActivity";

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
    }


    @Override
    protected void onDestroy() {
        albumDetailPresenter.unRegisterCallback(this);
        super.onDestroy();
    }

    @Override
    public void onAlbumDetailLoaded(List<Track> tracks) {

    }

    @Override
    public void onAlbumDataLoaded(Album album) {

        if(null != album){
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
        }

    }
}
