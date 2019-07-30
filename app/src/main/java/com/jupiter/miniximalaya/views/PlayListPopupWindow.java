package com.jupiter.miniximalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.base.BaseApplication;

public class PlayListPopupWindow extends PopupWindow {

    private final View playListView;
    private TextView closeView;

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
    }
}