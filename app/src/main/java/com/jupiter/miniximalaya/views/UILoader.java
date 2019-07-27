package com.jupiter.miniximalaya.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.base.BaseApplication;

import lombok.Data;

@Data
public abstract class UILoader extends FrameLayout {

    private OnRetryClickListener retryClickListener = null;

    public enum UIStatus{
        NONE, LOADING, ERROR, EMPTY, SUCCESS
    }

    private UIStatus uiStatus = UIStatus.NONE;
    private View loadingView = null;
    private View errorView = null ;
    private View emptyView = null ;
    private View successView = null ;

    public UILoader(Context context) {
        this(context, null);
    }

    public UILoader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UILoader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
        init();
    }

    private void init() {
        switchUIByCurrentStatus();
    }

    public void updateUIStatus(final UIStatus uiStatus){
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
            successView = getSuccessView(this);
            addView(successView);
        }
        successView.setVisibility(uiStatus == UIStatus.SUCCESS?View.VISIBLE: View.GONE);

    }

    protected abstract View getSuccessView(ViewGroup container);

    private View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view, this, false);
    }

    private View getErrorView() {
        View errorView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_error_view, this, false);
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

    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view, this, false);
    }

    public interface  OnRetryClickListener{
        void onRetry();
    }
}
