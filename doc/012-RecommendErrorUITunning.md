#推荐错误页面优化

##1. 优化错误页面的布局文件
在出现错误时，显示错误页面，并提示用户重试


**fragment_error_view.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ImageView
        android:id="@+id/iv_recommend_error"
        android:layout_width="75dp"
        android:layout_height="75dp"
        app:layout_constraintBottom_toTopOf="@id/tx_recommend_error"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.498"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_chainStyle="packed"
        android:contentDescription="@string/recommend_error"
        android:src="@mipmap/network_error" />

    <TextView
        android:id="@+id/tx_recommend_error"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintTop_toBottomOf="@id/iv_recommend_error"
        android:text="@string/recommend_error" />
    </androidx.constraintlayout.widget.ConstraintLayout>


##1. 设置点击事件
用户点击错误页面时重新加载推荐数据

**UILoader.java:**

	private View getErrorView() {
        View errorView = LayoutInflater.from(getContext()).inflate(R.layout.fragement_error_view, this, false);
        errorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != retryClickListener){
                    retryClickListener.onRetry();
                }
            }
        });
        return errorView;
	}
	
**RecommendFragment.java:**

注册监听事件

	uiLoader.setRetryClickListener(this);

回调函数重新加载

	@Override
    public void onRetry() {
        if(null != recommendPresenter){
            recommendPresenter.getRecommendList();
        }
    }

##2. 最终效果图
![错误页面](./pics/RecommendErrorTunningUI.png)
