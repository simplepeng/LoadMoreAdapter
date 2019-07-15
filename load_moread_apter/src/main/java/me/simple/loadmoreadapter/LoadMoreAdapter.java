package me.simple.loadmoreadapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private int VIEW_TYPE_LOAD_MORE = 1111;
    /**
     *
     */
    private OnLoadMoreListener onLoadMoreListener;

    /**
     * 是否为上拉
     */
    private boolean isScrollLoadMore = false;

    /**
     *
     */
    public static final int TYPE_LOADING = 0;
    public static final int TYPE_LOAD_COMPLETE = 1;
    public static final int TYPE_LOAD_FAILED = 2;
    public static final int TYPE_NO_MORE_DATA = 3;
    private int stateType = TYPE_LOADING;
    /**
     *
     */
    private boolean noMoreData = false;

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
            return VIEW_TYPE_LOAD_MORE;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE) {
            return new LoadMoreVH(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_load_more, parent, false));
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        Log.d("LoadMoreAdapter", "onBindViewHolder ----> " + holder.getClass().getSimpleName());
        if (getItemViewType(position) == VIEW_TYPE_LOAD_MORE) {
            ((LoadMoreVH) holder).setState(stateType);
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
        adapter.registerAdapterDataObserver(dataObserver);
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        adapter.unregisterAdapterDataObserver(dataObserver);
        recyclerView.removeOnScrollListener(mOnScrollListener);
    }

    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            LoadMoreAdapter.this.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            LoadMoreAdapter.this.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            LoadMoreAdapter.this.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            LoadMoreAdapter.this.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            LoadMoreAdapter.this.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            LoadMoreAdapter.this.notifyItemRangeChanged(fromPosition, toPosition, itemCount);
        }
    };

    public interface OnLoadMoreListener {
        void onLoadMore(LoadMoreAdapter adapter);
    }

    public LoadMoreAdapter addLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
        return this;
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

            if (newState != RecyclerView.SCROLL_STATE_IDLE
                    || onLoadMoreListener == null
                    || noMoreData
                    || !isScrollLoadMore) return;

            if (canLoadMore(recyclerView.getLayoutManager())) {
                loading();
                Log.d(LoadMoreAdapter.this.getClass().getSimpleName(), "loading");
                onLoadMoreListener.onLoadMore(LoadMoreAdapter.this);
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            isScrollLoadMore = dy > 0;
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

    public RecyclerView.Adapter<RecyclerView.ViewHolder> getRealAdapter() {
        return adapter;
    }

    public void loadComplete() {
        this.stateType = TYPE_LOAD_COMPLETE;
        notifyLoadMoreVH();
    }

    private void loading() {
        this.stateType = TYPE_LOADING;
        notifyLoadMoreVH();
    }

    public void loadFailed() {
        this.stateType = TYPE_LOAD_FAILED;
        notifyLoadMoreVH();
    }

    public void noMoreData() {
        this.stateType = TYPE_NO_MORE_DATA;
        noMoreData = true;
        notifyLoadMoreVH();
    }

    public void resetNoMoreData() {
        noMoreData = false;
    }

    private void notifyLoadMoreVH() {
        notifyItemChanged(getItemCount() - 1);
    }
}
