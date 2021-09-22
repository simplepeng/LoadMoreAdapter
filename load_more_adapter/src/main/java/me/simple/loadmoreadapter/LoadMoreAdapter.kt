package me.simple.loadmoreadapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.util.*

@SuppressLint("NotifyDataSetChanged")
class LoadMoreAdapter<VH : RecyclerView.ViewHolder> private constructor(
    private val realAdapter: RecyclerView.Adapter<VH>,
    private val footer: ILoadMoreFooter = LoadMoreFooter()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        /**
         * LoadMore的ViewType
         */
        private const val VIEW_TYPE_LOAD_MORE = 1112

        /**
         * footer的状态
         */
        const val STATE_IS_LOADING = 0//正在加载更多
        const val STATE_NORMAL = 1//正常状态
        const val STATE_LOAD_FAILED = 2//加载失败
        const val STATE_NO_MORE_DATA = 3//没有更多数据

        /**
         * 包装函数
         */
        @JvmStatic
        fun <VH : RecyclerView.ViewHolder> wrap(
            adapter: RecyclerView.Adapter<VH>,
            footer: ILoadMoreFooter = LoadMoreFooter()
        ): LoadMoreAdapter<VH> {
            return LoadMoreAdapter(adapter, footer)
        }
    }

    /**
     * 加载更多的监听
     */
    private var mOnLoadMoreListener: ((adapter: LoadMoreAdapter<*>) -> Unit)? = null

    /**
     * 加载失败的点击事件
     */
    private var mOnFailedClickListener: ((adapter: LoadMoreAdapter<*>, view: View) -> Unit)? = null

    /**
     * 是否为上拉
     */
    private var mIsScrollLoadMore = false

    /**
     * 状态
     */
    private var mStateType = STATE_NORMAL

    /**
     * 已无更多数据
     */
    private var mNoMoreData = false

    /**
     * attached的RecyclerView
     */
    private var mRecyclerView: RecyclerView? = null

    override fun getItemCount(): Int {
        return realAdapter.itemCount + 1
//        val count = realAdapter.itemCount
//        return if (count > 0) (count + 1) else 0
    }

    override fun getItemId(position: Int): Long {
        return if (getItemViewType(position) == VIEW_TYPE_LOAD_MORE)
            VIEW_TYPE_LOAD_MORE.toLong()
        else
            realAdapter.getItemId(position)

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1)
            VIEW_TYPE_LOAD_MORE
        else
            realAdapter.getItemViewType(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        //如果viewType是loadMore
        if (viewType == VIEW_TYPE_LOAD_MORE) {
            val footView = LayoutInflater.from(parent.context)
                .inflate(footer.setLayoutRes(), parent, false)
            footer.onCreate(footView)
            return LoadMoreViewHolder(footView, footer)
        }

        return realAdapter.onCreateViewHolder(parent, viewType)
    }

    /**
     *
     */
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        onBindViewHolder(holder, position, emptyList())
    }

    /**
     *
     */
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        Log.d("LoadMoreAdapter", "onBindViewHolder -- ${holder.adapterPosition}")
        //如果是加载更多的VH执行onBind
        if (holder is LoadMoreViewHolder) {
            //加载失败点击事件
            if (mOnFailedClickListener != null) {
                holder.itemView.setOnClickListener {
                    if (mStateType == STATE_LOAD_FAILED) {
                        mStateType = STATE_IS_LOADING
                        holder.setState(mStateType)
                        mOnFailedClickListener?.invoke(this@LoadMoreAdapter, holder.itemView)
                    }
                }
            }

            //如果是加载失败或无更多数据
            if (mStateType == STATE_LOAD_FAILED || mStateType == STATE_NO_MORE_DATA) {
                holder.setState(mStateType)
                return
            }

            //
            if (mOnLoadMoreListener != null && !mNoMoreData && mStateType != STATE_IS_LOADING) {
                //fix bug Cannot call this method while RecyclerView is computing a layout or scrolling
                mRecyclerView?.post {
                    mStateType = STATE_IS_LOADING
                    holder.setState(mStateType)
                    mOnLoadMoreListener?.invoke(this@LoadMoreAdapter)
                }
            }
            return
        }

        //执行正常的onBind
        realAdapter.onBindViewHolder(castAttachedViewHolder(holder), position, payloads)
    }

    /**
     * 是否可以垂直滚动
     */
