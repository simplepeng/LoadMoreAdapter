package me.simple.loadmoreadapter.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder

class LinearItemBinder(
    val onDel: (position:Int) -> Unit
) : ItemViewBinder<String, LinearItemBinder.VH>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH {
        return VH(inflater.inflate(R.layout.item_linear, parent, false))
    }

    override fun onBindViewHolder(holder: VH, item: String) {
        holder.textView.text = String.format("%s ----- %s", item, holder.adapterPosition + 1)
        holder.itemView.setOnClickListener {
            onDel.invoke(holder.adapterPosition)
        }
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.tv_linear)
    }
}