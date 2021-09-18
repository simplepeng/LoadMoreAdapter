package me.simple.loadmoreadapter.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.drakeet.multitype.MultiTypeAdapter
import me.simple.loadmoreadapter.LoadMoreAdapter

class GridFragment : Fragment() {

    var mItems = mutableListOf<Any>()
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

        mAdapter.register(String::class, GridItemBinder())

        val rv: RecyclerView = view.findViewById(R.id.rv)
        rv.layoutManager = GridLayoutManager(activity, 3)

        loadMoreAdapter = LoadMoreAdapter.wrap(mAdapter, CustomFooter())
            .setOnLoadMoreListener {
                addData()
            }
        rv.adapter = loadMoreAdapter

//        val refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
//        refreshLayout.setOnRefreshListener {
//            refreshLayout.isRefreshing = false
//
//            mItems.clear()
//            mAdapter.notifyDataSetChanged()
//
//            loadMoreAdapter?.resetNoMoreData()
//            addData()
//        }

        addData()
    }

    private fun addData() {
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

            if (mItems.size > 25) {
                loadMoreAdapter!!.noMoreData()
            }
        }, 1000)
    }
}