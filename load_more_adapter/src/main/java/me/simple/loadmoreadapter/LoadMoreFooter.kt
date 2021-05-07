package me.simple.loadmoreadapter

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

/**
 * 默认的底部加载布局
 */
internal class LoadMoreFooter : ILoadMoreFooter {

    override fun setLayoutRes() =  R.layout.adapter_load_more

    override fun onCreate(footerView: View) {
    }

    override fun loading(footerView: View) {
        footerView.findViewById<ProgressBar>(R.id.load_more_pb)?.apply {
            visibility = View.VISIBLE
        }
        footerView.findViewById<TextView>(R.id.load_more_tv)?.apply {
            visibility = View.GONE
        }
    }

    override fun noMoreData(footerView: View) {
        footerView.findViewById<ProgressBar>(R.id.load_more_pb)?.apply {
            visibility = View.GONE
        }
        footerView.findViewById<TextView>(R.id.load_more_tv)?.apply {
            visibility = View.VISIBLE
            text = "已无更多数据"
        }
    }

    override fun loadFailed(footerView: View) {
        footerView.findViewById<ProgressBar>(R.id.load_more_pb)?.apply {
            visibility = View.GONE
        }
        footerView.findViewById<TextView>(R.id.load_more_tv)?.apply {
            visibility = View.VISIBLE
            text = "加载失败"
        }
    }
}