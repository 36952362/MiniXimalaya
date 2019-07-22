package com.jupiter.miniximalaya;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.jupiter.miniximalaya.adapters.MainContentAdapter;
import com.jupiter.miniximalaya.adapters.MainIndicatorAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private MagicIndicator mainIndicator;
    private ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initIndicate();
    }

    private void initView() {
        mainIndicator = findViewById(R.id.main_indicator);
        mainViewPager = findViewById(R.id.main_viewpager);
    }

    private void initIndicate() {

        //对导航栏的设置
        CommonNavigator commonNavigator = new CommonNavigator(this);
        //条目位置平均放置
        commonNavigator.setAdjustMode(true);
        MainIndicatorAdapter mainIndicatorAdapter = new MainIndicatorAdapter(this, mainViewPager);
        commonNavigator.setAdapter(mainIndicatorAdapter);
        mainIndicator.setNavigator(commonNavigator);

        //创建内容适配器
        FragmentManager fragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(fragmentManager);
        mainViewPager.setAdapter(mainContentAdapter);
        
        ViewPagerHelper.bind(mainIndicator, mainViewPager);
    }
}
