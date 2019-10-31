package com.dinokeylas.toyotafunservice.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinokeylas.toyotafunservice.R
import com.dinokeylas.toyotafunservice.adapter.EmergencyCallAdapter
import com.dinokeylas.toyotafunservice.model.EmergencyCall
import com.dinokeylas.toyotafunservice.util.Constant.Collection
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class EmergencyCallActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: EmergencyCallAdapter
    lateinit var toolbar: Toolbar
    var list: ArrayList<EmergencyCall> = ArrayList()

    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_call)
        checkCallPermission()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.setTitle("Daftar Nomor Darurat")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.rv_emergency_call)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = EmergencyCallAdapter(this, list)
        recyclerView.adapter = adapter

        val firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection(Collection.EMERGENCY_CALL).get()
            .addOnSuccessListener { documents ->
                if(documents!=null){
                    for (document in documents) {
                        val emergencyCall: EmergencyCall? = document.toObject(
                            EmergencyCall::class.java)
                        list.add(emergencyCall!!)
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Log.d("CALL-DATA", "fail to catch user data")
                }
            }
            .addOnFailureListener {
                Log.d("CALL-DATA", it.toString())
            }
    }

    private fun checkCallPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //ask permission of not yet granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),
                MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
            startActivity(Intent(this, HomeActivity::class.java))
    }
}
