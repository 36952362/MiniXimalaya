package com.jupiter.miniximalaya;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.jupiter.miniximalaya.adaptors.IndicatorAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;

public class MainActivity extends AppCompatActivity {

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

        mainIndicator.setBackgroundColor(getResources().getColor(R.color.main_color));

        CommonNavigator commonNavigator = new CommonNavigator(this);

        IndicatorAdapter indicatorAdapter = new IndicatorAdapter(this, mainViewPager);

        commonNavigator.setAdapter(indicatorAdapter);
        mainIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mainIndicator, mainViewPager);
    }
}
