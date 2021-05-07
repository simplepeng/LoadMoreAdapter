package me.simple.loadmoreadapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 底部加载更多的ViewHolder
 */
internal class LoadMoreViewHolder(
    itemView: View,
    private val mFooter: ILoadMoreFooter
) : RecyclerView.ViewHolder(itemView), ILoadMore {

    init {
        val params = itemView.layoutParams
        if (params is StaggeredGridLayoutManager.LayoutParams) {
            params.isFullSpan = true
        }
    }

    /**
     * 设置状态
     */
    fun setState(stateType: Int) {
        when (stateType) {
            LoadMoreAdapter.STATE_IS_LOADING -> loading()
            LoadMoreAdapter.STATE_LOAD_FAILED -> loadFailed()
            LoadMoreAdapter.STATE_NO_MORE_DATA -> noMoreData()
        }
    }

    override fun loading() {
        mFooter.loading(itemView)
    }

    override fun noMoreData() {
        mFooter.noMoreData(itemView)
    }

    override fun loadFailed() {
        mFooter.loadFailed(itemView)
    }


}