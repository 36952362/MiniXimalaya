#喜马拉雅播放器集成

##1. 依赖包导入
将SDK包中的两个so包(libxmediaplayer.so, libxmediaplayer_x.so)拷贝到您的项目libs/armeabi目录下

##2. SDK导入
TingPhoneOpenSDK_XXX.jar拷贝到libs目录下

##3.声明权限
在项目Manifest文件中声明以下权限（监听电话状态，当有来电或去电时，自动暂停，当电话结束后，自动恢复播放）

	<uses-permission android:name="android.permission.READ_PHONE_STATE" />
	<uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
	
##4.注册播放器服务
在App的AndroidManifest.xml文件中注册播放器服务

	<application
        ...
        <service android:name="com.ximalaya.ting.android.opensdk.player.service.XmPlayerService"
        android:process=":player"/>
    </application>
##5. 初始化播放器
**BaseApplication.java:**

	XmPlayerManager.getInstance(this).init();

##6. 播放器页面创建逻辑处理类
创建一个播放逻辑处理类和喜马拉雅播放器进行交互
**PlayerPresenter.java**

	public class PlayerPresenter implements IPlayerPresenter {

	    private final String TAG = "PlayerPresenter";
	
	    private static  PlayerPresenter sInstance = null;
	
	    private XmPlayerManager xmPlayerManager;
	
	    private boolean isPlayListSet = false;
	
	    private PlayerPresenter(){
	        xmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
	    }
	
	    public static  PlayerPresenter getsInstance() {
	        if(null == sInstance){
	            synchronized (PlayerPresenter.class){}
	            if(null == sInstance){
	                sInstance = new PlayerPresenter();
	            }
	        }
	        return sInstance;
	    }
	
	
	    public void setPlayList(List<Track> tracks, int currentIndex){
	
	        if (xmPlayerManager != null) {
	            xmPlayerManager.setPlayList(tracks,currentIndex);
	            isPlayListSet = true;
	        }else {
	            LogUtil.e(TAG, "xmPlayerManager is null");
	        }
	    }
	
	    @Override
	    public void play() {
	
	        if(isPlayListSet){
	            xmPlayerManager.play();
	        }
	    }
	
	    @Override
	    public void pause() {
	
	    }
	
	    @Override
	    public void stop() {
	
	    }
	
	    @Override
	    public void playPre() {
	
	    }
	
	    @Override
	    public void playNext() {
	
	    }
	
	    @Override
	    public void setPlayMode(XmPlayListControl.PlayMode playMode) {
	
	    }
	
	    @Override
	    public void getPlayList() {
	
	    }
	
	    @Override
	    public void playByIndex(int index) {
	
	    }
	
	    @Override
	    public void seekTo(int progress) {
	
	    }
	
	    @Override
	    public void registerCallback(IPlayerCallback iPlayerCallback) {
	
	    }
	
	    @Override
	    public void unRegisterCallback(IPlayerCallback iPlayerCallback) {
	
	    }
	}
    
##7. 专辑页面点击时传递专辑列表到播放器
**AlbumDetailActivity.java:**

	@Override
    public void onItemClick(List<Track> tracks, int position) {
        PlayerPresenter.getsInstance().setPlayList(tracks, position);
        Intent intent = new Intent(this, TrackPlayerActivity.class);
        startActivity(intent);
    }