#008: 获取推荐列表

#[首页](./../README.md)

##1. 定义获取推荐条目数
定义一个常量类，并定义一个常量表示每次获取的推荐条目数
**Constants.java:**

	public class Constants {
	    //推荐条目数
	    public static final int RECOMMEND_COUNT = 20;
	}

##2. 获取推荐数据
在RecommendFragement类中，调用喜马拉雅SDK接口获取推荐数据

	private void getRecommendData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.i(TAG, "getGuessLikeAlbum success");
            }
	
            @Override
            public void onError(int i, String s) {
                LogUtil.e(TAG, "getGuessLikeAlbum failed");
                LogUtil.e(TAG, "errorCode: " + i + ", reason:" + s);
            }
        });
    }