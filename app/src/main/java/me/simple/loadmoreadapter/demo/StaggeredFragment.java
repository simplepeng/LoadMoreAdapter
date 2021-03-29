package me.simple.loadmoreadapter.demo;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.simple.loadmoreadapter.LoadMoreAdapter;

public class StaggeredFragment extends Fragment {

    Items mItems = new Items();
    MultiTypeAdapter mAdapter = new MultiTypeAdapter(mItems);
    LoadMoreAdapter loadMoreAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter.register(String.class, new GridItemBinder());

        RecyclerView rv = view.findViewById(R.id.rv);
        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);

        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        loadMoreAdapter = LoadMoreAdapter.wrap(mAdapter)
                .setLoadMoreListener(new LoadMoreAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(LoadMoreAdapter adapter) {
                        getData();
                    }
                });
        rv.setAdapter(loadMoreAdapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoreAdapter.resetNoMoreData();
                mItems.clear();
                getData();
            }
        });

        getData();
    }

    private void getData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                loadMoreAdapter.loadComplete();

                mItems.add("");
                mItems.add("");
                mItems.add("");
                mItems.add("");
                mItems.add("");
                mAdapter.notifyItemRangeInserted(mItems.size() - 5, 5);

                if (mItems.size() >= 30) {
                    loadMoreAdapter.noMoreData();
                }
            }
        }, 1500);
    }
}
