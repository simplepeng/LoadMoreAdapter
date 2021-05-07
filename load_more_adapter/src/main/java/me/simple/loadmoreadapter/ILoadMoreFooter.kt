package me.simple.loadmoreadapter

import android.view.View

interface ILoadMoreFooter {
    /**
     * 设置footer的布局
     */
    fun setLayoutRes(): Int

    /**
     * footer布局初始化完成
     */
    fun onCreate(footerView: View)

    /**
     * 加载更多中
     */
    fun loading(footerView: View)

    /**
     * 加载完成-已无更多数据
     */
    fun noMoreData(footerView: View)

    /**
     * 加载失败
     */
    fun loadFailed(footerView: View)
}