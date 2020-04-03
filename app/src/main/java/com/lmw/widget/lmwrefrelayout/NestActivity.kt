package com.lmw.widget.lmwrefrelayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_nest.*

class NestActivity : AppCompatActivity() {
    private var mCurrentItem: Int = 0
    private var mTabFragments: ArrayList<Fragment>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest)

        mTabFragments = arrayListOf()
        mTabFragments?.add(ScrollFragment.newInstance("", ""))

        viewPager?.clearOnPageChangeListeners()
        viewPager?.offscreenPageLimit = 0
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mCurrentItem = position
            }

        })
        viewPager?.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return mTabFragments!![position]
            }

            override fun getCount(): Int {
                return mTabFragments!!.size
            }
        }
    }
}
