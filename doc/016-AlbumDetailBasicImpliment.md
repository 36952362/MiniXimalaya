#专辑页面基础实现

##1. 专辑页面基础信息布局文件
**activity_album_detail.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:background="#ff0000"
	    tools:context=".AlbumDetailActivity">
	
	    <ImageView
	        android:id="@+id/iv_album_large_cover"
	        android:layout_width="match_parent"
	        android:scaleType="fitXY"
	        android:layout_height="150dp"
	        app:layout_constraintLeft_toLeftOf="parent"
	        app:layout_constraintRight_toRightOf="parent"
	        app:layout_constraintTop_toTopOf="parent"
	        tools:src="@tools:sample/avatars" />
	
	    <com.jupiter.miniximalaya.views.RoundRectImageView
	        android:id="@+id/iv_album_small_cover"
	        android:layout_width="75dp"
	        android:layout_height="75dp"
	        android:scaleType="fitXY"
	        app:layout_constraintTop_toTopOf="parent"
	        app:layout_constraintLeft_toLeftOf="parent"
	        android:layout_marginTop="110dp"
	        android:layout_marginStart="10dp"
	        tools:src="@tools:sample/avatars" />
	
	    <TextView
	        android:id="@+id/tv_album_item_title"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:text="这是标题"
	        android:textSize="18sp"
	        android:textColor="#ffffff"
	        android:layout_marginTop="125dp"
	        android:layout_marginLeft="20dp"
	        app:layout_constraintTop_toTopOf="parent"
	        app:layout_constraintStart_toEndOf="@id/iv_album_small_cover" />
	
	    <TextView
	        android:id="@+id/tv_album_item_author"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:text="这是作者"
	        android:textColor="#ffffff"
	        android:textSize="12sp"
	        android:layout_marginLeft="20dp"
	        android:layout_marginTop="5dp"
	        app:layout_constraintTop_toBottomOf="@id/tv_album_item_title"
	        app:layout_constraintStart_toEndOf="@id/iv_album_small_cover" />
	
	</androidx.constraintlayout.widget.ConstraintLayout>
##2. 设计专辑获取数据类和回调方法注册
**AlbumDetailPresenter.java**

	public class AlbumDetailPresenter implements IAlbumDetailPresenter {
	
	    private List<IAlbumDetailCallback> albumDetailCallbackList = new ArrayList<>();
	    private Album album = null;
	
	    private  AlbumDetailPresenter(){}
	
	    private static  AlbumDetailPresenter sInstance = null;
	
	    public static AlbumDetailPresenter getsInstance(){
	        if(null == sInstance){
	            synchronized (AlbumDetailPresenter.class){
	                if(null == sInstance){
	                    sInstance = new AlbumDetailPresenter();
	                }
	            }
	        }
	        return  sInstance;
	    }
	
	    @Override
	    public void pull2RefreshMore() {
	
	    }
	
	    @Override
	    public void loadMore() {
	
	    }
	
	    @Override
	    public void getAlbumDetail(int albumId, int pager) {
	
	    }
	
	    @Override
	    public void registerCallback(IAlbumDetailCallback albumDetailCallback){
	        if(!albumDetailCallbackList.contains(albumDetailCallback)){
	            albumDetailCallbackList.add(albumDetailCallback);
	            albumDetailCallback.onAlbumDataLoaded(album);
	        }
	    }
	
	    @Override
	    public void unRegisterCallback(IAlbumDetailCallback albumDetailCallback){
	        if(albumDetailCallbackList.contains(albumDetailCallback)){
	            albumDetailCallbackList.remove(albumDetailCallback);
	        }
	    }
	
	    public void setAlbum(Album album) {
	        this.album = album;
	    }
	}

**AlbumDetailActivity.java:**

	protected void onCreate(Bundle savedInstanceState) {
		albumDetailPresenter = AlbumDetailPresenter.getsInstance();
		albumDetailPresenter.registerCallback(this);
	}
	
##3. 回调方法中更新专辑页面

**AlbumDetailActivity.java:**
专辑页面基本信息中包含专辑封面大图，小图，专辑名和作者信息

	public void onAlbumDataLoaded(Album album) {
	
        if(null != album){
            Picasso.with(this).load(album.getCoverUrlLarge()).into(albumLargeCover);
            Picasso.with(this).load(album.getCoverUrlSmall()).into(albumSmallCover);
            albumTitle.setText(album.getAlbumTitle());
            albumAuthor.setText(album.getAnnouncer().getNickname());
        }
	}

##4. 修改专辑页面不包括状态栏和透明
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ...
	}
##5. 效果图
![详细页面](./pics/AlbumDetailBasicInfo.png)
