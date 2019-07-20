#导航栏布局调整

##1. 设置导航栏布局为平均分配模式
	commonNavigator.setAdjustMode(true);
##2. 导航栏添加搜素图标
在素材中添加一个搜素图标文件

mipmap/search_icon

![搜索图标](./pics/search_icon.png)
	
##3. 布局文件调整
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    tools:context=".MainActivity">
	
	
	    <androidx.constraintlayout.widget.ConstraintLayout
	        android:id="@+id/indicator_constraintLayout"
	        android:layout_width="match_parent"
	        android:layout_height="50dp"
	        android:background="@color/main_color"
	        app:layout_constraintEnd_toEndOf="parent"
	        app:layout_constraintStart_toStartOf="parent"
	        app:layout_constraintTop_toTopOf="parent">
	
	        <net.lucode.hackware.magicindicator.MagicIndicator
	            android:id="@+id/main_indicator"
	            android:layout_width="0dp"
	            android:layout_height="match_parent"
	            app:layout_constraintHorizontal_weight="3"
	            app:layout_constraintEnd_toStartOf="@+id/search_constraintLayout"
	            app:layout_constraintStart_toStartOf="parent"
	            app:layout_constraintTop_toTopOf="parent" />
	
	        <androidx.constraintlayout.widget.ConstraintLayout
	            android:id="@+id/search_constraintLayout"
	            android:layout_width="0dp"
	            android:layout_height="match_parent"
	            app:layout_constraintEnd_toEndOf="parent"
	            app:layout_constraintHorizontal_weight="1"
	            app:layout_constraintStart_toEndOf="@+id/main_indicator"
	            app:layout_constraintTop_toTopOf="parent">
	
	            <ImageView
	                android:id="@+id/main_imageView"
	                android:layout_width="15dp"
	                android:layout_height="15dp"
	                android:background="@mipmap/search_icon"
	                android:contentDescription="@string/app_name"
	                app:layout_constraintStart_toStartOf="parent"
	                app:layout_constraintEnd_toEndOf="parent"
	                app:layout_constraintHorizontal_weight="1"
	                app:layout_constraintTop_toTopOf="parent"
	                app:layout_constraintBottom_toBottomOf="parent"
	                tools:src="@tools:sample/avatars" />
	
	        </androidx.constraintlayout.widget.ConstraintLayout>
	
	    </androidx.constraintlayout.widget.ConstraintLayout>
	
	
	    <androidx.viewpager.widget.ViewPager
	        android:id="@+id/main_viewpager"
	        android:layout_width="match_parent"
	        android:layout_height="0dp"
	        app:layout_constraintVertical_weight="1"
	        app:layout_constraintTop_toBottomOf="@id/indicator_constraintLayout"
	        app:layout_constraintBottom_toBottomOf="parent"
	        app:layout_constraintEnd_toEndOf="parent"
	        app:layout_constraintStart_toStartOf="parent">
	
	    </androidx.viewpager.widget.ViewPager>
	
	
	</androidx.constraintlayout.widget.ConstraintLayout>
##7. 最终效果
![效果图](./pics/NavigatorAdjust.png)