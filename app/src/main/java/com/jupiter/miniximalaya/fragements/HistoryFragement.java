package com.jupiter.miniximalaya.fragements;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.base.BaseFragement;

public class HistoryFragement extends BaseFragement {
    @Override
    protected View onSubViewLoaded() {
        return  onLoadLayout(R.layout.fragement_history);
    }
}
