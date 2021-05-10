package me.simple.loadmoreadapter.demo

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import me.simple.loadmoreadapter.LoadMoreAdapter

class StaggeredFragment : Fragment() {

    var mItems = Items()
    var mAdapter = MultiTypeAdapter(mItems)
    var loadMoreAdapter: LoadMoreAdapter<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter.register(String::class.java, GridItemBinder())

        val rv: RecyclerView = view.findViewById(R.id.rv)
        val refreshLayout: SwipeRefreshLayout = view.findViewById(R.id.refreshLayout)

        rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        loadMoreAdapter = LoadMoreAdapter.wrap(mAdapter).setOnLoadMoreListener {
            getData()
        }
        rv.adapter = loadMoreAdapter

        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false

            mItems.clear()
            mAdapter.notifyDataSetChanged()

            loadMoreAdapter?.resetNoMoreData()
            getData()
        }

        getData()
    }

    private fun getData() {
        view?.postDelayed({
            val item = mutableListOf<String>()
            item.add("")
            item.add("")
            item.add("")
            item.add("")
            item.add("")
            mItems.addAll(item)

            loadMoreAdapter?.finishLoadMore()
            mAdapter.notifyItemRangeInserted(mItems.size - item.size, item.size)
//            mAdapter.notifyDataSetChanged()

            if (mItems.size >= 30) {
                loadMoreAdapter?.noMoreData()
            }
        }, 1500)
    }

}