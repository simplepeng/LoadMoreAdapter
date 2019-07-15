package me.simple.loadmoreadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private int TYPE_LOAD_MORE = 1111;
    /**
     *
     */
    private OnLoadMoreListener onLoadMoreListener;

    /**
     * 是否上拉
     */
    public boolean isScrollLoadMore = false;

    public static LoadMoreAdapter wrap(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        return new LoadMoreAdapter(adapter);
    }

    private LoadMoreAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter can not be null");
        }
        this.adapter = adapter;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_LOAD_MORE;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOAD_MORE) {
            return new BaseLoadMoreVH(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_load_more, parent, false));
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseLoadMoreVH) {

        } else {
            adapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (holder instanceof BaseLoadMoreVH) {

        } else {
            adapter.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        int count = adapter.getItemCount();
        return count + 1;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(mOnScrollListener);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void addLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState != RecyclerView.SCROLL_STATE_IDLE || !isScrollLoadMore
                    || onLoadMoreListener == null) return;

            if (canLoadMore(recyclerView.getLayoutManager())) {
                onLoadMoreListener.onLoadMore();
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollLoadMore = dx > 0;
        }
    };

    private boolean canLoadMore(final RecyclerView.LayoutManager layoutManager) {
        boolean canLoadMore = false;
        if (layoutManager instanceof GridLayoutManager) {
            canLoadMore = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1;
        } else if (layoutManager instanceof LinearLayoutManager) {
            canLoadMore = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager sgLayoutManager = ((StaggeredGridLayoutManager) layoutManager);
            int[] into = new int[sgLayoutManager.getSpanCount()];
            int[] lastVisibleItemPositions = sgLayoutManager.findLastVisibleItemPositions(into);
            int lastPosition = lastVisibleItemPositions[0];
            for (int value : into) {
                if (value > lastPosition) {
                    lastPosition = value;
                }
            }
            canLoadMore = lastPosition >= layoutManager.getItemCount() - 1;
        }
        return canLoadMore;
    }
}
