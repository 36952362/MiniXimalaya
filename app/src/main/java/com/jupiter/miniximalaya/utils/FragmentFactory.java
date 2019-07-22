package com.jupiter.miniximalaya.utils;

import com.jupiter.miniximalaya.base.BaseFragment;
import com.jupiter.miniximalaya.fragements.HistoryFragment;
import com.jupiter.miniximalaya.fragements.RecommendFragment;
import com.jupiter.miniximalaya.fragements.SubscriptionFragment;

import java.util.HashMap;
import java.util.Map;

public class FragmentFactory {

    public static final int FRAGMENT_RECOMMEND = 0;
    public static final int FRAGMENT_SUBSCRIPTION = 1;
    public static final int FRAGMENT_HISTORY = 2;

    public static final int PAGE_COUNT = 3;

    private  static Map<Integer, BaseFragment> sCache = new HashMap<>();

    public static BaseFragment getFragment(int index){
        BaseFragment baseFragment = sCache.get(index);
        if(null != baseFragment)
            return baseFragment;

        switch (index){
            case FRAGMENT_RECOMMEND:
                baseFragment = new RecommendFragment();
                break;
            case FRAGMENT_SUBSCRIPTION:
                baseFragment = new SubscriptionFragment();
                break;
            case FRAGMENT_HISTORY:
                baseFragment = new HistoryFragment();
                break;
        }
        sCache.put(index, baseFragment);
        return  baseFragment;
    }
}
