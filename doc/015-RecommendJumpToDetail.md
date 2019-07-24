#推荐页面跳转到具体内容
点击推荐页面中的某一项，跳转到具体内容的页面

##1. 对每一项设置一个点击监听事件

**RecommendAdapater.java:**

	@Override
    public void onBindViewHolder(@NonNull final InnerHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != onRecommendItemClickListener){
                    int clickPosition = (int)view.getTag();
                    Album album = albumList.get(clickPosition);
                    onRecommendItemClickListener.onItemClick(clickPosition, album);
                }
            }
        });
        holder.update(albumList.get(position));
    }

##2. 注册回调方法

**RecommendFragment.java**

	private View createSuccessView() {
		...
        //设置适配器
        recommendAdapter = new RecommendAdapter();
        recommendAdapter.setOnRecommendItemClickListener(this);
		...
    }
##3. 创建具体内容Activity和布局文件
###3.1 设计具体内容布局文件
**activity_album_detail.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".AlbumDetailActivity">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="条目详细信息"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
	</androidx.constraintlayout.widget.ConstraintLayout>

###3.2 创新具体内容Activity
**AlbumDetailActivity.java:**

	public class AlbumDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
    }
	}

##4. 收到点击事件后调整到具体内容页面
**RecommendFragment.java:**

	@Override
    public void onItemClick(int position, Album album) {
        Intent intent = new Intent(getContext(), AlbumDetailActivity.class);
        startActivity(intent);
    }
##5. 效果图
![详细页面](./pics/RecommendDetailUI.png)
