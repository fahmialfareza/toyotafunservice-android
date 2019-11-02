package com.dinokeylas.toyotafunservice

import com.dinokeylas.toyotafunservice.util.Constant.Collection
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinokeylas.toyotafunservice.adapter.ServiceHistoryAdapter
import com.dinokeylas.toyotafunservice.model.Bookings
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ServiceHistoryFragment : Fragment() {

    private var historyList: ArrayList<Bookings> = ArrayList()
    private lateinit var adapter: ServiceHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_service_history, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_history)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter = ServiceHistoryAdapter(context!!, historyList)
        recyclerView.adapter = adapter

        val db = FirebaseDatabase.getInstance().reference
        db.child(Collection.BOOKINGS).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (data in dataSnapshot.children) {
                        val bookings = data.getValue(Bookings::class.java)
                        historyList.add(bookings!!)
                    }
                        adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.d("DATA-KU", "Failed to read value.", error.toException())
            }
        })

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = ServiceHistoryFragment()
    }
}
