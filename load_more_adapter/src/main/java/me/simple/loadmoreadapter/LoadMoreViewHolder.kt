package me.simple.loadmoreadapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 底部加载更多的ViewHolder
 */
class LoadMoreViewHolder extends RecyclerView.ViewHolder implements ILoadMore {

    private final ILoadMoreFooter mFooter;

    LoadMoreViewHolder(View itemView, ILoadMoreFooter mFooter) {
        super(itemView);
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
        }
        this.mFooter = mFooter;
    }

    void setState(int stateType) {
        switch (stateType) {
            case LoadMoreAdapter.STATE_LOADING:
                loading();
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
    public void noMoreData() {
        mFooter.noMoreData();
    }

    @Override
    public void loadFailed() {
        mFooter.loadFailed();
    }
}
