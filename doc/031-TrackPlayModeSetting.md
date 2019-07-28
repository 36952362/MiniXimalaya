#031: 播放器播放模式的设置

#[首页](./../README.md)

正常的播放器会提供几种播放模式，比如单曲循环，列表顺序，列表循环以及随机播放

##1. 定义一个播放模式切换的顺序

**TrackPlayerActivity.java:**

	private static Map<XmPlayListControl.PlayMode, XmPlayListControl.PlayMode> sPlayMode = new HashMap<>();

    static {
        sPlayMode.put(PLAY_MODEL_LIST, PLAY_MODEL_LIST_LOOP);
        sPlayMode.put(PLAY_MODEL_LIST_LOOP, PLAY_MODEL_RANDOM);
        sPlayMode.put(PLAY_MODEL_RANDOM, PLAY_MODEL_SINGLE_LOOP);
        sPlayMode.put(PLAY_MODEL_SINGLE_LOOP, PLAY_MODEL_LIST);
    }
##2. 初始化播放模式为列表顺序

**TrackPlayerActivity.java:**

    private XmPlayListControl.PlayMode currentPlayMode = PLAY_MODEL_LIST;

##3. 播放模式按钮设置点击监听事件并切换播放模式和更新图标

**TrackPlayerActivity.java:**

	if (playMode != null) {
        playMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPlayMode();
            }
        });
    }
    
    private void switchPlayMode() {
        XmPlayListControl.PlayMode nextPlayMode = sPlayMode.get(currentPlayMode);
        if (playerPresenter != null) {
            playerPresenter.setPlayMode(nextPlayMode);
            currentPlayMode = nextPlayMode;
            updatePlayModeImage();
        }
    }
    
    private void updatePlayModeImage() {
        int resId = R.drawable.selector_player_mode_list;
        switch (currentPlayMode){
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.selector_player_mode_list_loop;
                break;
            case PLAY_MODEL_LIST:
                resId = R.drawable.selector_player_mode_list;
                break;
            case PLAY_MODEL_RANDOM:
                resId = R.drawable.selector_player_mode_random;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resId = R.drawable.selector_player_mode_loop_one;
        }
        playMode.setImageResource(resId);
    }