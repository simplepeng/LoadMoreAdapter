package me.simple.loadmoreadapter.demo

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import me.simple.loadmoreadapter.LoadMoreAdapter

class GridFragment : Fragment() {

    var mItems = Items()
    var mAdapter = MultiTypeAdapter(mItems)
    var loadMoreAdapter: LoadMoreAdapter? = null

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
        rv.layoutManager = GridLayoutManager(activity, 3)
        loadMoreAdapter = LoadMoreAdapter.wrap(mAdapter, CustomFooter())
            .setOnLoadMoreListener {
                addData()
            }
        rv.adapter = loadMoreAdapter
        addData()
    }

    private fun addData() {
        Handler().postDelayed({
            mItems.add("")
            mItems.add("")
            mItems.add("")
            mItems.add("")
            mItems.add("")
            mItems.add("")
            mAdapter.notifyItemRangeInserted(mItems.size - 5, 5)
            if (mItems.size > 25) {
                loadMoreAdapter!!.noMoreData()
            }
        }, 1000)
    }
}