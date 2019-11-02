package com.dinokeylas.toyotafunservice.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.NonNull
import com.dinokeylas.toyotafunservice.R
import com.dinokeylas.toyotafunservice.model.PlusService

class PlusServiceAdapter(context: Context, resource: Int, list: ArrayList<PlusService>) :
    ArrayAdapter<PlusService>(context, resource, list) {

    private var plusServiceList: ArrayList<PlusService> = ArrayList()
    private var mContext: Context

    init {
        this.plusServiceList.addAll(list)
        this.mContext = context
    }

    fun getServicePlusList(): ArrayList<PlusService> {
        return plusServiceList
    }

    private class ViewHolder {
        internal var wasteName: TextView? = null
        internal var checkBox: CheckBox? = null
    }

    override fun getCount() = plusServiceList.size

    override fun getItem(position: Int) = plusServiceList[position]

    @SuppressLint("InflateParams")
    @NonNull
    override fun getView(position: Int, convertView: View?, @NonNull parent: ViewGroup): View? {
        var mConvertView = convertView

        var viewHolder: ViewHolder? = null
        Log.v("Convert View", position.toString())

        if (mConvertView == null) {
            val vi = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            mConvertView = vi.inflate(R.layout.row_item_plus_service, null,  true)

            viewHolder = ViewHolder()
            viewHolder.wasteName = mConvertView?.findViewById(R.id.tv_wasteName)
            viewHolder.checkBox = mConvertView?.findViewById(R.id.cb_checkBox)

            mConvertView?.tag = viewHolder

            viewHolder.checkBox?.setOnClickListener { v ->
                val cb = v as CheckBox
                val model = cb.tag as PlusService
                model.checked = cb.isChecked
            }
        } else {
            viewHolder = mConvertView.tag as ViewHolder
        }

        val plusService = plusServiceList[position]
        viewHolder.wasteName?.text = plusService.name
        viewHolder.checkBox?.isChecked = plusService.checked
        viewHolder.checkBox?.tag = plusService

        return mConvertView
    }

}