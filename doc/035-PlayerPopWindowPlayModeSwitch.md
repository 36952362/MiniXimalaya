#035: 播放器弹出窗口播放模式切换

#[首页](./../README.md)

点击播放器节目列表弹出窗口上切换模式图标能够改变播放模式，并同时修改播放器节目相应的播放模式图标

##1. 创建一个播放模式监听接口和注册接口


**PlayListPopupWindow.java:**

    public interface PlayModeClickListener{
        void onClick();
    }
    
    public void setPlayModeClickListener(PlayModeClickListener playModeClickListener){
	        this.playModeClickListener = playModeClickListener;
	    }

##2. 播放器类注册播放器监听接口

**TrackPlayerActivity.java:**

	playListPopupWindow.setPlayModeClickListener(new PlayListPopupWindow.PlayModeClickListener() {
        @Override
        public void onClick() {
            switchPlayMode();
        }
    });

##3. 播放器列表弹出窗口播放模式图标添加点击事件，并回调注册方法

**PlayListPopupWindow.java:**

	playModeIV.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            playModeClickListener.onClick();
        }
    });
##4. 修改播放器列表弹出窗口播放模式图标和文字

**TrackPlayerActivity.java:**

	public void onPlayModeChanged(XmPlayListControl.PlayMode playMode) {
        currentPlayMode = playMode;

        if (playListPopupWindow != null) {
            playListPopupWindow.updatePlayModeImage(playMode);
        }
        updatePlayModeImage();
    }
    
**PlayListPopupWindow.java:**

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
##5. 效果图
![效果图](./pics/PlayerPopWindowModeSwitch.gif)