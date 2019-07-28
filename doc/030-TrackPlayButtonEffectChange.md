#030: 播放器按钮效果变化

#[首页](./../README.md)

为了给用户更好的使用体验，在用户按压按钮时切换不同的按钮效果，及时给用户一个反馈，避免用户多次按压按钮。

##1. 为每个按钮添加一个selector类型的drawable资源文件

为播放， 停止， 前一个节目， 下一个节目按钮创建一个selecotr资源文件

**selector_player_next.xml:**

此处有一小细节， 一定要把有特定属性设置的放在前面，否则不起作用

**不起作用**

	<selector xmlns:android="http://schemas.android.com/apk/res/android">
	    <item android:drawable="@mipmap/next" />
	    <item android:state_pressed="true"  android:drawable="@mipmap/next_press"/>
	</selector>
	
**起作用**

	<selector xmlns:android="http://schemas.android.com/apk/res/android">
	    <item android:state_pressed="true"  android:drawable="@mipmap/next_press"/>
	    <item android:drawable="@mipmap/next" />
	</selector>
	
**最佳方式**

为了避免顺序依赖，最佳方式是把所有要用到的属性在每一项中都添加上

	<selector xmlns:android="http://schemas.android.com/apk/res/android">
	    <item android:state_pressed="false" android:drawable="@mipmap/next" />
	    <item android:state_pressed="true"  android:drawable="@mipmap/next_press"/>
	</selector>
	
**selector_player_play.xml:**

	<selector xmlns:android="http://schemas.android.com/apk/res/android">
	    <item android:state_pressed="false" android:drawable="@mipmap/play" />
	    <item android:state_pressed="true"  android:drawable="@mipmap/play_press"/>
	</selector>

**selector_player_previous.xml:**

	<selector xmlns:android="http://schemas.android.com/apk/res/android">
	    <item android:state_pressed="false" android:drawable="@mipmap/previous" />
	    <item android:state_pressed="true"  android:drawable="@mipmap/previous_press"/>
	</selector>

**selector_player_stop.xml:**

	<selector xmlns:android="http://schemas.android.com/apk/res/android">
	    <item android:state_pressed="false" android:drawable="@mipmap/stop" />
	    <item android:state_pressed="true"  android:drawable="@mipmap/stop_press"/>
	</selector>

##2. 修改播放器布局文件中按钮的资源文件

**activity_track_player.xml:**

	<ImageView
		android:id="@+id/iv_play"
		android:layout_width="40dp"
		android:layout_height="40dp"
		android:layout_marginTop="15dp"
		app:layout_constraintStart_toEndOf="@id/iv_previous"
		app:layout_constraintEnd_toStartOf="@id/iv_next"
		app:layout_constraintTop_toBottomOf="@id/tv_escaped_time"
		android:src="@drawable/selector_player_play"
		tools:srcCompat="@tools:sample/avatars" />
		
	<ImageView
        android:id="@+id/iv_next"
        android:src="@drawable/selector_player_next"
        android:layout_width="25dp"
        android:layout_height="25dp"
        android:layout_marginTop="20dp"
        app:layout_constraintEnd_toStartOf="@id/iv_player_icon_list"
        app:layout_constraintStart_toEndOf="@id/iv_play"
        app:layout_constraintTop_toBottomOf="@id/tv_escaped_time"
        tools:srcCompat="@tools:sample/avatars" />
        
    <ImageView
        android:id="@+id/iv_previous"
        android:layout_width="25dp"
        android:layout_height="25dp"
        app:layout_constraintStart_toEndOf="@id/iv_sort_descending"
        app:layout_constraintEnd_toStartOf="@id/iv_play"
        app:layout_constraintTop_toBottomOf="@id/tv_escaped_time"
        android:layout_marginTop="20dp"
        android:src="@drawable/selector_player_previous"
        tools:srcCompat="@tools:sample/avatars" />
        
##3. 修改代码中使用到的资源文件

	@Override
    public void onPlayStart() {
        if (playImageView!=null) {
            playImageView.setImageResource(R.drawable.selector_player_stop);
        }
    }

    @Override
    public void onPlayPause() {
        if (playImageView!=null) {
            playImageView.setImageResource(R.drawable.selector_player_play);
        }
    }

    @Override
    public void onPlayStop() {
        if (playImageView != null) {
            playImageView.setImageResource(R.drawable.selector_player_stop);
        }
    }