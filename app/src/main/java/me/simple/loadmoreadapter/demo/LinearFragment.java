package me.simple.loadmoreadapter.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class LinearFragment extends Fragment {

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
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(mAdapter);

        addData();
    }

    private void addData() {
        mItems.add("Java");
        mItems.add("Kotlin");
        mItems.add("C++");
        mItems.add("Go");
        mItems.add("Ruby");
        mItems.add("Python");
        mItems.add("Rust");
        mAdapter.notifyItemRangeInserted(mItems.size() - 7, 7);
    }
}
