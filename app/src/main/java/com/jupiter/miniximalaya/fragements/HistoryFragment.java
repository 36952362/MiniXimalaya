package com.jupiter.miniximalaya.fragements;

import android.view.View;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.base.BaseFragment;

public class HistoryFragment extends BaseFragment {
    @Override
    protected View onCreateSubView() {
        return  onLoadLayout(R.layout.fragment_history);
    }
}
