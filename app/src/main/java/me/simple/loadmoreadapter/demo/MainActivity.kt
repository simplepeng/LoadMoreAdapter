package me.simple.loadmoreadapter.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val tab1 = tabLayout.newTab()
        tab1.text = "Linear"
        val tab2 = tabLayout.newTab()
        tab2.text = "Grid"
        val tab3 = tabLayout.newTab()
        tab3.text = "Sta"
        tabLayout.addTab(tab1)
        tabLayout.addTab(tab2)
        tabLayout.addTab(tab3)
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(i: Int): Fragment {
                when (i) {
                    0 -> return LinearFragment()
                    1 -> return GridFragment()
                    2 -> return StaggeredFragment()
                }
                return LinearFragment()
            }

            override fun getPageTitle(position: Int): CharSequence? {
                when (position) {
                    0 -> return "Linear"
                    1 -> return "Grid"
                    2 -> return "Sta"
                }
                return ""
            }

            override fun getCount(): Int {
                return 3
            }
        }
    }
}