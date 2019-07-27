#006: ViewPager数据展示

#[首页](./../README.md)

##1. 创建不同内容的Fragement
由于有3类(推荐，订阅和历史)三类数据需要展示，所以要创建三个Fragement:RecommendFragement, SubscriptionFragement, HistoryFragement. 由于这三个Framement可能有共同的功能和代码，所以创建一个基类BaseFragement, 方便代码的重构和复用。

**BaseFragement.java:**

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

**HistoryFragement.java:**

	public class HistoryFragement extends BaseFragement 	{
	    @Override
	    protected View onSubViewLoaded() {
	        return  onLoadLayout(R.layout.fragement_history);
	    }
	}
	
**RecommendFragement.java:**

	public class RecommendFragment extends BaseFragement {
	    @Override
	    protected View onSubViewLoaded() {
	        return  onLoadLayout(R.layout.fragement_recommend);
	    }
	}

**SubscriptionFragement.java:**

	public class SubscriptionFragement extends BaseFragement {
	    @Override
	    protected View onSubViewLoaded() {
	        return  onLoadLayout(R.layout.fragement_subscription);    }
	}

##2. 对每个Fragement创建一个布局文件，并在布局中添加一个TextView控件用于显示内容
**fragement_history.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:orientation="vertical" android:layout_width="match_parent"
	    android:layout_height="match_parent">

    <TextView
        android:id="@+id/tv_history"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:textSize="30sp"
        android:text="@string/history" />
	</androidx.constraintlayout.widget.ConstraintLayout>

**fragement_recommend.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:orientation="vertical" android:layout_width="match_parent"
	    android:layout_height="match_parent">
	
	    <TextView
	        android:id="@+id/tv_recommend"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        android:gravity="center"
	        android:textSize="30sp"
	        android:text="@string/recommend" />
	</androidx.constraintlayout.widget.ConstraintLayout>

**fragement_subscription.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:orientation="vertical" android:layout_width="match_parent"
	    android:layout_height="match_parent">
	
	    <TextView
	        android:id="@+id/tv_subscription"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        android:gravity="center"
	        android:textSize="30sp"
	        android:text="@string/subscription" />
	</androidx.constraintlayout.widget.ConstraintLayout>

##3. 对ViewPager创建一个内容适配器并和三个Fragement进行绑定
###3.1 创建内容适配器
**MainActivity.java:**

        FragmentManager fragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(fragmentManager);
        mainViewPager.setAdapter(mainContentAdapter);
###3.2 适配器和Fragement绑定
**MainContentAdapter.java:**

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
	
##7. 最终效果
![效果图](./pics/ContentDisplay.png)