package me.simple.loadmoreadapter.demo;

import android.view.View;
import android.widget.TextView;

import me.simple.loadmoreadapter.ILoadMoreFooter;


public class CustomFooter implements ILoadMoreFooter {

    private TextView mTextView;

    @Override
    public int setLayoutRes() {
        return R.layout.footer_custom;
    }

    @Override
    public void onCreate(View footerView) {
        mTextView = footerView.findViewById(R.id.tv_custom);
    }

    @Override
    public void loading() {
        mTextView.setText("加载更多中...");
    }

    @Override
    public void noMoreData() {
        mTextView.setText("我是有底线的");
    }

    @Override
    public void loadFailed() {
        mTextView.setText("服务器开了小差");
    }
}
