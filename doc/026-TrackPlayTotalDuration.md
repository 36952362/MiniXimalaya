#026: 播放器进度条设置

#[首页](./../README.md)

进度条的变化有两种情况，一种是随着播放器的播放自动更新播放进度条，另外一种情况是用户可以手动拖到进度条改变播放进度
##1. 自动更新播放进度条
播放器在播放的过程中通过回调方法onPlayProgress实时告诉当前的播放进度
 
 **TrackPlayerActivity.java:**
 
 	public void onPlayProgressChanged(long currentProgress, long total) {
        progressBar.setMax(total);
        if (!userTouched) {
            if (progressBar != null) {
                progressBar.setProgress(currProgress);
            }
        }
    }
    
##2. 用户手动拖动进度条
###2.1 给SeekBar设置一个进度条变化的监听事件

**TrackPlayerActivity.java:**

	private void initEvent() {
 		...
        if (progressBar != null) {
            progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int curProgress,  boolean isFromUser) {
                    if (isFromUser) {
                        currentProgress = curProgress;
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    userTouched = true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    userTouched = false;
                    playerPresenter.seekTo(currentProgress);
                }
            });
        }
    }
###2.2 通知播放器更新进度

**PlayerPresenter.java:**

	public void seekTo(int progress) {
        if (xmPlayerManager != null) {
            xmPlayerManager.seekTo(progress);
        }
    }    
 
##3. 效果图
![效果图](./pics/PlayerSeekBarSetting.png)