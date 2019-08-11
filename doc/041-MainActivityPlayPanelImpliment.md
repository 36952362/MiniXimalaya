#041: 主界面播放面板实现

#[首页](./../README.md)

在主界面添加播放面板的控制，可以实现播放，暂停功能，并显示播放专辑的封面，播放节目的标题和作者信息。

##1. 修改主界面的布局文件

**activity_main.xml:**

	<com.jupiter.miniximalaya.views.RoundRectImageView
        android:id="@+id/iv_track_icon"
        android:layout_width="40dp"
        android:layout_height="40dp"
        tools:src="@tools:sample/avatars"
        android:layout_marginStart="5dp"
        android:layout_marginTop="5dp"
        android:layout_marginBottom="5dp"
        android:src="@mipmap/logo"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintBottom_toBottomOf="parent" />
    <TextView
        android:id="@+id/tv_main_play_title"
        android:text="播放节目标题"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="55dp"
        android:layout_marginEnd="55dp"
        android:layout_marginTop="3dp"
        android:textSize="18sp"
        android:singleLine="true"
        android:ellipsize="marquee"
        android:marqueeRepeatLimit="marquee_forever"
        app:layout_constraintTop_toBottomOf="@id/main_viewpager"
        app:layout_constraintStart_toEndOf="@id/iv_track_icon" />

    <TextView
        android:id="@+id/tv_album_author"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="作者"
        android:layout_marginStart="10dp"
        android:layout_marginTop="3dp"
        android:layout_marginBottom="3dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintTop_toBottomOf="@id/tv_main_play_title"
        app:layout_constraintStart_toEndOf="@id/iv_track_icon" />

    <ImageView
        android:id="@+id/iv_main_play_icon"
        tools:src="@tools:sample/avatars"
        android:layout_marginEnd="10dp"
        android:layout_marginBottom="5dp"
        android:src="@drawable/selector_player_play"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_width="40dp"
        android:layout_height="40dp" />

##2. 注册播放模块并实现回调方法

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	...
		playerPresenter = PlayerPresenter.getsInstance();
    	playerPresenter.registerCallback(this);
    }
    
    @Override
    protected void onDestroy() {
        //销毁时取消注册，避免内存泄露
        if (playerPresenter != null) {
            playerPresenter.unRegisterCallback(this);
        }
        super.onDestroy();
    }

##3. 实现播放模块回调方法并更新界面

	@Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel, int currentIndex) {
        if (curModel != null && curModel instanceof Track) {
            Track track = (Track)curModel;
            String trackTitle = track.getTrackTitle();
            String nickname = track.getAnnouncer().getNickname();
            String coverUrlMiddle = track.getCoverUrlMiddle();

            if (mainTrackIcon != null) {
                Picasso.with(this).load(track.getCoverUrlMiddle()).into(mainTrackIcon);
            }

            if (mainPlayTitle != null) {
                mainPlayTitle.setText(track.getTrackTitle());
            }

            if (mainAlbumAuthor != null) {
                mainAlbumAuthor.setText(track.getAnnouncer().getNickname());
            }
        }
    }

##4. 播放按钮的点击事件实现播放和暂停功能

	private void initEvent() {

        if (mainPlayControl != null) {
            mainPlayControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (playerPresenter != null) {
                        if (playerPresenter.isPlaying()) {
                            playerPresenter.pause();
                        }
                        else{
                            playerPresenter.play();
                        }
                    }
                }
            });
        }
    }
##5. 实现播放状态的回调并更新播放按钮状态

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
        if (mainPlayControl != null ) {
            if(isPlaying){
                mainPlayControl.setImageResource(R.drawable.selector_player_pause);
            }
            else{
                mainPlayControl.setImageResource(R.drawable.selector_player_play);
            }
        }
    }
    
    
##6. 效果图
![效果图](./pics/MainActivityPlayPanelImpliment.gif)