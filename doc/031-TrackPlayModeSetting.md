#031: 播放器播放模式设置

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
    
##4. 播放模式持久化
用户设置的播放模式要持久化，这样用户切换到其他的页面或退出重启后就可以继续使用最后一次设置的播放模式. 这里使用SharePreference方式存储用户设置的播放模式。

**playerPresenter.java:**

	private XmPlayListControl.PlayMode currentPlayMode = PLAY_MODEL_LIST;

    private static String PLAY_MODE_NAME = "PlayMode";
    private static String PLAY_MODE_KEY = "CurrentPlayMode";
    private final SharedPreferences sharedPreferences;

    private final int PLAY_MODE_LIST_INT = 0;
    private final int PLAY_MODE_LIST_LOOP_INT = 1;
    private final int PLAY_MODE_RANDOM_INT = 2;
    private final int PLAY_MODE_SINGLE_LOOP_INT = 3;

	private PlayerPresenter(){
        xmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        xmPlayerManager.addAdsStatusListener(this);
        xmPlayerManager.addPlayerStatusListener(this);
        xmPlayerManager.setPlayMode(currentPlayMode);
        sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(PLAY_MODE_NAME, Context.MODE_PRIVATE);
    }
 
在模式切换时存储切换后的模式

	public void setPlayMode(XmPlayListControl.PlayMode playMode) {
	    if (xmPlayerManager != null) {
	        currentPlayMode = playMode;
	        xmPlayerManager.setPlayMode(playMode);
	
	        for (IPlayerCallback playerCallback : playerCallbacks) {
	            playerCallback.onPlayModeChanged(playMode);
	        }
	
	        SharedPreferences.Editor edit = sharedPreferences.edit();
	        edit.putInt(PLAY_MODE_KEY, getIntFromMode(playMode));
	        edit.commit();
	    }
	}
	
在播放器页面注册时，获取存储的播放模式

	public void registerCallback(IPlayerCallback playerCallback) {
        playerCallback.onPlayTitle(trackTitle);

        int playModeInt = sharedPreferences.getInt(PLAY_MODE_KEY, PLAY_MODE_LIST_INT);

        XmPlayListControl.PlayMode playMode = getModeByInt(playModeInt);
        playerCallback.onPlayModeChanged(playMode);

        if (!playerCallbacks.contains(playerCallback)) {
            playerCallbacks.add(playerCallback);
        }
    }