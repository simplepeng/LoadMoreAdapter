package me.simple.loadmoreadapter.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.drakeet.multitype.MultiTypeAdapter
import me.simple.loadmoreadapter.LoadMoreAdapter

class LinearFragment : Fragment() {

    var mItems = mutableListOf<Any>()
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
            initData()
        }

        mAdapter.register(String::class, LinearItemBinder())

        val rv: RecyclerView = view.findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(activity)
        loadMoreAdapter = LoadMoreAdapter.wrap(mAdapter)


        loadMoreAdapter?.setOnLoadMoreListener {
            addItems()
            count++
        }
        loadMoreAdapter?.setOnFailedClickListener { adapter, view ->
            failedClick()
            count++
        }
        rv.adapter = loadMoreAdapter

        initData()
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

    private fun initData() {
        view?.postDelayed({
            mItems.add("Java")
            mItems.add("Kotlin")
            mAdapter.notifyDataSetChanged()
        }, 1000)
    }

    private fun addItems() {
        if (mItems.size >= 25) {
            loadMoreAdapter!!.noMoreData()
            return
        }

        view?.postDelayed({
            val item = mutableListOf<String>()
            item.add("Java")
            item.add("C++")
//            item.add("Python")
            mItems.addAll(item)

            loadMoreAdapter?.finishLoadMore()
//            mAdapter.notifyDataSetChanged()
            mAdapter.notifyItemRangeChanged(mItems.size - item.size, item.size)
        }, 1000)
    }
}