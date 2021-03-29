package me.simple.loadmoreadapter.demo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.drakeet.multitype.ItemViewBinder;

public class LinearItemBinder extends ItemViewBinder<String, LinearItemBinder.VH> {

    @NonNull
    @Override
    protected VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new VH(inflater.inflate(R.layout.item_linear, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull VH holder, @NonNull String item) {
        holder.textView.setText(String.format("%s ----- %s", item, holder.getAdapterPosition() + 1));
    }

    class VH extends RecyclerView.ViewHolder {

        TextView textView;

        public VH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_linear);
        }
    }
}
