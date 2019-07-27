#011: 推荐页面异常处理

#[首页](./../README.md)

在获取推荐数据时，除了能获取到数据时，也有可能出现几种异常情况，比如网络出错时显示错误信息并让用户重试，网络很慢时要显示一个加载状态，没有获取到数据时要给用户一个提示信息等。

##1. 为不同状态创建不同的布局文件
**fragment_loading_view.xml:**
	
	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent">
	
    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="正在加载"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
	</androidx.constraintlayout.widget.ConstraintLayout>

**fragment_error_view.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="加载出错"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
	</androidx.constraintlayout.widget.ConstraintLayout>
**fragment_empty_view.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="数据为空"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
	</androidx.constraintlayout.widget.ConstraintLayout>

##1. 定义UILoader类
由于在同一个推荐页面要显示不同的状态，定义一个统一类来根据不同的状态来显示不同的状态信息。
**UILoader.java:**

	package com.jupiter.miniximalaya.views;
	
	import android.content.Context;
	import android.util.AttributeSet;
	import android.view.LayoutInflater;
	import android.view.View;
	import android.widget.FrameLayout;
	
	import com.jupiter.miniximalaya.R;
	import com.jupiter.miniximalaya.base.BaseApplication;
	
	import lombok.Data;
	
	@Data
	public abstract class UILoader extends FrameLayout {

	    public UILoader(Context context) {
	        super(context, null);
	    }
	
	    public UILoader(Context context, AttributeSet attrs) {
	        super(context, attrs, 0);
	    }
	
	    public UILoader(Context context, AttributeSet attrs, int defStyleAttr) {
	        super(context, attrs, defStyleAttr, 0);
	    }
	
	    public enum UIStatus{
	        NONE, LOADING, ERROR, EMPTY, SUCCESS
	    }
	
	    private UIStatus uiStatus = UIStatus.NONE;
	    private View loadingView;
	    private View errorView;
	    private View emptyView;
	    private View successView;
	
	    public void updateUIStatus(UIStatus uiStatus){
	        this.uiStatus = uiStatus;
	        //更新UI必须在UI线程中执行
	        BaseApplication.getHandler().post(new Runnable() {
	            @Override
	            public void run() {
	                switchUIByCurrentStatus();
	            }
	        });
	    }
	
	
	    private void switchUIByCurrentStatus() {
	
	        if(loadingView == null){
	            loadingView = getLoadingView();
	            addView(loadingView);
	        }
	        loadingView.setVisibility(uiStatus == UIStatus.LOADING?View.VISIBLE: View.GONE);
	
	        if(errorView == null){
	            errorView = getErrorView();
	            addView(errorView);
	        }
	        errorView.setVisibility(uiStatus == UIStatus.ERROR?View.VISIBLE: View.GONE);
	
	        if(emptyView == null){
	            emptyView = getEmptyView();
	            addView(emptyView);
	        }
	        emptyView.setVisibility(uiStatus == UIStatus.EMPTY?View.VISIBLE: View.GONE);
	
	        if(successView == null){
	            successView = getSuccessView();
	            addView(successView);
	        }
	        successView.setVisibility(uiStatus == UIStatus.SUCCESS?View.VISIBLE: View.GONE);
	    }
	
	    protected abstract View getSuccessView();
	
	    private View getEmptyView() {
	        return LayoutInflater.from(getContext()).inflate(R.layout.fragement_empty_view, this, false);
	    }
	
	    private View getErrorView() {
	        return LayoutInflater.from(getContext()).inflate(R.layout.fragement_error_view, this, false);
	    }
	
	    private View getLoadingView() {
	        return LayoutInflater.from(getContext()).inflate(R.layout.fragement_loading_view, this, false);
	    }
	}

这里需要注意的是，由于页面状态的更新只能在主线程(UI线程)，在其他工作线程获取到数据后通过使用Handler的方式让主线程去更新。

	public void updateUIStatus(UIStatus uiStatus){
        this.uiStatus = uiStatus;
        //更新UI必须在UI线程中执行
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }
 
 在推荐页面渲染时通过喜马拉雅SDK获取数据，并通过返回不同的状态渲染不同的页面。
 
 **RecommendFragment.java:**
 
 	@Override
    public void onRecommendListLoaded(List<Album> result) {
        uiLoader.updateUIStatus(UILoader.UIStatus.SUCCESS);
        recommendAdapter.updateData(result);
    }

    @Override
    public void onError(int errorCode, String desc) {
        uiLoader.updateUIStatus(UILoader.UIStatus.ERROR);
    }

    @Override
    public void onEmpty() {
        uiLoader.updateUIStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        uiLoader.updateUIStatus(UILoader.UIStatus.LOADING);
    }

##2. 最终效果图
![加载页面](./pics/RecommendLoadingUI.png)
![错误页面](./pics/RecommendErrorUI.png)
![空数据页面](./pics/RecommendEmptyUI.png)
