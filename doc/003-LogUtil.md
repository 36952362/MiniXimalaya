# 编写属于自己的Log输出控制
### 1. 在应用工程中创建utils目录并在此目录中添加一个LogUtil.java文件
### 2. 实现LogUtil.java
	public class LogUtil {
	    public static String sTAG = "LogUtil";
	
	    //控制是否要输出log
	    public static boolean sIsRelease = false;
	
	    /**
	     * 如果是要发布了，可以在application里面把这里release一下，这样子就没有log输出了
	     */
	    public static void init(String baseTag, boolean isRelease) {
	        sTAG = baseTag;
	        sIsRelease = isRelease;
	    }
	
	    public static void d(String TAG, String content) {
	        if (!sIsRelease) {
	            Log.d("[" + sTAG + "]" + TAG, content);
	        }
	    }
	
	    public static void v(String TAG, String content) {
	        if (!sIsRelease) {
	            Log.d("[" + sTAG + "]" + TAG, content);
	        }
	    }
	
	    public static void i(String TAG, String content) {
	        if (!sIsRelease) {
	            Log.d("[" + sTAG + "]" + TAG, content);
	        }
	    }

### 3. 使用方式
在BaseApplication初始化的时候初始化

	LogUtil.init(this.getPackageName(), false);
	
调用

	LogUtil.d(TAG, "log内容");
