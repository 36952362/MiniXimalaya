#022: 播放器页面布局

#[首页](./../README.md)

##1. 创建播放器页面布局文件
**activity_track_player.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".TrackPlayerActivity">

    <TextView
        android:id="@+id/tv_track_title"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:maxLines="2"
        android:ellipsize="end"
        app:layout_constraintHeight_percent="0.1"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        android:gravity="center"
        android:textSize="50px"
        android:text="播放器标题" />

    <androidx.viewpager.widget.ViewPager
        android:id="@+id/vp_track_cover"
        android:background="#3c3c3c"
        android:layout_width="wrap_content"
        app:layout_constraintHeight_percent="0.7"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="@id/tv_track_title"
        android:layout_height="0dp"/>


    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/play_container"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHeight_percent="0.2"
        app:layout_constraintTop_toBottomOf="@id/vp_track_cover"
        android:layout_width="match_parent"
        android:layout_height="0dp">

        <TextView
            android:id="@+id/tv_escaped_time"
            android:layout_width="0dp"
            app:layout_constraintWidth_percent="0.25"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:gravity="center"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintEnd_toStartOf="@id/sb_play_progress"
            android:text="12:00" />

        <SeekBar
            android:id="@+id/sb_play_progress"
            app:layout_constraintStart_toEndOf="@id/tv_escaped_time"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintEnd_toStartOf="@id/tv_remain_time"
            android:layout_width="0dp"
            android:layout_marginTop="20dp"
            app:layout_constraintWidth_percent="0.5"
            android:layout_height="wrap_content" />

        <TextView
            android:id="@+id/tv_remain_time"
            android:layout_width="0dp"
            android:layout_marginTop="20dp"
            android:gravity="center"
            app:layout_constraintWidth_percent="0.25"
            android:layout_height="wrap_content"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toEndOf="@id/sb_play_progress"
            android:text="04:30" />

        <ImageView
            android:id="@+id/iv_sort_descending"
            android:layout_width="25dp"
            android:layout_height="25dp"
            app:layout_constraintHorizontal_weight="1"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toStartOf="@id/iv_previous"
            app:layout_constraintTop_toBottomOf="@id/tv_escaped_time"
            android:layout_marginTop="20dp"
            android:src="@mipmap/sort_descending"
            tools:srcCompat="@tools:sample/avatars" />

        <ImageView
            android:id="@+id/iv_previous"
            android:layout_width="25dp"
            android:layout_height="25dp"
            app:layout_constraintStart_toEndOf="@id/iv_sort_descending"
            app:layout_constraintEnd_toStartOf="@id/iv_play"
            app:layout_constraintTop_toBottomOf="@id/tv_escaped_time"
            android:layout_marginTop="20dp"
            android:src="@mipmap/previous"
            tools:srcCompat="@tools:sample/avatars" />

        <ImageView
            android:id="@+id/iv_play"
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:layout_marginTop="15dp"
            app:layout_constraintStart_toEndOf="@id/iv_previous"
            app:layout_constraintEnd_toStartOf="@id/iv_next"
            app:layout_constraintTop_toBottomOf="@id/tv_escaped_time"
            android:src="@mipmap/play"
            tools:srcCompat="@tools:sample/avatars" />

        <ImageView
            android:id="@+id/iv_next"
            android:src="@mipmap/next"
            android:layout_width="25dp"
            android:layout_height="25dp"
            android:layout_marginTop="20dp"
            app:layout_constraintEnd_toStartOf="@id/iv_player_icon_list"
            app:layout_constraintStart_toEndOf="@id/iv_play"
            app:layout_constraintTop_toBottomOf="@id/tv_escaped_time"
            tools:srcCompat="@tools:sample/avatars" />

        <ImageView
            android:id="@+id/iv_player_icon_list"
            android:layout_width="25dp"
            android:layout_height="25dp"
            android:layout_marginTop="20dp"
            android:src="@mipmap/player_icon_list"
            app:layout_constraintStart_toEndOf="@id/iv_next"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@id/tv_escaped_time"
            tools:srcCompat="@tools:sample/avatars" />

    </androidx.constraintlayout.widget.ConstraintLayout>
	</androidx.constraintlayout.widget.ConstraintLayout>
    
    
##2. 效果图

![效果图](./pics/TrackPlayerLayout.png)