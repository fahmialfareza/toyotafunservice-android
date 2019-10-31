package com.dinokeylas.toyotafunservice.presenter

import com.dinokeylas.toyotafunservice.contract.LoginContract
import com.dinokeylas.toyotafunservice.util.MD5
import com.google.firebase.auth.FirebaseAuth

class LoginPresenter(_view: LoginContract.View): LoginContract.Presenter{

    private var view: LoginContract.View = _view

    init {
        val firebaseAuth = FirebaseAuth.getInstance()
        if(firebaseAuth.currentUser!=null) view.navigateToHome()
    }

    override fun isValidInput(email: String, password: String): Boolean {
        return view.validateInput(email, password)
    }

    override fun login(email: String, password: String) {
        val mAuth = FirebaseAuth.getInstance()

        if (isValidInput(email, password)){
            view.showProgressBar()
            mAuth.signInWithEmailAndPassword(email, MD5.encript(password))
                .addOnSuccessListener {
                    onLoginSuccess()
                }
                .addOnFailureListener {
                    onLoginFailure()
                }
        }
    }

    override fun onLoginSuccess() {
        view.hideProgressBar()
        view.showToastMessage("Anda Berhasil Login")
        view.navigateToHome()
    }

    override fun onLoginFailure() {
        view.hideProgressBar()
        view.showToastMessage("Pastikan pasangan email dan password yang anda masukkan adalah benar")
    }

}