#043: 主界面上下拉回弹效果

#[首页](./../README.md)

在主界面中推荐列表中不支持刷新和加载更多列表的功能，为了给用户更好的体验，最好加一个上下拉回弹效果

##1. 修改推荐列表布局文件
使用第三方的布局控件包裹推荐列表的控件

**fragment_recommend.xml:**

	<com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
        android:id="@+id/over_scroll_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.recyclerview.widget.RecyclerView
            android:background="#fff4f4f4"
            android:id="@+id/rv_recommend"
            android:overScrollMode="never"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout>
       
##2. 获取第三方控件并设置回弹效果

**RecommendFragment.java:**

	TwinklingRefreshLayout refreshLayout = successView.findViewById(R.id.over_scroll_view);
    refreshLayout.setPureScrollModeOn();

##4. 效果图
![效果图](./pics/MainActivityPureScrollMode.gif)