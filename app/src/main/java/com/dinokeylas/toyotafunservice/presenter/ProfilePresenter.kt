package com.dinokeylas.toyotafunservice.presenter

import android.util.Log
import com.dinokeylas.toyotafunservice.contract.ProfileContract
import com.dinokeylas.toyotafunservice.model.User
import com.dinokeylas.toyotafunservice.util.Constant.Collection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class ProfilePresenter(_view: ProfileContract.View): ProfileContract.Presenter {

    private var view: ProfileContract.View = _view

    init {
        loadUserData()
    }

    override fun loadUserData() {
        val db = FirebaseDatabase.getInstance().reference
        val mUser = FirebaseAuth.getInstance().currentUser
        val userId = mUser?.uid

        val dateListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("loadPost:onCancelled ${p0.toException()}")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                fillUserDataToLayout(user)
            }
        }
        //get child data
        db.child(Collection.USER).child(userId ?: "user").addListenerForSingleValueEvent(dateListener)
    }

    override fun fillUserDataToLayout(user: User?) {
        view.fillDataToLayout(user)
    }

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
        view.navigateToLogin()
    }
}