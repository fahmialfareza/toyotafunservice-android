package com.dinokeylas.toyotafunservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dinokeylas.toyotafunservice.util.Constant.Collection
import com.google.firebase.firestore.FirebaseFirestore

class EmergencyCallActivity : AppCompatActivity() {

    var list: MutableList<EmergencyCall> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_call)

        val firebaseFirestore = FirebaseFirestore.getInstance()

        firebaseFirestore.collection(Collection.EMERGENCY_CALL).get()
            .addOnSuccessListener { documents ->
                if(documents!=null){
                    for (document in documents) {
                        val emergencyCall: EmergencyCall? = document.toObject(EmergencyCall::class.java)
                        list.add(emergencyCall!!)
                    }
                } else {
                    //show toast message
                    Log.d("CALL-DATA", "fail to catch user data")
                }
            }
            .addOnFailureListener {
                Log.d("CALL-DATA", it.toString())
            }
    }
}
