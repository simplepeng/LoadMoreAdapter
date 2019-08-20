## LoadMoreAdapter

|                     LinearLayoutManager                      | GridLayoutManager                                            | StaggeredGridLayoutManager                                   |
| :----------------------------------------------------------: | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](https://raw.githubusercontent.com/simplepeng/ImageRepo/master/lm_linear.png) | ![](https://raw.githubusercontent.com/simplepeng/ImageRepo/master/lm_grid.png) | ![](https://raw.githubusercontent.com/simplepeng/ImageRepo/master/lm_staggered.png) |

## 导入依赖

```groovy
implementation 'me.simple:load-more-adapter:1.0.0'
```

## 使用

```java
loadMoreAdapter = LoadMoreAdapter.wrap(mAdapter)
                .setLoadMoreListener(new LoadMoreAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(LoadMoreAdapter adapter) {
                       
                    }
                })
                .setOnFailedClickListener(new LoadMoreAdapter.OnFailedClickListener() {
                    @Override
                    public void onClick(LoadMoreAdapter adapter, View view) {
                        
                    }
});
rv.setAdapter(loadMoreAdapter);
```

## 自定义底部

```java
public class CustomFooter extends AbsLoadMoreFooter {

    private TextView mTextView;

    @Override
    public int setLayoutRes() {
        return R.layout.footer_custom;
    }

    @Override
    public void onCreate(View footerView) {
        mTextView = footerView.findViewById(R.id.tv_custom);
    }

    @Override
    public void loading() {
        mTextView.setText("加载更多中...");
    }

    @Override
    public void noMoreData() {
        mTextView.setText("我是有底线的");
    }

    @Override
    public void loadFailed() {
        mTextView.setText("服务器开了小差");
    }
}
```

```java
loadMoreAdapter = LoadMoreAdapter.wrap(mAdapter, new CustomFooter());
rv.setAdapter(loadMoreAdapter);
```

## 可用方法

```java
//加载失败
loadMoreAdapter.loadFailed();
//已无更多数据
loadMoreAdapter.noMoreData();
//重置状态
loadMoreAdapter.resetNoMoreData();
```