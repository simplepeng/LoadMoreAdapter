package me.simple.loadmoreadapter.demo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.drakeet.multitype.ItemViewBinder;

public class GridItemBinder extends ItemViewBinder<String, GridItemBinder.VH> {

    @NonNull
    @Override
    protected VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new VH(inflater.inflate(R.layout.item_grid, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull VH holder, @NonNull String item) {
        holder.textView.setText(String.format("%s", holder.getAdapterPosition() + 1));
    }

    class VH extends RecyclerView.ViewHolder {

        TextView textView;

        public VH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
