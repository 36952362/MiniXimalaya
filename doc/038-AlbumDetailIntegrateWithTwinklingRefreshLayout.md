#038: 绑定开源项目TwinklingRefreshLayout

#[首页](./../README.md)

使用开源框架[TwinklingRefreshLayout](https://github.com/lcodecorex/TwinklingRefreshLayout)去实现播放列表中的刷新和加载更多功能

##1. 添加gradle依赖

**build.gradle(Module):**

	implementation 'com.lcodecorex:tkrefreshlayout:1.0.7'
	
##2. 在布局文件中添加TwinklingRefreshLayout

**item_album_detail_list.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

	    <com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
	        android:id="@+id/albumDetailRefreshLayout"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent">
	
	        <androidx.recyclerview.widget.RecyclerView
	            android:overScrollMode="never"
	            android:id="@+id/rv_album_detail_info"
	            android:layout_width="match_parent"
	            android:layout_height="match_parent"/>
	
	    </com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout>
	</FrameLayout>
##3. 获取TwinklingRefreshLayout控件并添加刷新和加载更多监听事件

**AlbumDetailActivity.java:**

	private View createSuccessView(ViewGroup container) {
		...
		View successView = LayoutInflater.from(this).inflate(R.layout.item_album_detail_list, container, false);
		albumDetailRefreshLayout = successView.findViewById(R.id.albumDetailRefreshLayout);

        albumDetailRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                BaseApplication.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        albumDetailRefreshLayout.finishRefreshing();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                BaseApplication.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        albumDetailRefreshLayout.finishLoadmore();
                    }
                }, 2000);
            }
        });
	}
##4. 定制化刷新层和加载更多层显示
	private View createSuccessView(ViewGroup container) {
		...
		BezierLayout headView = new BezierLayout(this);
        albumDetailRefreshLayout.setHeaderView(headView);
        albumDetailRefreshLayout.setMaxHeadHeight(140);
	}

##5. 效果图
![效果图](./pics/TwinklingRefreshLayoutIntegration.gif)