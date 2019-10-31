package com.dinokeylas.toyotafunservice.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.dinokeylas.toyotafunservice.model.EmergencyCall
import com.dinokeylas.toyotafunservice.R

class EmergencyCallAdapter(private val context: Context, private val list: ArrayList<EmergencyCall>): RecyclerView.Adapter<EmergencyCallAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_emergency_call,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.tvPhoneNumber.text = list[position].phoneNumber
        holder.tvName.text = list[position].name
        holder.cvEmergencyCall.setOnClickListener(onClickListener(position))
    }

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var cvEmergencyCall : CardView = itemView.findViewById(R.id.cv_emergency_call)
        var tvPhoneNumber: TextView =itemView.findViewById(R.id.tv_phone_number)
        var tvName: TextView = itemView.findViewById(R.id.tv_ec_name)
    }

    private fun onClickListener(position: Int): View.OnClickListener{
        return View.OnClickListener {
            val callIntent = Intent(Intent.ACTION_VIEW)
            callIntent.data = Uri.parse("tel:"+list[position].phoneNumber)
            context.startActivity(callIntent)
        }
    }
}