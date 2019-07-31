package com.jupiter.miniximalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.adapters.TrackPlayListAdapter;
import com.jupiter.miniximalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class PlayListPopupWindow extends PopupWindow {

    private final View playListView;
    private TextView closeView;
    private RecyclerView playListRecycleView;
    private final TrackPlayListAdapter trackPlayListAdapter;
    private List<Track> tracks = new ArrayList<>();

    public PlayListPopupWindow(){
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //设置外部点击Pop窗口消失前，要设置setBackgroundDrawable,否则无效
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);

        playListView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.pop_play_list, null);
        setContentView(playListView);

        //设置窗口进入和退出动画
        setAnimationStyle(R.style.playListPopAnimation);

        initView();
        initEvent();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getAppContext());
        playListRecycleView.setLayoutManager(linearLayoutManager);

        trackPlayListAdapter = new TrackPlayListAdapter();
        playListRecycleView.setAdapter(trackPlayListAdapter);
    }

    private void initEvent() {
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }

    private void initView() {

        closeView = playListView.findViewById(R.id.tv_playlist_close);
        playListRecycleView = playListView.findViewById(R.id.rv_playlist_container);

    }

    public void setData(List<Track> tracks) {
        this.tracks.clear();
        this.tracks = tracks;
        if (trackPlayListAdapter != null) {
            trackPlayListAdapter.setData(tracks);
        }
    }

    public void setCurrentPlayIndex(int currentIndex) {

        if (trackPlayListAdapter != null) {
            trackPlayListAdapter.setCurrentPlayIndex(currentIndex);
        }

        if (playListRecycleView != null) {
            playListRecycleView.scrollToPosition(currentIndex);
        }
    }
}