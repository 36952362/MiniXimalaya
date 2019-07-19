package com.jupiter.miniximalaya.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragement  extends Fragment {

    private View view;
    private LayoutInflater inflater;
    private ViewGroup container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        view = onSubViewLoaded();
        return view;
    }

    protected abstract View onSubViewLoaded();

    protected View onLoadLayout(int id){
        return  inflater.inflate(id, container, false);
    }
}
