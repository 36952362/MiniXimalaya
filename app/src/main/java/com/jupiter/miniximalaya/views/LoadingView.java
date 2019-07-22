package com.jupiter.miniximalaya.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class LoadingView extends ImageView {

    private int rotateDegree = 0;

    private boolean needRotate = false;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0 );
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 第一个参数是旋转的角度
         * 第二，三个参数是旋转的X,Y坐标
         */
        canvas.rotate(rotateDegree, getWidth()/2, getHeight()/2);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //绑定到Window时
        needRotate = true;
        post(new Runnable() {
            @Override
            public void run() {
                rotateDegree += 30;
                rotateDegree = rotateDegree <=360 ? rotateDegree : 0;
                invalidate();
                if(needRotate) {
                    postDelayed(this, 100);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //解绑窗口时
        needRotate = false;
    }
}
