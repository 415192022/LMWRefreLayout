package com.lmw.widget.lmwrefrelayout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn01.setOnClickListener {
            startActivity(Intent(this@MainActivity, VerticalRefreshActivity::class.java))
        }

        btn02.setOnClickListener {
            startActivity(Intent(this@MainActivity, HorizonRefreshActivity::class.java))
        }

        btn03.setOnClickListener {
            startActivity(Intent(this@MainActivity, SlideDelActivity::class.java))
        }

        btn04.setOnClickListener {
            startActivity(Intent(this@MainActivity, AnimationRefreshActivity::class.java))
        }

        btn05.setOnClickListener {
            startActivity(Intent(this@MainActivity, NestActivity::class.java))
        }

    }
}
