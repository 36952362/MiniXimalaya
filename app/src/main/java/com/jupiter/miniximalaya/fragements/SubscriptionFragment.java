package com.jupiter.miniximalaya.fragements;

import android.view.View;
import android.view.ViewGroup;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.base.BaseFragment;

public class SubscriptionFragment extends BaseFragment {
    @Override
    protected View onCreateSubView(ViewGroup container) {
        return  onLoadLayout(R.layout.fragment_subscription, container);    }
}
