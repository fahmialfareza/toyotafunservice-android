package com.dinokeylas.toyotafunservice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dinokeylas.toyotafunservice.R
import com.dinokeylas.toyotafunservice.model.Bookings


class ServiceHistoryAdapter(private val context: Context, private val historyList: ArrayList<Bookings>): RecyclerView.Adapter<ServiceHistoryAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_service_history,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = historyList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvDate.text = historyList[position].date
        holder.tvTime.text = historyList[position].time
        holder.tvPay.text = historyList[position].totalCost.toString()
        holder.tvOfficer.text = historyList[position].officer
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvDate: TextView = itemView.findViewById(R.id.tv_date)
        var tvTime: TextView = itemView.findViewById(R.id.tv_time)
        var tvPay: TextView = itemView.findViewById(R.id.tv_pay)
        var tvOfficer: TextView = itemView.findViewById(R.id.tv_officer_name)
    }

}