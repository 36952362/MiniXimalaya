package com.jupiter.miniximalaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jupiter.miniximalaya.adapters.AlbumAdapter;
import com.jupiter.miniximalaya.interfaces.ISearchCallback;
import com.jupiter.miniximalaya.presenters.SearchPresenter;
import com.jupiter.miniximalaya.utils.LogUtil;
import com.jupiter.miniximalaya.views.FlowTextLayout;
import com.jupiter.miniximalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements ISearchCallback, UILoader.OnRetryClickListener {


    private static final String TAG = "SearchActivity";
    private ImageView searchReturn;
    private EditText searchInput;
    private TextView searchBtn;
    private FrameLayout searchContain;
    private SearchPresenter searchPresenter;
    private FlowTextLayout flowTextLayout;
    private UILoader uiLoader;
    private RecyclerView searchResultImageView;
    private AlbumAdapter albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchPresenter = SearchPresenter.getsInstance();
        initView();
        searchPresenter.registerCallback(this);
        initEvent();
        searchPresenter.getHotWords();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchPresenter.unRegisterCallback(this);
    }

    private void initEvent() {
        searchReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKeyword = searchInput.getText().toString().trim();
                if (!TextUtils.isEmpty(searchKeyword)) {
                    searchPresenter.doSearch(searchKeyword);
                    uiLoader.updateUIStatus(UILoader.UIStatus.LOADING);
                }
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LogUtil.d(TAG, "content:" + charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void initView() {
        searchReturn = findViewById(R.id.iv_search_return);
        searchInput = findViewById(R.id.et_search);
        searchBtn = findViewById(R.id.tv_search);
        searchContain = findViewById(R.id.fl_search);
        //flowTextLayout = findViewById(R.id.search_flowText);
        if (uiLoader == null) {
            uiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
            uiLoader.setRetryClickListener(this);
            if (uiLoader.getParent() instanceof ViewGroup) {
                ((ViewGroup) uiLoader.getParent()).removeView(uiLoader);
            }
            searchContain.addView(uiLoader);
        }
    }

    private View createSuccessView(ViewGroup container) {
        View resultView = LayoutInflater.from(this).inflate(R.layout.search_result_view, container, false);
        searchResultImageView = resultView.findViewById(R.id.rv_search_result);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        searchResultImageView.setLayoutManager(linearLayoutManager);
        albumAdapter = new AlbumAdapter();
        searchResultImageView.setAdapter(albumAdapter);
        return resultView;
    }

    @Override
    public void onSearchResult(List<Album> albums) {
        if (albums.size() == 0) {
            uiLoader.updateUIStatus(UILoader.UIStatus.EMPTY);
        }
        else {
            albumAdapter.updateData(albums);
            uiLoader.updateUIStatus(UILoader.UIStatus.SUCCESS);
        }
    }

    @Override
    public void onLoadMoreResult(List<Album> albums, boolean hasMore) {

    }

    @Override
    public void onHotWordResult(List<HotWord> hotWordList) {
        List<String> hotWords = new ArrayList<>();
        for (HotWord hotWord : hotWordList) {
            hotWords.add(hotWord.getSearchword());
        }
        //flowTextLayout.setTextContents(hotWords);
    }

    @Override
    public void onSuggestResult(List<QueryResult> keyWordList) {

    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        uiLoader.updateUIStatus(UILoader.UIStatus.ERROR);
    }

    @Override
    public void onRetry() {
        searchPresenter.reSearch();
        uiLoader.updateUIStatus(UILoader.UIStatus.LOADING);
    }
}
