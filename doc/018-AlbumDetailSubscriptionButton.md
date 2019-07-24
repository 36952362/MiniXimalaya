#添加订阅按钮

##1. 创建一个drawable文件作为订阅按钮的背景
**shape_album_subscribe_bg.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
	
	    <solid android:color="@color/subscribe_color"/>
	    <corners android:radius="30dp"/>
	    <size android:width="150dp" android:height="60dp"/>
	</shape>
##2. 添加一个文本控件到布局文件
**activity_album_detail.xml:**

	<TextView
        android:id="@+id/tv_subscribe"
        android:layout_width="75dp"
        android:layout_height="33dp"
        android:text="@string/album_subscribe"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginEnd="28dp"
        android:gravity="center"
        android:textColor="@color/white"
        android:background="@drawable/shape_album_subscribe_bg"
        app:layout_constraintTop_toBottomOf="@id/iv_album_large_cover"
        tools:layout_editor_absoluteX="195dp"
        tools:layout_editor_absoluteY="498dp" />


##3. 效果图
![效果图](./pics/AlbumDetailSubscription.png)