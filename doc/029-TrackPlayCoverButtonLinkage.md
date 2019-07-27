#029: 播放器封面和按钮联动

#[首页](./../README.md)

当切换封面时，要切换节目，同时按钮切换节目时也要更新播放器封面

##1. 切换封面时切换节目
在播放器封面控件添加一个页面切换监听事件，在收到onPageSelected回调时调用喜玛拉雅SDK切换到指定的节目并播放

**TrackPlayerActivity.java:**

	if (playCoverViewPager != null) {
        playCoverViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (playerPresenter != null) {
                    playerPresenter.play(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
     		});
   	}
##2. 按钮切换节目时更新播放器封面         
在按钮切换节目时，会收到onSoundSwitch回调方法，在此方法中更新播放器页面. 

**TrackPlayerActivity.java:**

	public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel, int currentIndex) {
        ...
        
        if (playCoverViewPager != null && userTouchedViewPager) {
            playCoverViewPager.setCurrentItem(currentIndex, true);
        }

        userTouchedViewPager = false;

    }
    
为了避免死循环(切换播放器页面 -> 切换歌曲 -> onSoundSwitch -> 切换播放器页面), 只有手动切换播放器页面时才更新页面封面. 所以播放器页面添加一个触摸监听事件

	playCoverViewPager.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if(MotionEvent.ACTION_DOWN == action)
                userTouchedViewPager = true;
            return false;
        }
    });

