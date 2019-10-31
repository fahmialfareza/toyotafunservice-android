package com.dinokeylas.toyotafunservice.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.dinokeylas.toyotafunservice.R
import com.dinokeylas.toyotafunservice.adapter.SliderImageAdapter
import com.dinokeylas.toyotafunservice.model.SliderModel
import com.viewpagerindicator.CirclePageIndicator
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var viewPager: ViewPager? = null
    private var currentPage = 0
    private var NUM_PAGES = 0

    private lateinit var indicator: CirclePageIndicator
    private var imageModelArrayList: ArrayList<SliderModel>? = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //initialize view
        viewPager = view.findViewById(R.id.pager)
        indicator = view.findViewById(R.id.indicator)

        imageModelArrayList = populateList()

        //set the adapter
        viewPager!!.adapter = SliderImageAdapter(
            view.context,
            imageModelArrayList!!
        )

        //set the indicator
        setIndicator()

        val cvBooking: CardView = view.findViewById(R.id.cv_booking)
        val cvServiceInfo: CardView = view.findViewById(R.id.cv_service_info)
        val cvPayService: CardView = view.findViewById(R.id.cv_pay_service)
        val cvServiceTutorial: CardView = view.findViewById(R.id.cv_service_tutorial)
        val cvEmergencyCall: CardView = view.findViewById(R.id.cv_emergency_call)

        cvBooking.setOnClickListener { startActivity(Intent(context, ServiceBookingActivity::class.java)) }
        cvServiceInfo.setOnClickListener {  }
        cvPayService.setOnClickListener {  }
        cvServiceTutorial.setOnClickListener {  }
        cvEmergencyCall.setOnClickListener { startActivity(Intent(context, EmergencyCallActivity::class.java)) }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment = HomeFragment()
    }

    private fun populateList(): ArrayList<SliderModel> {
        val imageList = intArrayOf(
            R.drawable.place,
            R.drawable.place,
            R.drawable.default_icon
        )
        val list = ArrayList<SliderModel>()

        for (i in 0..2) {
            val imageModel = SliderModel()
            imageModel.setImage(imageList[i])
            list.add(imageModel)
        }

        return list
    }

    private fun setIndicator() {
        indicator.setViewPager(viewPager)
        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.setRadius(5 * density)
        NUM_PAGES = imageModelArrayList!!.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            viewPager!!.setCurrentItem(currentPage++, true)
        }

        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position
            }
            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {  }
            override fun onPageScrollStateChanged(pos: Int) {  }
        })
    }
}
