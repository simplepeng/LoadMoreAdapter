package me.simple.loadmoreadapter;

import android.view.View;

public interface ILoadMoreFooter {

    /**
     * 设置footer的布局
     */
    int setLayoutRes();

    /**
     * footer布局初始化完成
     */
    void onCreate(View footerView);

    /**
     * 加载更多中
     */
    void loading();

//    /**
//     * 加载完成
//     */
//    public abstract void loadComplete();

    /**
     * 加载完成-已无更多数据
     */
    void noMoreData();

    /**
     * 加载失败
     */
    void loadFailed();


}
