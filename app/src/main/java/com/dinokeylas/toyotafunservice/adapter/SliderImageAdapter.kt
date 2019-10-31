package com.dinokeylas.toyotafunservice.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.dinokeylas.toyotafunservice.R
import com.dinokeylas.toyotafunservice.model.SliderModel

class SliderImageAdapter (context: Context, private val imageModelArrayList: ArrayList<SliderModel>): PagerAdapter(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.item_slider_layout, view, false)!!
        val imageView = imageLayout.findViewById(R.id.slider_image) as ImageView
        imageView.setImageResource(imageModelArrayList[position].getImage())
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return imageModelArrayList.size
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        //nothing to do here
    }

    override fun saveState(): Parcelable? {
        return null
    }

}