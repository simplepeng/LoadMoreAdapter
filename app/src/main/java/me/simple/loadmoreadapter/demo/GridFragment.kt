package me.simple.loadmoreadapter.demo;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.simple.loadmoreadapter.LoadMoreAdapter;

public class GridFragment extends Fragment {

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
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        loadMoreAdapter = LoadMoreAdapter.wrap(mAdapter, new CustomFooter())
                .setLoadMoreListener(new LoadMoreAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(LoadMoreAdapter adapter) {
                        addData();
                    }
                })
                .setOnFailedClickListener(new LoadMoreAdapter.OnFailedClickListener() {
                    @Override
                    public void onClick(LoadMoreAdapter adapter, View view) {

                    }
                });
        rv.setAdapter(loadMoreAdapter);

        addData();
    }

    private void addData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                mItems.add("");
                mItems.add("");
                mItems.add("");
                mItems.add("");
                mItems.add("");
                mItems.add("");
                mAdapter.notifyItemRangeInserted(mItems.size() - 5, 5);

                if (mItems.size() > 25) {
                    loadMoreAdapter.noMoreData();
                }
            }
        }, 1000);
    }
}
