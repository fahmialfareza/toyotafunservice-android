package com.dinokeylas.toyotafunservice

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.dinokeylas.toyotafunservice.util.Constant.Collection
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.dinokeylas.toyotafunservice.model.Bookings
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_service_detail.*

class ServiceDetailFragment : Fragment() {

    private lateinit var bookings: Bookings
    private lateinit var bookingID: String

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_service_detail, container, false)

        val db = FirebaseDatabase.getInstance().reference
        db.child(Collection.BOOKINGS).orderByChild("status").equalTo("On Service")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            for (data in dataSnapshot.children) {
                                bookingID = data.key!!
                                bookings = data.getValue(Bookings::class.java)!!
                            }
                            insertToLayout()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        println("loadPost:onCancelled ${p0.toException()}")
                    }
                }
            )

        val btnApprove: Button = view.findViewById(R.id.btn_approve)
        btnApprove.setOnClickListener {
            val colorValue = ContextCompat.getColor(context!!, R.color.colorGreen)
            btnApprove.setBackgroundColor(colorValue)
            btnApprove.text = "Sudah Disetujui"

            val dbIN = FirebaseDatabase.getInstance().reference
            dbIN.child(Collection.BOOKINGS).child(bookingID).child("waitingApproval").setValue(true)
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    fun insertToLayout(){
        tv_officer_name.text = bookings.officer
        tv_estimation.text = ""+ bookings.estimation + " Jam"
        tv_problem.text = bookings.complaint
        tv_approve.text = bookings.component
    }

    companion object {
        @JvmStatic
        fun newInstance() = ServiceDetailFragment()
    }
}
