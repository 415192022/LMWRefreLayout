package com.lmw.widget.lmwrefrelayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lmw.widget.lmwrefrelayout.adapter.UserAdapter
import com.lmw.widget.lmwrefreshlayout.lib.listener.OnPullToRefreshListener
import com.lmw.widget.lmwrefrelayout.model.User
import kotlinx.android.synthetic.main.activity_horizon_refresh.*

class HorizonRefreshActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizon_refresh)

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = UserAdapter(this, mData)

        refreshLayout.setPullToRefreshListener(object : OnPullToRefreshListener {
            override fun onRefresh() {
                refreshLayout.postDelayed({
                    refreshLayout.onFinish()
                }, 3000)
            }

            override fun onLoadMore() {
                refreshLayout.postDelayed({
                    refreshLayout.onFinish()
                }, 3000)
            }

        })
    }
}
