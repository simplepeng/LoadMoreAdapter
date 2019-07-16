package me.simple.loadmoreadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadMoreVH extends RecyclerView.ViewHolder implements ILoadMore {

    ProgressBar pb;
    TextView tv;

    public LoadMoreVH(View itemView) {
        super(itemView);
        pb = itemView.findViewById(R.id.pb);
        tv = itemView.findViewById(R.id.tv);
    }

    public void setState(int stateType) {
        switch (stateType) {
            case LoadMoreAdapter.STATE_LOADING:
                loading();
                break;
            case LoadMoreAdapter.STATE_LOAD_COMPLETE:
                loadComplete();
                break;
            case LoadMoreAdapter.STATE_LOAD_FAILED:
                loadFailed();
                break;
            case LoadMoreAdapter.STATE_NO_MORE_DATA:
                noMoreData();
                break;
        }
    }

    @Override
    public void loading() {
        pb.setVisibility(View.VISIBLE);
        tv.setVisibility(View.GONE);
    }

    @Override
    public void loadComplete() {
        pb.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);

        tv.setText("加载完成");
    }

    @Override
    public void noMoreData() {
        pb.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);

        tv.setText("已无更多数据");
    }

    @Override
    public void loadFailed() {
        pb.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);

        tv.setText("加载失败");
    }
}
