package me.simple.loadmoreadapter.demo;

import android.view.View;
import android.widget.TextView;

import me.simple.loadmoreadapter.AbsLoadMoreFooter;

public class CustomFooter extends AbsLoadMoreFooter {

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
    public void loadComplete() {
        mTextView.setText("加载完成，上划加载等多");
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