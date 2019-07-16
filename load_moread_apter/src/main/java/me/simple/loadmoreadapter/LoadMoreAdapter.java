package me.simple.loadmoreadapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
    public static final int STATE_LOADING = 0;
    public static final int STATE_LOAD_COMPLETE = 1;
    public static final int STATE_LOAD_FAILED = 2;
    public static final int STATE_NO_MORE_DATA = 3;
    private int stateType = STATE_LOADING;

    /**
     * 已无更多数据
     */
    private boolean noMoreData = false;

    /**
     *
     */
    private RecyclerView mRecyclerView;

    private AbsLoadMoreFooter mFooter;

    public static LoadMoreAdapter wrap(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        return new LoadMoreAdapter(adapter);
    }

    public static LoadMoreAdapter wrap(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, AbsLoadMoreFooter footer) {
        return new LoadMoreAdapter(adapter, footer);
    }

    private LoadMoreAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        this(adapter, new LoadMoreFooter());
    }

    private LoadMoreAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, AbsLoadMoreFooter footer) {
        if (adapter == null) {
            throw new NullPointerException("adapter can not be null");
        }
        this.adapter = adapter;
        this.mFooter = footer;
    }

    @Override
    public int getItemCount() {
        int count = adapter.getItemCount();
        return count > 0 ? count + 1 : 0;
    }

    @Override
    public long getItemId(int position) {
        if (position == 0) return adapter.getItemId(position);
        if (getItemViewType(position) == VIEW_TYPE_LOAD_MORE) return VIEW_TYPE_LOAD_MORE;
        return adapter.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= 0) return super.getItemViewType(position);
        if (position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log("onCreateViewHolder ---> " + viewType);
        if (viewType == VIEW_TYPE_LOAD_MORE) {
            final View footView = LayoutInflater.from(parent.getContext())
                    .inflate(mFooter.setLayoutRes(), parent, false);
            mFooter.onCreate(footView);
            return new LoadMoreViewHolder(footView, mFooter);
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
//        Log("onBindViewHolder ----> " + holder.getClass().getSimpleName() +
//                " ---- position == " + position);
        if (holder instanceof LoadMoreViewHolder) {
            final LoadMoreViewHolder loadMoreViewHolder = (LoadMoreViewHolder) holder;
            loadMoreViewHolder.setState(stateType);
            if (!mRecyclerView.canScrollVertically(-1) && onLoadMoreListener != null) {
                loadMoreViewHolder.setState(STATE_LOADING);
                onLoadMoreListener.onLoadMore(LoadMoreAdapter.this);
            }
        } else {
            adapter.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        adapter.registerAdapterDataObserver(dataObserver);
        recyclerView.addOnScrollListener(mOnScrollListener);

        adapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = null;
        adapter.unregisterAdapterDataObserver(dataObserver);
        recyclerView.removeOnScrollListener(mOnScrollListener);

        adapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof LoadMoreViewHolder) return;
        adapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof LoadMoreViewHolder) return;
        adapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof LoadMoreViewHolder) return;
        adapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof LoadMoreViewHolder) return false;
        return adapter.onFailedToRecycleView(holder);
    }

    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            Log("onChanged");
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
        this.stateType = STATE_LOAD_COMPLETE;
        notifyLoadMoreVH();
    }

    private void loading() {
        this.stateType = STATE_LOADING;
        notifyLoadMoreVH();
    }

    public void loadFailed() {
        this.stateType = STATE_LOAD_FAILED;
        notifyLoadMoreVH();
    }

    public void noMoreData() {
        this.stateType = STATE_NO_MORE_DATA;
        noMoreData = true;
        notifyLoadMoreVH();
    }

    public void resetNoMoreData() {
        noMoreData = false;
    }

    private void notifyLoadMoreVH() {
        if (getItemCount() <= 0) return;
        LoadMoreAdapter.this.notifyItemChanged(getItemCount() - 1);
    }

    private void Log(String text) {
        Log.d("LoadMoreAdapter", text);
    }
}
