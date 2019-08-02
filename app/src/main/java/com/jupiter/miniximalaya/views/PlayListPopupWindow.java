package com.jupiter.miniximalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.adapters.TrackPlayListAdapter;
import com.jupiter.miniximalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.ArrayList;
import java.util.List;

public class PlayListPopupWindow extends PopupWindow {

    private final View playListView;
    private TextView closeView;
    private RecyclerView playListRecycleView;
    private final TrackPlayListAdapter trackPlayListAdapter;
    private List<Track> tracks = new ArrayList<>();
    private ImageView playModeIV;
    private TextView playModeTitle;
    private PlayListClickListener playListClickListener;
    private ImageView playListSortImageView;
    private TextView playListSortTextView;

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

        playModeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playListClickListener.onPlayModeClick();
            }
        });

        playModeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playListClickListener.onPlayModeClick();
            }
        });

        playListSortImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playListClickListener.onPlaySortClick();
            }
        });

        playListSortTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playListClickListener.onPlaySortClick();
            }
        });

    }

    private void initView() {

        closeView = playListView.findViewById(R.id.tv_playlist_close);
        playListRecycleView = playListView.findViewById(R.id.rv_playlist_container);
        playModeIV = playListView.findViewById(R.id.iv_play_mode_switch);
        playModeTitle = playListView.findViewById(R.id.tv_playlist_mode_title);

        playListSortImageView = playListView.findViewById(R.id.iv_play_list_sort);
        playListSortTextView = playListView.findViewById(R.id.tv_playlist_sort);

    }

    public void setData(List<Track> tracks) {
        this.tracks.clear();
        this.tracks.addAll(tracks);
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

    public void setPlayItemClickListener(PlayItemClickListener  playItemClickListener){
        if (trackPlayListAdapter != null) {
            trackPlayListAdapter.setPlayItemClickListener(playItemClickListener);
        }
    }


    public void setPlayListSort(boolean isAscending){
        if(isAscending){
            playListSortImageView.setImageResource(R.drawable.selector_playlist_sort_ascending);
            playListSortTextView.setText(R.string.play_list_sort_ascending);
        }
        else{
            playListSortImageView.setImageResource(R.drawable.selector_playlist_sort_descending);
            playListSortTextView.setText(R.string.play_list_sort_descending);
        }
    }


    public void updatePlayModeImage(XmPlayListControl.PlayMode playMode) {
        int resId = R.drawable.selector_player_mode_list;
        int tvResId = R.string.play_mode_order;

        switch (playMode){
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.selector_player_mode_list_loop;
                tvResId = R.string.play_mode_order_loop;
                break;
            case PLAY_MODEL_LIST:
                resId = R.drawable.selector_player_mode_list;
                tvResId = R.string.play_mode_order;
                break;
            case PLAY_MODEL_RANDOM:
                resId = R.drawable.selector_player_mode_random;
                tvResId = R.string.play_mode_random;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resId = R.drawable.selector_player_mode_loop_one;
                tvResId = R.string.play_mode_single_loop;
                break;
        }
        playModeIV.setImageResource(resId);
        playModeTitle.setText(tvResId);
    }

    public interface PlayItemClickListener{
        void onClick(int position);
    }

    public void setPlayListClickListener(PlayListClickListener playListClickListener){
        this.playListClickListener = playListClickListener;
    }

    public interface PlayListClickListener {
        void onPlayModeClick();
        void onPlaySortClick();
    }
}