package me.simple.loadmoreadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AbsLoadMoreVH extends RecyclerView.ViewHolder {

    private AbsLoadMoreVH(@NonNull View itemView) {
        super(itemView);
    }

    public AbsLoadMoreVH(Context context, int layoutRes, ViewGroup parent) {
        this(LayoutInflater.from(context).inflate(layoutRes, parent, false));
    }

    /**
     * 加载更多中
     */

    /**
     * 加载完成
     */

    /**
     * 加载完成-已无更多数据
     */

    /**
     * 加载失败
     */
}
