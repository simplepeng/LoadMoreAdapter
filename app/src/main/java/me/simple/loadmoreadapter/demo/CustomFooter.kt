package me.simple.loadmoreadapter.demo

import android.view.View
import android.widget.TextView
import me.simple.loadmoreadapter.ILoadMoreFooter

class CustomFooter : ILoadMoreFooter {

    private var mTextView: TextView? = null

    override fun setLayoutRes(): Int {
        return R.layout.footer_custom
    }

    override fun onCreate(footerView: View) {
        mTextView = footerView.findViewById(R.id.tv_custom)
    }

    override fun loading(footerView: View) {
        mTextView!!.text = "加载更多中..."
    }

    override fun noMoreData(footerView: View) {
        mTextView!!.text = "我是有底线的"
    }

    override fun loadFailed(footerView: View) {
        mTextView!!.text = "服务器开了小差"
    }
}