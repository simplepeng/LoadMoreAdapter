package me.simple.loadmoreadapter.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.simple.loadmoreadapter.LoadMoreAdapter;

public class LinearFragment extends Fragment {

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

        mAdapter.register(String.class, new LinearItemBinder());

        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadMoreAdapter = LoadMoreAdapter.wrap(mAdapter)
                .setLoadMoreListener(new LoadMoreAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(LoadMoreAdapter adapter) {
                        getData();
                    }
                })
                .setOnFailedClickListener(new LoadMoreAdapter.OnFailedClickListener() {
                    @Override
                    public void onClick(LoadMoreAdapter adapter, View view) {
                        Toast.makeText(getContext(), "onFailedClick", Toast.LENGTH_SHORT).show();
                        mItems.add("1");
                        mItems.add("2");
                        mItems.add("3");
                        mItems.add("4");
                        mItems.add("5");
                        mItems.add("6");
                        mAdapter.notifyItemRangeInserted(mItems.size() - 6, 6);
                    }
                });
        rv.setAdapter(loadMoreAdapter);

        getData();
    }

    private void getData() {
        if (mItems.size() > 10 && mItems.size() < 15) {
            loadMoreAdapter.loadFailed();
            return;
        }

        if (mItems.size() >= 5) {
            loadMoreAdapter.noMoreData();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mItems.add("Java");
                mItems.add("C++");
                mItems.add("Python");
                mAdapter.notifyDataSetChanged();
//                mItems.add("Kotlin");
//                mItems.add("Rust");
//                mItems.add("C");
//                mAdapter.notifyItemRangeInserted(mItems.size() - 6, 6);

//                if (mItems.size() >= 5) {
//                    loadMoreAdapter.noMoreData();
//                }
            }
        }, 1500);
    }
}
