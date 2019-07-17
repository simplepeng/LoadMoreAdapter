package me.simple.loadmoreadapter.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.simple.loadmoreadapter.LoadMoreAdapter;

public class GridFragment extends Fragment {

    Items mItems = new Items();
    MultiTypeAdapter mAdapter = new MultiTypeAdapter(mItems);

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
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        LoadMoreAdapter loadMoreAdapter = LoadMoreAdapter.wrap(mAdapter)
                .setLoadMoreListener(new LoadMoreAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(LoadMoreAdapter adapter) {
                        addData(adapter);
                    }
                });
        rv.setAdapter(loadMoreAdapter);

        addData(loadMoreAdapter);
    }

    private void addData(final LoadMoreAdapter adapter) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                adapter.loadComplete();

                mItems.add("1");
                mItems.add("2");
                mItems.add("3");
                mItems.add("4");
                mItems.add("5");
//                mItems.add("Java");
//                mItems.add("Kotlin");
//                mItems.add("C++");
//                mItems.add("Go");
//                mItems.add("Ruby");
//                mAdapter.notifyDataSetChanged();
//                mAdapter.notifyItemInserted(0);
                mAdapter.notifyItemRangeInserted(mItems.size() - 5, 5);

                if (mItems.size() > 80) {
                    adapter.noMoreData();
                }
            }
        }, 1500);
    }
}
