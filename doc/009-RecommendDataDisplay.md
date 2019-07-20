#推荐列表展示

##1. 推荐列表布局中创建一个RecyclerView控件
**fragement_recommend.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rv_recommend"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

	</androidx.constraintlayout.widget.ConstraintLayout>
	

##2. 创建一个RecyclerView的适配器
**RecommendAdapter.java:**

	public class RecommendAdapter  extends RecyclerView.Adapter<RecommendAdapter.InnerHolder> {
	
	    List<Album> albumList = new ArrayList<>();
	    @NonNull
	    @Override
	    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent, false);
	        return new InnerHolder(itemView);
	    }
	
	    @Override
	    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
	            holder.update(albumList.get(position));
	    }
	
	    @Override
	    public int getItemCount() {
	        return albumList.size();
	    }
	
	    public void updateData(List<Album> albumList) {
	        this.albumList.clear();
	        this.albumList.addAll(albumList);
	
	        notifyDataSetChanged();
	    }
	
	    public class InnerHolder extends RecyclerView.ViewHolder {
	
	        public InnerHolder(@NonNull View itemView) {
	            super(itemView);
	        }
	
	        public void update(Album album) {
	            ImageView albumCover = itemView.findViewById(R.id.iv_album_cover);
	            TextView albumTitle = itemView.findViewById(R.id.tv_album_title);
	            TextView albumDesc = itemView.findViewById(R.id.tv_album_desc);
	            TextView albumPlayCount = itemView.findViewById(R.id.tv_album_play_count);
	            TextView albumTrackCount = itemView.findViewById(R.id.tv_album_track_count);
	
	            Picasso.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCover);
	            albumTitle.setText(album.getAlbumTitle());
	            albumDesc.setText(album.getAlbumIntro());
	            albumPlayCount.setText(album.getPlayCount() + "");
	            albumTrackCount.setText(album.getFreeTrackCount() + "");
	
	        }
	    }
	}

**RecommendFragement.java:**

	public class RecommendFragment extends BaseFragement {
	
	    private static final  String TAG = "RecommendFragment";
	    private View rootView;
	    private RecyclerView recommendRecyclerView;
	    private RecommendAdapter recommendAdapter;
	
	    @Override
	    protected View onSubViewLoaded() {
	        rootView = onLoadLayout(R.layout.fragement_recommend);
	        recommendRecyclerView = rootView.findViewById(R.id.rv_recommend);
	
	        //设置布局管理器
	        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
	        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
	        recommendRecyclerView.setLayoutManager(linearLayoutManager);
	
	        //设置适配器
	        recommendAdapter = new RecommendAdapter();
	        recommendRecyclerView.setAdapter(recommendAdapter);
	
	        getRecommendData();
	        return rootView;
	    }
	
	    private void getRecommendData() {
	        Map<String, String> map = new HashMap<String, String>();
	        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT + "");
	        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
	            @Override
	            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
	                LogUtil.i(TAG, "getGuessLikeAlbum success");
	                if(null != gussLikeAlbumList){
	                    recommendAdapter.updateData(gussLikeAlbumList.getAlbumList());
	                }
	            }
	
	            @Override
	            public void onError(int i, String s) {
	                LogUtil.e(TAG, "getGuessLikeAlbum failed");
	                LogUtil.e(TAG, "errorCode: " + i + ", reason:" + s);
	            }
	        });
	    }
	}

##3. 为适配器创建一个展示数据的布局文件
**item_recommend.xml:**
	
	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:orientation="vertical"
	    android:layout_width="match_parent"
	    android:layout_height="75dp">
	
	
	    <ImageView
	        android:id="@+id/iv_album_cover"
	        android:layout_width="68dp"
	        android:layout_height="65dp"
	        android:layout_marginStart="5dp"
	        android:layout_marginTop="5dp"
	        android:contentDescription="@string/albumImageView"
	        app:layout_constraintStart_toStartOf="parent"
	        app:layout_constraintTop_toTopOf="parent" />
	
	    <TextView
	        android:id="@+id/tv_album_title"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:ellipsize="end"
	        android:lines="1"
	        android:textSize="18sp"
	        app:layout_constraintStart_toEndOf="@id/iv_album_cover"
	        app:layout_constraintTop_toTopOf="parent"
	        app:layout_constraintEnd_toEndOf="parent"
	        android:layout_marginStart="84dp"
	        android:layout_marginBottom="3dp"
	        android:text="@string/alum_title"/>
	
	    <TextView
	        android:id="@+id/tv_album_subtitle"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:layout_marginTop="3dp"
	        android:lines="1"
	        android:ellipsize="end"
	        android:textSize="12sp"
	        android:layout_marginStart="84dp"
	        app:layout_constraintStart_toEndOf="@id/iv_album_cover"
	        app:layout_constraintTop_toBottomOf="@id/tv_album_title"
	        app:layout_constraintEnd_toEndOf="parent"
	        android:text="@string/albumSubTitle"/>
	
	    <ImageView
	        android:id="@+id/iv_album_play"
	        android:layout_width="4dp"
	        android:layout_height="5dp"
	        android:layout_marginStart="10dp"
	        android:layout_marginTop="13dp"
	        android:src="@mipmap/play_info_icon"
	        android:contentDescription="@string/album_play"
	        app:layout_constraintStart_toEndOf="@id/iv_album_cover"
	        app:layout_constraintTop_toBottomOf="@id/tv_album_subtitle"
	        tools:srcCompat="@tools:sample/avatars" />
	
	    <TextView
	        android:id="@+id/tv_album_play_count"
	        android:layout_width="45dp"
	        android:layout_height="10dp"
	        android:textSize="7sp"
	        android:textColor="#848484"
	        android:layout_marginStart="4dp"
	        android:layout_marginTop="11dp"
	        app:layout_constraintTop_toBottomOf="@id/tv_album_subtitle"
	        app:layout_constraintStart_toEndOf="@id/iv_album_play"
	        android:text="@string/album_play" />
	
	    <ImageView
	        android:id="@+id/iv_album_listen"
	        android:layout_width="6dp"
	        android:layout_height="6dp"
	        android:src="@mipmap/ic_sound"
	        android:layout_marginStart="29dp"
	        android:layout_marginTop="13dp"
	        android:contentDescription="@string/album_listen"
	        app:layout_constraintTop_toBottomOf="@id/tv_album_subtitle"
	        app:layout_constraintStart_toEndOf="@id/tv_album_play_count"
	        tools:srcCompat="@tools:sample/avatars" />
	
	    <TextView
	        android:id="@+id/tv_album_listen_count"
	        android:layout_width="45dp"
	        android:layout_height="10dp"
	        app:layout_constraintTop_toBottomOf="@id/tv_album_subtitle"
	        app:layout_constraintStart_toEndOf="@id/iv_album_listen"
	        android:textSize="7sp"
	        android:textColor="#848484"
	        android:layout_marginStart="2dp"
	        android:layout_marginTop="11dp"
	        android:text="@string/album_listen" />
	
	</androidx.constraintlayout.widget.ConstraintLayout>

##4. 效果图
