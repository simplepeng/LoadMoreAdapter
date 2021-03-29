package me.simple.loadmoreadapter.demo

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import me.simple.loadmoreadapter.LoadMoreAdapter
import me.simple.loadmoreadapter.LoadMoreAdapter.Companion.wrap
import me.simple.loadmoreadapter.LoadMoreAdapter.OnFailedClickListener
import me.simple.loadmoreadapter.LoadMoreAdapter.OnLoadMoreListener

class LinearFragment : Fragment() {
    var mItems = Items()
    var mAdapter = MultiTypeAdapter(mItems)
    var loadMoreAdapter: LoadMoreAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter.register(String::class.java, LinearItemBinder())
        val rv: RecyclerView = view.findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(activity)
        loadMoreAdapter = wrap(mAdapter)
                .setLoadMoreListener(object : OnLoadMoreListener {
                    override fun onLoadMore(adapter: LoadMoreAdapter?) {
                        data
                    }
                })
                .setOnFailedClickListener(object : OnFailedClickListener {
                    override fun onClick(adapter: LoadMoreAdapter?, view: View?) {
                        Toast.makeText(context, "onFailedClick", Toast.LENGTH_SHORT).show()
                        mItems.add("1")
                        mItems.add("2")
                        mItems.add("3")
                        mItems.add("4")
                        mItems.add("5")
                        mItems.add("6")
                        mAdapter.notifyItemRangeInserted(mItems.size - 6, 6)
                    }
                })
        rv.adapter = loadMoreAdapter
        data
    }
    //                mItems.add("Kotlin");
//                mItems.add("Rust");
//                mItems.add("C");
//                mAdapter.notifyItemRangeInserted(mItems.size() - 6, 6);

    //                if (mItems.size() >= 5) {
//                    loadMoreAdapter.noMoreData();
//                }
    private val data: Unit
        private get() {
            if (mItems.size > 10 && mItems.size < 15) {
                loadMoreAdapter!!.loadFailed()
                return
            }
            if (mItems.size >= 5) {
                loadMoreAdapter!!.noMoreData()
                return
            }
            Handler().postDelayed({
                mItems.add("Java")
                mItems.add("C++")
                mItems.add("Python")
                mAdapter.notifyDataSetChanged()
                //                mItems.add("Kotlin");
//                mItems.add("Rust");
//                mItems.add("C");
//                mAdapter.notifyItemRangeInserted(mItems.size() - 6, 6);

//                if (mItems.size() >= 5) {
//                    loadMoreAdapter.noMoreData();
//                }
            }, 1500)
        }
}