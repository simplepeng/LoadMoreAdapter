package me.simple.loadmoreadapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import me.simple.loadmoreadapter.LoadMoreAdapter

/**
 * 底部加载更多的ViewHolder
 */
internal class LoadMoreViewHolder(itemView: View, mFooter: ILoadMoreFooter) : RecyclerView.ViewHolder(itemView), ILoadMore {
    private val mFooter: ILoadMoreFooter
    fun setState(stateType: Int) {
        when (stateType) {
            LoadMoreAdapter.Companion.STATE_LOADING -> loading()
            LoadMoreAdapter.Companion.STATE_LOAD_FAILED -> loadFailed()
            LoadMoreAdapter.Companion.STATE_NO_MORE_DATA -> noMoreData()
        }
    }

    override fun loading() {
        mFooter.loading()
    }

    override fun noMoreData() {
        mFooter.noMoreData()
    }

    override fun loadFailed() {
        mFooter.loadFailed()
    }

    init {
        val params = itemView.layoutParams
        if (params is StaggeredGridLayoutManager.LayoutParams) {
            params.isFullSpan = true
        }
        this.mFooter = mFooter
    }
}