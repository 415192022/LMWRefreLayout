package com.lmw.widget.lmwrefrelayout

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lmw.widget.lmwrefrelayout.adapter.UserAdapter
import com.lmw.widget.lmwrefreshlayout.lib.listener.OnPullToRefreshListener
import com.lmw.widget.lmwrefrelayout.model.User
import kotlinx.android.synthetic.main.activity_vertical_refresh.*

class VerticalRefreshActivity : AppCompatActivity() {

    private var mData: ArrayList<User> = arrayListOf(
            User("jenny"),
            User("sum"),
            User("sun"),
            User("money"),
            User("jason"),
            User("jackson"),
            User("blues"),
            User("link"),
            User("ken"),
            User("js"),
            User("break"),
            User("private"),
            User("public"),
            User("protect"),
            User("class"),
            User("bush")
    )

    private var adapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_refresh)

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = UserAdapter(this, mData)
        recycler.adapter = adapter

        refreshLayout.setPullToRefreshListener(object : OnPullToRefreshListener {
            override fun onRefresh() {
                refreshLayout.postDelayed({
                    Log.e("onRefresh", "下拉刷新")
                    mData?.clear()
                    mData?.addAll(getNewData())
                    adapter?.notifyDataSetChanged()
                    refreshLayout.onFinish()
                }, 3000)
            }

            override fun onLoadMore() {
                refreshLayout.postDelayed({
                    Log.e("onRefresh", "上拉加载")
                    refreshLayout.onFinish()
                    mData.addAll(getNewData())
                    adapter?.notifyDataSetChanged()
                }, 3000)
            }

        })
    }

    fun getNewData(): ArrayList<User> {
        return arrayListOf(
                User("jenny"),
                User("sum"),
                User("sun"),
                User("money"),
                User("jason"),
                User("jackson"),
                User("blues"),
                User("link"),
                User("ken"),
                User("js"),
                User("break"),
                User("private"),
                User("public"),
                User("protect"),
                User("class"),
                User("bush")
        )
    }
}
