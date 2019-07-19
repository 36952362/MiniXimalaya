package com.jupiter.miniximalaya.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jupiter.miniximalaya.utils.FragementFactory;

public class MainContentAdapter extends FragmentPagerAdapter {
    public MainContentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return FragementFactory.getFragement(position);
    }

    @Override
    public int getCount() {
        return FragementFactory.PAGE_COUNT;
    }
}
