# GRefreshLayout
下拉刷新、上拉加载控件，该控件为RecyclerView提供刷新和加载操作


### 示例
<img src="/imgs/demo1.gif" width="360" hegiht="120" align=center />
<img src="/imgs/demo2.gif" width="360" hegiht="120" align=center />
<img src="/imgs/demo3.gif" width="360" hegiht="120" align=center />
<img src="/imgs/demo4.gif" width="360" hegiht="120" align=center />

### 特性
- 支持上拉刷新
- 支持下拉加载
- 支持左拉刷新
- 支持右拉加载
- 支持自定义Header
- 支持自定义Footer
- 支持滑动到底部自动加载
- 支持嵌套滚动
- 支持下拉后，回到桌面，再返回App，不会出现界面异常
- 支持进入二层
- 支持侧滑删除
- 支持刷新中也可以滑动界面


### 开始
引入依赖
```
dependencies {
    implementation 'com.github.QiHang-Android:GRefreshLayout:latest.release'
}
```

xml中使用
```
<com.lmw.widget.lmwrefrelayout.widget.LMWRefreshLayout
            android:id="@+id/refreshLayout"
            android:layout_width="match_parent"
            app:refresh_orientation="vertical"
            app:can_pull_down="true"
            app:can_pull_up="true"
            app:customer_header="true"
            android:layout_height="match_parent">

        <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/recycler"
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>

</com.lmw.widget.lmwrefrelayout.widget.LMWRefreshLayout>
    
<com.lmw.widget.lmwrefrelayout.widget.LMWHorizonRefreshLayout
            android:id="@+id/refreshLayout"
            android:layout_width="match_parent"
            app:refresh_orientation="vertical"
            app:can_pull_down="true"
            app:can_pull_up="true"
            app:customer_header="true"
            android:layout_height="wrap_content">

        <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/recycler"
                android:layout_width="match_parent"
                android:layout_height="150dp"/>

</com.lmw.widget.lmwrefrelayout.widget.LMWHorizonRefreshLayout>

```
