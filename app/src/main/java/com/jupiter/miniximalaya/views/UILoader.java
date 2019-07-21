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
