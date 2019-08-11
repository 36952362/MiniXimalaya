#044: 搜索界面布局和接口定义

#[首页](./../README.md)


##1. 添加搜索Activity和相关的布局文件

**SearchActivity.java:**

	public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}

**activity_search.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".SearchActivity">


    <ImageView
        android:id="@+id/iv_search_return"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="15dp"
        android:layout_marginStart="10dp"
        android:layout_width="15dp"
        android:layout_height="25dp"
        android:paddingStart="10dp"
        android:paddingEnd="10dp"
        android:background="@mipmap/return_icon"/>

    <EditText
        android:id="@+id/et_search"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:background="@drawable/shape_search_bg"
        android:ems="50"
        android:textCursorDrawable="@drawable/shape_search_cursor_bg"
        android:maxLines="1"
        android:paddingStart="10dp"
        android:paddingEnd="10dp"
        android:hint="@string/search_hint"
        android:layout_marginTop="10dp"
        android:layout_marginStart="40dp"
        android:layout_marginEnd="70dp"
        android:inputType="textPersonName"
        app:layout_constraintEnd_toStartOf="@id/tv_search"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toEndOf="@id/iv_search_return" />

    <TextView
        android:id="@+id/tv_search"
        android:layout_width="45dp"
        android:layout_height="30dp"
        android:gravity="center"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginEnd="15dp"
        android:layout_marginTop="15dp"
        android:textSize="20sp"
        android:textColor="@color/main_color"
        android:text="@string/search" />

    <FrameLayout
        android:id="@+id/fl_search"
        android:layout_marginTop="60dp"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintTop_toBottomOf="@id/et_search"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent">

    </FrameLayout>
	</androidx.constraintlayout.widget.ConstraintLayout>

##2. 搜素逻辑层接口和回调方法接口定义

**ISearchPresenter.java:**


	public interface ISearchPresenter extends IBasePresenter<ISearchCallback> {

	    void doSearch(String keyword);
	
	    void reSearch();
	
	    void loadMore(String keyword);
	
	    void getHotword();
	
	    void getSuggestWords(String keyword);
	}
	
**ISearchCallback.java:**

	public interface ISearchCallback {

	    void onSearchResult(List<Album> albums);
	
	    void onLoadMoreResult(List<Album> albums, boolean hasMore);
	
	    void onHotwordResult(List<HotWord> hotWordList);
	
	    void onSuggestResult(List<QueryResult> keyWordList);
	}
