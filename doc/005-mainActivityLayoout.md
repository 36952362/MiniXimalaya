# 布局主界面

##1. 导入magicindicator模块
在build.gradler(Project)文件中添加magicindicator maven仓库
	
	repositories {
	    ...
	    maven {
	        url "https://jitpack.io"
	    }
	}

##2. 添加magicindicator编译依赖
在build.gradle(Moudle)文件中添加:

	dependencies {
	    ...
	    compile 'com.github.hackware1993:MagicIndicator:1.5.0'
	}

##3. 在布局文件中添加Magicindicator和ViewPager控件

	<net.lucode.hackware.magicindicator.MagicIndicator
        android:id="@+id/main_indicator"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <androidx.viewpager.widget.ViewPager
        android:id="@+id/main_viewpager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
##4. 创建指示器和适配器并绑定

	mainIndicator.setBackgroundColor(getResources().getColor(R.color.main_color));
   	CommonNavigator commonNavigator = new CommonNavigator(this);
   	IndicatorAdapter indicatorAdapter = new IndicatorAdapter(this, mainViewPager);
   	commonNavigator.setAdapter(indicatorAdapter);
   	mainIndicator.setNavigator(commonNavigator);
   	ViewPagerHelper.bind(mainIndicator, mainViewPager);
   	
##5. 初始化数据
在strings.xml文件中定义一个字符串数组

	<string-array name="indicate_title">
        <item>推荐</item>
        <item>订阅</item>
        <item>历史</item>
    </string-array>

##5.在适配器中绑定数据

public class IndicatorAdapter extends CommonNavigatorAdapter {

    private final String[] titiles;
    private ViewPager mainViewPager ;

    public IndicatorAdapter(Context context, ViewPager mainViewPager) {
        titiles = context.getResources().getStringArray(R.array.indicate_title);
        this.mainViewPager = mainViewPager;
    }

    @Override
    public int getCount() {
        return titiles == null ? 0 : titiles.length;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        //创建View
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        //设置要显示的内容
        colorTransitionPagerTitleView.setText(titiles[index]);
        //设置一般情况下颜色为灰色
        colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#aaffffff"));
        //设置选中情况下颜色为黑色
        colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#ffffff"));
        //设置字体大小
        colorTransitionPagerTitleView.setTextSize(18);
        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewPager.setCurrentItem(index);
            }
        });
        return colorTransitionPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setColors(Color.parseColor("#ffffff"));
        return indicator;
    }
}

##6. 界面调优
修改节目的主体为NoActionBar, 并把颜色都设置成和指示器相同的颜色
styles.xml:

	<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/main_color</item>
        <item name="colorPrimaryDark">@color/main_color</item>
        <item name="colorAccent">@color/main_color</item>
    </style>
##7. 最终效果
![效果图](./pics/MainActivity.png)