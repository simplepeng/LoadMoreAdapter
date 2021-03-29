package me.simple.loadmoreadapter.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.drakeet.multitype.ItemViewBinder

class GridItemBinder : ItemViewBinder<String, GridItemBinder.VH>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH {
        return VH(inflater.inflate(R.layout.item_grid, parent, false))
    }

    override fun onBindViewHolder(holder: VH, item: String) {
        holder.textView.text = String.format("%s", holder.adapterPosition + 1)
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.textView)
    }
}