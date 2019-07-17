package me.simple.loadmoreadapter;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

class LoadMoreFooter extends AbsLoadMoreFooter {

    private ProgressBar mProgressBar;
    private TextView mTextView;

    @Override
    public int setLayoutRes() {
        return R.layout.adapter_load_more;
    }

    @Override
    public void onCreate(View footerView) {
        mProgressBar = footerView.findViewById(R.id.load_more_pb);
        mTextView = footerView.findViewById(R.id.load_more_tv);
    }

    @Override
    public void loading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);
    }

    @Override
    public void loadComplete() {
        mProgressBar.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText("加载完成");
    }

    @Override
    public void noMoreData() {
        mProgressBar.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText("已无更多数据");
    }

    @Override
    public void loadFailed() {
        mProgressBar.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText("加载失败");
    }

}
