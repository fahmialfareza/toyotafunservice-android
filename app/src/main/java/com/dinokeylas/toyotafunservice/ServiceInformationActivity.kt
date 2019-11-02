package com.dinokeylas.toyotafunservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dinokeylas.toyotafunservice.view.HomeActivity
import com.google.android.material.tabs.TabLayout
import java.util.ArrayList

class ServiceInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_information)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        val viewPager: ViewPager = findViewById(R.id.viewpager)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Daftar Transaksi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ServiceDetailFragment(), "DETAIL SERVICE")
        adapter.addFragment(ServiceHistoryFragment(), "RIWAYAT SERVICE")
        viewPager.adapter = adapter
    }

    class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        fun addFragment(fragment: Fragment, tittle: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(tittle)
        }

        @Nullable
        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }
}
