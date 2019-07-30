#033: 播放器列表窗口关闭按钮功能

#[首页](./../README.md)

##1. 获取关闭按钮控件

**PlayListPopupWindow.java:**

	private void initView() {
        closeView = playListView.findViewById(R.id.tv_playlist_close);
    }

##2. 设置点击监听事件并退出

**PlayListPopupWindow.java:**

	private void initEvent() {
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }