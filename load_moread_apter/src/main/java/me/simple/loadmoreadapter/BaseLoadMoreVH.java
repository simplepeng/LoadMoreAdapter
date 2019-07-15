package me.simple.loadmoreadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseLoadMoreVH  extends RecyclerView.ViewHolder{

    public BaseLoadMoreVH(View itemView) {
        super(itemView);
    }

    public void loadingMore(){

    }

    public void loadComplete(){

    }

    public void loadFailure(){

    }

    public void loadEnd(){

    }
}
