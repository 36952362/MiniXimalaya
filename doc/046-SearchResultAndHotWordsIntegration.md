#046: 搜索结果和热词整合

#[首页](./../README.md)

在用户还没有开始搜索时，列出当前的搜索热词提供参考。一旦用户输入自己的关键词并获得返回结果时，隐藏搜索热词并展示用户搜索结果。

##1. 更新搜索结果布局文件。

添加展示热词自定义控件, 该控件和搜索结果是互相覆盖。通过Visibility属性在适当时机展示或隐藏.

**search_result_view.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.recyclerview.widget.RecyclerView
        android:visibility="gone"
        android:id="@+id/rv_search_result"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:overScrollMode="never"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />


    <com.jupiter.miniximalaya.views.FlowTextLayout
        android:visibility="gone"
        android:id="@+id/search_flowText"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent">

    </com.jupiter.miniximalaya.views.FlowTextLayout>

	</androidx.constraintlayout.widget.ConstraintLayout>

##2. 展示热词页面
在获取到热词结果时，隐藏搜索结果页面，显示热词页面，并更新内容。

**SearchActivity.java:**

	@Override
    public void onHotWordResult(List<HotWord> hotWordList) {
        flowTextLayout.setVisibility(View.VISIBLE);
        searchResultImageView.setVisibility(View.GONE);
        uiLoader.updateUIStatus(UILoader.UIStatus.SUCCESS);
        List<String> hotWords = new ArrayList<>();
        for (HotWord hotWord : hotWordList) {
            hotWords.add(hotWord.getSearchword());
        }
        flowTextLayout.setTextContents(hotWords);
    }

##3. 展示搜索结果页面
在获取到搜索结果时，隐藏热词页面，显示搜素结果页面，并更新内容.

**SearchActivity.java:**

	@Override
    public void onSearchResult(List<Album> albums) {
        flowTextLayout.setVisibility(View.GONE);
        searchResultImageView.setVisibility(View.VISIBLE);
        if (albums.size() == 0) {
            uiLoader.updateUIStatus(UILoader.UIStatus.EMPTY);
        }
        else {
            albumAdapter.updateData(albums);
            uiLoader.updateUIStatus(UILoader.UIStatus.SUCCESS);
        }
    }
##4. 通过热词搜索
对热词设置点击事件，并把热词添加到搜索输入框，调用喜马拉雅SDK获取搜索结果。

**SearchActivity.java:**

	flowTextLayout.setClickListener(new FlowTextLayout.ItemClickListener() {
        @Override
        public void onItemClick(String text) {
            searchInput.setText(text);
            searchInput.setSelection(text.length());
            if (searchPresenter != null) {
                searchPresenter.doSearch(text);
                uiLoader.updateUIStatus(UILoader.UIStatus.LOADING);
            }
        }
    });
 
##5. 当搜索输入框内容清空后显示热词页面
通过监听搜索输入框的addTextChangedListener事件判断输入框中内容长度，当长度变成0时调用喜马拉雅SDK获取热词

**SearchActivity.java:**

	searchInput.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (TextUtils.isEmpty(charSequence)) {
                if (searchPresenter != null) {
                    searchPresenter.getHotWords();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    });
    
##6. 添加删除按钮
###6.1 修改布局文件
在搜素输入框末端添加一个删除按钮

**activity_search.xml:**

	<ImageView
	    android:id="@+id/search_input_delete"
	    android:layout_width="35dp"
	    android:layout_height="35dp"
	    android:src="@mipmap/delete"
	    app:layout_constraintTop_toTopOf="@id/et_search"
	    app:layout_constraintEnd_toEndOf="@id/et_search"
	    />
###6.2 删除按钮添加点击事件
当收到点击事件时，清空搜索输入框中的内容

**SearchActivity.java:**

	searchInputDelete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            searchInput.setText("");
        }
    });
###6.3 删除按钮的隐藏和显示
当搜索输入框中有内容时显示删除按钮，当搜索输入框中没有内容时隐藏删除按钮

**SearchActivity.java:**

	searchInput.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (TextUtils.isEmpty(charSequence)) {
                if (searchPresenter != null) {
                    searchPresenter.getHotWords();
                    searchInputDelete.setVisibility(View.GONE);
                }
            }else{
                searchInputDelete.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    });
        
##7. 效果图
![效果图](./pics/SearchWithHotWord.gif)