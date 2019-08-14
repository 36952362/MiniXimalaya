package com.jupiter.miniximalaya;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jupiter.miniximalaya.adapters.AlbumAdapter;
import com.jupiter.miniximalaya.interfaces.ISearchCallback;
import com.jupiter.miniximalaya.presenters.SearchPresenter;
import com.jupiter.miniximalaya.utils.DPPXConverter;
import com.jupiter.miniximalaya.views.FlowTextLayout;
import com.jupiter.miniximalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.ArrayList;
import java.util.List;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import static android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT;

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
    private InputMethodManager inputMethodManager;
    private View searchInputDelete;

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

                inputMethodManager.hideSoftInputFromWindow(searchInput.getWindowToken(), HIDE_NOT_ALWAYS);

            }
        });

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

        searchInputDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput.setText("");
            }
        });

    }

    private void initView() {
        inputMethodManager = (InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        searchReturn = findViewById(R.id.iv_search_return);
        searchInput = findViewById(R.id.et_search);

        searchInput.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchInput.setFocusable(true);
                inputMethodManager.showSoftInput(searchInput, SHOW_IMPLICIT);
            }
        }, 500);

        searchBtn = findViewById(R.id.tv_search);
        searchContain = findViewById(R.id.fl_search);
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

        searchInputDelete = findViewById(R.id.search_input_delete);
        searchInputDelete.setVisibility(View.GONE);

    }

    private View createSuccessView(ViewGroup container) {
        View resultView = LayoutInflater.from(this).inflate(R.layout.search_result_view, container, false);
        searchResultImageView = resultView.findViewById(R.id.rv_search_result);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        searchResultImageView.setLayoutManager(linearLayoutManager);
        albumAdapter = new AlbumAdapter();
        searchResultImageView.setAdapter(albumAdapter);
        searchResultImageView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.left = DPPXConverter.dip2px(view.getContext(), 5);
                outRect.right = DPPXConverter.dip2px(view.getContext(), 5);
                outRect.top = DPPXConverter.dip2px(view.getContext(), 5);
                outRect.bottom = DPPXConverter.dip2px(view.getContext(), 5);
            }
        });
        flowTextLayout = resultView.findViewById(R.id.search_flowText);

        return resultView;
    }

    @Override
    public void onSearchResult(List<Album> albums) {
        inputMethodManager.hideSoftInputFromWindow(searchInput.getWindowToken(), HIDE_NOT_ALWAYS);
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

    @Override
    public void onLoadMoreResult(List<Album> albums, boolean hasMore) {

    }

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
