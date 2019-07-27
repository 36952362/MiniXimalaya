#025: 播放器播放时间和总时间显示

#[首页](./../README.md)

##1. 获取播放进度
播放器在播放的过程中通过回调方法onPlayProgress实时告诉当前的播放进度

**PlayerPresenter.java:**

	public void onPlayProgress(int currentProgress, int total) {
        //unit: ms
        for (IPlayerCallback playerCallback : playerCallbacks) {
            playerCallback.onPlayProgressChanged(currentProgress, total);
        }

    }
##2. 更新播放进度和总时间页面
 
 **TrackPlayerActivity.java:**
 
 	public void onPlayProgressChanged(long currentProgress, long total) {
        String totalDuration;
        String escapedTime;

        if (total >= 60 * 60 * 100) {
            totalDuration = hourFormat.format(total);
            escapedTime = hourFormat.format(currentProgress);
        }
        else{
            totalDuration = minFormat.format(total);
            escapedTime = hourFormat.format(currentProgress);
        }

        if (totalTimeTextView != null) {
            totalTimeTextView.setText(totalDuration);
        }

        if (escapedTimeTextView != null) {

            escapedTimeTextView.setText(escapedTime);
        }
    }