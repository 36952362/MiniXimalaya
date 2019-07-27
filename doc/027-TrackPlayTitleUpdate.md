#027: 播放器标题设置

#[首页](./../README.md)

播放器页面要显示节目标题，同时在节目切换的时候要更新节目标题
##1. 从专辑页面跳转后要更新节目标题
###1.1 专辑跳转时传递节目列表和索引
**AlbumDetailActivity.java:**

	public void onItemClick(List<Track> tracks, int position) {
	    PlayerPresenter.getsInstance().setPlayList(tracks, position);
        Intent intent = new Intent(this, TrackPlayerActivity.class);
        startActivity(intent);
    }
 
 **PlayPresenter.java:**
 
 	public void setPlayList(List<Track> tracks, int currentIndex){
        if (xmPlayerManager != null) {
            xmPlayerManager.setPlayList(tracks,currentIndex);
            isPlayListSet = true;
            Track track = tracks.get(currentIndex);
            trackTitle = track.getTrackTitle();
        }else {
            LogUtil.e(TAG, "xmPlayerManager is null");
        }
    }
###1.2 当播放器页面注册到业务类时回传节目标题

**PlayPresenter.java:**

	public void registerCallback(IPlayerCallback playerCallback) {
        playerCallback.onPlayTitle(trackTitle);
        if (!playerCallbacks.contains(playerCallback)) {
            playerCallbacks.add(playerCallback);
        }

    }
 **TrackPlayerActivity.java:**
 
 	public void onPlayTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }
 
###1.3 布局文件初始化时更新节目标题

**TrackPlayerActivity.java:**

	tvPlayTitle = findViewById(R.id.tv_track_title);
    if (!TextUtils.isEmpty(trackTitle)) {
        tvPlayTitle.setText(trackTitle);
    }

##2. 节目切换时更新节目标题
节目切换时，喜马拉雅播放器回调onSoundSwitch

**PlayPresenter.java:**

	public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        for (IPlayerCallback playerCallback : playerCallbacks) {
            playerCallback.onSoundSwitch(lastModel, curModel);
        }
    }
    
 **TrackPlayerActivity.java:**
 
  	public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        if (curModel != null && curModel instanceof Track) {
            Track track = (Track)curModel;
            if (tvPlayTitle != null) {
                tvPlayTitle.setText(track.getTrackTitle());
            }
        }
    }

##3. 效果图
![效果图](./pics/PlayerTrackTitle.png)