package me.simple.loadmoreadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private AbsLoadMoreVH loadMoreVH;
    private int TYPE_LOAD_MORE = 1111;

    public LoadMoreAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter can not be null");
        }
        this.adapter = adapter;
    }

    public LoadMoreAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, AbsLoadMoreVH loadMoreVH) {
        if (adapter == null) {
            throw new NullPointerException("adapter can not be null");
        }
        this.adapter = adapter;
        this.loadMoreVH = loadMoreVH;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOAD_MORE) {
            return loadMoreVH == null ? new BaseLoadMoreVH(parent.getContext(), R.layout.adapter_load_more, parent) : loadMoreVH;
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {

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

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
}
