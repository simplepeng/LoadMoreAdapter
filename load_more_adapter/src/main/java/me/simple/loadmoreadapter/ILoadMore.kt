package me.simple.loadmoreadapter

internal interface ILoadMore {
    /**
     * 加载更多中
     */
    fun loading()

    /**
     * 加载完成-已无更多数据
     */
    fun noMoreData()

    /**
     * 加载失败
     */
    fun loadFailed()
}