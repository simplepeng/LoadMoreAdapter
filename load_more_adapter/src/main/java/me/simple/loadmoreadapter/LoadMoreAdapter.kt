package me.simple.loadmoreadapter

import android.database.Observable
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

class LoadMoreAdapter private constructor(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?, footer: ILoadMoreFooter = LoadMoreFooter()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     *
     */
    val realAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    /**
     *
     */
    private val VIEW_TYPE_LOAD_MORE = 1112

    /**
     *
     */
    private var mOnLoadMoreListener: OnLoadMoreListener? = null

    /**
     *
     */
    private var mOnFailedClickListener: OnFailedClickListener? = null

    /**
     * 是否为上拉
     */
    private var mIsScrollLoadMore = false
    private var mStateType = STATE_LOADING

    /**
     * 已无更多数据
     */
    private var mNoMoreData = false

    /**
     *
     */
    private var mRecyclerView: RecyclerView? = null

    /**
     *
     */
    private val mFooter: ILoadMoreFooter
    override fun getItemCount(): Int {
        val count = realAdapter.itemCount
        return if (count > 0) count + 1 else 0
    }

    override fun getItemId(position: Int): Long {
        if (position == 0) return realAdapter.getItemId(position)
        return if (getItemViewType(position) == VIEW_TYPE_LOAD_MORE) VIEW_TYPE_LOAD_MORE.toLong() else realAdapter.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        if (position <= 0) return super.getItemViewType(position)
        return if (position == itemCount - 1) {
            VIEW_TYPE_LOAD_MORE
        } else realAdapter.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_LOAD_MORE) {
            val footView = LayoutInflater.from(parent.context)
                    .inflate(mFooter.setLayoutRes(), parent, false)
            mFooter.onCreate(footView)
            return LoadMoreViewHolder(footView, mFooter)
        }
        return realAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, emptyList())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        if (holder is LoadMoreViewHolder) {
            val loadMoreVH = holder

            //首次如果itemView没有填充满RecyclerView，继续加载更多
            if (!mRecyclerView!!.canScrollVertically(-1)
                    && mOnLoadMoreListener != null && !mNoMoreData) {
                //fix bug Cannot call this method while RecyclerView is computing a layout or scrolling
                mRecyclerView!!.post {
                    mStateType = STATE_LOADING
                    loadMoreVH.setState(mStateType)
                    mOnLoadMoreListener!!.onLoadMore(this@LoadMoreAdapter)
                }
            }

            //加载失败点击事件
            holder.itemView.setOnClickListener {
                if (mStateType == STATE_LOAD_FAILED && mOnFailedClickListener != null) {
                    mStateType = STATE_LOADING
                    mOnFailedClickListener!!.onClick(this@LoadMoreAdapter, holder.itemView)
                }
            }

            //更新状态
            loadMoreVH.setState(mStateType)
        } else {
            realAdapter.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView
        setFullSpan(recyclerView)
        if (!isRegistered) {
            realAdapter.registerAdapterDataObserver(dataObserver)
        }
        recyclerView.addOnScrollListener(mOnScrollListener)
        realAdapter.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = null
        if (isRegistered) {
            realAdapter.unregisterAdapterDataObserver(dataObserver)
        }
        recyclerView.removeOnScrollListener(mOnScrollListener)
        realAdapter.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        if (holder is LoadMoreViewHolder) return
        realAdapter.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        if (holder is LoadMoreViewHolder) return
        realAdapter.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is LoadMoreViewHolder) return
        realAdapter.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return if (holder is LoadMoreViewHolder) false else realAdapter.onFailedToRecycleView(holder)
    }

    private val dataObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            this@LoadMoreAdapter.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            this@LoadMoreAdapter.notifyItemRangeChanged(positionStart, itemCount, payload)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            this@LoadMoreAdapter.notifyItemRangeChanged(fromPosition, toPosition, itemCount)
        }
    }
    private val isRegistered: Boolean
        private get() {
            var isRegistered = false
            try {
                val clazz: Class<out RecyclerView.Adapter<*>?> = RecyclerView.Adapter::class.java
                val field = clazz.getDeclaredField("mObservable")
                field.isAccessible = true
                val observable = field[realAdapter] as Observable<*>
                val observersField = Observable::class.java.getDeclaredField("mObservers")
                observersField.isAccessible = true
                val list = observersField[observable] as ArrayList<Any>
                isRegistered = list.contains(dataObserver)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return isRegistered
        }

    interface OnLoadMoreListener {
        fun onLoadMore(adapter: LoadMoreAdapter?)
    }

    interface OnFailedClickListener {
        fun onClick(adapter: LoadMoreAdapter?, view: View?)
    }

    fun setLoadMoreListener(listener: OnLoadMoreListener?): LoadMoreAdapter {
        mOnLoadMoreListener = listener
        return this
    }

    fun setOnFailedClickListener(listener: OnFailedClickListener?): LoadMoreAdapter {
        mOnFailedClickListener = listener
        return this
    }

    private val mOnScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState != RecyclerView.SCROLL_STATE_IDLE || mOnLoadMoreListener == null || mNoMoreData
                    || !mIsScrollLoadMore) return
            if (canLoadMore(recyclerView.layoutManager)) {
                loadingMore()
                mOnLoadMoreListener!!.onLoadMore(this@LoadMoreAdapter)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            mIsScrollLoadMore = dy > 0
        }
    }

    private fun canLoadMore(layoutManager: RecyclerView.LayoutManager?): Boolean {
        var canLoadMore = false
        if (layoutManager is GridLayoutManager) {
            canLoadMore = layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1
        } else if (layoutManager is LinearLayoutManager) {
            canLoadMore = layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val sgLayoutManager = layoutManager
            val into = IntArray(sgLayoutManager.spanCount)
            val lastVisibleItemPositions = sgLayoutManager.findLastVisibleItemPositions(into)
            var lastPosition = lastVisibleItemPositions[0]
            for (value in into) {
                if (value > lastPosition) {
                    lastPosition = value
                }
            }
            canLoadMore = lastPosition >= layoutManager.getItemCount() - 1
        }
        return canLoadMore
    }

    private fun loadingMore() {
        setState(STATE_LOADING)
    }

    fun loadFailed() {
        setState(STATE_LOAD_FAILED)
    }

    fun noMoreData() {
        mNoMoreData = true
        setState(STATE_NO_MORE_DATA)
    }

    private fun setState(state: Int) {
        if (mStateType == state) return
        mStateType = state
        notifyLoadMoreVH()
    }

    fun resetNoMoreData() {
        mNoMoreData = false
    }

    private fun notifyLoadMoreVH() {
        if (itemCount <= 0) return
        this@LoadMoreAdapter.notifyItemChanged(itemCount - 1)
    }

    private fun setFullSpan(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager ?: return
        if (layoutManager is GridLayoutManager) {
            val gm = layoutManager
            gm.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = getItemViewType(position)
                    return if (viewType == VIEW_TYPE_LOAD_MORE) gm.spanCount else 1
                }
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val sgm = layoutManager
        }
    }

    private fun Log(text: String) {
        android.util.Log.d("LoadMoreAdapter", text)
    }

    companion object {
        /**
         * footer的状态
         */
        const val STATE_LOADING = 0

        //    static final int STATE_LOAD_COMPLETE = 1;
        const val STATE_LOAD_FAILED = 2
        const val STATE_NO_MORE_DATA = 3
        fun wrap(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?): LoadMoreAdapter {
            return LoadMoreAdapter(adapter)
        }

        @JvmStatic
        fun wrap(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?, footer: ILoadMoreFooter): LoadMoreAdapter {
            return LoadMoreAdapter(adapter, footer)
        }
    }

    init {
        if (adapter == null) {
            throw NullPointerException("mAdapter can not be null")
        }
        realAdapter = adapter
        mFooter = footer
    }
}