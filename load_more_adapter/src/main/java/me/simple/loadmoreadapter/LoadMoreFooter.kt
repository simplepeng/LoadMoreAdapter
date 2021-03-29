package me.simple.loadmoreadapter

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

/**
 * 默认的底部加载布局
 */
internal class LoadMoreFooter : ILoadMoreFooter {

    private var mProgressBar: ProgressBar? = null
    private var mTextView: TextView? = null

    override fun setLayoutRes(): Int {
        return R.layout.adapter_load_more
    }

    override fun onCreate(footerView: View) {
        mProgressBar = footerView.findViewById(R.id.load_more_pb)
        mTextView = footerView.findViewById(R.id.load_more_tv)
    }

    override fun loading() {
        mProgressBar!!.visibility = View.VISIBLE
        mTextView!!.visibility = View.GONE
    }

    override fun noMoreData() {
        mProgressBar!!.visibility = View.GONE
        mTextView!!.visibility = View.VISIBLE
        mTextView!!.text = "已无更多数据"
    }

    override fun loadFailed() {
        mProgressBar!!.visibility = View.GONE
        mTextView!!.visibility = View.VISIBLE
        mTextView!!.text = "加载失败"
    }
}