package me.simple.loadmoreadapter;

import android.view.View;

public abstract class AbsLoadMoreFooter {

    /**
     * 设置footer的布局
     */
    public abstract int setLayoutRes();

    /**
     * footer布局初始化完成
     */
    public abstract void onCreate(View footerView);

    /**
     * 加载更多中
     */
    public abstract void loading();

    /**
     * 加载完成
     */
    public abstract void loadComplete();

    /**
     * 加载完成-已无更多数据
     */
    public abstract void noMoreData();

    /**
     * 加载失败
     */
    public abstract void loadFailed();


}
