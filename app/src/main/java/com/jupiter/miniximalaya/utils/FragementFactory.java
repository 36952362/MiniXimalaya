package com.jupiter.miniximalaya.utils;

import com.jupiter.miniximalaya.base.BaseFragement;
import com.jupiter.miniximalaya.fragements.HistoryFragement;
import com.jupiter.miniximalaya.fragements.RecommendFragment;
import com.jupiter.miniximalaya.fragements.SubscriptionFragement;

import java.util.HashMap;
import java.util.Map;

public class FragementFactory {

    public static final  int FRAGEMENT_RECOMMEND = 0;
    public static final int FRAGEMENT_SUBSCRIPTION = 1;
    public static final int FRAGEMENT_HISTORY = 2;

    public static final int PAGE_COUNT = 3;

    private  static Map<Integer, BaseFragement> sCache = new HashMap<>();

    public static BaseFragement getFragement(int index){
        BaseFragement baseFragement = sCache.get(index);
        if(null != baseFragement)
            return baseFragement;

        switch (index){
            case FRAGEMENT_RECOMMEND:
                baseFragement = new RecommendFragment();
                break;
            case FRAGEMENT_SUBSCRIPTION:
                baseFragement = new SubscriptionFragement();
                break;
            case FRAGEMENT_HISTORY:
                baseFragement = new HistoryFragement();
                break;
        }
        sCache.put(index, baseFragement);
        return  baseFragement;
    }
}
