package me.simple.loadmoreadapter.demo

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import me.simple.loadmoreadapter.LoadMoreAdapter

class LinearFragment : Fragment() {

    var mItems = Items()
    var mAdapter = MultiTypeAdapter(mItems)
    var loadMoreAdapter: LoadMoreAdapter<*>? = null
    var count = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
            mItems.clear()
            mAdapter.notifyDataSetChanged()

            loadMoreAdapter?.resetNoMoreData()
            count = 1
            getData()
        }

        mAdapter.register(String::class.java, LinearItemBinder())

        val rv: RecyclerView = view.findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(activity)
        loadMoreAdapter = LoadMoreAdapter.wrap(mAdapter)


        loadMoreAdapter?.setOnLoadMoreListener {
//            if (count == 2) {
//                loadMoreAdapter?.loadMoreFailed()
//                return@setOnLoadMoreListener
//            }

            getData()
//            Log.d("LinearFragment", count.toString())
            count++
        }
        loadMoreAdapter?.setOnFailedClickListener { adapter, view ->
            failedClick()
            count++
        }
        rv.adapter = loadMoreAdapter

        getData()
    }

    private fun failedClick() {
        Toast.makeText(context, "onFailedClick", Toast.LENGTH_SHORT).show()
        view?.postDelayed({
            mItems.add("1")
            mItems.add("2")
            mItems.add("3")
            mItems.add("4")
            mItems.add("5")
            mItems.add("6")

            loadMoreAdapter?.finishLoadMore()
            mAdapter.notifyItemRangeInserted(mItems.size - 6, 6)
        }, 1500)
    }

    private fun getData() {
        if (mItems.size >= 15) {
            loadMoreAdapter!!.noMoreData()
            return
        }

        view?.postDelayed({
            val item = mutableListOf<String>()
            item.add("Java")
            item.add("C++")
            item.add("Python")
            mItems.addAll(item)

            loadMoreAdapter?.finishLoadMore()
//            mAdapter.notifyDataSetChanged()
            mAdapter.notifyItemRangeChanged(mItems.size - item.size, item.size)
        }, 1500)
    }
}