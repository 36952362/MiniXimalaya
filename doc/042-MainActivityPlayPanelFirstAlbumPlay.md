#042: 主界面播放面板首次播放

#[首页](./../README.md)

在主界面刚启动时，由于没有给喜马拉雅播放器模块设置播放列表，所以在主界面播放面板第一次点击播放按钮时是没有效果的。解决的方式是如果还没有设置播放列表，则立即获取第一个专辑列表并设置到喜马拉雅播放器模块中。

##1. 修改主界面播放面板播放按钮点击事件
如果喜马拉雅播放器模块还没有设置播放列表，则播放第一个专辑第一个节目

**MainActivity.java:**

	mainPlayControl.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (playerPresenter != null) {
                if (playerPresenter.hasSetPlayList()) {
                    if (playerPresenter.isPlaying()) {
                        playerPresenter.pause();
                    }
                    else{
                        playerPresenter.play();
                    }
                }
                else{
                    playFirstAlbum();
                }
            }
        }
    });
    
    private void playFirstAlbum() {
        RecommendPresenter recommendPresenter = RecommendPresenter.getsInstance();
        List<Album> currentRecoomandList = recommendPresenter.getCurrentRecommendList();
        if (currentRecoomandList != null && currentRecoomandList.size() > 0) {
            Album album = currentRecoomandList.get(0);
            playerPresenter.playByAlbumId(album.getId());
        }
    }
##2. 通过专辑Id获取专辑列表并设置到喜马拉雅播放模块

	public void playByAlbumId(long albumId) {
        XimalayaApi ximalayaApi = XimalayaApi.getsInstance();
        ximalayaApi.getAlbumById((int)albumId, 1, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                setPlayList(trackList.getTracks(), 0);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(BaseApplication.getAppContext(), "请求数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
##3. 点击播放器面板跳转到播放器界面

	if (playPanelContain != null) {
            playPanelContain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (playerPresenter != null && playerPresenter.hasSetPlayList()) {
                        Intent intent = new Intent(MainActivity.this, TrackPlayerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
        
##4. 效果图
![效果图](./pics/MainActivityPlayPanelFirstAlbumPlay.gif)