//    private fun canScrollVertically(): Boolean {
//        return mRecyclerView?.canScrollVertically(-1) ?: false
//    }

    /**
     *
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.mRecyclerView = recyclerView
        setFullSpan(recyclerView)
        realAdapter.registerAdapterDataObserver(mProxyDataObserver)
        recyclerView.addOnScrollListener(mOnScrollListener)
        realAdapter.onAttachedToRecyclerView(recyclerView)
    }

    /**
     *
     */
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.mRecyclerView = null
        realAdapter.unregisterAdapterDataObserver(mProxyDataObserver)
        recyclerView.removeOnScrollListener(mOnScrollListener)
        realAdapter.onDetachedFromRecyclerView(recyclerView)
    }

    /**
     *
     */
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        if (holder is LoadMoreViewHolder) return
        realAdapter.onViewAttachedToWindow(castAttachedViewHolder(holder))
    }

    /**
     *
     */
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        if (holder is LoadMoreViewHolder) return
        realAdapter.onViewDetachedFromWindow(castAttachedViewHolder(holder))
    }

    /**
     *
     */
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is LoadMoreViewHolder) return
        realAdapter.onViewRecycled(castAttachedViewHolder(holder))
    }

    /**
     *
     */
    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return if (holder is LoadMoreViewHolder) {
            false
        } else {
            realAdapter.onFailedToRecycleView(castAttachedViewHolder(holder))
        }
    }

    /**
     *
     */
    @Suppress("UNCHECKED_CAST")
    private fun castAttachedViewHolder(holder: RecyclerView.ViewHolder): VH = holder as VH


    /**
     * 代理原来的AdapterDataObserver
     */
    private val mProxyDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            this@LoadMoreAdapter.notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            this@LoadMoreAdapter.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            this@LoadMoreAdapter.notifyItemRangeChanged(positionStart, itemCount, payload)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            this@LoadMoreAdapter.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            this@LoadMoreAdapter.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            this@LoadMoreAdapter.notifyItemRangeChanged(fromPosition, toPosition, itemCount)
        }
    }

    /**
     * 加载更多的监听
     */
    fun setOnLoadMoreListener(
        listener: ((adapter: LoadMoreAdapter<*>) -> Unit)? = null
    ): LoadMoreAdapter<VH> {
        this.mOnLoadMoreListener = listener
        return this
    }

    /**
     * 加载失败的监听
     */
    fun setOnFailedClickListener(
        listener: ((adapter: LoadMoreAdapter<*>, view: View) -> Unit)? = null
    ): LoadMoreAdapter<VH> {
        mOnFailedClickListener = listener
        return this
    }

    /**
     * 滚动监听
     */
    private val mOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            //判断是否能加载更多
            if (newState != RecyclerView.SCROLL_STATE_IDLE
                || mOnLoadMoreListener == null
                || mNoMoreData
                || !mIsScrollLoadMore
            ) return

            if (canLoadMore(recyclerView.layoutManager) && mStateType != STATE_IS_LOADING) {
                startLoading()
                mOnLoadMoreListener?.invoke(this@LoadMoreAdapter)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            mIsScrollLoadMore = dy > 0
        }
    }

    /**
     * 是否可以加载更多
     */
    private fun canLoadMore(layoutManager: RecyclerView.LayoutManager?): Boolean {
        return when (layoutManager) {
            is LinearLayoutManager -> {
                layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1
            }
            is StaggeredGridLayoutManager -> {
                val into = IntArray(layoutManager.spanCount)
                val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(into)
                var lastPosition = lastVisibleItemPositions[0]
                for (value in into) {
                    if (value > lastPosition) {
                        lastPosition = value
                    }
                }
                lastPosition >= layoutManager.getItemCount() - 1
            }
            else -> false
        }
    }

    /**
     * 开始加载更多
     */
    fun startLoading() {
        setState(STATE_IS_LOADING)
    }

    /**
     * 完成一次加载更多
     */
    fun finishLoadMore() {
//        mStateType = STATE_NORMAL
        setState(STATE_NORMAL)
    }

    /**
     * 加载更多失败
     */
    fun loadMoreFailed() {
        setState(STATE_LOAD_FAILED)
    }

    /**
     * 没有更多数据了
     */
    fun noMoreData() {
        mNoMoreData = true
        setState(STATE_NO_MORE_DATA)
    }

    /**
     * 重置状态
     */
    fun resetNoMoreData() {
        mStateType = STATE_NORMAL
        mNoMoreData = false
    }

    /**
     * 设置底部LoadMoreViewHolder的状态
     */
    private fun setState(state: Int) {
        if (mStateType == state) return
        mStateType = state
        notifyLoadMoreViewHolder()
    }

    /**
     *
     */
    private fun notifyLoadMoreViewHolder() {
        if (itemCount <= 0) return
        this@LoadMoreAdapter.notifyItemChanged(itemCount - 1)
    }

    /**
     * 设置加载更多的item能填充满一横格
     */
    private fun setFullSpan(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager ?: return

        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = getItemViewType(position)
                    return if (viewType == VIEW_TYPE_LOAD_MORE) layoutManager.spanCount else 1
                }
            }
        }
    }


}