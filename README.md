## LoadMoreAdapter

用包装器模式给RecyclerView添加可以加载更多的Adapter

|                     LinearLayoutManager                      | GridLayoutManager                                            | StaggeredGridLayoutManager                                   |
| :----------------------------------------------------------: | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](https://raw.githubusercontent.com/simplepeng/ImageRepo/master/lm_linear.png) | ![](https://raw.githubusercontent.com/simplepeng/ImageRepo/master/lm_grid.png) | ![](https://raw.githubusercontent.com/simplepeng/ImageRepo/master/lm_staggered.png) |

## 导入依赖

```css
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```groovy
	dependencies {
	        implementation 'com.github.simplepeng:LoadMoreAdapter:v1.0.3'
	}
```

## 使用

```kotlin
loadMoreAdapter = LoadMoreAdapter.wrap(mAdapter)
loadMoreAdapter?.setOnLoadMoreListener {
    loadMoreData()
}
loadMoreAdapter?.setOnFailedClickListener { adapter, view ->
                                           
}
rv.adapter = loadMoreAdapter
```

## 自定义底部

```kotlin
class CustomFooter : ILoadMoreFooter {

    private var mTextView: TextView? = null

    override fun setLayoutRes(): Int {
        return R.layout.footer_custom
    }

    override fun onCreate(footerView: View) {
        mTextView = footerView.findViewById(R.id.tv_custom)
    }

    override fun loading() {
        mTextView!!.text = "加载更多中..."
    }

    override fun noMoreData() {
        mTextView!!.text = "我是有底线的"
    }

    override fun loadFailed() {
        mTextView!!.text = "服务器开了小差"
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
loadMoreAdapter.loadMoreFailed();
//已无更多数据
loadMoreAdapter.noMoreData();
//重置状态
loadMoreAdapter.resetNoMoreData();
```

## 版本迭代

* v1.0.3：升级`AndroidX`，`Kotlin`，修复bug
* v1.0.2：迁移到`jitpack`
* v1.0.0：首次上传