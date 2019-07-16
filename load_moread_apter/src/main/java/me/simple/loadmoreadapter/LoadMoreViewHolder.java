package me.simple.loadmoreadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadMoreViewHolder extends RecyclerView.ViewHolder implements ILoadMore {

    private AbsLoadMoreFooter mFooter;

    public LoadMoreViewHolder(View itemView, AbsLoadMoreFooter mFooter) {
        super(itemView);
        this.mFooter = mFooter;
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
        mFooter.loading();
    }

    @Override
    public void loadComplete() {
        mFooter.loadComplete();
    }

    @Override
    public void noMoreData() {
        mFooter.noMoreData();
    }

    @Override
    public void loadFailed() {
        mFooter.loadFailed();
    }
}
